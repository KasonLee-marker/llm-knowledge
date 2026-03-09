# Anthropic Claude API

## 1. 简介

Anthropic 是 OpenAI 前成员创立的公司，Claude 系列以安全性和长上下文著称。

## 2. 核心特性

| 特性 | 说明 |
|------|------|
| **长上下文** | 200K tokens（Claude 3 系列）|
| **安全性** | 内置 Constitutional AI |
| **多模态** | Claude 3 支持图像输入 |
| **扩展思考** | Claude 3.7 支持 Extended Thinking |

## 3. API 格式

```java
// REST API 示例
POST https://api.anthropic.com/v1/messages
Headers:
  x-api-key: your-api-key
  anthropic-version: 2023-06-01

Body:
{
  "model": "claude-3-7-sonnet-20250219",
  "max_tokens": 4096,
  "messages": [
    {"role": "user", "content": "你好"}
  ]
}
```

## 4. Java 集成

```java
// 使用 HttpClient
HttpClient client = HttpClient.newHttpClient();

String requestBody = """
    {
        "model": "claude-3-7-sonnet-20250219",
        "max_tokens": 4096,
        "messages": [
            {"role": "user", "content": "你好"}
        ]
    }
    """;

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.anthropic.com/v1/messages"))
    .header("x-api-key", apiKey)
    .header("anthropic-version", "2023-06-01")
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
    .build();

HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());
```

## 5. Extended Thinking 模式

```java
// Claude 3.7 扩展思考
String requestBody = """
    {
        "model": "claude-3-7-sonnet-20250219",
        "max_tokens": 128000,
        "thinking": {
            "type": "enabled",
            "budget_tokens": 32000
        },
        "messages": [
            {"role": "user", "content": "复杂的数学问题..."}
        ]
    }
    """;
```

## 6. 工具使用（Function Calling）

```java
String requestBody = """
    {
        "model": "claude-3-7-sonnet-20250219",
        "max_tokens": 4096,
        "tools": [
            {
                "name": "get_weather",
                "description": "获取天气",
                "input_schema": {
                    "type": "object",
                    "properties": {
                        "city": {"type": "string"}
                    },
                    "required": ["city"]
                }
            }
        ],
        "messages": [
            {"role": "user", "content": "北京天气怎么样？"}
        ]
    }
    """;
```

## 7. 与 OpenAI API 对比

| 特性 | Claude API | OpenAI API |
|------|------------|------------|
| **消息格式** | 独立 messages 数组 | 兼容 messages |
| **System Prompt** | 单独字段 | messages[0].role=system |
| **工具调用** | tools / tool_use | functions / function_call |
| **流式输出** | 支持 | 支持 |
| **多模态** | 支持（base64）| 支持（url/base64）|

## 8. 适用场景

- ✅ 超长文档处理（200K 上下文）
- ✅ 对安全性要求高的场景
- ✅ 复杂推理任务（Extended Thinking）
- ✅ 创意写作、内容生成
- ❌ 成本敏感场景（价格较高）
- ❌ 需要多模态输出（仅支持输入）
