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

| # | 文档 | 简介 |
|---|------|------|
| 1 | [LLM 市场全景概览](./03-llm-models-research/01-llm-landscape-overview.md) | 全局分类、市场格局、主流模型一览与选型维度 |
| 2 | [OpenAI GPT 系列](./03-llm-models-research/02-openai-gpt-series.md) | GPT-4o、GPT-4o mini、o1、o3-mini 详细调研与对比 |
| 3 | [Anthropic Claude 系列](./03-llm-models-research/03-anthropic-claude.md) | Claude 3/3.5/3.7 系列特性、安全机制与集成指南 |
| 4 | [Google Gemini 系列](./03-llm-models-research/04-google-gemini.md) | Gemini 2.0 Flash、1.5 Pro、Gemma 开源模型 |
| 5 | [Meta LLaMA 系列](./03-llm-models-research/05-meta-llama.md) | LLaMA 3.1/3.2/3.3 规格、部署与微调指南 |
| 6 | [阿里通义千问（Qwen）](./03-llm-models-research/06-alibaba-qwen.md) | Qwen2.5 全系列、代码/数学专项、中文优势 |
| 7 | [DeepSeek 系列](./03-llm-models-research/07-deepseek.md) | DeepSeek-V3、R1 技术突破、超低成本分析 |
| 8 | [其他主要模型](./03-llm-models-research/08-other-major-models.md) | Mistral、Phi、ERNIE、混元、ChatGLM、Yi、InternLM 等 |
| 9 | [全面模型对比表格](./03-llm-models-research/09-model-comparison.md) | 多维度横向对比：性能、成本、延迟、上下文、合规 |
| 10 | [模型选型指南](./03-llm-models-research/10-model-selection-guide.md) | 场景化决策树、Java 集成策略与成本控制 |
| 11 | [微调候选模型](./03-llm-models-research/11-fine-tuning-candidates.md) | 微调方法对比、候选模型推荐、工具链指南 |
| 12 | [模型发展趋势](./03-llm-models-research/12-model-trends.md) | 推理革命、MoE 架构、多模态演进与市场预测 |

---

## 04 - Agent 框架

> 模块入口：[04-agent-frameworks.md](./04-agent-frameworks.md)

| # | 文档 | 简介 |
|---|------|------|
| 1 | [LangChain 概述](./04-agent-frameworks/01-langchain-overview.md) | LangChain 核心概念与生态 |
| 2 | [LangChain in Java](./04-agent-frameworks/02-langchain-in-java.md) | Java 集成实践 |
| 3 | [Semantic Kernel](./04-agent-frameworks/03-semantic-kernel.md) | 微软 Semantic Kernel 框架 |
| 4 | [LlamaIndex](./04-agent-frameworks/04-llamaindex.md) | RAG 专项框架 |
| 5 | [自定义 Agent 开发](./04-agent-frameworks/05-custom-agent-development.md) | 不依赖框架的 Agent 工程实践 |

---

## 05 - LLM APIs 与服务提供商

| # | 文档 | 简介 |
|---|------|------|
| 1 | [OpenAI API](./05-llm-apis-providers/01-openai-api.md) | ChatCompletion、Embedding、Fine-tuning API |
| 2 | [Azure OpenAI](./05-llm-apis-providers/02-azure-openai.md) | 企业级 Azure 部署与鉴权 |
| 3 | [Anthropic Claude API](./05-llm-apis-providers/03-anthropic-claude.md) | Messages API 与 Claude 特性 |
| 4 | [Google Gemini API](./05-llm-apis-providers/04-google-gemini.md) | Vertex AI 与 Gemini API |
| 5 | [本地 LLM 部署](./05-llm-apis-providers/05-local-llms.md) | Ollama、vLLM、LM Studio |
| 6 | [API 集成模式](./05-llm-apis-providers/06-api-integration-patterns.md) | 统一客户端抽象、熔断、限流 |

---

## 快速上手

```
入门路径：
01 Agent 基础 → 02 LLM 基础 → 04 Agent 框架 → 05 APIs
                   ↓
           03 LLM 模型研究（按需查阅）
```
