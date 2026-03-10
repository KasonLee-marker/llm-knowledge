# LLM Knowledge Base

> 面向 Java 后端开发者的大语言模型（LLM）与 AI Agent 知识库，涵盖核心概念、工程实践、模型研究与框架应用。

---

## 目录（Table of Contents）

- [01 - Agent 基础](#01---agent-基础)
- [02 - LLM 基础](#02---llm-基础)
- [03 - LLM 模型研究](#03---llm-模型研究)
- [04 - Agent 框架](#04---agent-框架)
- [05 - LLM APIs 与服务提供商](#05---llm-apis-与服务提供商)

---

## 01 - Agent 基础

> 模块入口：[01-agent-basics.md](./01-agent-basics.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [什么是 Agent](./01-agent-basics/01-what-is-agent.md) | Agent 定义、核心特征与分类 |
| 2 | [Agent 架构：感知-推理-行动循环](./01-agent-basics/02-agent-architecture.md) | 四大核心组件与 Agent Loop |
| 3 | [LLM 驱动的 Agent（ReAct、规划等）](./01-agent-basics/03-llm-agents.md) | ReAct、Plan-and-Execute、Reflexion、Multi-Agent |
| 4 | [Agent 工具（Tools / Function Calling）](./01-agent-basics/04-agent-tools.md) | 工具定义、类型与 Java 接口设计 |
| 5 | [Agent 记忆（Memory）](./01-agent-basics/05-agent-memory.md) | 短期/长期/情节/语义记忆与 RAG |
| 6 | [意图识别（Intent Recognition）](./01-agent-basics/06-intent-recognition.md) | 意图识别技术演进、槽位填充与 Java 实现 |

---

## 02 - LLM 基础

> 模块入口：[02-llm-fundamentals.md](./02-llm-fundamentals.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [Tokens 与 Context 窗口](./02-llm-fundamentals/01-tokens-and-context.md) | Token 原理、主流 Tokenizer、上下文管理策略 |
| 2 | [Prompt 工程](./02-llm-fundamentals/02-prompt-engineering.md) | Prompt 结构、Few-Shot、CoT、ToT、调试技巧 |
| 3 | [Function Calling（函数调用）](./02-llm-fundamentals/03-function-calling.md) | 工作流程、Schema 定义、并行调用、安全验证 |
| 4 | [推理技术（Reasoning Techniques）](./02-llm-fundamentals/04-reasoning-techniques.md) | CoT、ToT、Self-Consistency、ReAct、Reflexion |
| 5 | [Embeddings 与向量表示](./02-llm-fundamentals/05-embeddings-and-vectors.md) | Embedding 模型、相似度计算、RAG、向量数据库选型 |
| 6 | [LLM 输出控制](./02-llm-fundamentals/06-llm-output-control.md) | Temperature、Top-P、结构化输出、Streaming |

---

## 03 - LLM 模型研究

> 模块入口：[03-llm-models-research.md](./03-llm-models-research.md)
> 数据更新至 **2025 年底**，价格单位为 ¥/百万 token

| # | 文档 | 简介 |
|---|------|------|
| 1 | [LLM 市场全景概览](./03-llm-models-research/01-llm-landscape-overview.md) | 2025年全局模型分类、市场格局、¥/百万定价速查 |
| 2 | [OpenAI GPT 系列](./03-llm-models-research/02-openai-gpt-series.md) | GPT-4.1/mini/nano、o3、o4-mini、o1 详细对比 |
| 3 | [Anthropic Claude 系列](./03-llm-models-research/03-anthropic-claude.md) | Claude 3.7 Extended Thinking、3.5 系列 |
| 4 | [Google Gemini 系列](./03-llm-models-research/04-google-gemini.md) | Gemini 2.5 Pro/Flash、Gemma 3（MIT）|
| 5 | [Meta LLaMA 系列](./03-llm-models-research/05-meta-llama.md) | LLaMA 4 Scout（10M上下文）/ Maverick 与 3.x |
| 6 | [阿里通义千问（Qwen）](./03-llm-models-research/06-alibaba-qwen.md) | Qwen3 双模式 MoE、QwQ-32B 推理开源 |
| 7 | [DeepSeek 系列](./03-llm-models-research/07-deepseek.md) | V3-0324 更新、R1 蒸馏、精确成本定价 |
| 8 | [其他主要模型](./03-llm-models-research/08-other-major-models.md) | **Kimi k2**、**Doubao**、**GLM-Z1**、Grok-3、Hunyuan-T1、Mistral Small 3.1、ERNIE 4.5 |
| 9 | [全面模型对比表格](./03-llm-models-research/09-model-comparison.md) | **¥/百万token 完整定价表**，30+ 模型横向对比 |
| 10 | [模型选型指南](./03-llm-models-research/10-model-selection-guide.md) | 场景化决策树、Java 集成策略与成本控制 |
| 11 | [微调候选模型](./03-llm-models-research/11-fine-tuning-candidates.md) | 微调方法对比、候选模型推荐、工具链指南 |
| 12 | [模型发展趋势](./03-llm-models-research/12-model-trends.md) | 推理革命、价格战、MoE 普及与市场预测 |

---

## 04 - Agent 框架 ✅（已完善）

> 模块入口：[04-agent-frameworks.md](./04-agent-frameworks.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [LangChain 概述](./04-agent-frameworks/01-langchain-overview.md) | LangChain 核心概念与生态 |
| 2 | [LangChain in Java](./04-agent-frameworks/02-langchain-in-java.md) | Java 集成实践（LangChain4j、Spring AI）|
| 3 | [Semantic Kernel](./04-agent-frameworks/03-semantic-kernel.md) | 微软 Semantic Kernel 框架 |
| 4 | [LlamaIndex](./04-agent-frameworks/04-llamaindex.md) | RAG 专项框架 |
| 5 | [自定义 Agent 开发](./04-agent-frameworks/05-custom-agent-development.md) | 不依赖框架的 Agent 工程实践 |
| 6 | [框架对比 2026](./04-agent-frameworks/06-frameworks-comparison-2026.md) | 2026年最新框架趋势与选型指南 |

---

## 05 - LLM APIs 与服务提供商 ✅（已完善）

> 模块入口：[05-llm-apis-providers.md](./05-llm-apis-providers.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [OpenAI API](./05-llm-apis-providers/01-openai-api.md) | ChatCompletion、Embedding、Fine-tuning API |
| 2 | [Azure OpenAI](./05-llm-apis-providers/02-azure-openai.md) | 企业级 Azure 部署与鉴权 |
| 3 | [Anthropic Claude API](./05-llm-apis-providers/03-anthropic-claude.md) | Messages API 与 Claude 特性 |
| 4 | [Google Gemini API](./05-llm-apis-providers/04-google-gemini.md) | Vertex AI 与 Gemini API |
| 5 | [本地 LLM 部署](./05-llm-apis-providers/05-local-llms.md) | Ollama、vLLM、LM Studio |
| 6 | [API 集成模式](./05-llm-apis-providers/06-api-integration-patterns.md) | 统一客户端抽象、熔断、限流、缓存 |

---

## 06 - RAG / 知识检索 ✅（已完善）

> 模块入口：[06-rag-knowledge-retrieval.md](./06-rag-knowledge-retrieval.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [RAG 基础概念](./06-rag-knowledge-retrieval/01-rag-basics.md) | 什么是 RAG、核心流程、与 Fine-tuning 对比 |
| 2 | [Embedding 模型选型](./06-rag-knowledge-retrieval/02-embedding-models.md) | OpenAI/BGE/M3E/Jina 对比与 Java 集成 |
| 3 | [向量数据库对比](./06-rag-knowledge-retrieval/03-vector-databases.md) | Milvus/Pinecone/Weaviate/Qdrant 选型 |
| 4 | [检索策略与优化](./06-rag-knowledge-retrieval/04-retrieval-strategies.md) | Dense/Sparse/Hybrid 检索与重排序 |
| 5 | [RAG 架构演进](./06-rag-knowledge-retrieval/05-rag-architectures.md) | Naive → Advanced → Modular RAG |
| 6 | [文档处理与分块](./06-rag-knowledge-retrieval/06-document-processing.md) | 文档加载、分块策略、元数据管理 |
| 7 | [RAG 评估与优化](./06-rag-knowledge-retrieval/07-rag-evaluation.md) | 评估指标、常见问题、优化策略 |
| 8 | [Java 实战](./06-rag-knowledge-retrieval/08-java-rag-practice.md) | Spring Boot + Milvus 完整项目 |

---

## 07 - 多智能体系统 ✅（已完善）

> 模块入口：[07-multi-agent-systems.md](./07-multi-agent-systems.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [多智能体基础](./07-multi-agent-systems/01-multi-agent-basics.md) | 概念、单 Agent vs 多 Agent、核心术语 |
| 2 | [Agent 通信机制](./07-multi-agent-systems/02-agent-communication.md) | 对话式、共享内存、消息队列 |
| 3 | [协作模式](./07-multi-agent-systems/03-coordination-patterns.md) | 分工、竞争、层级、市场模式 |
| 4 | [主流框架对比](./07-multi-agent-systems/04-frameworks-comparison.md) | AutoGen/CrewAI/LangGraph/MetaGPT |
| 5 | [任务分解与规划](./07-multi-agent-systems/05-task-decomposition.md) | 分解策略、规划算法、动态调整 |
| 6 | [冲突解决](./07-multi-agent-systems/06-conflict-resolution.md) | 冲突类型、检测机制、解决策略 |
| 7 | [Java 实战](./07-multi-agent-systems/07-java-multi-agent-practice.md) | Spring Boot 多智能体系统实现 |

---

## 08 - 模型安全与对齐 ✅（已完善）

> 模块入口：[08-model-safety-alignment.md](./08-model-safety-alignment.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [安全概述](./08-model-safety-alignment/01-safety-overview.md) | LLM 安全风险、安全生命周期 |
| 2 | [Prompt 注入](./08-model-safety-alignment/02-prompt-injection.md) | 攻击类型、防御策略、Java 实现 |
| 3 | [输出审查](./08-model-safety-alignment/03-output-safety.md) | 有害内容检测、PII 过滤 |
| 4 | [隐私保护](./08-model-safety-alignment/04-privacy-protection.md) | PII 检测、数据脱敏、差分隐私 |
| 5 | [越狱防御](./08-model-safety-alignment/05-jailbreak-defense.md) | 攻击类型、多层防御策略 |
| 6 | [幻觉缓解](./08-model-safety-alignment/06-hallucination-mitigation.md) | 检测方法、缓解策略 |
| 7 | [Java 实战](./08-model-safety-alignment/07-java-safety-practice.md) | Spring Boot 安全系统实现 |

---

## 09 - 性能优化与监控 ✅（已完善）

> 模块入口：[09-performance-monitoring.md](./09-performance-monitoring.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [性能指标](./09-performance-monitoring/01-performance-metrics.md) | TTFT、TPS、RPM、成本指标 |
| 2 | [缓存策略](./09-performance-monitoring/02-caching-strategies.md) | 三级缓存架构（本地+Redis+语义）|
| 3 | [流式优化](./09-performance-monitoring/03-streaming-optimization.md) | SSE、WebSocket、增量渲染 |
| 4 | [成本优化](./09-performance-monitoring/04-cost-optimization.md) | 模型路由、Token 优化、批量处理 |
| 5 | [可观测性](./09-performance-monitoring/05-observability.md) | Logging、Tracing、Metrics、工具推荐 |
| 6 | [Java 实战](./09-performance-monitoring/06-java-performance-practice.md) | Micrometer + Prometheus + Grafana |

---

## 10 - 实战案例集 ✅（已完善）

> 模块入口：[10-practical-cases.md](./10-practical-cases.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [代码生成助手](./10-practical-cases/01-code-assistant.md) | 代码补全、审查、重构助手实现 |
| 2 | [智能客服机器人](./10-practical-cases/02-customer-service-bot.md) | RAG + 多轮对话客服系统 |
| 3 | [文档问答系统](./10-practical-cases/03-doc-qa-system.md) | 企业知识库问答系统 |
| 4 | [SQL 生成助手](./10-practical-cases/04-sql-assistant.md) | Text2SQL 实现与验证 |
| 5 | [数据分析助手](./10-practical-cases/05-data-analysis-assistant.md) | 数据可视化分析助手 |

---

## 更新日志

| 日期 | 更新内容 |
|------|---------|
| 2026-03-09 | ✅ 完善 04 Agent 框架模块（5篇文档）|
| 2026-03-09 | ✅ 完善 05 LLM APIs 模块（6篇文档）|
| 2026-03-09 | ✅ 更新 03 LLM 模型研究（时间线、最新动态）|
| 2026-03-10 | ✅ 更新 03 模块：添加 2026年2月最新模型（Gemini 3.1 Pro、Claude Opus 4.6、GPT-5.2）|
| 2026-03-10 | ✅ 新增 04 模块：2026年框架对比与趋势分析|
| 2026-03-10 | ✅ **新增 06-10 模块**：RAG、多智能体、安全、性能优化、实战案例（35+篇文档）|

---

## 快速上手

### 入门路径

```
第一阶段（基础）：
01 Agent 基础 → 02 LLM 基础 → 04 Agent 框架 → 05 APIs
                      ↓
              03 LLM 模型研究（按需查阅）

第二阶段（进阶）：
06 RAG 知识检索 → 07 多智能体系统 → 08 模型安全与对齐
                      ↓
              09 性能优化与监控

第三阶段（实战）：
10 实战案例集（代码助手、客服机器人、文档问答等）
```

### 学习建议

| 目标 | 推荐路径 |
|------|---------|
| 快速入门 | 01 → 02 → 04 → 05 |
| 构建 RAG 应用 | 02 → 03 → 06 → 10 |
| 生产级部署 | 06 → 07 → 08 → 09 → 10 |
| 全栈掌握 | 按顺序完成所有模块 |

---

## 关于 03 模块的说明

03 LLM 模型研究模块已包含较完整的 2025 年模型信息，包括：
- GPT-4.1 / o3 / o4-mini
- Claude 3.7 / Gemini 2.5
- DeepSeek-V3 / Qwen3 / Kimi k2

**已更新**：2025年3月后时间线（Gemini 2.5 Flash、GPT-4.5 等）

如需进一步更新特定模型信息，请告诉我具体需要补充哪些内容。
