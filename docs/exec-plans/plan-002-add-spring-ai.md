# 任务：新增 Spring AI 专题文档

**优先级**：P1
**目标模块**：04-agent-frameworks
**创建时间**：2026-04-03
**状态**：✅ 已完成

## 任务描述

`04-agent-frameworks` 模块目前缺少 Spring AI 的专题文档。Spring AI 是 Spring 官方推出的 AI 集成框架，是 2024-2026 年 Java 生态最重要的 AI 框架之一，需要为其创建完整的专题文档。

### 涉及文件

| 文件 | 内容 |
|------|------|
| `04-agent-frameworks/07-spring-ai.md` | Spring AI 完整专题文档（新增）|
| `04-agent-frameworks.md` | 更新目录导航，添加 Spring AI 条目 |
| `docs/coverage-matrix.md` | 更新 04 模块文档数从 6 到 7 |

## 验收标准

- [x] Spring AI 核心概念（ChatClient, EmbeddingModel, VectorStore, Advisor）
- [x] 与 Spring Boot 的集成方式（Auto-configuration, Starters）
- [x] 支持的模型提供商列表（OpenAI, Azure, Anthropic, Ollama 等）
- [x] 完整 Java 代码示例（包含 Maven 依赖、application.yml 配置）
- [x] 与 LangChain4j 的详细对比表格
- [x] 最佳实践和常见问题
- [x] `docs/coverage-matrix.md` 对应条目已更新

## 参考资源

- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [Spring AI GitHub](https://github.com/spring-projects/spring-ai)
- [Spring AI Starter 列表](https://docs.spring.io/spring-ai/reference/getting-started.html)

## 完成记录

| 日期 | 操作 | 内容 |
|------|------|------|
| 2026-04-03 | ✅ 完成 | 创建 07-spring-ai.md，完整 1000+ 行文档 |
