# 10 - 实战案例集（Practical Cases）

本模块通过 5 个完整的实战案例，展示如何将 LLM 技术应用于实际业务场景。每个案例包含完整的需求分析、架构设计、核心功能实现和 Java 代码示例，帮助开发者快速掌握 LLM 应用开发的核心技能。

## 目录

| # | 文档 | 简介 |
|---|------|------|
| 1 | [代码生成助手](./10-practical-cases/01-code-assistant.md) | 基于 LLM 的智能代码生成、补全与优化工具 |
| 2 | [智能客服机器人](./10-practical-cases/02-customer-service-bot.md) | RAG + 多轮对话的企业级客服解决方案 |
| 3 | [文档问答系统](./10-practical-cases/03-doc-qa-system.md) | 基于私有文档的知识问答平台 |
| 4 | [SQL 生成助手](./10-practical-cases/04-sql-assistant.md) | 自然语言转 SQL 的智能数据库查询工具 |
| 5 | [数据分析助手](./10-practical-cases/05-data-analysis-assistant.md) | 自动化数据分析与可视化报告生成 |

## 实战案例架构总览

```mermaid
graph TB
    subgraph "10 - 实战案例集"
        direction TB
        
        subgraph "案例层"
            CA[01-代码生成助手]
            CS[02-智能客服机器人]
            DQ[03-文档问答系统]
            SA[04-SQL生成助手]
            DA[05-数据分析助手]
        end
        
        subgraph "技术基础层"
            LLM[LLM API<br/>OpenAI/Claude/通义]
            RAG[RAG 检索<br/>向量数据库]
            MEM[记忆管理<br/>上下文持久化]
            TOOL[工具调用<br/>Function Calling]
        end
        
        subgraph "基础设施层"
            SB[Spring Boot<br/>微服务框架]
            DB[(关系型数据库<br/>MySQL/PostgreSQL)]
            VDB[(向量数据库<br/>Milvus/PGVector)]
            CACHE[(缓存<br/>Redis)]
            MQ[消息队列<br/>RabbitMQ/Kafka]
        end
    end
    
    CA --> LLM
    CA --> TOOL
    CS --> LLM
    CS --> RAG
    CS --> MEM
    DQ --> LLM
    DQ --> RAG
    SA --> LLM
    SA --> TOOL
    DA --> LLM
    DA --> TOOL
    
    LLM --> SB
    RAG --> VDB
    MEM --> CACHE
    MEM --> DB
    TOOL --> DB
    
    SB --> DB
    SB --> CACHE
    SB --> MQ
    
    style CA fill:#e3f2fd
    style CS fill:#f3e5f5
    style DQ fill:#e8f5e9
    style SA fill:#fff3e0
    style DA fill:#fce4ec
    style LLM fill:#ffebee
    style RAG fill:#ffebee
    style MEM fill:#ffebee
    style TOOL fill:#ffebee
```

## 案例技术栈对比

```mermaid
graph LR
    subgraph "技术栈矩阵"
        direction TB
        
        Tech[技术组件] --> LLM[LLM API]
        Tech --> RAG[RAG检索]
        Tech --> MEM[记忆管理]
        Tech --> TOOL[工具调用]
        Tech --> VIZ[数据可视化]
        
        LLM --> CA[✓ 代码生成]
        LLM --> CS[✓ 智能客服]
        LLM --> DQ[✓ 文档问答]
        LLM --> SA[✓ SQL生成]
        LLM --> DA[✓ 数据分析]
        
        RAG --> CA[✗]
        RAG --> CS[✓]
        RAG --> DQ[✓]
        RAG --> SA[✗]
        RAG --> DA[✗]
        
        MEM --> CA[✗]
        MEM --> CS[✓]
        MEM --> DQ[✗]
        MEM --> SA[✗]
        MEM --> DA[✗]
        
        TOOL --> CA[✓]
        TOOL --> CS[✗]
        TOOL --> DQ[✗]
        TOOL --> SA[✓]
        TOOL --> DA[✓]
        
        VIZ --> CA[✗]
        VIZ --> CS[✗]
        VIZ --> DQ[✗]
        VIZ --> SA[✗]
        VIZ --> DA[✓]
    end
    
    style CA fill:#e3f2fd
    style CS fill:#f3e5f5
    style DQ fill:#e8f5e9
    style SA fill:#fff3e0
    style DA fill:#fce4ec
```

## 各案例核心能力

### 01 - 代码生成助手
- **核心能力**: 代码生成、代码补全、代码解释、代码优化
- **技术亮点**: Prompt 工程、Function Calling、代码解析
- **适用场景**: 开发者工具、IDE 插件、代码审查

### 02 - 智能客服机器人
- **核心能力**: 意图识别、多轮对话、知识检索、工单创建
- **技术亮点**: RAG、对话状态管理、情感分析
- **适用场景**: 企业客服、售后支持、售前咨询

### 03 - 文档问答系统
- **核心能力**: 文档解析、知识检索、问答生成、引用溯源
- **技术亮点**: 文档分块、Embedding、向量检索
- **适用场景**: 企业知识库、产品文档、技术手册

### 04 - SQL 生成助手
- **核心能力**: 自然语言理解、Schema 解析、SQL 生成、结果验证
- **技术亮点**: Text2SQL、数据库元数据、查询优化
- **适用场景**: 数据查询、BI 工具、报表生成

### 05 - 数据分析助手
- **核心能力**: 数据理解、分析建议、可视化生成、报告撰写
- **技术亮点**: 数据分析 Agent、图表生成、Markdown 报告
- **适用场景**: 业务分析、数据探索、自动化报告

## 学习路径建议

```mermaid
graph TD
    A[01-代码生成助手<br/>基础LLM应用] --> B[04-SQL生成助手<br/>工具调用进阶]
    A --> C[03-文档问答系统<br/>RAG基础应用]
    C --> D[02-智能客服机器人<br/>RAG+对话管理]
    B --> E[05-数据分析助手<br/>综合应用]
    D --> E
    
    style A fill:#e3f2fd
    style B fill:#fff3e0
    style C fill:#e8f5e9
    style D fill:#f3e5f5
    style E fill:#fce4ec
```

## 前置知识要求

在学习本模块之前，建议先掌握以下内容：

1. **[02 - LLM 基础](./02-llm-fundamentals.md)**: 理解 LLM 基本原理和 API 使用
2. **[04 - Agent 框架](./04-agent-frameworks.md)**: 掌握 Agent 设计模式
3. **[06 - RAG / 知识检索](./06-rag-knowledge-retrieval.md)**: 了解 RAG 架构（案例 02、03 需要）
4. **Java/Spring Boot**: 具备基础的后端开发能力

## 项目结构参考

每个案例的完整项目结构：

```
code-assistant/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── config/          # 配置类
│   │   │       ├── controller/      # API 接口
│   │   │       ├── service/         # 业务逻辑
│   │   │       ├── domain/          # 领域模型
│   │   │       ├── repository/      # 数据访问
│   │   │       └── client/          # LLM 客户端
│   │   └── resources/
│   │       ├── application.yml      # 配置文件
│   │       └── prompts/             # Prompt 模板
│   └── test/                        # 测试代码
├── pom.xml                          # Maven 配置
└── README.md                        # 项目说明
```

## 推荐开发环境

- **JDK**: 17 或更高版本
- **Spring Boot**: 3.2.x
- **构建工具**: Maven 或 Gradle
- **数据库**: PostgreSQL / MySQL
- **向量数据库**: Milvus / PGVector / Chroma
- **缓存**: Redis
- **LLM API**: OpenAI / 通义千问 / Claude

## 与其他模块的关系

- 本模块依赖 [02 - LLM 基础](./02-llm-fundamentals.md) 的 API 调用知识
- 本模块依赖 [04 - Agent 框架](./04-agent-frameworks.md) 的设计模式
- 案例 02、03 依赖 [06 - RAG / 知识检索](./06-rag-knowledge-retrieval.md)
- 案例 05 可与 [07 - 多智能体系统](./07-multi-agent-systems.md) 结合扩展

---

> 📌 详细内容见各子章节，每个案例都包含完整的可运行代码示例。
