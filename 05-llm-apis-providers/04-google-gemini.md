# Google Gemini API

## 1. 简介

Gemini 是 Google 的旗舰大模型，提供强大的多模态能力和超长上下文。

## 2. 接入方式

| 方式 | 说明 |
|------|------|
| **Google AI Studio** | 免费层，快速开始 |
| **Vertex AI** | 企业级，GCP 集成 |
| **API Key** | 直接调用 |

## 3. 核心特性

| 特性 | 说明 |
|------|------|
| **超长上下文** | Gemini 1.5 Pro 支持 2M tokens |
| **多模态** | 原生支持文本、图像、音频、视频 |
| **实时 API** | Gemini 2.0 Flash 支持实时对话 |
| **价格优势** | Flash 系列价格极低 |

## 4. Java 集成

```java
// Maven依赖
// <dependency>
//     <groupId>com.google.cloud</groupId>
//     <artifactId>google-cloud-vertexai</artifactId>
// </dependency>

// Vertex AI 方式
try (VertexAI vertexAi = new VertexAI("project-id", "us-central1")) {
    GenerativeModel model = new GenerativeModel("gemini-2.5-pro", vertexAi);
    
    GenerateContentResponse response = model.generateContent(
        ContentMaker.fromMultiModalData(
            PartMaker.fromText("描述这张图片"),
            PartMaker.fromMimeTypeAndData("image/jpeg", imageBytes)
        )
    );
    
    System.out.println(response.getContent().getParts(0).getText());
}
```

## 5. REST API 方式

```java
// 直接调用 API
String requestBody = """
    {
        "contents": [{
            "parts": [
                {"text": "你好"}
            ]
        }]
    }
    """;

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-pro:generateContent?key=" + apiKey))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
    .build();
```

## 6. 多模态示例

```java
// 图片 + 文本
String requestBody = """
    {
        "contents": [{
            "parts": [
                {"text": "这张图片里有什么？"},
                {
                    "inline_data": {
                        "mime_type": "image/jpeg",
                        "data": "base64encodedstring"
                    }
                }
            ]
        }]
    }
    """;
```

## 7. Function Calling

```java
String requestBody = """
    {
        "contents": [{"parts": [{"text": "北京天气"}]}],
        "tools": [{
            "function_declarations": [{
                "name": "get_weather",
                "description": "获取天气",
                "parameters": {
                    "type": "object",
                    "properties": {
                        "city": {"type": "string"}
                    }
                }
            }]
        }]
    }
    """;
```

## 8. 与 OpenAI 对比

| 特性 | Gemini | OpenAI |
|------|--------|--------|
| **上下文** | 2M（1.5 Pro）| 128K-1M |
| **多模态** | 原生支持视频 | 图像/音频 |
| **价格** | Flash 极低 | 中等 |
| **中文** | 一般 | 较好 |
| **生态** | GCP 生态 | 更通用 |

## 9. 适用场景

- ✅ 超长文档/视频分析
- ✅ 多模态应用（视频理解）
- ✅ 成本敏感场景（Flash 系列）
- ✅ GCP 生态用户
- ❌ 中文为主的应用
- ❌ 需要复杂工具调用的场景
