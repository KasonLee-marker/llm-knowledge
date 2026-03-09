# API 集成模式

## 1. 统一客户端抽象

### 1.1 设计目标
- 屏蔽不同厂商 API 差异
- 支持运行时切换模型
- 统一错误处理和重试

### 1.2 Java 实现

```java
// 统一接口
public interface ChatModel {
    ChatResponse chat(ChatRequest request);
    Flux<ChatResponse> chatStream(ChatRequest request);
}

// OpenAI 实现
public class OpenAiChatModel implements ChatModel {
    private final OpenAiApi api;
    
    @Override
    public ChatResponse chat(ChatRequest request) {
        return api.chatCompletion(toOpenAiRequest(request));
    }
}

// Azure 实现
public class AzureOpenAiChatModel implements ChatModel {
    // ...
}

// 国产模型实现（兼容 OpenAI 格式）
public class CompatibleChatModel implements ChatModel {
    // DeepSeek、Qwen 等
}
```

## 2. 路由策略

### 2.1 简单路由
```java
@Service
public class ModelRouter {
    
    public ChatResponse route(String taskType, String message) {
        switch (taskType) {
            case "simple": return simpleModel.chat(message);
            case "complex": return advancedModel.chat(message);
            case "code": return codeModel.chat(message);
            default: return defaultModel.chat(message);
        }
    }
}
```

### 2.2 智能路由（基于内容）
```java
@Service
public class SmartRouter {
    
    public ChatResponse route(String message) {
        // 先让轻量模型分类
        String category = classifierModel.chat(
            "分类以下任务：简单问答/复杂推理/代码生成\n" + message
        );
        
        return routeByCategory(category, message);
    }
}
```

### 2.3 成本优先路由
```java
public class CostAwareRouter {
    private final List<ModelConfig> models = Arrays.asList(
        new ModelConfig("doubao-lite", 0.3, 0.6, 10000),     // 便宜，简单任务
        new ModelConfig("deepseek-v3", 2, 8, 50000),         // 中等，一般任务
        new ModelConfig("gpt-4.1", 14.6, 58.4, Integer.MAX_VALUE) // 贵，复杂任务
    );
    
    public ChatResponse routeWithBudget(String message, int budgetTokens) {
        for (ModelConfig model : models) {
            if (model.canHandle(budgetTokens)) {
                return tryWithFallback(model, message);
            }
        }
        throw new IllegalArgumentException("预算不足");
    }
}
```

## 3. 熔断与降级

### 3.1 Resilience4j 集成
```java
@Service
public class ResilientChatService {
    
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    
    public ChatResponse chat(String message) {
        return circuitBreaker.executeSupplier(() ->
            retry.executeSupplier(() ->
                model.chat(message)
            )
        );
    }
    
    // 降级方法
    public ChatResponse fallback(String message, Throwable ex) {
        log.warn("模型服务降级: {}", ex.getMessage());
        return localModel.chat(message); // 切换到本地模型
    }
}
```

### 3.2 配置
```yaml
resilience4j:
  circuitbreaker:
    instances:
      openai:
        failureRateThreshold: 50
        waitDurationInOpenState: 30s
        slidingWindowSize: 10
  retry:
    instances:
      openai:
        maxAttempts: 3
        waitDuration: 1s
        exponentialBackoffMultiplier: 2
```

## 4. 限流与配额

### 4.1 令牌桶限流
```java
@Component
public class RateLimitedChatService {
    
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    public ChatResponse chat(String userId, String message) {
        Bucket bucket = buckets.computeIfAbsent(userId, 
            k -> Bucket.builder()
                .addLimit(Bandwidth.classic(100, Duration.ofMinutes(1)))
                .build());
        
        if (bucket.tryConsume(1)) {
            return model.chat(message);
        } else {
            throw new RateLimitException("请求过于频繁");
        }
    }
}
```

### 4.2 成本配额
```java
@Service
public class QuotaService {
    
    public boolean checkQuota(String userId, int estimatedTokens) {
        UserQuota quota = quotaRepository.findByUserId(userId);
        
        if (quota.getRemainingTokens() < estimatedTokens) {
            throw new QuotaExceededException("额度不足");
        }
        
        quota.deduct(estimatedTokens);
        return true;
    }
}
```

## 5. 缓存策略

### 5.1 Prompt 缓存
```java
@Service
public class CachingChatService {
    
    private final Cache<String, ChatResponse> cache = Caffeine.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(Duration.ofMinutes(10))
        .build();
    
    public ChatResponse chat(String message) {
        String key = hash(message);
        return cache.get(key, k -> model.chat(message));
    }
}
```

### 5.2 语义缓存（Embedding 相似度）
```java
@Service
public class SemanticCache {
    
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    
    public ChatResponse chat(String message) {
        // 计算 embedding
        Embedding embedding = embeddingModel.embed(message);
        
        // 搜索相似历史
        List<ScoredPoint> similar = vectorStore.search(embedding, 1, 0.95);
        
        if (!similar.isEmpty()) {
            return cache.get(similar.get(0).getId());
        }
        
        // 调用模型并缓存
        ChatResponse response = model.chat(message);
        vectorStore.store(embedding, response);
        return response;
    }
}
```

## 6. 监控与可观测性

### 6.1 Metrics
```java
@Component
public class ChatMetrics {
    
    private final MeterRegistry registry;
    
    public void record(ChatRequest request, ChatResponse response, Duration latency) {
        // Token 消耗
        registry.counter("llm.tokens.input", 
            "model", request.getModel()).increment(request.getInputTokens());
        registry.counter("llm.tokens.output", 
            "model", request.getModel()).increment(response.getOutputTokens());
        
        // 延迟
        registry.timer("llm.latency", 
            "model", request.getModel()).record(latency);
        
        // 成本
        registry.counter("llm.cost", 
            "model", request.getModel()).increment(calculateCost(request, response));
    }
}
```

### 6.2 Tracing
```java
@Service
public class TracedChatService {
    
    @NewSpan("llm-chat")
    public ChatResponse chat(@SpanTag("model") String model, String message) {
        // 自动创建 span，记录调用链
        return model.chat(message);
    }
}
```

## 7. 完整架构示例

```
用户请求 → API Gateway
              ↓
        认证/鉴权
              ↓
        限流检查（Rate Limiter）
              ↓
        智能路由（Router）
              ↓
        缓存检查（Cache）
              ↓
        熔断器（Circuit Breaker）
              ↓
        重试机制（Retry）
              ↓
        模型调用
              ↓
        监控记录（Metrics）
              ↓
        返回响应
```
