# module-1 - Java 示例代码模块

> 本目录是 `llm-knowledge` 知识库的配套 Java 示例代码模块，提供可运行的工程实践代码。

---

## 用途说明

本模块对应 **01-agent-basics** 章节的示例代码，包含：

- **Agent 基础接口与实现** — `Agent`、`Tool`、`Memory` 等核心接口定义
- **工具调用（Tool / Function Calling）封装** — 基于 LangChain4j 或 Spring AI 的工具调用示例
- **ReAct 模式实现** — Reasoning + Acting 的简化示例
- **记忆管理** — 短期记忆（对话历史）和长期记忆（向量存储）的示例

---

## 项目结构

```
module-1/
├── pom.xml                          # Maven 依赖配置
└── src/
    └── main/
        └── java/
            └── com/example/
                ├── agent/           # Agent 接口与基础实现
                ├── tool/            # 工具（Tool）封装
                ├── memory/          # 记忆管理
                └── react/           # ReAct 模式示例
```

---

## 快速开始

### 依赖

- Java 17+
- Spring Boot 3.x
- LangChain4j 0.35+ 或 Spring AI 1.0+

### 构建与运行

```bash
cd module-1
mvn clean compile
mvn spring-boot:run
```

---

## 与文档的对应关系

| 代码目录 | 对应文档 |
|---------|---------|
| `agent/` | [01-what-is-agent.md](../01-agent-basics/01-what-is-agent.md) |
| `agent/` | [02-agent-architecture.md](../01-agent-basics/02-agent-architecture.md) |
| `tool/` | [04-agent-tools.md](../01-agent-basics/04-agent-tools.md) |
| `memory/` | [05-agent-memory.md](../01-agent-basics/05-agent-memory.md) |
| `react/` | [03-llm-agents.md](../01-agent-basics/03-llm-agents.md) |

---

> 💡 **提示**：本模块代码仅作为学习参考，生产环境使用请参考各文档中的最佳实践章节。
