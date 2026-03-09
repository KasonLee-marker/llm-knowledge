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

### 商业模型

| 文档 | 说明 |
|------|------|
| [OpenAI GPT 系列](./03-llm-models-research/commercial-models/openai-gpt-series.md) | GPT-4o、GPT-4 等系列对比 |
| [Anthropic Claude](./03-llm-models-research/commercial-models/anthropic-claude.md) | Claude 3 系列特性 |
| [Google Gemini](./03-llm-models-research/commercial-models/google-gemini.md) | Gemini 1.5 Pro/Flash 等 |
| [Meta LLaMA](./03-llm-models-research/commercial-models/meta-llama.md) | LLaMA 3 系列 |
| [阿里 Qwen](./03-llm-models-research/commercial-models/alibaba-qwen.md) | Qwen 系列 |
| [百度文心](./03-llm-models-research/commercial-models/baidu-ernie.md) | ERNIE 系列 |
| [腾讯混元](./03-llm-models-research/commercial-models/tencent-models.md) | 混元系列 |
| [Microsoft Phi](./03-llm-models-research/commercial-models/microsoft-phi.md) | Phi 小模型系列 |

### 开源模型

| 文档 | 说明 |
|------|------|
| [DeepSeek](./03-llm-models-research/open-source-models/deepseek.md) | DeepSeek 系列 |
| [ChatGLM](./03-llm-models-research/open-source-models/chatglm.md) | 清华 GLM 系列 |
| [Mistral 系列](./03-llm-models-research/open-source-models/mistral-series.md) | Mistral、Mixtral |
| [Yi 系列](./03-llm-models-research/open-source-models/yi-models.md) | 零一万物 Yi 模型 |
| [InternLM](./03-llm-models-research/open-source-models/intern-llm.md) | 书生浦语系列 |

### 模型对比

| 文档 | 说明 |
|------|------|
| [能力矩阵](./03-llm-models-research/model-comparison/capability-matrix.md) | 多维度能力对比 |
| [Context 窗口分析](./03-llm-models-research/model-comparison/context-window-analysis.md) | 各模型上下文长度 |
| [成本性能比](./03-llm-models-research/model-comparison/cost-performance.md) | 价格与性能综合评估 |
| [延迟对比](./03-llm-models-research/model-comparison/latency-comparison.md) | TTFT 与吞吐量对比 |

### 其他

| 文档 | 说明 |
|------|------|
| [模型选型指南](./03-llm-models-research/model-selection-guide.md) | 场景化选型决策树 |
| [Fine-tuning 候选模型](./03-llm-models-research/fine-tuning-candidates.md) | 适合微调的模型推荐 |
| [模型发展趋势](./03-llm-models-research/model-trends.md) | 行业动态与技术方向 |

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
