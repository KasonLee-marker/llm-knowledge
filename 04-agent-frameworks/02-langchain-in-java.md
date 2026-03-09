# LangChain in Java

## 1. Java 生态现状

LangChain 官方主要支持 Python/JS，Java 社区有多个替代方案：

| 项目 | 说明 | 活跃度 |
|------|------|--------|
| **LangChain4j** | 最成熟的 Java 移植版 | ⭐⭐⭐⭐⭐ |
| **Spring AI** | Spring 官方 AI 框架 | ⭐⭐⭐⭐⭐ |
| **Semantic Kernel Java** | 微软 Java 版 | ⭐⭐⭐⭐ |

## 2. LangChain4j 详解

### 2.1 核心特性

```java
// 模型接入
OpenAiChatModel model = OpenAiChatModel.builder()
    .apiKey("your-api-key")
    .modelName("gpt-4")
    .build();

// 简单对话
String response = model.generate("你好");

// 带记忆对话
ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
ConversationalChain chain = ConversationalChain.builder()
    .chatLanguageModel(model)
    .chatMemory(chatMemory)
    .build();
```

### 2.2 RAG 示例

```java
// 文档嵌入
EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

// 存储文档
Document document = Document.from("文档内容...");
List<TextSegment> segments = splitter.split(document);
List<Embedding> embeddings = embeddingModel.embedAll(segments);
embeddingStore.addAll(embeddings, segments);

// 检索生成
RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
    .queryTransformer(new DefaultQueryTransformer())
    .contentRetriever(EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .build())
    .build();

AiServices.builder(Assistant.class)
    .chatLanguageModel(model)
    .retrievalAugmentor(retrievalAugmentor)
    .build();
```

### 2.3 Agent 示例

```java
// 定义工具
class WeatherTool {
    @Tool("获取指定城市的天气")
    String getWeather(@ToolParam("城市名称") String city) {
        // 调用天气 API
        return "晴天，25°C";
    }
}

// Agent 构建
Agent agent = AiServices.builder(Agent.class)
    .chatLanguageModel(model)
    .tools(new WeatherTool())
    .build();

// 执行
String result = agent.chat("北京今天天气怎么样？");
```

## 3. Spring AI 对比

| 特性 | LangChain4j | Spring AI |
|------|-------------|-----------|
| 框架依赖 | 独立 | Spring Boot |
| 自动配置 | 手动 | 自动 |
| 生态集成 | 独立生态 | Spring 生态 |
| 学习成本 | 中等 | 低（Spring 开发者）|
| 模型支持 | 广泛 | 主流模型 |

## 4. 推荐选择

- **新项目 + Spring Boot** → Spring AI
- **已有项目集成** → LangChain4j
- **企业级应用** → Spring AI（更好的生态支持）
