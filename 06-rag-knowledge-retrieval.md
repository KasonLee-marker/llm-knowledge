# 06 - RAG / 知识检索（Retrieval-Augmented Generation）

本模块系统介绍 RAG（检索增强生成）技术的核心概念、架构模式、关键组件与实战应用，重点面向 Java 后端开发者的生产实践需求。

## 目录

| # | 文档 | 简介 |
|---|------|------|
| 1 | [RAG 基础概念](./06-rag-knowledge-retrieval/01-rag-basics.md) | 什么是 RAG、为什么需要 RAG、核心流程 |
| 2 | [Embedding 模型选型](./06-rag-knowledge-retrieval/02-embedding-models.md) | 文本嵌入模型对比、多语言支持、Java 集成 |
| 3 | [向量数据库对比](./06-rag-knowledge-retrieval/03-vector-databases.md) | Milvus、Pinecone、Weaviate、Qdrant、Chroma 等选型 |
| 4 | [检索策略与优化](./06-rag-knowledge-retrieval/04-retrieval-strategies.md) | Dense、Sparse、Hybrid 检索，重排序技术 |
| 5 | [RAG 架构演进](./06-rag-knowledge-retrieval/05-rag-architectures.md) | Naive RAG、Advanced RAG、Modular RAG |
| 6 | [文档处理与分块](./06-rag-knowledge-retrieval/06-document-processing.md) | 文档加载、分块策略、元数据管理 |
| 7 | [RAG 评估与优化](./06-rag-knowledge-retrieval/07-rag-evaluation.md) | 评估指标、常见问题、优化技巧 |
| 8 | [Java 实战：构建 RAG 系统](./06-rag-knowledge-retrieval/08-java-rag-practice.md) | Spring Boot + Milvus + OpenAI 完整示例 |

## 核心概念速览

### 什么是 RAG？

RAG（Retrieval-Augmented Generation，检索增强生成）是一种将外部知识检索与大语言模型生成相结合的技术架构。

```
用户提问 → 检索相关文档 → 拼接上下文 → LLM生成回答
                ↑
         [向量数据库]
              ↑
    [Embedding模型]
              ↑
    [文档预处理]
```

### 为什么需要 RAG？

| LLM 原生局限 | RAG 解决方案 |
|-------------|-------------|
| 知识有截止日期 | 实时检索外部知识库 |
| 容易产生幻觉 | 基于检索到的真实文档生成 |
| 无法访问私有数据 | 连接企业私有知识库 |
| 推理成本高 | 减少不必要的上下文长度 |
| 难以溯源 | 提供引用来源 |

### RAG vs Fine-tuning

| 维度 | RAG | Fine-tuning |
|------|-----|-------------|
| 知识更新 | 实时 | 需要重新训练 |
| 成本 | 低（检索+推理） | 高（训练+推理） |
| 数据需求 | 无需训练数据 | 需要高质量训练数据 |
| 适用场景 | 知识频繁更新 | 需要改变模型行为/风格 |
| 可解释性 | 高（可溯源） | 低（黑盒） |

## RAG 核心流程

```
┌─────────────────────────────────────────────────────────────┐
│                      索引阶段（离线）                          │
├─────────────────────────────────────────────────────────────┤
│  原始文档 → 文档加载 → 文本分块 → Embedding → 向量存储          │
│     ↑           ↑          ↑          ↑           ↑         │
│   PDF/Word   Unstructured  Chunking   BGE/M3E    Milvus     │
│   Markdown   Tika          Strategy   OpenAI     Weaviate   │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      查询阶段（在线）                          │
├─────────────────────────────────────────────────────────────┤
│  用户查询 → 查询Embedding → 向量检索 → 重排序 → 上下文拼接       │
│     ↑           ↑             ↑          ↑         ↓        │
│   问题      同Embedding模型  Top-K      Reranker   构建Prompt │
│                                                    ↓        │
│                                              LLM生成回答     │
└─────────────────────────────────────────────────────────────┘
```

## 关键技术组件

### 1. Embedding 模型
- **OpenAI**: text-embedding-3-large/small
- **BAAI**: BGE 系列（中文友好）
- **MokaAI**: M3E 系列（中文开源）
- **Jina**: jina-embeddings-v2

### 2. 向量数据库
- **Milvus**: 开源、云原生、高性能
- **Pinecone**: 托管服务、易用
- **Weaviate**: 开源、GraphQL 接口
- **Qdrant**: Rust 实现、高性能
- **Chroma**: 轻量级、适合原型

### 3. 检索策略
- **Dense Retrieval**: 语义相似度
- **Sparse Retrieval**: 关键词匹配（BM25）
- **Hybrid Retrieval**: 语义+关键词融合
- **Reranking**: 精排优化

### 4. 分块策略
- **固定长度**: 简单直接
- **递归分块**: 保持语义完整
- **语义分块**: 基于句子边界
- **Agentic 分块**: 智能决策分块点

## 学习路径建议

```
RAG 基础概念（01）
       │
       ├── Embedding 模型选型（02）
       │       └── 向量数据库选型（03）
       │
       ├── 检索策略与优化（04）
       │       └── 重排序技术
       │
       ├── RAG 架构演进（05）
       │       ├── Naive RAG
       │       ├── Advanced RAG
       │       └── Modular RAG
       │
       ├── 文档处理与分块（06）
       │
       ├── RAG 评估与优化（07）
       │
       └── Java 实战（08）
```

## 与其他模块的关系

- 本模块依赖 [03 - LLM 模型研究](./03-llm-models-research.md) 中的 Embedding 模型选型
- 本模块为 [04 - Agent 框架](./04-agent-frameworks.md) 提供知识检索能力
- 本模块与 [07 - 多智能体系统](./07-multi-agent-systems.md) 结合可实现 Agentic RAG

## 推荐资源

### 论文
- **Retrieval-Augmented Generation for Knowledge-Intensive NLP Tasks** (2020)
- **Dense Passage Retrieval for Open-Domain Question Answering** (2020)
- **Precise Zero-Shot Dense Retrieval without Relevance Labels** (2022)

### 开源项目
- **LangChain**: Python/JS RAG 框架
- **LlamaIndex**: 数据框架 for LLM
- **RAGFlow**: 开源 RAG 引擎
- **QAnything**: 网易开源 RAG 系统

---

> 📌 详细内容见各子章节，Java 实战示例见 [08-java-rag-practice.md](./06-rag-knowledge-retrieval/08-java-rag-practice.md)
