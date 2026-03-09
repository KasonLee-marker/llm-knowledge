# 意图识别（Intent Recognition）

## 什么是意图识别

意图识别（Intent Recognition）是 NLP 和对话系统中的核心技术，指从用户的自然语言输入中**自动识别用户所要表达的目的或意图**。例如：

| 用户输入 | 识别到的意图 |
|---------|------------|
| "帮我查一下北京明天的天气" | `query_weather` |
| "我想预订一张下周五去上海的机票" | `book_flight` |
| "这个订单为什么还没发货？" | `order_status_inquiry` |
| "取消我的会员" | `cancel_subscription` |

在 LLM Agent 场景中，意图识别是**感知层的核心任务**：Agent 必须先准确理解用户意图，才能做出正确的规划与行动决策。

---

## 意图识别的技术演进

### 阶段一：规则与关键词匹配（早期）

最简单的方式：维护意图关键词词典，对输入进行模式匹配。

```java
// 简单关键词匹配示例
public String recognizeIntent(String input) {
    if (input.contains("天气") || input.contains("气温")) {
        return "query_weather";
    } else if (input.contains("订单") || input.contains("发货")) {
        return "order_inquiry";
    }
    return "unknown";
}
```

**优点**：简单、可控、零延迟  
**缺点**：覆盖率低，无法处理语义相似但表达不同的输入（"今天热不热" → `query_weather`）

---

### 阶段二：机器学习分类模型（传统 NLP）

将意图识别转化为**多分类问题**，使用特征工程 + 分类器：
- TF-IDF + SVM/Logistic Regression
- FastText
- BiLSTM / TextCNN

**优点**：泛化能力比规则强  
**缺点**：需要大量标注数据；难以处理歧义和槽位提取；维护成本高

---

### 阶段三：预训练语言模型（BERT 时代）

使用 BERT/RoBERTa 等模型进行 Fine-tuning，在标注数据集上训练意图分类头：

```
[CLS] 帮我查一下北京明天的天气 [SEP]
        ↓ BERT Encoder
       [CLS] representation
        ↓ 全连接层
    query_weather (概率 0.97)
```

**优点**：语义理解能力强，数据效率高  
**缺点**：需要领域标注数据；新增意图需要重新训练

---

### 阶段四：LLM 零样本 / 少样本意图识别（当前主流）

使用大语言模型（GPT-4、Claude、Qwen 等）通过 Prompt 直接完成意图分类，**无需大量标注数据**：

```
System: 你是一个意图分类器，请从以下意图列表中选择最匹配的：
  - query_weather: 查询天气
  - book_flight: 预订机票
  - order_inquiry: 订单查询
  - cancel_subscription: 取消订阅
  - general_chat: 闲聊
  
User: 帮我查一下北京明天的天气
LLM Response: {"intent": "query_weather", "confidence": 0.98}
```

**优点**：无需标注数据，支持新增意图只需修改 Prompt  
**缺点**：延迟较高（需要 LLM 调用），成本高于本地模型

---

## 意图识别与槽位填充

完整的对话理解通常包含两个任务：

1. **意图识别（Intent Recognition）**：识别用户"想做什么"
2. **槽位填充（Slot Filling）**：提取实现意图所需的关键参数（实体）

```
用户输入: "帮我预订下周五下午3点从北京去上海的机票"
            ↓
意图识别: book_flight
            ↓
槽位填充:
  - departure_city: 北京
  - destination_city: 上海
  - date: 下周五（2024-XX-XX）
  - time: 15:00
  - flight_class: null（未指定，需追问）
```

### 多轮对话中的槽位补全

当槽位信息不完整时，Agent 需要通过多轮对话追问：

```
用户: 帮我订机票
Agent: 请问您要从哪里出发，去哪里？
用户: 从北京去上海
Agent: 请问您希望哪天出发？
用户: 下周五
Agent: 好的，已为您查询 2024-XX-XX 北京→上海的航班...
```

---

## 基于 LLM 的意图识别实现

### 方案一：结构化输出（推荐）

使用 LLM 的 JSON 输出模式，返回结构化的意图识别结果：

```java
public IntentResult recognizeIntent(String userInput) {
    String prompt = """
        分析用户输入，识别意图并提取槽位信息。
        
        支持的意图列表：
        - query_weather: 查询天气（槽位: city, date）
        - book_flight: 预订机票（槽位: from, to, date, seat_class）
        - order_inquiry: 订单查询（槽位: order_id, status_type）
        - cancel_subscription: 取消订阅（槽位: subscription_type）
        - general_chat: 闲聊（无槽位）
        
        请以 JSON 格式返回：
        {
          "intent": "<意图名称>",
          "confidence": <0.0-1.0>,
          "slots": {<槽位名>: <槽位值>},
          "missing_slots": [<未填充的必填槽位>]
        }
        
        用户输入: """ + userInput;

    String jsonResult = llmClient.complete(prompt, ResponseFormat.JSON);
    return objectMapper.readValue(jsonResult, IntentResult.class);
}
```

### 方案二：Function Calling 意图路由

将每个意图定义为一个 Function，让 LLM 通过 Function Calling 机制自动选择意图：

```java
List<Tool> intentTools = List.of(
    Tool.function("query_weather",
        "当用户想查询某地天气时调用",
        schema(Map.of(
            "city", Property.string("城市名称"),
            "date", Property.string("日期，如'今天'/'明天'/'2024-12-01'")
        )),
        Arrays.asList("city")
    ),
    Tool.function("book_flight",
        "当用户想预订机票时调用",
        schema(Map.of(
            "from", Property.string("出发城市"),
            "to", Property.string("目的城市"),
            "date", Property.string("出发日期")
        )),
        Arrays.asList("from", "to", "date")
    )
);

LLMResponse response = llmClient.chat(
    List.of(new UserMessage(userInput)),
    intentTools
);

if (response.hasFunctionCall()) {
    FunctionCall call = response.getFunctionCall();
    // call.getName() 就是识别到的意图
    // call.getArguments() 就是提取的槽位
    return IntentResult.from(call);
}
```

---

## 意图识别的常见挑战

### 1. 意图歧义

同一句话可能对应多个意图：

```
"能帮我处理一下这个问题吗？"
→ 可能是: 技术支持 / 投诉 / 咨询 / 通用请求
```

**应对策略**：
- 返回 Top-K 意图及置信度，设置置信度阈值
- 低置信度时通过追问澄清
- 结合对话历史和用户画像提升准确率

### 2. 多意图（Multi-Intent）

用户一句话包含多个意图：

```
"查一下明天北京天气，顺便帮我订10点的闹钟"
→ 意图1: query_weather（city=北京, date=明天）
→ 意图2: set_alarm（time=10:00）
```

**应对策略**：
- 使用 LLM 拆分为多个子意图
- 分别处理每个子任务

### 3. 意图漂移（Intent Drift）

对话过程中用户改变了目标：

```
用户: 帮我预订明天去上海的机票
Agent: 好的，请问您需要什么舱位？
用户: 算了，我想改坐高铁
→ 意图从 book_flight 切换为 book_train
```

**应对策略**：
- 每轮重新进行意图识别
- 维护意图历史栈，支持回退

### 4. 领域外意图（Out-of-Domain）

用户的意图超出系统支持范围：

```
用户: 帮我写一首诗
→ 系统只支持电商售后场景，无法处理
```

**应对策略**：
- 训练"领域外"（out-of-scope）意图分类器
- 优雅降级：告知用户当前系统的能力边界

---

## 意图识别在 Agent 中的位置

意图识别作为感知层的第一步，决定了 Agent 后续的整个处理流程：

```
用户输入
    ↓
意图识别 + 槽位填充
    ↓
意图路由（选择对应的处理逻辑/工具）
    ↓
槽位完整性检查
    ↓
[缺少槽位] → 追问用户补全
[槽位完整] → 执行对应工具/API
    ↓
生成最终回复
```

### 意图路由示例（Java）

```java
public AgentResponse handleUserInput(String userInput, ConversationContext ctx) {
    // 步骤1: 意图识别
    IntentResult intentResult = intentRecognizer.recognize(userInput, ctx.getHistory());
    
    // 步骤2: 置信度检查
    if (intentResult.getConfidence() < 0.7) {
        return AgentResponse.clarify("我不太确定您的意思，您是想...");
    }
    
    // 步骤3: 意图路由
    return switch (intentResult.getIntent()) {
        case "query_weather"      -> weatherHandler.handle(intentResult.getSlots());
        case "book_flight"        -> flightHandler.handle(intentResult.getSlots());
        case "order_inquiry"      -> orderHandler.handle(intentResult.getSlots());
        case "cancel_subscription"-> cancelHandler.handle(intentResult.getSlots());
        default                   -> AgentResponse.fallback("抱歉，这个问题超出了我的服务范围");
    };
}
```

---

## 工程实践建议

| 建议 | 说明 |
|------|------|
| **使用结构化输出** | 用 JSON Schema 约束 LLM 输出，避免自由文本解析的不稳定性 |
| **设置置信度阈值** | 低于阈值时触发澄清，而非盲目猜测意图 |
| **意图版本管理** | 意图列表变化时，维护版本记录，避免线上 Prompt 不一致 |
| **监控意图分布** | 统计高频/低频意图，定期优化意图分类粒度 |
| **日志全链路追踪** | 记录原始输入、识别意图、置信度、槽位，便于问题排查 |
| **A/B 测试** | 对比不同 Prompt 或模型在意图识别准确率上的表现 |

---
