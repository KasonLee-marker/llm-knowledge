# 04 - Agent 框架

> 本章介绍主流的 AI Agent 开发框架，帮助 Java 后端开发者选择合适的工具。

---

## 目录

| # | 文档 | 简介 |
|---|------|------|
| 1 | [LangChain 概述](./04-agent-frameworks/01-langchain-overview.md) | LangChain 核心概念与生态 |
| 2 | [LangChain in Java](./04-agent-frameworks/02-langchain-in-java.md) | Java 集成实践（LangChain4j、Spring AI）|
| 3 | [Semantic Kernel](./04-agent-frameworks/03-semantic-kernel.md) | 微软 Semantic Kernel 框架 |
| 4 | [LlamaIndex](./04-agent-frameworks/04-llamaindex.md) | RAG 专项框架 |
| 5 | [自定义 Agent 开发](./04-agent-frameworks/05-custom-agent-development.md) | 不依赖框架的 Agent 工程实践 |

---

## 框架对比速查

| 框架 | 语言 | 定位 | 学习曲线 | 推荐场景 |
|------|------|------|---------|---------|
| **LangChain** | Python/JS | 通用框架 | ⭐⭐⭐ | 快速原型、复杂链 |
| **LangChain4j** | Java | Java 版 LangChain | ⭐⭐⭐ | Java 项目集成 |
| **Spring AI** | Java | Spring 官方 AI | ⭐⭐ | Spring Boot 项目 |
| **Semantic Kernel** | .NET/Python/Java | 微软官方 | ⭐⭐⭐ | Azure/OpenAI 用户 |
| **LlamaIndex** | Python | RAG 专用 | ⭐⭐⭐ | 复杂 RAG 应用 |

---

## Java 开发者选型建议

### 新项目
- **Spring Boot 技术栈** → Spring AI
- **非 Spring 项目** → LangChain4j

### 已有项目集成
- **轻量级集成** → LangChain4j
- **深度 AI 能力** → Spring AI + Spring Boot

### 特殊需求
- **Azure 云服务** → Semantic Kernel
- **复杂 RAG** → Python LlamaIndex（微服务化）
- **极致性能/定制** → 自定义 Agent

---

## 趋势与建议

1. **Spring AI 正在崛起**：Spring 官方背书，生态整合更好
2. **LangChain4j 成熟稳定**：功能丰富，社区活跃
3. **框架不是必须的**：简单场景可以直接调用 API
4. **关注标准化**：OpenAI API 格式已成为事实标准
