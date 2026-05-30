# agent - Agent 接口与基础实现

本包提供 LLM Agent 的核心接口定义和 ReAct 模式的简化实现。

## 类说明

| 类 | 说明 |
|---|------|
| `AgentDecision` | Agent 决策结果，包含推理过程、是否完成、最终答案或工具调用信息 |
| `ReActAgent` | 简化版 ReAct Agent，实现 Reasoning + Acting 交替的决策循环 |

## ReAct 模式

ReAct（Reasoning + Acting）是 LLM Agent 的核心模式：

1. **Thought（推理）**：分析用户输入，决定下一步行动
2. **Action（行动）**：调用工具执行操作
3. **Observation（观察）**：获取工具返回结果
4. 重复 1-3，直到生成最终答案

本实现使用规则引擎模拟 LLM 推理，最大决策步数为 5 步。

## 对应文档

- [01-what-is-agent.md](../../01-agent-basics/01-what-is-agent.md)
- [02-agent-architecture.md](../../01-agent-basics/02-agent-architecture.md)
- [03-llm-agents.md](../../01-agent-basics/03-llm-agents.md)
