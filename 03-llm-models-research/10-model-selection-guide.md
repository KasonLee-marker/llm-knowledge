# 10 - 模型选型指南

## 1. 选型核心维度

在选择 LLM 模型前，需要明确以下关键维度：

| 维度 | 关键问题 | 影响因素 |
|------|---------|---------|
| **任务类型** | 对话/代码/推理/多模态？ | 模型专长与基准测试 |
| **质量要求** | 准确率/格式/一致性要求？ | 基准测试、人工评估 |
| **延迟要求** | 实时 (<2s) / 批量 (<60s)？| TTFT、TPS 指标 |
| **成本预算** | 月预算 / 每次调用上限？ | $/1M token |
| **上下文长度** | 最长输入有多大？ | 文档页数、代码量 |
| **多模态需求** | 需要图像/视频/音频？ | 模型支持的模态 |
| **隐私合规** | 数据能否出境？能否用第三方？ | 部署方式 |
| **中文能力** | 主要语言是中文还是英文？ | C-Eval、CMMLU |
| **生态成熟** | 需要哪些工具链/SDK？ | API 生态 |

## 2. 决策树

```
开始选型
    │
    ├── 数据能否发往外部 API？
    │       │
    │       ├── 否（隐私/合规限制）
    │       │       │
    │       │       └── 自托管开源模型
    │       │               │
    │       │               ├── 高质量优先 → DeepSeek-V3 / LLaMA 3.1 70B / Qwen2.5 72B
    │       │               ├── 轻量边缘  → Phi-3.5 mini / Qwen2.5-3B / LLaMA 3.2 3B
    │       │               └── 代码专项  → Qwen2.5-Coder-32B / DeepSeek-Coder-V2
    │       │
    │       └── 是（可用外部 API）
    │               │
    │               ├── 主要语言是中文？
    │               │       │
    │               │       ├── 是 → DeepSeek-V3 / Qwen2.5-Max / GLM-4-Air
    │               │       └── 否 → 继续判断
    │               │
    │               ├── 成本优先？
    │               │       │
    │               │       ├── 是（<$1/1M）→ Gemini Flash / GPT-4o mini / DeepSeek-V3
    │               │       └── 否 → 继续判断
    │               │
    │               ├── 需要超强推理（数学/科学）？
    │               │       │
    │               │       ├── 是 → o1 / o3-mini / DeepSeek-R1
    │               │       └── 否 → 继续判断
    │               │
    │               ├── 需要处理超长文档 (>128K)？
    │               │       │
    │               │       ├── 是 → Gemini 1.5 Pro (2M) / Claude 3.5 (200K)
    │               │       └── 否 → 继续判断
    │               │
    │               ├── 综合能力最强优先？
    │               │       │
    │               │       └── 是 → GPT-4o / Claude 3.5 Sonnet / Gemini 2.0 Flash
    │               │
    │               └── 代码生成专项？
    │                       │
    │                       └── 是 → Claude 3.5 Sonnet / GPT-4o / Qwen2.5-Coder
```

## 3. 场景化推荐

### 3.1 企业通用对话 / 智能客服

| 需求级别 | 推荐模型 | 理由 | 月成本参考（100万次/月）|
|---------|---------|------|----------------------|
| 入门级 | GLM-4-Air | 极低成本，中文优秀 | ~¥100 |
| 标准级 | GPT-4o mini / DeepSeek-V3 | 高性价比 | ~$150 / ¥20 |
| 高质量 | GPT-4o / Claude 3.5 Sonnet | 最强综合 | ~$2,500 |

### 3.2 代码助手 / AI 编程

| 场景 | 推荐方案 | 说明 |
|------|---------|------|
| 代码补全（IDE 插件）| GPT-4o mini / Qwen2.5-Coder-7B（本地）| 低延迟实时补全 |
| 代码生成（API）| Claude 3.5 Sonnet / GPT-4o | SWE-Bench 最强 |
| 代码审查 | Claude 3.5 Sonnet | 指令遵循精确，输出可靠 |
| 本地代码模型 | Qwen2.5-Coder-32B / DeepSeek-Coder-V2 | 开源顶级代码 |
| 复杂算法推理 | o3-mini / DeepSeek-R1 | 推理专项 |

### 3.3 文档分析 / RAG

| 文档规模 | 推荐模型 | 方案 |
|---------|---------|------|
| <32K tokens | 任何模型 | 直接放入上下文 |
| 32K–128K | GPT-4o / LLaMA 3.1 70B | 128K 上下文 |
| 128K–200K | Claude 3.5 Sonnet | 200K 上下文 |
| 200K–1M | Gemini 2.0 Flash | 1M 上下文 |
| >1M tokens | Gemini 1.5 Pro | 2M 上下文 |
| 超大型企业文档 | RAG + Qwen-Long | 10M 上下文 |

> 💡 **建议**：超过 128K 的文档优先考虑 RAG（检索增强生成）而非直接放入上下文，以节省成本且获得更精准的引用。

### 3.4 数学/科学推理

| 难度级别 | 推荐模型 | 原因 |
|---------|---------|------|
| 基础（K12）| GPT-4o mini / Qwen2.5-7B | 足够准确，低成本 |
| 中级（本科）| GPT-4o / DeepSeek-V3 | 良好推理 |
| 高级（竞赛/研究）| o3-mini / DeepSeek-R1 | 专项推理，接近博士水平 |
| 极限（AIME/奥数）| o1 / DeepSeek-R1 | 79%+ AIME2024 |

### 3.5 图像/多模态处理

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 图像文字提取（OCR+理解）| GPT-4o / Claude 3.5 Sonnet | 最佳质量 |
| 图像内容描述 | Gemini 2.0 Flash | 高性价比 |
| 视频内容分析 | Gemini 1.5 Pro | 唯一支持长视频 |
| 图文多轮对话 | GPT-4o | 最稳定 |
| 本地视觉模型 | Qwen2-VL-7B / LLaMA 3.2 11B | 开源可部署 |

### 3.6 私有化部署（隐私保护）

| 服务器规格 | 推荐模型 | 量化方案 |
|---------|---------|---------|
| 消费级 GPU（8–16GB）| Phi-3.5 mini / Qwen2.5-7B / LLaMA 3.2 3B | Q4 量化 |
| 专业 GPU（24–48GB）| Mistral Small 3 / Phi-4 / Qwen2.5-14B | Q4/Q8 量化 |
| 服务器级（80GB×1）| LLaMA 3.1 70B / Qwen2.5-72B | Q4 量化 |
| 多卡集群（80GB×4+）| DeepSeek-V3 / LLaMA 3.1 405B | FP8/BF16 |
| CPU 推理 | LLaMA 3.2 1B / Phi-3.5 mini | GGUF Q4 |

## 4. Java 后端集成策略

### 4.1 推荐架构：多模型路由

```java
// 基于场景的模型路由策略
public class ModelRouter {

    private final Map<TaskType, String> routingTable = Map.of(
        TaskType.SIMPLE_CHAT,     "gpt-4o-mini",      // 低成本通用
        TaskType.COMPLEX_REASON,  "deepseek-reasoner", // 推理专项
        TaskType.CODE_GENERATION, "claude-3-5-sonnet", // 代码最强
        TaskType.DOCUMENT_ANALYZE,"gemini-1-5-pro",    // 长上下文
        TaskType.CHINESE_CONTENT, "deepseek-chat",     // 中文优先
        TaskType.IMAGE_ANALYSIS,  "gpt-4o"             // 多模态
    );

    public String selectModel(Task task) {
        return routingTable.getOrDefault(task.getType(), "gpt-4o-mini");
    }
}
```

### 4.2 成本控制策略

| 策略 | 实现方式 | 预估节省 |
|------|---------|---------|
| 分层路由 | 简单请求用小模型，复杂请求用大模型 | 50–80% |
| Prompt 缓存 | 相同前缀复用缓存（OpenAI/Claude 支持）| 50% 输入 |
| Batch API | 非实时请求走批量处理 | 50% 折扣 |
| 输出截断 | 精确 `max_tokens` 避免超量输出 | 按实际节省 |
| 本地缓存 | 相同查询结果缓存（Redis）| 100%（命中时）|
| 降级策略 | 主力模型故障时降级到备用 | 可用性提升 |

### 4.3 主要 SDK 对比

| SDK | 语言 | 支持模型 | 推荐场景 |
|-----|------|---------|---------|
| Spring AI | Java | OpenAI/Claude/Gemini/Ollama | **Java 首选** ⭐ |
| LangChain4j | Java | 所有主流模型 | Java 备选 |
| OpenAI Java SDK | Java | OpenAI 系列 | OpenAI 专用 |
| LangChain | Python | 全部 | Python 生态 |
| LlamaIndex | Python | 全部 | RAG 专项 |

## 5. 选型决策矩阵

| 优先级 | 最优选 | 次优选 | 预算受限 |
|--------|--------|--------|---------|
| 综合能力最强 | GPT-4.1 / Gemini 2.5 Pro | Claude 3.7 Sonnet | GPT-4.1 mini |
| 推理/数学最强 | o4-mini / DeepSeek-R1 | o3 / Gemini 2.5 Pro | Kimi k1.5 |
| 代码最强 | Claude 3.7 Sonnet | GPT-4.1 | Qwen2.5-Coder-32B |
| 成本最低（国内）| doubao-1.5-pro (¥0.4/百万) | GLM-Z1-Flash (免费) | GLM-4-Flash (免费) |
| 成本最低（国际）| GPT-4.1 nano ($0.1/百万) | Gemini 2.0 Flash-Lite | Mistral Small 3.1 |
| 长上下文 | LLaMA 4 Scout（10M）| Gemini 2.5 Pro（1M）| GPT-4.1（1M）|
| 中文最强 | DeepSeek-V3 / Qwen3 | Kimi k2 | GLM-Z1 |
| 私有部署 | DeepSeek-V3 自托管 | Qwen3-235B | LLaMA 4 Maverick |
| 安全合规 | Claude 3.7 Sonnet | GPT-4.1（Azure）| Qwen 自托管 |
| 速度最快 | Groq+LLaMA 3.3 | Gemini 2.0 Flash | Doubao-1.5-pro |
| 小型本地 | Qwen3-8B / Phi-4 | Gemma 3 12B | Mistral Small 3.1 |
| 推理+低价 | GLM-Z1-Air (¥2/百万) | DeepSeek-R1 (¥4/百万) | QwQ-32B（开源）|

---

> 📌 相关文档：[09-model-comparison.md](./09-model-comparison.md) | [11-fine-tuning-candidates.md](./11-fine-tuning-candidates.md)
