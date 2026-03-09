# 推理技巧（Reasoning Techniques）

- Chain of Thought (CoT)：让模型输出中间推理步骤，提高复杂问题的正确率。
- Tree of Thought (ToT)：通过并行探索多条推理路径，选出最优路径。
- Self-Consistency：多次采样推理并取多数结果以提高稳定性。
- Tool-Augmented Reasoning：结合检索/外部工具（计算器、数据库）提升准确性。

工程实践：
- 对敏感/关键推理步骤加入校验、证据来源。
- 控制输出长度与温度，权衡创造性和可靠性。

---