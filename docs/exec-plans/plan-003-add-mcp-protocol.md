# 任务：新增 MCP（Model Context Protocol）文档

**优先级**：P1
**目标模块**：01-agent-basics
**创建时间**：2026-04-03
**状态**：✅ 已完成

## 任务描述

MCP（Model Context Protocol）是 Anthropic 于 2024 年底推出的开放协议，用于标准化 LLM 与外部数据源和工具的连接方式。2025-2026 年 MCP 已成为 Agent 工具调用和上下文传递的重要标准，需要在 `01-agent-basics` 模块中创建专题文档。

### 涉及文件

| 文件 | 内容 |
|------|------|
| `01-agent-basics/07-model-context-protocol.md` | MCP 完整专题文档（新增）|
| `01-agent-basics.md` | 更新目录导航，添加 MCP 条目 |
| `docs/coverage-matrix.md` | 更新 01 模块文档数从 6 到 7 |

## 验收标准

- [x] MCP 协议概述与设计理念
- [x] MCP 三层架构（Host、Client、Server）详解
- [x] 四大核心能力（Resources、Tools、Prompts、Sampling）
- [x] MCP 与 Function Calling 的关系和区别（对比表格）
- [x] Java 集成实践（Spring AI MCP、LangChain4j MCP）
- [x] 完整 Java 代码示例（MCP Server、MCP Client）
- [x] 最佳实践和常见问题
- [x] mermaid 架构图和时序图
- [x] `docs/coverage-matrix.md` 对应条目已更新

## 参考资源

- [MCP 官方文档](https://modelcontextprotocol.io/)
- [MCP GitHub](https://github.com/modelcontextprotocol/servers)
- [Spring AI MCP 集成](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [LangChain4j MCP 支持](https://docs.langchain4j.dev/integrations/mcp)

## 完成记录

| 日期 | 操作 | 内容 |
|------|------|------|
| 2026-04-03 | ✅ 完成 | 创建 07-model-context-protocol.md，完整 770+ 行文档 |
