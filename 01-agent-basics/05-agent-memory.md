# Agent 记忆（Memory）

## 为什么 Agent 需要记忆

LLM 本身是无状态的：每次调用都是独立的，不会自动记住历史对话或过去的任务经验。Agent 的记忆系统解决了以下问题：
- 在多轮对话中保持上下文连贯
- 在长时间运行的任务中跨步骤共享信息
- 从历史任务中学习，避免重复错误
- 存储用户偏好和个性化信息

## 记忆的四种类型

### 1. 短期记忆（Short-term / Working Memory）

**存储位置**：LLM 的 Context 窗口（in-context）  
**特点**：
- 访问速度最快（已在 Prompt 中）
- 受 Context 窗口大小限制（通常 4k～128k tokens）
- 会话结束后自动清除

**适用内容**：当前对话历史、任务执行记录、工具调用结果

**管理策略**：
- **滑动窗口**：保留最近 N 轮对话
- **摘要压缩**：对历史对话进行摘要，保留关键信息
- **重要性过滤**：只保留对当前任务有价值的上下文

### 2. 长期记忆（Long-term Memory）

**存储位置**：外部持久化存储（向量数据库、关系数据库）  
**特点**：
- 跨会话持久化
- 需要主动检索（相似性搜索或关键词搜索）
- 存储容量大，但检索有额外延迟

**适用内容**：用户偏好、历史交互记录、领域知识库

**常用技术**：
- 向量数据库（Chroma、Milvus、Pinecone、pgvector）
- 语义相似度检索（Embedding + Cosine Similarity）

### 3. 情节记忆（Episodic Memory）

**存储位置**：结构化数据库  
**特点**：
- 存储完整的任务执行记录（输入、步骤、结果、评估）
- 用于 Reflexion 等反思模式中的经验学习
- 支持 Agent 从历史错误中改进

**适用内容**：任务执行日志、成功/失败案例

### 4. 语义记忆（Semantic Memory）

**存储位置**：向量数据库 + 知识图谱  
**特点**：
- 存储结构化知识和事实
- 通过 RAG（检索增强生成）注入到 LLM 上下文
- 知识可独立更新，无需重新训练模型

**适用内容**：产品文档、FAQ、企业知识库

## 记忆架构图

```
┌────────────────────────────────────────────────┐
│                   Agent 记忆系统                │
│                                                │
│  ┌─────────────────┐   ┌─────────────────────┐ │
│  │  短期记忆        │   │     长期记忆          │ │
│  │  (Context 窗口)  │   │  (向量DB / 关系DB)   │ │
│  │  - 当前对话历史  │   │  - 用户偏好          │ │
│  │  - 工具调用记录  │   │  - 历史任务          │ │
│  └─────────────────┘   │  - 知识库            │ │
│                        └─────────────────────┘ │
│  ┌─────────────────┐   ┌─────────────────────┐ │
│  │  情节记忆        │   │     语义记忆          │ │
│  │  (任务日志DB)    │   │  (知识图谱/向量DB)   │ │
│  │  - 执行记录      │   │  - 结构化知识        │ │
│  │  - 反思总结      │   │  - 文档库/FAQ        │ │
│  └─────────────────┘   └─────────────────────┘ │
└────────────────────────────────────────────────┘
```

## RAG（检索增强生成）

RAG 是最常见的长期记忆实现方式：

```
用户输入
    ↓
生成 Query Embedding（向量化）
    ↓
在向量数据库中检索相似文档（Top-K）
    ↓
将检索到的文档拼接到 Prompt 中
    ↓
LLM 基于增强上下文生成回答
```

**工程要点**：
- 文档分块（Chunking）策略影响检索质量
- Embedding 模型选择（OpenAI text-embedding-3、本地 BGE 等）
- 相似度阈值过滤，避免注入无关内容
- 混合检索（向量 + 关键词）提升召回率

## Java 记忆管理示例

```java
/**
 * Agent 记忆管理器
 */
public class AgentMemoryManager {
    private final List<Message> shortTermMemory = new ArrayList<>();
    private final VectorStore longTermMemory;
    private final int maxContextMessages;

    public AgentMemoryManager(VectorStore vectorStore, int maxContextMessages) {
        this.longTermMemory = vectorStore;
        this.maxContextMessages = maxContextMessages;
    }

    /** 添加消息到短期记忆 */
    public void addToShortTerm(Message message) {
        shortTermMemory.add(message);
        // 超出限制时，压缩旧消息
        if (shortTermMemory.size() > maxContextMessages) {
            compressOldMessages();
        }
    }

    /** 保存重要信息到长期记忆 */
    public void saveToLongTerm(String content, Map<String, String> metadata) {
        longTermMemory.store(content, metadata);
    }

    /** 从长期记忆中检索相关信息 */
    public List<String> retrieveFromLongTerm(String query, int topK) {
        return longTermMemory.search(query, topK);
    }

    /** 构建包含记忆的完整上下文 */
    public List<Message> buildContext(String currentInput) {
        List<Message> context = new ArrayList<>();

        // 1. 从长期记忆检索相关内容
        List<String> relevant = retrieveFromLongTerm(currentInput, 3);
        if (!relevant.isEmpty()) {
            String memoryContext = "相关历史信息：\n" + String.join("\n", relevant);
            context.add(new SystemMessage(memoryContext));
        }

        // 2. 加入短期记忆（当前对话）
        context.addAll(shortTermMemory);

        return context;
    }

    private void compressOldMessages() {
        // 保留最后 maxContextMessages/2 条消息，其余摘要处理
        int keepFrom = shortTermMemory.size() - maxContextMessages / 2;
        shortTermMemory.subList(0, keepFrom).clear();
    }
}
```

## 工程实践建议

- **短期记忆**：控制对话轮次在 10-20 轮，超出时进行摘要压缩
- **长期记忆**：使用异步写入，避免阻塞 Agent 主流程
- **隐私保护**：对用户数据进行脱敏处理，符合数据合规要求
- **记忆有效期**：为记忆条目设置 TTL，定期清理过期数据
- **检索质量监控**：记录检索相似度分数，监控记忆系统健康度

---
