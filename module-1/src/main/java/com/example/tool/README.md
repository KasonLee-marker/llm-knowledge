# tool - 工具（Tool）封装

本包提供 Agent 工具调用机制的完整封装。

## 类说明

| 类/接口 | 说明 |
|---------|------|
| `AgentTool` | 工具统一接口，定义 `getName()`、`getDescription()`、`execute()` |
| `WeatherTool` | 天气查询工具（模拟实现，查询城市天气） |
| `CalculatorTool` | 计算器工具，支持加减乘除四则运算 |
| `ToolExecutor` | 工具注册与执行中心，管理工具注册表并分发调用 |

## 特点

- **统一接口**：所有工具实现 `AgentTool` 接口，易于扩展
- **工具注册表**：`ToolExecutor` 管理工具注册、查找和执行
- **容错处理**：除数为零等异常场景返回友好错误信息

## 对应文档

- [04-agent-tools.md](../../01-agent-basics/04-agent-tools.md)
