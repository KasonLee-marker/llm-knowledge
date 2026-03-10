# 07 - 多智能体系统（Multi-Agent Systems）

本模块系统介绍多智能体系统（Multi-Agent Systems, MAS）的核心概念、架构模式、通信机制与协作策略，重点面向 Java 后端开发者构建分布式智能 Agent 应用。

## 目录

| # | 文档 | 简介 |
|---|------|------|
| 1 | [多智能体基础](./07-multi-agent-systems/01-multi-agent-basics.md) | 什么是多智能体系统、单Agent vs 多Agent、核心概念 |
| 2 | [Agent 通信机制](./07-multi-agent-systems/02-agent-communication.md) | 对话式通信、共享内存、消息队列 |
| 3 | [协作模式](./07-multi-agent-systems/03-coordination-patterns.md) | 分工协作、竞争模式、层级结构 |
| 4 | [主流框架对比](./07-multi-agent-systems/04-frameworks-comparison.md) | AutoGen、CrewAI、LangGraph、MetaGPT 对比 |
| 5 | [任务分解与规划](./07-multi-agent-systems/05-task-decomposition.md) | 任务分解策略、规划算法 |
| 6 | [冲突解决](./07-multi-agent-systems/06-conflict-resolution.md) | 冲突类型、解决策略 |
| 7 | [Java 实战](./07-multi-agent-systems/07-java-multi-agent-practice.md) | Spring Boot 实现多智能体系统 |

## 核心概念速览

### 什么是多智能体系统？

多智能体系统（Multi-Agent System, MAS）是由多个自主 Agent 组成的分布式系统，这些 Agent 通过协作、通信和协调来完成单个 Agent 难以完成的复杂任务。

```mermaid
graph TB
    subgraph "多智能体系统"
        A[Agent A<br/>规划者]
        B[Agent B<br/>执行者]
        C[Agent C<br/>验证者]
        D[Agent D<br/>协调者]
    end
    
    User[用户] --> D
    D --> A
    D --> B
    D --> C
    A --> B
    B --> C
    C --> D
    
    style User fill:#e1f5ff
    style A fill:#fff3e0
    style B fill:#fff3e0
    style C fill:#fff3e0
    style D fill:#ffe0e0
```

### 单 Agent vs 多 Agent

| 维度 | 单 Agent | 多 Agent |
|------|----------|----------|
| 任务复杂度 | 适合简单、线性的任务 | 适合复杂、并行的任务 |
| 专业化程度 | 通用能力 | 各司其职，专业化处理 |
| 容错能力 | 单点故障 | 分布式容错 |
| 可扩展性 | 受限于单个 LLM 上下文 | 可水平扩展 |
| 开发复杂度 | 低 | 高 |
| 协调开销 | 无 | 需要通信和协调机制 |
| 适用场景 | 问答、简单工具调用 | 复杂工作流、团队协作模拟 |

### 多智能体核心优势

```mermaid
mindmap
  root((多智能体系统<br/>核心优势))
    专业化
      每个Agent专注特定领域
      更高的任务处理质量
      模块化设计
    并行化
      任务并行执行
      缩短总体耗时
      提高吞吐量
    可扩展性
      动态添加Agent
      水平扩展能力
      负载均衡
    容错性
      单点故障不影响整体
      任务可重新分配
      系统鲁棒性
    灵活性
      动态组建团队
      适应不同场景
      可配置协作模式
```

## 多智能体系统架构

```mermaid
graph TB
    subgraph "用户交互层"
        UI[用户界面/API]
    end
    
    subgraph "协调层"
        Orchestrator[协调器<br/>Orchestrator]
        TaskPlanner[任务规划器<br/>Task Planner]
    end
    
    subgraph "Agent 层"
        Agent1[Agent 1<br/>角色: 分析师]
        Agent2[Agent 2<br/>角色: 开发者]
        Agent3[Agent 3<br/>角色: 测试员]
        Agent4[Agent 4<br/>角色: 审查员]
    end
    
    subgraph "通信层"
        MessageBus[消息总线<br/>Message Bus]
        SharedMemory[共享内存<br/>Shared Memory]
    end
    
    subgraph "工具层"
        Tools[工具集<br/>Tools]
        KnowledgeBase[知识库<br/>Knowledge Base]
    end
    
    UI --> Orchestrator
    Orchestrator --> TaskPlanner
    TaskPlanner --> Agent1
    TaskPlanner --> Agent2
    TaskPlanner --> Agent3
    TaskPlanner --> Agent4
    
    Agent1 --> MessageBus
    Agent2 --> MessageBus
    Agent3 --> MessageBus
    Agent4 --> MessageBus
    
    Agent1 --> SharedMemory
    Agent2 --> SharedMemory
    Agent3 --> SharedMemory
    Agent4 --> SharedMemory
    
    Agent1 --> Tools
    Agent2 --> Tools
    Agent3 --> Tools
    Agent4 --> Tools
    
    Agent1 --> KnowledgeBase
    Agent2 --> KnowledgeBase
    Agent3 --> KnowledgeBase
    Agent4 --> KnowledgeBase
    
    style Orchestrator fill:#ffe0e0
    style TaskPlanner fill:#ffe0e0
    style Agent1 fill:#fff3e0
    style Agent2 fill:#fff3e0
    style Agent3 fill:#fff3e0
    style Agent4 fill:#fff3e0
    style MessageBus fill:#e8f5e9
    style SharedMemory fill:#e8f5e9
```

## 典型应用场景

```mermaid
graph LR
    subgraph "软件开发"
        A1[需求分析Agent] --> A2[架构设计Agent]
        A2 --> A3[编码Agent]
        A3 --> A4[测试Agent]
        A4 --> A5[审查Agent]
    end
    
    subgraph "客户服务"
        B1[意图识别Agent] --> B2[路由Agent]
        B2 --> B3[技术支持Agent]
        B2 --> B4[销售Agent]
        B2 --> B5[投诉处理Agent]
    end
    
    subgraph "数据分析"
        C1[数据采集Agent] --> C2[清洗Agent]
        C2 --> C3[分析Agent]
        C3 --> C4[可视化Agent]
        C3 --> C5[报告Agent]
    end
    
    style A1 fill:#e3f2fd
    style A2 fill:#e3f2fd
    style A3 fill:#e3f2fd
    style A4 fill:#e3f2fd
    style A5 fill:#e3f2fd
    style B1 fill:#f3e5f5
    style B2 fill:#f3e5f5
    style B3 fill:#f3e5f5
    style B4 fill:#f3e5f5
    style B5 fill:#f3e5f5
    style C1 fill:#e8f5e9
    style C2 fill:#e8f5e9
    style C3 fill:#e8f5e9
    style C4 fill:#e8f5e9
    style C5 fill:#e8f5e9
```

## 学习路径建议

```mermaid
graph TD
    A[多智能体基础] --> B[Agent通信机制]
    B --> C[协作模式]
    C --> D[主流框架对比]
    D --> E[任务分解与规划]
    E --> F[冲突解决]
    F --> G[Java实战]
    
    style A fill:#fff3e0
    style B fill:#fff3e0
    style C fill:#fff3e0
    style D fill:#e3f2fd
    style E fill:#e3f2fd
    style F fill:#e3f2fd
    style G fill:#ffe0e0
```

## 与其他模块的关系

- 本模块依赖 [01 - Agent 基础](./01-agent-basics.md) 中的核心概念
- 本模块依赖 [04 - Agent 框架](./04-agent-frameworks.md) 中的框架基础
- 本模块可与 [06 - RAG / 知识检索](./06-rag-knowledge-retrieval.md) 结合实现分布式知识检索
- 本模块为复杂 Agent 应用提供分布式架构支持

## 推荐资源

### 论文
- **Multi-Agent Reinforcement Learning: A Selective Overview of Theories and Algorithms** (2021)
- **CAMEL: Communicative Agents for "Mind" Exploration of Large Scale Language Model Society** (2023)
- **AutoGen: Enabling Next-Gen LLM Applications via Multi-Agent Conversation** (2023)
- **MetaGPT: Meta Programming for Multi-Agent Collaborative Framework** (2023)

### 开源项目
- **AutoGen**: 微软开源的多 Agent 对话框架
- **CrewAI**: 用于编排角色扮演 Agent 的框架
- **LangGraph**: LangChain 的图结构 Agent 编排
- **MetaGPT**: 多 Agent 协作软件开发框架

---

> 📌 详细内容见各子章节，Java 实战示例见 [07-java-multi-agent-practice.md](./07-multi-agent-systems/07-java-multi-agent-practice.md)
