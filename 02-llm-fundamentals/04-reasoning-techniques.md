# 推理技术（Reasoning Techniques）

## 为什么需要推理技术

LLM 在处理简单问题时表现出色，但对于复杂的多步骤推理任务（数学题、逻辑推断、代码调试），直接给出答案容易出错。**推理技术**通过改变模型的"思考方式"来提升复杂任务的准确率。

---

## 1. Chain-of-Thought（思维链，CoT）

### 原理

CoT 由 Wei et al.（2022）提出，核心思想是让模型在给出最终答案之前，**显式输出推理步骤**。研究表明，这种"一步一步"的方式能显著提升复杂推理任务的正确率。

### Zero-Shot CoT

只需在 Prompt 末尾加上 "Let's think step by step" 或 "让我们一步步来思考"：

```
用户: 一个班有 36 名学生，其中 1/4 参加了足球队，足球队员中有 1/3 同时参加了篮球队，
      请问同时参加两个队的学生有多少人？

让我们一步步来：
1. 参加足球队的学生数 = 36 × 1/4 = 9 人
2. 同时参加篮球队的学生数 = 9 × 1/3 = 3 人
答案：3 人
```

### Few-Shot CoT

提供带有推理步骤的示例，帮助模型学习推理格式：

```
问题: 小明有 5 个苹果，给了小红 2 个，又买了 3 个，现在有几个？
推理: 初始 5 个 → 给出 2 个 = 5-2=3 个 → 买入 3 个 = 3+3=6 个
答案: 6 个

问题: 某工厂一天生产 240 件产品，工作 8 小时，每小时生产多少件？
推理: [模型补全]
```

### Java 工程实践

```java
public String chainOfThoughtPrompt(String question) {
    return """
        请解决以下问题。在给出最终答案之前，请展示完整的推理过程。
        
        格式：
        推理过程：
        1. [第一步]
        2. [第二步]
        ...
        
        最终答案：[答案]
        
        问题：%s
        """.formatted(question);
}
```

---

## 2. Tree of Thoughts（思维树，ToT）

### 原理

ToT 由 Yao et al.（2023）提出，将问题求解过程建模为一棵树：
- 每个节点代表一个**中间思维状态**
- 通过广度优先或深度优先搜索探索多条推理路径
- 使用评估函数对每个路径进行打分，选择最优路径

```
              [问题]
             /   |   \
         [思路A] [思路B] [思路C]
          / \      |      / \
        [A1] [A2]  [B1] [C1] [C2]
         ✓    ✗    ✗    ✓    ✗
         
最优路径: 问题 → 思路A → A1（✓ 评分最高）
```

### 适用场景

- 创意写作（探索多种故事发展方向）
- 代码调试（尝试多种修复方案）
- 策略规划（评估多种行动方案）
- 数学证明（探索不同证明路径）

### 简化实现（Prompt-based ToT）

```java
public String treeOfThoughtsPrompt(String problem) {
    return """
        请用"思维树"方式解决以下问题：
        
        步骤1：列出3种不同的解题思路（每种思路用1-2句话概括）
        
        步骤2：对每种思路进行评估：
        - 可行性（高/中/低）
        - 预期效果（高/中/低）
        - 实施难度（高/中/低）
        
        步骤3：选择最优思路并详细展开解决方案
        
        问题：%s
        """.formatted(problem);
}
```

---

## 3. Self-Consistency（自我一致性）

### 原理

Self-Consistency 由 Wang et al.（2022）提出。对同一问题**多次采样**（temperature > 0），得到多个答案，然后通过**多数投票**选出最终答案。

这利用了 LLM 的随机性：虽然单次答案可能出错，但多次采样的众数通常更可靠。

```
问题 → 采样推理路径1 → 答案: 42
问题 → 采样推理路径2 → 答案: 42
问题 → 采样推理路径3 → 答案: 38
问题 → 采样推理路径4 → 答案: 42
问题 → 采样推理路径5 → 答案: 42

多数投票 → 最终答案: 42（得票 4/5）
```

### Java 实现

```java
public String selfConsistency(String prompt, int sampleCount) {
    Map<String, Integer> answerVotes = new HashMap<>();
    
    // 多次采样（temperature 设置为 0.7-1.0）
    for (int i = 0; i < sampleCount; i++) {
        String answer = llmClient.complete(prompt, 
            ChatOptions.builder().temperature(0.8).build());
        String extracted = extractFinalAnswer(answer);
        answerVotes.merge(extracted, 1, Integer::sum);
    }
    
    // 返回得票最多的答案
    return answerVotes.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("无法确定");
}
```

**注意**：Self-Consistency 会增加 N 倍的 LLM 调用成本，建议仅用于高准确率要求的关键场景。

---

## 4. ReAct（推理 + 行动）

### 原理

ReAct（Yao et al., 2022）将推理和工具调用交织在一起：模型每次输出"Thought（思考）"后执行"Action（行动）"，观察"Observation（结果）"，再进行下一轮思考。详见 [01-agent-basics/03-llm-agents.md](../01-agent-basics/03-llm-agents.md)。

```
Thought: 我需要查询用户的订单状态
Action: search_orders(user_id="U123")
Observation: 订单 #456 状态为"已发货"，预计明天到达

Thought: 已获取订单信息，可以回答用户了
Final Answer: 您的订单 #456 已于昨天发货，预计明天（12月15日）送达。
```

---

## 5. Reflection（反思）

### 原理

让模型对自己的初始答案进行**批判性审查**，识别错误并给出改进版本：

```java
public String reflectionPrompt(String question, String initialAnswer) {
    return """
        你已经给出了以下答案：
        %s
        
        请仔细审查这个答案，考虑：
        1. 答案是否完全正确？
        2. 推理过程是否有逻辑漏洞？
        3. 是否遗漏了重要信息？
        4. 是否有更好的表达方式？
        
        如果需要修正，请给出改进后的最终答案。
        问题：%s
        """.formatted(initialAnswer, question);
}
```

### 在 Agent 中的 Reflexion 模式

Reflexion（Shinn et al., 2023）将反思机制与记忆结合：Agent 每次任务完成后将反思结果存入长期记忆，下次面对类似任务时参考历史教训：

```
任务执行 → 失败/次优 → 自我反思（分析原因）→ 将反思写入记忆 → 下次任务参考
```

---

## 6. Least-to-Most Prompting（从简到繁）

将复杂问题**分解为简单子问题**，逐步求解：

```
原问题：设计一个支持百万用户的实时聊天系统架构

分解：
1. 首先解决：如何处理用户的 WebSocket 连接？
2. 基于此：如何在多台服务器间路由消息？
3. 基于此：如何保证消息的持久化和可靠传递？
4. 最后：如何整合以上方案，处理百万级并发？
```

---

## 推理技术对比与选型

| 技术 | 适用场景 | 计算成本 | 实现复杂度 |
|------|---------|---------|----------|
| Zero-Shot CoT | 通用推理任务（数学、逻辑）| 1x | ⭐ 最低（一行 Prompt）|
| Few-Shot CoT | 有标准格式的任务 | 1x | ⭐⭐ 需准备示例 |
| Tree of Thoughts | 创意/多路径探索 | 3-5x | ⭐⭐⭐ 需要搜索逻辑 |
| Self-Consistency | 高准确率要求的关键任务 | Nx | ⭐⭐ 需多次采样+投票 |
| ReAct | 需要工具调用的 Agent 任务 | 多轮 | ⭐⭐⭐ 需工具集成 |
| Reflexion | 需要迭代改进的长期任务 | 多轮+记忆 | ⭐⭐⭐⭐ 需要记忆系统 |

---

## 工程实践建议

- **优先使用 Zero-Shot CoT**：成本最低，对大多数推理任务效果显著，是首选
- **生产环境谨慎使用 Self-Consistency**：成本乘以 N 倍，仅用于对准确率要求极高的场景（如医疗、金融）
- **记录推理过程**：即使最终用户不看 CoT 输出，也要在日志中保留，便于调试
- **设置输出长度限制**：CoT 会增加输出 token 数，注意控制成本
- **结合工具使用**：对于需要实时数据或精确计算的推理，结合工具调用（ReAct）效果更佳

---
