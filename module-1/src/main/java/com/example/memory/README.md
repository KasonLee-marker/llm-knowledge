# memory - 记忆管理

本包提供 Agent 的短期记忆（对话历史）管理功能。

## 类说明

| 类 | 说明 |
|---|------|
| `MessageRole` | 消息角色枚举：SYSTEM、USER、ASSISTANT、TOOL |
| `Message` | 对话消息实体，包含角色、内容和工具调用 ID |
| `ShortTermMemory` | 短期记忆管理器，维护对话历史列表，支持容量限制与自动淘汰 |

## 特点

- **容量控制**：超出 `maxMessages` 限制时自动淘汰最早的非系统消息
- **系统消息保护**：`clear()` 和淘汰机制均保留系统消息
- **防御性拷贝**：`getMessages()` 返回不可修改的列表视图

## 对应文档

- [01-what-is-agent.md](../../01-agent-basics/01-what-is-agent.md)
- [05-agent-memory.md](../../01-agent-basics/05-agent-memory.md)
