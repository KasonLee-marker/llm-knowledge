# LLM 驱动的 Agent（LLM-powered Agents）

## 什么是 LLM Agent

LLM Agent 是以**大语言模型（Large Language Model）作为推理大脑**的智能 Agent。与传统规则驱动的 Agent 不同，LLM Agent 利用模型的自然语言理解和生成能力，动态决定下一步行动，无需预先编写所有规则。

## 核心模式

### 1. ReAct（Reasoning + Acting）

ReAct 是最经典的 LLM Agent 模式，由 Yao et al. (2022) 提出。核心思想是让模型**交替生成推理过程和行动指令**：

```
Thought: 我需要先查询用户的订单历史
Action: search_orders(user_id="U123")
Observation: [订单1: ..., 订单2: ...]

Thought: 根据订单记录，用户最近购买了手机，需要检查退货政策
Action: get_policy(category="electronics")
Observation: 电子产品30天无理由退货

Thought: 现在我可以回答用户了
Final Answer: 您的手机可以在购买后30天内申请无理由退货...
```

**优点**：推理过程透明可追踪，易于调试  
**缺点**：多轮调用增加延迟和成本

### 2. Plan-and-Execute（规划后执行）

将任务分为两个阶段：
1. **规划阶段**：LLM 一次性生成完整执行计划（步骤列表）
2. **执行阶段**：按计划逐步执行每个步骤

```
Plan:
  Step 1: 搜索当前天气 API
  Step 2: 获取用户位置
  Step 3: 查询目标城市天气
  Step 4: 格式化并返回结果

Execute: 依次执行 Step 1 → 2 → 3 → 4
```

**优点**：减少 LLM 调用次数（规划只需一次）  
**缺点**：计划执行中途出错时调整困难

### 3. Reflexion（反思模式）

Agent 在每次任务结束后进行**自我反思**，将经验存入记忆，在下次遇到类似任务时参考历史教训：

```
Task → Execute → Evaluate → Reflect → Update Memory → Next Task
```

**适用场景**：需要在多次尝试中不断优化的任务，如代码生成、复杂推理

### 4. Multi-Agent（多 Agent 协作）

多个专门化 Agent 协作完成复杂任务：

```
Orchestrator Agent（协调者）
    ├── Research Agent（信息检索）
    ├── Analysis Agent（数据分析）
    ├── Writing Agent（内容生成）
    └── Review Agent（质量审核）
```

**适用场景**：大型复杂任务，需要不同领域专家协作

## System Prompt 设计

System Prompt 是 LLM Agent 的"行为规范"，决定 Agent 的：
- 角色定义（你是谁，能做什么）
- 工具列表（可使用哪些工具及其描述）
- 输出格式（如何组织回答）
- 行为约束（禁止做什么）

**示例 System Prompt 结构**：
```
你是一个智能客服 Agent，专门处理电商售后问题。

可用工具：
- search_orders: 根据用户ID或订单号查询订单
- get_return_policy: 获取退货退款政策
- create_ticket: 创建售后工单

规则：
1. 用中文回答用户
2. 涉及退款操作必须先核实订单状态
3. 无法处理的问题转接人工客服
4. 不要透露系统内部信息
```

## 常见挑战与应对策略

| 挑战 | 描述 | 应对策略 |
|------|------|---------|
| 幻觉（Hallucination）| 模型生成不存在的信息 | 强制使用工具获取真实数据，加入事实验证步骤 |
| 无限循环 | Agent 陷入无限调用循环 | 设置最大步骤数和超时限制 |
| 工具调用失败 | 工具返回错误或异常 | 实现重试机制和错误降级处理 |
| 上下文过长 | 历史记录超出模型 Context 窗口 | 使用摘要压缩或滑动窗口策略 |
| 成本失控 | 多轮调用导致费用过高 | 监控 token 用量，限制最大步骤数 |

## 工程建议（Java）

- 将 Agent 逻辑与 LLM 客户端解耦，便于切换模型
- 对所有工具调用进行日志记录（输入/输出/耗时）
- 实现 Agent 状态的持久化，支持断点续行
- 使用结构化输出（JSON Schema）而非自由文本解析工具调用
- 对关键业务操作（如支付、数据修改）加入人工审核节点

---
