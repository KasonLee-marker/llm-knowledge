# 01 - Agent Basics（Agent 基础）

本模块系统介绍 AI Agent 的核心概念、架构与工程实践，重点面向 Java 后端开发者构建 LLM 驱动的 Agent 应用。

## 目录

1. [什么是 Agent](./01-agent-basics/what-is-agent.md)
2. [Agent 架构：感知-推理-行动循环](./01-agent-basics/02-agent-architecture.md)
3. [LLM 驱动的 Agent（ReAct、规划等）](./01-agent-basics/03-llm-agents.md)
4. [Agent 工具（Tools / Function Calling）](./01-agent-basics/04-agent-tools.md)
5. [Agent 记忆（Memory）](./01-agent-basics/05-agent-memory.md)

## 核心概念速览

| 概念 | 说明 |
|------|------|
| Agent | 能感知环境并自主采取行动以完成目标的实体 |
| LLM Agent | 以大语言模型为推理核心的智能 Agent |
| 工具（Tool） | Agent 可调用的外部能力（API、数据库、代码执行等）|
| 记忆（Memory）| Agent 存储和检索上下文信息的机制 |
| 规划（Planning）| Agent 将复杂目标分解为可执行步骤的能力 |
| ReAct | Reasoning + Acting：交替推理与行动的经典 Agent 模式 |

## 示例代码

Java 示例代码位于 [`module-1/src/main/java/com/example/`](./module-1/src/main/java/com/example/)，涵盖：
- 基础 Agent 接口与实现
- 工具调用（Tool）封装
- ReAct 模式的简化实现
- 简单记忆管理（短期/长期）