# 02 - LLM Fundamentals（LLM 基础）

本模块系统介绍大语言模型（LLM）的核心基础概念，包括 Token、Context 窗口、Prompt 工程、Function Calling、推理技术、Embedding 向量与输出控制，帮助开发者建立完整的 LLM 工程知识体系。

## 目录

1. [Tokens 与 Context 窗口](./02-llm-fundamentals/01-tokens-and-context.md)
2. [Prompt 工程（Prompt Engineering）](./02-llm-fundamentals/02-prompt-engineering.md)
3. [Function Calling（函数调用）](./02-llm-fundamentals/03-function-calling.md)
4. [推理技术（Reasoning Techniques）](./02-llm-fundamentals/04-reasoning-techniques.md)
5. [Embeddings 与向量表示](./02-llm-fundamentals/05-embeddings-and-vectors.md)
6. [LLM 输出控制（Output Control）](./02-llm-fundamentals/06-llm-output-control.md)

## 核心概念速览

| 概念 | 说明 |
|------|------|
| Token | LLM 处理文本的基本单位（子词/subword），计费和长度限制均以 token 为单位 |
| Context 窗口 | 模型在一次调用中可处理的最大 token 数（输入+输出共享） |
| Prompt Engineering | 通过精心设计提示词来最大化 LLM 输出质量的工程实践 |
| Function Calling | LLM 动态决定调用哪个外部函数及其参数的机制 |
| Chain-of-Thought | 让模型显式输出推理步骤，提升复杂任务准确率 |
| Embedding | 将文本映射为高维数值向量，语义相近的文本向量距离更近 |
| RAG | 检索增强生成：将外部知识检索后注入 LLM 上下文 |
| Temperature | 控制 LLM 输出随机性的核心参数（0=确定，1=随机）|
| Structured Output | 强制 LLM 按照 JSON Schema 输出结构化结果 |

## 学习路径建议

```
Token & Context → Prompt Engineering → Function Calling
      ↓                                      ↓
 理解基础概念         掌握工程实践           集成外部工具
                                             ↓
Embeddings & RAG ← Reasoning Techniques ← Output Control
      ↓
构建知识库与语义搜索
```

## 与其他模块的关系

- 本模块为 [01 - Agent Basics](./01-agent-basics.md) 提供基础技术支撑
  - Function Calling → [Agent 工具](./01-agent-basics/04-agent-tools.md)
  - Embeddings/RAG → [Agent 记忆](./01-agent-basics/05-agent-memory.md)
  - Reasoning Techniques → [LLM 驱动的 Agent](./01-agent-basics/03-llm-agents.md)
- 深入框架实践见 [04 - Agent Frameworks](./04-agent-frameworks.md)
