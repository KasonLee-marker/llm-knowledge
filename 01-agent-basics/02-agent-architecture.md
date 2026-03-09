# Agent 架构：感知-推理-行动循环（Perception-Reasoning-Action Loop）

## 核心架构概述

LLM Agent 的标准架构由以下四大核心组件构成：

```
用户 / 环境输入
      |
      v
+----------------------------------+
| 1. Perception（感知层）          |
+----------------------------------+
      |
      v
+----------------------------------+        +----------------------------------+
| 2. Reasoning / Planning（推理）  |<------>| 4. Memory（记忆）               |
+----------------------------------+        +----------------------------------+
      |
      v
+----------------------------------+
| 3. Action（行动 / Tool Call）    |
+----------------------------------+
      |
      v
外部工具执行 / 环境反馈
```

## 四大核心组件详解

### 1. 感知层（Perception）

Agent 通过感知层获取外部输入：
- **用户输入**：自然语言指令、问题
- **工具返回**：API 响应、数据库查询结果、代码执行输出
- **环境状态**：文件系统、网络状态、传感器数据

在 LLM Agent 中，所有感知信息最终会被转化为文本/token 序列作为 LLM 的输入上下文。

### 2. 推理核心（Reasoning / Planning）

LLM 作为推理核心，负责：
- **意图理解**：解析用户目标
- **任务规划**：将复杂目标分解为可执行子任务
- **决策制定**：决定下一步调用哪个工具或生成什么输出
- **结果整合**：将多步骤的执行结果汇总为最终答案

### 3. 行动层（Action）

Agent 执行推理结果的输出层：
- **工具调用（Tool Use）**：调用外部 API、搜索引擎、代码执行器等
- **文件操作**：读写文件系统
- **API 交互**：调用第三方服务
- **直接输出**：向用户返回文本响应

### 4. 记忆层（Memory）

存储和检索 Agent 运行所需的上下文信息：
- **短期记忆（Working Memory）**：当前对话历史，存储在 LLM 的 Context 窗口中
- **长期记忆（Long-term Memory）**：历史任务、用户偏好，存储在向量数据库或传统数据库中
- **情节记忆（Episodic Memory）**：过去任务执行记录，用于经验学习

## Agent 运行循环（Agent Loop）

LLM Agent 的核心运行机制是一个**迭代循环**，直到目标完成或达到终止条件：

```
while (goal not achieved && steps < max_steps):
    1. 观察（Observe）：收集当前环境状态和历史上下文
    2. 思考（Think）：调用 LLM 推理下一步行动
    3. 行动（Act）：执行 LLM 决定的操作（调用工具或输出答案）
    4. 反馈（Feedback）：将行动结果加入上下文，进入下一轮循环
```

## 关键设计原则

- **幂等性**：工具调用应尽量设计为幂等操作，避免重复执行造成副作用
- **错误恢复**：Agent 应能识别工具调用失败并重试或切换策略
- **终止条件**：必须设置最大步骤数或超时，防止无限循环
- **成本控制**：每次 LLM 调用都有费用，应合理控制循环次数

## Java 工程实践要点

```java
// Agent Loop 的基本骨架
public AgentResult run(String userInput) {
    List<Message> context = new ArrayList<>();
    context.add(new SystemMessage(SYSTEM_PROMPT));
    context.add(new UserMessage(userInput));

    for (int step = 0; step < MAX_STEPS; step++) {
        LLMResponse response = llmClient.chat(context);

        if (response.isFinished()) {
            return AgentResult.success(response.getContent());
        }

        // 执行工具调用
        ToolCall toolCall = response.getToolCall();
        String toolResult = toolExecutor.execute(toolCall);

        // 将工具结果加入上下文
        context.add(new AssistantMessage(response));
        context.add(new ToolMessage(toolCall.getId(), toolResult));
    }

    return AgentResult.maxStepsReached();
}
```

---
