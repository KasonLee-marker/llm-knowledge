# OpenAI API 使用指南

## 1. 简介

OpenAI 是当前最主流的大模型服务提供商，提供 GPT 系列模型的云端 API 接入。本章涵盖 Chat Completion、Embeddings、Function Calling 等核心功能，以及 Java 集成、密钥管理、速率限制和最佳实践。

## 2. 接入方式

| 方式 | 说明 |
|------|------|
| **OpenAI 直连** | api.openai.com，标准 REST API，全球可用 |
| **官方 SDK** | Python / Node.js / Go / Java（社区）多语言支持 |
| **Azure OpenAI** | 企业级托管，数据驻留、私有网络（见第2章） |
| **代理/网关** | 自建代理统一管理密钥、速率限制、审计日志 |

### 基础 API 端点

```
https://api.openai.com/v1/chat/completions   # Chat Completion
https://api.openai.com/v1/embeddings          # Embeddings
https://api.openai.com/v1/models              # 模型列表
https://api.openai.com/v1/files               # 文件管理
```

### 认证方式

所有请求通过 HTTP Header 携带 API Key：

```
Authorization: Bearer sk-xxxxxxxxxxxxxxxxxxxx
```

## 3. Chat Completion API

### 3.1 核心概念

Chat Completion API 是 OpenAI 最常用的接口，支持多轮对话、System Prompt、Function Calling 等。

**请求结构：**

```json
{
  "model": "gpt-4.1",
  "messages": [
    {"role": "system", "content": "你是一个有用的助手"},
    {"role": "user", "content": "你好"},
    {"role": "assistant", "content": "你好！有什么可以帮助你的？"},
    {"role": "user", "content": "介绍一下OpenAI API"}
  ],
  "temperature": 0.7,
  "max_tokens": 4096,
  "top_p": 1.0,
  "frequency_penalty": 0,
  "presence_penalty": 0
}
```

**响应结构：**

```json
{
  "id": "chatcmpl-xxx",
  "object": "chat.completion",
  "created": 1717000000,
  "model": "gpt-4.1",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "OpenAI API 提供..."
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 50,
    "completion_tokens": 200,
    "total_tokens": 250
  }
}
```

### 3.2 主要模型一览

| 模型 | 上下文 | 特点 | 适用场景 |
|------|--------|------|---------|
| **gpt-4.1** | 1M | 旗舰编码模型，最强推理 | 复杂代码、逻辑推理 |
| **gpt-4.1-mini** | 1M | 性价比高，速度更快 | 日常编码辅助 |
| **gpt-4.1-nano** | 1M | 最快最便宜 | 简单任务、分类、路由 |
| **gpt-4o** | 128K | 多模态旗舰，更快 | 多模态对话、实时交互 |
| **gpt-4o-mini** | 128K | 低成本多模态 | 轻量级多模态任务 |
| **o3 / o4-mini** | 200K | 深度推理链，适合复杂逻辑 | 数学、科学、编程竞赛 |
| **gpt-4-turbo** | 128K | 已由 4.1 取代，成本较高 | 遗留兼容 |

### 3.3 核心参数详解

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `model` | string | 必填 | 模型名称，如 `gpt-4.1` |
| `messages` | array | 必填 | 对话历史，按时间顺序排列 |
| `temperature` | float | 1.0 | 0-2，越高越随机、越低越确定 |
| `max_tokens` | int | - | 限制输出 token 数上限 |
| `top_p` | float | 1.0 | 核采样，与 temperature 二选一 |
| `n` | int | 1 | 为每个 prompt 生成几个结果 |
| `frequency_penalty` | float | 0 | -2.0 到 2.0，降低重复词概率 |
| `presence_penalty` | float | 0 | -2.0 到 2.0，提高话题多样性 |
| `stop` | string/array | - | 遇到该词时停止生成 |
| `stream` | boolean | false | 是否启用流式输出 |
| `logprobs` | boolean | false | 是否返回 token 对数概率 |
| `user` | string | - | 终端用户标识，用于监控滥用 |

### 3.4 流式输出（Streaming）

流式输出可显著降低用户体感延迟，适合聊天场景。

**SSE 格式响应：**

```
data: {"id":"chatcmpl-xxx","object":"chat.completion.chunk","choices":[{"index":0,"delta":{"role":"assistant","content":"你好"}}]}

data: {"id":"chatcmpl-xxx","object":"chat.completion.chunk","choices":[{"index":0,"delta":{"content":"！"}}]}

data: {"id":"chatcmpl-xxx","object":"chat.completion.chunk","choices":[{"index":0,"delta":{"content":"有什么"},"finish_reason":null}]}

data: [DONE]
```

**请求只需加 `"stream": true`**：

```json
{
  "model": "gpt-4.1",
  "messages": [{"role": "user", "content": "你好"}],
  "stream": true
}
```

## 4. Embeddings API

用于将文本转换为向量表示，适用于语义搜索、聚类、推荐等场景。

### 4.1 请求格式

```json
POST https://api.openai.com/v1/embeddings

{
  "model": "text-embedding-3-small",
  "input": "Hello, world"
}
```

### 4.2 模型选择

| 模型 | 维度 | 价格（每1M tokens） | 说明 |
|------|------|---------------------|------|
| `text-embedding-3-small` | 512/1536 | $0.02 | 性价比之选 |
| `text-embedding-3-large` | 256/1024/3072 | $0.13 | 精度最高 |
| `text-embedding-ada-002` | 1536 | $0.10 | 旧版，已不推荐 |

> **提示**：`text-embedding-3` 系列支持通过 `dimensions` 参数控制输出维度（如设置 512 可节省大量存储空间，精度损失极小）。

### 4.3 批量处理

```json
{
  "model": "text-embedding-3-small",
  "input": ["文本1", "文本2", "文本3", "..."]
}
```

单次请求最多 2048 个文本。

## 5. Function Calling（工具调用）

Function Calling（现正式名称为 "tools"）是让模型决定何时以及如何调用外部工具/API 的机制。

### 5.1 工作原理

```
用户输入 → 模型判断是否需要调用工具
            ├─ 需要 → 返回 tool_calls（含函数名+参数）
            │          → 开发者执行函数 → 将结果返回模型
            │          → 模型整合结果给出最终回复
            └─ 不需要 → 直接返回文本回复
```

### 5.2 工具定义

```json
{
  "model": "gpt-4.1",
  "messages": [{"role": "user", "content": "北京今天天气怎么样？"}],
  "tools": [
    {
      "type": "function",
      "function": {
        "name": "get_weather",
        "description": "获取指定城市的实时天气信息",
        "parameters": {
          "type": "object",
          "properties": {
            "city": {
              "type": "string",
              "description": "城市名称，如 北京、上海"
            }
          },
          "required": ["city"]
        }
      }
    }
  ],
  "tool_choice": "auto"
}
```

### 5.3 模型返回 tool_calls

```json
{
  "choices": [{
    "index": 0,
    "message": {
      "role": "assistant",
      "content": null,
      "tool_calls": [{
        "id": "call_abc123",
        "type": "function",
        "function": {
          "name": "get_weather",
          "arguments": "{\"city\": \"北京\"}"
        }
      }]
    },
    "finish_reason": "tool_calls"
  }]
}
```

### 5.4 提交函数执行结果

```json
{
  "model": "gpt-4.1",
  "messages": [
    {"role": "user", "content": "北京今天天气怎么样？"},
    {"role": "assistant", "tool_calls": [{"id": "call_abc123", ...}]},
    {"role": "tool", "tool_call_id": "call_abc123", "content": "北京今天晴，18-28°C"}
  ]
}
```

### 5.5 tool_choice 选项

| 值 | 说明 |
|-----|------|
| `"auto"` | 默认，模型自行判断是否调用工具 |
| `"none"` | 强制不调用任何工具 |
| `"required"` | 强制必须调用工具 |
| `{"type":"function","function":{"name":"xxx"}}` | 强制调用指定函数 |

## 6. Java 集成

### 6.1 Maven 依赖

```xml
<!-- Spring AI OpenAI -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>

<!-- BOM 管理版本 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0-M6</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- Jackson JSON 处理（原生方式） -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

> **Milestone 仓库**：Spring AI 里程碑版本需要在 `pom.xml` 中添加 Spring Milestone Repository。

### 6.2 Spring AI 方式

#### application.yml 配置

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4.1
          temperature: 0.7
          max-tokens: 4096
      embedding:
        options:
          model: text-embedding-3-small
      # 代理配置（可选）
      # base-url: https://your-proxy.com/v1
```

#### Chat Completion 示例

```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {

    private final ChatClient chatClient;

    @Autowired
    public OpenAIChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    // 简单对话
    public String simpleChat(String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    // 带 System Prompt 的对话
    public String chatWithSystem(String systemPrompt, String userMessage) {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userMessage)
            .options(OpenAiChatOptions.builder()
                .model("gpt-4.1")
                .temperature(0.7)
                .maxTokens(4096)
                .build())
            .call()
            .content();
    }

    // 流式对话
    public Flux<String> streamChat(String message) {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content();
    }
}
```

#### Function Calling 示例

```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionCallingConfig {

    // 定义工具函数（由 Spring 自动发现）
    @Bean
    @Description("获取指定城市的实时天气信息")
    public Function<WeatherRequest, WeatherResponse> getWeather() {
        return request -> {
            // 实际调用外部天气 API
            String city = request.city();
            return new WeatherResponse(city, "晴", "18-28°C");
        };
    }

    // 请求参数
    public record WeatherRequest(String city) {}

    // 返回结果
    public record WeatherResponse(String city, String condition, String temperature) {}
}
```

```java
@Service
public class FunctionCallingService {

    private final ChatClient chatClient;

    @Autowired
    public FunctionCallingService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
            .defaultFunctions("getWeather")  // 注册工具
            .build();
    }

    public String askWithFunction(String userMessage) {
        return chatClient.prompt()
            .user(userMessage)
            .call()
            .content();
    }
}
```

#### Embeddings 示例

```java
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    @Autowired
    private EmbeddingModel embeddingModel;

    // 单文本向量化
    public float[] embed(String text) {
        return embeddingModel.embed(text);
    }

    // 批量文本向量化
    public List<float[]> embedBatch(List<String> texts) {
        return embeddingModel.embed(texts);
    }

    // 自定义维度
    public float[] embedWithDimensions(String text, int dimensions) {
        EmbeddingRequest request = new EmbeddingRequest(
            List.of(text),
            OpenAiEmbeddingOptions.builder()
                .model("text-embedding-3-small")
                .dimensions(dimensions)  // 如 512
                .build()
        );
        return embeddingModel.call(request)
            .getResults().get(0).getOutput();
    }
}
```

### 6.3 原生 HttpClient 方式

不依赖 Spring AI，使用 JDK 内置的 `java.net.http.HttpClient` 直接调用 OpenAI API。

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class NativeOpenAIClient {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient httpClient;
    private final String apiKey;

    public NativeOpenAIClient(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    // ========== Chat Completion（同步） ==========
    public String chat(String systemPrompt, String userMessage, 
                        String model, double temperature, int maxTokens) {
        try {
            String requestBody = MAPPER.writeValueAsString(Map.of(
                "model", model,
                "messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userMessage)
                ),
                "temperature", temperature,
                "max_tokens", maxTokens
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API 错误: " + response.body());
            }

            // 解析响应
            Map<String, Object> body = MAPPER.readValue(response.body(), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            throw new RuntimeException("OpenAI API 调用失败", e);
        }
    }

    // ========== 流式 Chat Completion ==========
    public void streamChat(String userMessage, StreamCallback callback) {
        try {
            String requestBody = MAPPER.writeValueAsString(Map.of(
                "model", "gpt-4.1",
                "messages", List.of(
                    Map.of("role", "user", "content", userMessage)
                ),
                "stream", true
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofLines())
                .body()
                .filter(line -> line.startsWith("data: "))
                .map(line -> line.substring(6))  // 去掉 "data: " 前缀
                .filter(line -> !"[DONE]".equals(line))
                .forEach(line -> {
                    try {
                        Map<String, Object> chunk = MAPPER.readValue(line, Map.class);
                        List<Map<String, Object>> choices = 
                            (List<Map<String, Object>>) chunk.get("choices");
                        if (choices == null || choices.isEmpty()) return;
                        Map<String, Object> delta = 
                            (Map<String, Object>) choices.get(0).get("delta");
                        if (delta == null) return;
                        String content = (String) delta.get("content");
                        if (content != null) {
                            callback.onToken(content);
                        }
                    } catch (JsonProcessingException e) {
                        // 忽略解析错误
                    }
                });

        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException("流式调用失败", e);
        }
    }

    // ========== Function Calling ==========
    public String functionCall(String userMessage, List<Map<String, Object>> tools) {
        try {
            String requestBody = MAPPER.writeValueAsString(Map.of(
                "model", "gpt-4.1",
                "messages", List.of(
                    Map.of("role", "user", "content", userMessage)
                ),
                "tools", tools,
                "tool_choice", "auto"
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Function Calling 失败", e);
        }
    }

    // ========== Embeddings ==========
    public List<Double> getEmbedding(String text, String model) {
        try {
            String requestBody = MAPPER.writeValueAsString(Map.of(
                "model", model,
                "input", text
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/embeddings"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            Map<String, Object> body = MAPPER.readValue(response.body(), Map.class);
            List<Map<String, Object>> data = (List<Map<String, Object>>) body.get("data");
            return (List<Double>) data.get(0).get("embedding");

        } catch (Exception e) {
            throw new RuntimeException("Embedding 调用失败", e);
        }
    }

    // ========== 流式回调接口 ==========
    @FunctionalInterface
    public interface StreamCallback {
        void onToken(String token);
    }

    // ========== 使用示例 ==========
    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        NativeOpenAIClient client = new NativeOpenAIClient(apiKey);

        // 同步调用
        String result = client.chat(
            "你是一个Java专家",
            "如何在Java中实现单例模式？",
            "gpt-4.1", 0.7, 2048
        );
        System.out.println(result);

        // 流式调用
        client.streamChat("讲个笑话", token -> System.out.print(token));

        // Embedding
        List<Double> embedding = client.getEmbedding(
            "Hello, world", "text-embedding-3-small"
        );
        System.out.println("向量维度: " + embedding.size());
    }
}
```

## 7. 密钥管理

### 7.1 创建与管理

1. 登录 [platform.openai.com/api-keys](https://platform.openai.com/api-keys)
2. 创建新 Key，设置权限范围（只读/读写）
3. 可设置月度预算上限、过期时间
4. **注意**：Key 只显示一次，创建后立即保存

### 7.2 安全最佳实践

| 实践 | 说明 |
|------|------|
| **环境变量** | 通过 `OPENAI_API_KEY` 传递，不硬编码 |
| **密钥轮换** | 定期创建新 Key 替换旧 Key |
| **最小权限** | 生产环境使用只读 Key |
| **密钥仓库** | 使用 Vault / AWS Secrets Manager / K8s Secrets |
| **审计日志** | 启用 API 使用日志，追踪每个 Key 的调用 |
| **IP 白名单** | 企业版可限制 Key 的 IP 来源 |

### 7.3 Java 环境变量配置

```java
// 方式1：读取环境变量
String apiKey = System.getenv("OPENAI_API_KEY");

// 方式2：Spring 配置注入
@Value("${openai.api-key}")
private String apiKey;

// 方式3：使用 KeyVault 动态获取
@Configuration
public class KeyVaultConfig {
    @Bean
    public String openAiApiKey() {
        // 从 Vault / AWS Secrets Manager 获取
        return secretManager.getSecret("openai/api-key");
    }
}
```

## 8. 速率限制

### 8.1 限制层级

OpenAI 提供基于 Usage Tier 的速率限制，使用量越大，额度越高。

| Tier | 要求 | RPM (req/min) | TPM (tokens/min) |
|------|------|---------------|------------------|
| **Free** | 新注册 | 3 | 40,000 |
| **Tier 1** | $5 消费 | 500 | 200,000 |
| **Tier 2** | $50 消费 + 7天 | 5,000 | 2,000,000 |
| **Tier 3** | $100 消费 + 7天 | 5,000 | 10,000,000 |
| **Tier 4** | $250 消费 + 14天 | 10,000 | 30,000,000 |
| **Tier 5** | $1,000 消费 + 30天 | 10,000 | 150,000,000 |

### 8.2 处理 429 错误

```java
public class OpenAIRateLimiter {

    private static final int MAX_RETRIES = 5;

    public String callWithRetry(Supplier<String> apiCall) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                return apiCall.get();
            } catch (RateLimitException e) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw new RuntimeException("超过最大重试次数", e);
                }
                // 指数退避：1s, 2s, 4s, 8s, 16s
                long waitMs = (long) Math.pow(2, attempt - 1) * 1000;
                // 优先使用 Retry-After 头
                if (e.getRetryAfterSeconds() > 0) {
                    waitMs = e.getRetryAfterSeconds() * 1000L;
                }
                try {
                    Thread.sleep(waitMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试被中断", ie);
                }
            }
        }
        throw new RuntimeException("无法完成请求");
    }
}
```

### 8.3 速率限制 Headers

每次 API 响应包含以下 Headers：

```
x-ratelimit-limit-requests        # 总 RPM 限额
x-ratelimit-limit-tokens          # 总 TPM 限额
x-ratelimit-remaining-requests    # 剩余 RPM
x-ratelimit-remaining-tokens      # 剩余 TPM
x-ratelimit-reset-requests        # RPM 重置时间（Unix time）
x-ratelimit-reset-tokens          # TPM 重置时间
```

> **建议**：在应用层实现令牌桶或滑动窗口限流，避免触发 429。

## 9. 最佳实践

### 9.1 网络与连接

```java
// 连接池与超时配置
HttpClient client = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .version(HttpClient.Version.HTTP_2)      // 使用 HTTP/2 提升并发
    .build();

// 生产环境务必设置合理超时
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(API_URL))
    .timeout(Duration.ofSeconds(60))         // 总超时 60s
    .build();
```

### 9.2 错误处理矩阵

| HTTP 状态码 | 含义 | 处理策略 |
|-------------|------|---------|
| **200** | 成功 | 正常处理 |
| **400** | 请求格式错误 | 检查参数，不重试 |
| **401** | API Key 无效 | 检查 Key 是否过期 |
| **403** | 权限不足/区域限制 | 检查 Key 权限和账户地区 |
| **429** | 速率限制 | 指数退避重试 |
| **500** | 服务器错误 | 退避重试（3次） |
| **503** | 服务过载 | 退避重试 + 切换备用模型 |

### 9.3 提示词工程要点

- **System Prompt 放开头**：设定角色、格式约束、输出风格
- **给模型留"退路"**：例如 "如果不确定，请回答不知道"
- **复杂任务分步执行**：先让模型制定计划，再逐步执行
- **合理使用 temperature**：创意写作 0.7-1.0，事实/代码 0.0-0.3

### 9.4 成本控制

| 策略 | 说明 |
|------|------|
| **模型选择** | 简单任务用 mini/nano，复杂任务用标准版 |
| **缓存 Prompt** | 长 System Prompt 使用 Prompt Caching（自动） |
| **限制 max_tokens** | 根据业务需要设置合理上限，避免浪费 |
| **设置月度预算** | 在 OpenAI Dashboard 设置硬性上限 |
| **批量处理** | 非实时任务使用 Batch API（50% 折扣） |
| **监控使用量** | 实时追踪 Token 消耗，设置告警阈值 |

### 9.5 生产环境 checklist

- [ ] 使用环境变量/密钥仓库管理 API Key
- [ ] 实现指数退避重试（429/500/503）
- [ ] 配置连接超时和读取超时
- [ ] 设置合理的 max_tokens 上限
- [ ] 添加使用量监控和成本告警
- [ ] 降级策略：主模型不可用时切换到备用模型
- [ ] 日志脱敏：不记录 API Key 和用户敏感信息
- [ ] 使用 HTTP/2 或连接复用提升性能

### 9.6 多模型降级策略

```java
@Component
public class ResilientChatService {

    // 主模型和降级模型列表
    private static final List<String> MODEL_FALLBACKS = List.of(
        "gpt-4.1",        // 首选
        "gpt-4.1-mini",   // 降级1
        "gpt-4o-mini"     // 降级2
    );

    public String chatWithFallback(ChatClient chatClient, String message) {
        for (int i = 0; i < MODEL_FALLBACKS.size(); i++) {
            try {
                String model = MODEL_FALLBACKS.get(i);
                return chatClient.prompt()
                    .user(message)
                    .options(OpenAiChatOptions.builder()
                        .model(model)
                        .build())
                    .call()
                    .content();
            } catch (Exception e) {
                if (i == MODEL_FALLBACKS.size() - 1) {
                    throw new RuntimeException("所有模型均不可用", e);
                }
                // 切换到下一个模型继续
            }
        }
        throw new RuntimeException("无法完成请求");
    }
}
```

## 10. 常见问题（FAQ）

### Q1：gpt-4.1 与 gpt-4o 有什么区别？

`gpt-4.1` 是 2025 年新系列，专精编码和指令遵循，上下文支持 1M tokens，性价比优于 `gpt-4-turbo`。`gpt-4o` 是多模态旗舰，支持图像/音频输入输出，响应更快。编码任务优先 `gpt-4.1`，多模态交互优先 `gpt-4o`。

### Q2：什么时候用 temperature=0？

需要确定性输出的场景：代码生成、数学计算、结构化数据提取、分类任务。设置 `temperature=0` 可保证相同输入得到相同输出（非绝对，模型内部仍有微小差异）。

### Q3：如何实现可靠的多轮对话？

每次请求需将完整对话历史放入 `messages` 数组。注意控制总 token 数，避免超出模型上下文窗口。可使用滑动窗口策略丢弃最早的消息。

### Q4：Stream 模式如何处理数据不完整的情况？

SSE 协议下最后一个 chunk 的 `finish_reason` 不为 null。收集所有 delta.content 拼装完整响应。若流中断（网络断开），需用同一个 messages 数组发起新的非流式请求补全。

### Q5：Embedding 维度选多大？

- `text-embedding-3-small`：512 维性价比最高，1536 维精度略高
- `text-embedding-3-large`：256 维存储最优，3072 维精度最高
- 一般推荐 512 或 1024 维，多数场景下精度差异可忽略

### Q6：Function Calling 与 Tool Calling 有什么区别？

同一个功能的不同叫法。OpenAI 官方现在统一使用 "tools" 命名，但社区仍常称 "Function Calling"。API 中 `functions` 参数已被弃用，请使用 `tools`。

### Q7：API Key 泄露了怎么办？

立即登录 OpenAI Dashboard → API Keys → Revoke 该 Key。重新创建一个新 Key。建议为每个应用创建独立 Key，便于单独撤销。

### Q8：如何在国内访问 OpenAI API？

- **代理**：通过 HTTP 代理转发请求（配置 `base-url`）
- **Azure OpenAI**：使用中国或亚太区域的 Azure OpenAI 服务
- **第三方 API 网关**：如 API2D、OhMyGPT 等服务

### Q9：Token 如何计算？

中文约 1 个汉字 = 1.5-2 tokens，英文约 1 个单词 = 1.3 tokens。可使用 [OpenAI Tokenizer](https://platform.openai.com/tokenizer) 在线工具精确计算，或使用 `tiktoken` 库离线计算。

### Q10：o3/o4-mini 系列和 GPT-4.1 怎么选？

`o3/o4-mini` 是推理增强模型，内部会进行多步推理，适合复杂数学、科学问题、编程竞赛。`gpt-4.1` 是指令遵循和执行型模型，适合常规任务。推理模型回复更慢但质量更高，`max_tokens` 需设置更大（如 100000+）。

---

> **延伸阅读**
> - [OpenAI 官方文档](https://platform.openai.com/docs)
> - [OpenAI Cookbook](https://cookbook.openai.com/)
> - [Spring AI 文档](https://docs.spring.io/spring-ai/reference/)
> - 第2章：Azure OpenAI 企业级部署
> - 第3章：Anthropic Claude API 对比
