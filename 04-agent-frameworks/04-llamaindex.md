# LlamaIndex

## 1. 简介

LlamaIndex（原 GPT Index）是专注于 **RAG（检索增强生成）** 的框架，提供数据连接、索引、查询的全链路支持。

## 2. 核心能力

| 能力 | 说明 |
|------|------|
| **Data Connectors** | 连接 100+ 数据源（PDF、DB、API 等）|
| **Indexes** | 多种索引策略（Vector、Tree、Keyword 等）|
| **Query Engines** | 智能查询引擎，支持多轮对话 |
| **Agents** | 基于 RAG 的 Agent 能力 |
| **Evaluation** | RAG 效果评估工具 |

## 3. Java 支持

LlamaIndex 官方主要支持 Python，Java 生态有限：

- **llama-index-java**: 社区移植版，功能有限
- **推荐**: 使用 LangChain4j 或 Spring AI 的 RAG 能力替代

## 4. Python 快速示例

```python
from llama_index import VectorStoreIndex, SimpleDirectoryReader

# 加载文档
documents = SimpleDirectoryReader("data").load_data()

# 构建索引
index = VectorStoreIndex.from_documents(documents)

# 查询
query_engine = index.as_query_engine()
response = query_engine.query("文档主要内容是什么？")
```

## 5. 高级 RAG 技巧

```python
# 多路检索（Multi-Query）
from llama_index.retrievers import QueryFusionRetriever

retriever = QueryFusionRetriever(
    [vector_retriever, bm25_retriever],
    similarity_top_k=2,
    num_queries=4,
)

# 重排序（Reranking）
from llama_index.postprocessor import SentenceTransformerRerank

rerank = SentenceTransformerRerank(top_n=3)
query_engine = index.as_query_engine(
    node_postprocessors=[rerank]
)
```

## 6. 与 LangChain RAG 对比

| 维度 | LlamaIndex | LangChain |
|------|------------|-----------|
| 专注领域 | RAG 专用 | 通用框架 |
| 数据连接器 | 更丰富 | 中等 |
| 索引策略 | 更多样 | 基础 |
| 评估工具 | 完善 | 有限 |
| 生态成熟度 | Python 成熟 | 多语言成熟 |

## 7. 适用场景

- ✅ 复杂 RAG 应用
- ✅ 多数据源集成
- ✅ 需要精细调优的检索
- ✅ Python 技术栈
- ❌ Java 项目（建议用 LangChain4j）
