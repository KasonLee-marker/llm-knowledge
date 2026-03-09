# LLM 输出控制（Output Control）

## 概述

LLM 在生成文本时，输出结果受到多个参数的控制。理解并正确设置这些参数，是 LLM 工程实践中的重要环节，直接影响：
- 输出的**随机性与创造性**
- 输出内容的**多样性**
- 输出的**长度与结构**
- **成本**与**延迟**

---

## 核心采样参数

### Temperature（温度）

Temperature 控制模型输出的**随机程度**，是最常用的参数。

```
Temperature = 0      确定性输出，总是选概率最高的 token（贪心解码）
Temperature = 0.7    平衡：有一定创造性，但整体稳定
Temperature = 1.0    原始概率分布，较高随机性
Temperature > 1.0    高度随机（通常不推荐）
```

**技术原理**：

```
原始 logits:    [A: 3.0, B: 2.0, C: 1.0]
Softmax(T=1.0): [A: 0.67, B: 0.24, C: 0.09]
Softmax(T=0.5): [A: 0.88, B: 0.11, C: 0.01]  （低温→更集中）
Softmax(T=2.0): [A: 0.51, B: 0.31, C: 0.18]  （高温→更均匀）
```

**场景建议**：

| 场景 | 推荐 Temperature |
|------|----------------|
| 代码生成、数据提取、分类 | 0 ~ 0.2 |
| 问答、摘要、翻译 | 0.3 ~ 0.7 |
| 创意写作、头脑风暴 | 0.8 ~ 1.2 |
| 多样性探索（Self-Consistency）| 0.7 ~ 1.0 |

---

### Top-P（核采样，Nucleus Sampling）

Top-P 从概率**累计超过 P 的最小候选 token 集合**中随机采样：

```
候选 tokens 及概率：[A: 0.45, B: 0.30, C: 0.15, D: 0.07, E: 0.03]

top_p = 0.9：
  累计概率: A(0.45) + B(0.30) + C(0.15) = 0.90
  候选集合: {A, B, C}，从中随机采样
  
top_p = 0.5：
  累计概率: A(0.45) ≈ 0.5
  候选集合: {A, B}，更保守
```

**与 Temperature 的关系**：
- Temperature 控制概率分布的"尖锐程度"
- Top-P 控制候选集合的"大小"
- **实践中通常二选一**：要么固定 Temperature，要么固定 Top-P（OpenAI 文档建议不要同时调整两者）

---

### Top-K

从概率最高的 **K 个 token** 中随机采样：

```
top_k = 5：从概率最高的 5 个 token 中随机选择
top_k = 1：等同于贪心解码（greedy decoding）
```

Top-K 在实践中不如 Top-P 常用，因为固定 K 值无法适应不同位置候选集合概率分布的差异。

---

### Frequency Penalty & Presence Penalty（重复惩罚）

| 参数 | 范围 | 作用 |
|------|------|------|
| `frequency_penalty` | -2.0 ~ 2.0 | 按 token 出现频率惩罚，频繁出现的 token 被抑制 |
| `presence_penalty` | -2.0 ~ 2.0 | 对已经出现过的 token 统一惩罚，促进话题多样性 |

```
frequency_penalty > 0：减少重复词语，适合长文本生成
presence_penalty > 0：鼓励使用新词汇，适合创意写作
两者 = 0：无惩罚（默认），适合代码生成等需要重复关键词的场景
```

---

### Max Tokens（最大输出长度）

控制模型最多生成多少个 token：

```java
// 不同场景的建议 max_tokens
int maxTokens = switch (taskType) {
    case CLASSIFICATION   -> 20;      // 只需返回类别标签
    case SUMMARIZATION    -> 500;     // 摘要任务
    case CODE_GENERATION  -> 2000;    // 代码生成
    case LONG_FORM_WRITING -> 4000;   // 长文写作
    default               -> 1024;
};
```

**注意**：设置过小的 `max_tokens` 会导致输出被截断。应根据任务预估合理值。

---

## 确定性 vs 创造性输出

```
           确定性 ←────────────────────────────→ 创造性
           
T=0       T=0.3      T=0.7      T=1.0      T=1.5
代码生成  问答/摘要  通用对话  创意写作  实验性输出
数据提取  分类任务   翻译      角色扮演
```

---

## 结构化输出（Structured Output）

### JSON 模式（JSON Mode）

强制要求 LLM 输出合法的 JSON 格式：

```java
// OpenAI JSON Mode
ChatCompletionRequest request = ChatCompletionRequest.builder()
    .model("gpt-4o")
    .messages(messages)
    .responseFormat(ResponseFormat.JSON_OBJECT) // 强制 JSON 输出
    .build();
```

**注意**：即使开启 JSON Mode，也要在 Prompt 中明确要求 JSON 格式，否则模型可能无法正确触发。

### JSON Schema 约束（Structured Outputs）

OpenAI 最新的 Structured Outputs 功能，通过提供 JSON Schema，**保证输出完全符合 Schema 定义**：

```java
// 定义输出 Schema
String schema = """
{
  "type": "object",
  "properties": {
    "intent": {
      "type": "string",
      "enum": ["query_weather", "book_flight", "order_inquiry", "general_chat"]
    },
    "confidence": {"type": "number"},
    "slots": {"type": "object"}
  },
  "required": ["intent", "confidence"]
}
""";

ChatCompletionRequest request = ChatCompletionRequest.builder()
    .model("gpt-4o-2024-08-06") // Structured Outputs 需要此版本或更新
    .messages(messages)
    .responseFormat(ResponseFormat.jsonSchema("IntentResult", schema))
    .build();
```

---

## Streaming（流式输出）

Streaming 允许客户端在 LLM 生成过程中**逐步接收 token**，显著降低用户感知的延迟：

```java
// 非流式：等待完整响应（用户等待 5-10 秒看到结果）
String result = llmClient.complete(prompt);

// 流式：逐 token 推送（用户立即看到第一个字）
llmClient.streamComplete(prompt, token -> {
    sseEmitter.send(SseEmitter.event()
        .data(token)
        .build());
});
```

### 适用场景

| 场景 | 推荐模式 | 原因 |
|------|---------|------|
| 对话界面 | Streaming | 减少用户等待感，体验更好 |
| 批量处理 | 非流式 | 简化代码，便于结果处理 |
| 结构化提取 | 非流式 | 等待完整 JSON 更安全 |
| 长文本生成 | Streaming | 超时风险低，响应更及时 |

---

## Java 工程实现

### 完整的请求参数封装

```java
@Builder
public class LLMRequestConfig {
    @Builder.Default
    private double temperature = 0.7;
    
    @Builder.Default
    private double topP = 1.0;
    
    @Builder.Default
    private int maxTokens = 1024;
    
    @Builder.Default
    private double frequencyPenalty = 0.0;
    
    @Builder.Default
    private double presencePenalty = 0.0;
    
    @Builder.Default
    private boolean stream = false;
    
    /** 为不同任务类型创建预设配置 */
    public static LLMRequestConfig forTask(TaskType taskType) {
        return switch (taskType) {
            case CODE_GENERATION -> LLMRequestConfig.builder()
                .temperature(0.1)
                .maxTokens(2000)
                .build();
            case CREATIVE_WRITING -> LLMRequestConfig.builder()
                .temperature(1.0)
                .presencePenalty(0.5)
                .maxTokens(3000)
                .build();
            case CLASSIFICATION -> LLMRequestConfig.builder()
                .temperature(0.0)
                .maxTokens(50)
                .build();
            default -> LLMRequestConfig.builder().build();
        };
    }
}
```

---

## 参数调优实践

### 快速定位最优 Temperature

```
1. 从 temperature=0.7 开始
2. 如果输出重复或过于保守 → 增大 temperature
3. 如果输出不稳定或偏离主题 → 减小 temperature
4. 代码/数据提取任务 → temperature=0 几乎总是最优选择
```

### 调试输出异常

| 问题现象 | 可能原因 | 调整方向 |
|---------|---------|---------|
| 输出被截断 | max_tokens 太小 | 增大 max_tokens |
| 大量重复词语 | frequency_penalty 太低 | 提高 frequency_penalty |
| 输出偏离指令 | temperature 太高 | 降低 temperature |
| 输出格式不符 | 未开启 JSON Mode | 开启结构化输出 |
| 总是给出相同答案 | temperature=0 | 适当提高 temperature |

---
