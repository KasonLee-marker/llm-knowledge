# 03 - Anthropic Claude 系列深度调研

## 1. 系列概览

Anthropic 由前 OpenAI 核心成员创立，以 AI 安全为核心使命。Claude 系列采用 Constitutional AI（CAI）训练方法，在安全对齐、指令遵循和长上下文处理方面有独特优势。2024 年 Claude 3 系列发布后，一跃成为 GPT-4 最强劲的竞争对手。

## 2. 主要模型版本对比

### 2.1 Claude 3 / 3.5 / 3.7 系列

> 价格单位：每百万 token

| 模型 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 输入($/M) | 输出($/M) | 最大输出 | 定位 |
|------|---------|--------|--------|------------|------------|---------|---------|---------|------|
| Claude 3.7 Sonnet | 2025-02 | 200K | ✅ | ¥21.9 | ¥109.5 | $3.00 | $15.00 | 128K | 旗舰 ⭐ |
| Claude 3.5 Sonnet | 2024-10 | 200K | ✅ | ¥21.9 | ¥109.5 | $3.00 | $15.00 | 8K | 次旗舰 |
| Claude 3.5 Haiku | 2024-11 | 200K | ✅ | ¥5.8 | ¥29.2 | $0.80 | $4.00 | 8K | 性价比 ⭐ |
| Claude 3 Opus | 2024-03 | 200K | ✅ | ¥109.5 | ¥547.5 | $15.00 | $75.00 | 4K | 已过时 |
| Claude 3 Haiku | 2024-03 | 200K | ✅ | ¥1.8 | ¥9.1 | $0.25 | $1.25 | 4K | 低成本 |

### 2.2 基准测试对比

| 基准 | Claude 3.5 Sonnet | Claude 3 Opus | GPT-4o | Gemini 1.5 Pro |
|------|-------------------|---------------|--------|----------------|
| MMLU | 88.7% | 86.8% | 88.7% | 85.9% |
| HumanEval | 92.0% | 84.9% | 90.2% | 84.1% |
| MATH | 71.1% | 60.1% | 76.6% | 67.7% |
| GPQA | 59.4% | 50.4% | 53.6% | 46.2% |
| GSM8K | 96.4% | 95.0% | 96.4% | 94.4% |
| 多语言（MGSM） | 91.6% | 90.7% | 90.5% | 89.2% |

## 3. Claude 3.7 Sonnet（当前旗舰，推荐 ⭐）

### 3.1 核心优势

- **Extended Thinking（扩展思维）**：支持 128K tokens 的超长思维链推理，远超其他模型
- **代码能力顶尖**：SWE-Bench 得分超越 GPT-4o，是修复真实代码问题最强的模型之一
- **最大输出 128K**：可一次生成完整的长文档/代码文件
- **长上下文稳定**：200K 输入窗口，Needle-in-Haystack 接近完美

### 3.2 Extended Thinking 使用

```json
{
  "model": "claude-3-7-sonnet-20250219",
  "thinking": {
    "type": "enabled",
    "budget_tokens": 10000
  },
  "messages": [{"role": "user", "content": "证明黎曼猜想..."}]
}
```

**Thinking 模式费用说明**：
- thinking tokens 按**输出 token** 计费（¥109.5/百万）
- 10000 budget_tokens ≈ 最多额外 ¥1.1（一般用不满）
- 推荐在复杂推理、数学证明、代码架构设计时启用

## 4. Constitutional AI 与安全特性

Claude 的训练方法包含两个阶段：

| 阶段 | 方法 | 效果 |
|------|------|------|
| 监督学习 | AI 模型根据 Constitution 原则生成修订版本 | 减少有害输出 |
| RLHF 改进 | 使用 AI 生成的偏好数据（不依赖人工标注）| 降低成本，更一致 |

**输出特点：**
- 拒绝有害请求时会解释原因（透明）
- 涉及不确定内容会主动声明局限
- 在道德模糊场景下更保守（适合合规业务）

## 5. 与 OpenAI API 的兼容性

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

## 6. 适用场景矩阵

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 代码生成/审查 | Claude 3.5 Sonnet | SWE-Bench 最强，代码理解深入 |
| 长文档分析 | Claude 3.5 Sonnet / 3.7 | 200K 上下文，长文稳定 |
| 合规敏感对话 | Claude 3.5 Haiku | 安全对齐好，成本低 |
| 数学/科学推理 | Claude 3.7 Sonnet（Extended Thinking）| 内置思维链推理 |
| 高频轻量对话 | Claude 3.5 Haiku | 低延迟，高性价比 |
| 企业内容审核 | Claude 3 Opus | 最保守最安全 |

## 7. 价格与 GPT 对比

| 模型对比 | 输入($/1M) | 输出($/1M) | 上下文 | 综合排名 |
|---------|-----------|-----------|--------|---------|
| Claude 3.5 Sonnet | $3.00 | $15.00 | 200K | ★★★★★ |
| GPT-4o | $2.50 | $10.00 | 128K | ★★★★★ |
| Gemini 1.5 Pro | $1.25 | $5.00 | 2M | ★★★★☆ |
| Claude 3.5 Haiku | $0.80 | $4.00 | 200K | ★★★★☆ |
| GPT-4o mini | $0.15 | $0.60 | 128K | ★★★★☆ |

## 8. Java 集成建议

```java
// Spring AI 集成 Claude
// 添加依赖：spring-ai-anthropic-spring-boot-starter
@Value("${spring.ai.anthropic.api-key}")
private String apiKey;

// application.yml
// spring.ai.anthropic.chat.options.model=claude-3-5-sonnet-20241022
// spring.ai.anthropic.chat.options.max-tokens=8096
```

**注意事项：**
1. System Prompt 需放在请求体顶层的 `system` 字段
2. 连续多轮对话必须严格交替 user/assistant 消息
3. 图像只能通过 Base64 传递（或预签名 URL），不支持直接外部链接

---

> 📌 相关文档：[02-openai-gpt-series.md](./02-openai-gpt-series.md) | [09-model-comparison.md](./09-model-comparison.md)
