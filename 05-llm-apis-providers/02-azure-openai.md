# Azure OpenAI

## 1. 简介

Azure OpenAI 是 OpenAI 的企业级托管服务，提供与 OpenAI API 兼容的接口，同时满足企业安全合规需求。

## 2. 与 OpenAI 直连对比

| 特性 | Azure OpenAI | OpenAI 直连 |
|------|-------------|-------------|
| **数据驻留** | ✅ 可选区域 | ❌ 美国 |
| **网络隔离** | ✅ 私有端点 | ❌ 公网 |
| **合规认证** | ✅ SOC2/ISO/GDPR | 有限 |
| **SLA 保障** | ✅ 99.9% | 无 |
| **价格** | 相同 | 相同 |
| **模型更新** | 稍滞后 | 最新 |

## 3. 部署架构

```
企业网络 → Azure VNet → 私有端点 → Azure OpenAI
                ↓
           Azure AD 认证
                ↓
           资源组/订阅管理
```

## 4. Java 集成

### 4.1 使用 Azure SDK

```java
// Maven依赖
// <dependency>
//     <groupId>com.azure</groupId>
//     <artifactId>azure-ai-openai</artifactId>
// </dependency>

OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://your-resource.openai.azure.com/")
    .credential(new AzureKeyCredential("your-api-key"))
    .buildClient();

ChatCompletions chatCompletions = client.getChatCompletions(
    "gpt-4-deployment-name",  // 部署名
    new ChatCompletionsOptions(Arrays.asList(
        new ChatMessage(ChatRole.USER, "你好")
    ))
);

String content = chatCompletions.getChoices().get(0).getMessage().getContent();
```

### 4.2 使用 OpenAI SDK（兼容模式）

```java
// Azure 也支持标准 OpenAI SDK
OpenAiService service = new OpenAiService(
    "your-api-key",
    "https://your-resource.openai.azure.com/openai/deployments/gpt-4",
    Duration.ofSeconds(30)
);

// 注意：需要在 URL 中包含 api-version
// ?api-version=2024-02-01
```

### 4.3 Spring AI 配置

```yaml
spring:
  ai:
    azure:
      openai:
        api-key: ${AZURE_OPENAI_KEY}
        endpoint: https://your-resource.openai.azure.com/
        deployment-name: gpt-4
```

```java
@Service
public class AzureChatService {
    
    private final AzureOpenAiChatModel chatModel;
    
    public String chat(String message) {
        return chatModel.call(message);
    }
}
```

## 5. 内容安全

### 5.1 内容过滤器
```java
// Azure 内置内容安全（无需代码）
// 在 Azure Portal 配置：
// - 仇恨/暴力/性内容过滤级别
// - 提示词注入防护
// - 自定义阻止列表
```

### 5.2 私有数据保护
```yaml
# 配置数据保留策略
# Azure Portal → Azure OpenAI → 内容筛选器 → 数据保留
# 选项：
# - 不保留（Zero Data Retention）
# - 30天（默认）
# - 自定义
```

## 6. 高可用配置

### 6.1 多区域部署
```java
@Configuration
public class AzureHAConfig {
    
    @Bean
    public ChatClient resilientChatClient() {
        // 主区域
        AzureOpenAiChatModel primary = new AzureOpenAiChatModel(
            "https://east-us.openai.azure.com/", ...);
        
        // 备用区域
        AzureOpenAiChatModel secondary = new AzureOpenAiChatModel(
            "https://west-europe.openai.azure.com/", ...);
        
        return ChatClient.builder(primary)
            .defaultFallback(secondary)
            .build();
    }
}
```

## 7. 成本优化

### 7.1 预配置吞吐量（PTU）
```
适用场景：
- 稳定流量（>100K tokens/分钟）
- 延迟敏感应用
- 成本可预测性要求高

不适用：
- 流量波动大
- 低频调用
```

### 7.2 批处理 API
```java
// Azure 支持批处理，与 OpenAI 相同
// 异步处理，24小时内完成，50% 折扣
```

## 8. 监控与诊断

### 8.1 Application Insights
```yaml
# 自动集成
management:
  azure:
    application-insights:
      enabled: true
      instrumentation-key: ${APP_INSIGHTS_KEY}
```

### 8.2 关键指标
- Token 消耗量
- 请求延迟（P50/P95/P99）
- 错误率（429/500/503）
- 内容安全触发次数

## 9. 企业 checklist

- [ ] Azure AD 集成（非 API Key）
- [ ] 私有端点配置
- [ ] 内容安全策略
- [ ] 数据驻留确认
- [ ] 备份区域部署
- [ ] 成本预算告警
- [ ] 审计日志开启
- [ ] SLA 监控配置
