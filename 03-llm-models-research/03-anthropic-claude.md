# 03 - Anthropic Claude 系列深度调研

## 1. 系列概览

Anthropic 由前 OpenAI 核心成员创立，以 AI 安全为核心使命。Claude 系列采用 Constitutional AI（CAI）训练方法，在安全对齐、指令遵循和长上下文处理方面有独特优势。2026 年 Claude 4.5/4.6 系列的发布标志着新一代智能助手的到来，在保持安全性的同时大幅提升了性能和降低了价格。

## 2. 主要模型版本对比

### 2.1 Claude 4.5 / 4.6 系列（2025-2026 最新）

> 价格单位：每百万 token

| 模型 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 输入($/M) | 输出($/M) | 最大输出 | 定位 |
|------|---------|--------|--------|------------|------------|---------|---------|---------|------|
| **Claude Opus 4.6** | 2026-02 | 1M (beta) | ✅ | ¥36.5 | ¥182.5 | $5.00 | $25.00 | 128K | 顶级旗舰 ⭐ |
| **Claude Opus 4.5** | 2025-11 | 200K | ✅ | ¥36.5 | ¥182.5 | $5.00 | $25.00 | 64K | 旗舰 ⭐ |
| **Claude Sonnet 4.6** | 2026-02 | 1M (beta) | ✅ | ¥21.9 | ¥109.5 | $3.00 | $15.00 | 128K | 主力推荐 ⭐ |
| **Claude Sonnet 4.5** | 2025-11 | 200K | ✅ | ¥21.9 | ¥109.5 | $3.00 | $15.00 | 64K | 主力推荐 ⭐ |
| **Claude Haiku 4.5** | 2025-10 | 200K | ✅ | ¥7.3 | ¥36.5 | $1.00 | $5.00 | 8K | 高性价比 ⭐ |
| Claude 3.7 Sonnet | 2025-02 | 200K | ✅ | ¥21.9 | ¥109.5 | $3.00 | $15.00 | 128K | 旧款（仍可用）|
| Claude 3.5 Sonnet | 2024-10 | 200K | ✅ | ¥21.9 | ¥109.5 | $3.00 | $15.00 | 8K | 旧款 |
| Claude 3.5 Haiku | 2024-11 | 200K | ✅ | ¥5.8 | ¥29.2 | $0.80 | $4.00 | 8K | 旧款 |
| Claude 3 Opus | 2024-03 | 200K | ✅ | ¥109.5 | ¥547.5 | $15.00 | $75.00 | 4K | 已过时 |

> 💡 **价格降幅**：Claude 4.5/4.6 系列相比前代 Opus 4.1 价格降低约 67%

### 2.2 长上下文定价（>200K tokens）

| 模型 | ≤200K 输入 | >200K 输入 | ≤200K 输出 | >200K 输出 |
|------|-----------|-----------|-----------|-----------|
| Claude Opus 4.6 | $5.00 | $10.00 | $25.00 | $37.50 |
| Claude Sonnet 4.6 | $3.00 | $6.00 | $15.00 | $22.50 |

### 2.3 基准测试对比

| 基准 | Claude Opus 4.6 | Claude Sonnet 4.5 | GPT-5.2 | Gemini 3.1 Pro |
|------|-----------------|-------------------|---------|----------------|
| MMLU | 90.5% | 88.7% | 89.5% | 89.0% |
| HumanEval | 94.0% | 92.0% | 93.5% | 92.5% |
| MATH-500 | 85.0% | 78.0% | 85.0% | 88.0% |
| GPQA | 82.0% | 75.0% | 75.0% | 84.0% |
| SWE-Bench | 79.6% | 72.0% | 70.0% | 68.0% |
| 多语言（MGSM）| 92.0% | 91.6% | 91.0% | 90.5% |

## 3. Claude Opus 4.6（当前旗舰，推荐 ⭐）

### 3.1 核心优势

- **1M Token 上下文**（beta）：业界领先的长文档处理能力
- **Agent Teams**：支持并行编码工作流
- **自适应思考（Adaptive Thinking）**：根据任务难度动态调整推理深度
- **Fast Mode**：2.5 倍输出速度（需额外付费 $30/$150 per MTok）
- **价格大幅降低**：较 Opus 4.1 降低 67%

### 3.2 技术规格

| 参数 | 规格 |
|------|------|
| 上下文窗口 | 1M tokens（beta）|
| 最大输出 | 128K tokens |
| 多模态 | 文本 + 图像 |
| 输入价格 | $5.00 / 百万 tokens |
| 输出价格 | $25.00 / 百万 tokens |
| 缓存写入 | $6.25 / 百万 tokens |
| 缓存读取 | $0.50 / 百万 tokens |

### 3.3 Extended Thinking 使用

```json
{
  "model": "claude-opus-4-6-20250219",
  "thinking": {
    "type": "enabled",
    "budget_tokens": 10000
  },
  "messages": [{"role": "user", "content": "复杂推理任务..."}]
}
```

**Thinking 模式费用说明**：
- thinking tokens 按**输出 token** 计费（$25/百万）
- 10000 budget_tokens ≈ 最多额外 $0.25
- 推荐在复杂推理、数学证明、代码架构设计时启用

## 4. Claude Sonnet 4.5/4.6（主力推荐 ⭐）

### 4.1 核心优势

- **最佳性价比**：$3/$15 per MTok，性能接近 Opus
- **200K/1M 上下文**：适合大多数生产场景
- **代码能力顶尖**：SWE-Bench 得分优秀
- **稳定可靠**：适合企业级应用

### 4.2 适用场景

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 代码生成/审查 | Claude Sonnet 4.6 | SWE-Bench 79.6%，代码理解深入 |
| 长文档分析 | Claude Sonnet 4.6 | 1M 上下文，长文稳定 |
| 企业内容审核 | Claude Opus 4.6 | 最保守最安全 |
| 高频轻量对话 | Claude Haiku 4.5 | 低延迟，高性价比 |
| 数学/科学推理 | Claude Opus 4.6 | 自适应思考模式 |

## 5. Claude Haiku 4.5（高性价比 ⭐）

### 5.1 核心优势

- **超低价格**：$1/$5 per MTok
- **200K 上下文**：远超同价位竞品
- **快速响应**：延迟最低
- **多模态支持**：支持图像输入

### 5.2 适用场景

- 实时聊天机器人
- 内容审核
- 简单分类/提取任务
- 高并发场景

## 6. Constitutional AI 与安全特性

Claude 的训练方法包含两个阶段：

| 阶段 | 方法 | 效果 |
|------|------|------|
| 监督学习 | AI 模型根据 Constitution 原则生成修订版本 | 减少有害输出 |
| RLHF 改进 | 使用 AI 生成的偏好数据（不依赖人工标注）| 降低成本，更一致 |

**输出特点：**
- 拒绝有害请求时会解释原因（透明）
- 涉及不确定内容会主动声明局限
- 在道德模糊场景下更保守（适合合规业务）

## 7. 与 OpenAI API 的兼容性

Anthropic 提供独立的 API，格式与 OpenAI 有所不同：

| 特性 | OpenAI API | Anthropic API |
|------|-----------|---------------|
| 端点 | `/v1/chat/completions` | `/v1/messages` |
| System Prompt | `role: "system"` 消息 | 顶层 `system` 字段 |
| Tool Calling | `functions` / `tools` | `tools` 数组 |
| Streaming | `stream: true` | `stream: true` |
| 最大输出 | `max_tokens` | `max_tokens` |
| Stop Sequences | `stop` | `stop_sequences` |

通过 Amazon Bedrock 或 Vertex AI 也可访问 Claude，API 格式各有不同。

## 8. 价格与竞品对比

| 模型对比 | 输入($/1M) | 输出($/1M) | 上下文 | 综合排名 |
|---------|-----------|-----------|--------|---------|
| Claude Opus 4.6 | $5.00 | $25.00 | 1M | ★★★★★ |
| Claude Sonnet 4.6 | $3.00 | $15.00 | 1M | ★★★★★ |
| GPT-5.2 | $1.75 | $14.00 | 400K | ★★★★★ |
| Gemini 3.1 Pro | $2.00 | $12.00 | 1M | ★★★★★ |
| Claude Haiku 4.5 | $1.00 | $5.00 | 200K | ★★★★☆ |
| GPT-5-mini | $0.25 | $2.00 | 400K | ★★★★☆ |

## 9. Java 集成建议

```java
// Spring AI 集成 Claude
// 添加依赖：spring-ai-anthropic-spring-boot-starter
@Value("${spring.ai.anthropic.api-key}")
private String apiKey;

// application.yml
// spring.ai.anthropic.chat.options.model=claude-opus-4-6-20260219
// spring.ai.anthropic.chat.options.max-tokens=8096
```

**注意事项：**
1. System Prompt 需放在请求体顶层的 `system` 字段
2. 连续多轮对话必须严格交替 user/assistant 消息
3. 图像只能通过 Base64 传递（或预签名 URL），不支持直接外部链接
4. 长上下文（>200K）价格会上涨

---

> 📌 相关文档：[02-openai-gpt-series.md](./02-openai-gpt-series.md) | [09-model-comparison.md](./09-model-comparison.md)

> 📅 最后更新：2026年3月
