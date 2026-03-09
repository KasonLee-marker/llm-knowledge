# 04 - Google Gemini 系列深度调研

## 1. 系列概览

Google DeepMind 于 2023 年 12 月发布 Gemini 系列，作为 PaLM 2 的继任者。Gemini 从设计之初就是原生多模态架构（非视觉适配器），并以超长上下文（2M tokens）和多模态能力著称。2025 年 Gemini 2.0 系列发布，能力全面升级。

## 2. 主要模型版本对比

> 价格单位：每百万 token

### 2.1 Gemini 2.5 系列（2025年旗舰）

| 模型 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 输入($/M) | 输出($/M) | 定位 |
|------|---------|--------|--------|------------|------------|---------|---------|------|
| Gemini 2.5 Pro | 2025-03 | 1M | ✅（图/视/音） | ¥9.1 | ¥36.5 | $1.25 | $5.00 | 推理旗舰 ⭐ |
| Gemini 2.5 Flash | 2025-05 | 1M | ✅ | ¥1.1 | ¥4.4 | $0.15 | $0.60 | 性价比首选 ⭐ |

### 2.2 Gemini 2.0 系列

| 模型 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 输入($/M) | 输出($/M) | 定位 |
|------|---------|--------|--------|------------|------------|---------|---------|------|
| Gemini 2.0 Flash | 2025-02 | 1M | ✅（图/视/音/实时）| ¥0.73 | ¥2.9 | $0.10 | $0.40 | 实时多模态 |
| Gemini 2.0 Flash-Lite | 2025-02 | 1M | ✅ | ¥0.55 | ¥2.2 | $0.075 | $0.30 | 极低成本 |

### 2.3 Gemini 1.5 系列（仍可用）

| 模型 | 发布时间 | 上下文 | 输入(¥/百万) | 输出(¥/百万) | 定位 |
|------|---------|--------|------------|------------|------|
| Gemini 1.5 Pro | 2024-05 | 2M | ¥9.1 | ¥36.5 | 2M 超长上下文 |
| Gemini 1.5 Flash | 2024-05 | 1M | ¥0.55 | ¥2.2 | 轻量 |

### 2.4 开源 Gemma 3 系列（2025-03 全面 MIT 开源）

| 模型 | 参数量 | 上下文 | 许可证 | 多模态 | 特点 |
|------|--------|--------|--------|--------|------|
| Gemma 3 1B | 1B | 32K | MIT | ❌ | 极端轻量，边缘设备 |
| Gemma 3 4B | 4B | 128K | MIT | ✅ | 轻量视觉 |
| Gemma 3 12B | 12B | 128K | MIT | ✅ | 均衡视觉 |
| Gemma 3 27B | 27B | 128K | MIT | ✅ | 顶级开源视觉 ⭐ |

> 🎉 Gemma 3 全系列升级为 **MIT 协议**（Gemma 2 为 Gemma License），可完全自由商用！

## 3. 核心技术特性

### 3.1 Gemini 2.5 Pro：2025年综合最强推理模型之一

**核心突破**：
- 内置"深度思考"（Deep Think）模式，在复杂推理任务上接近 o1 水平
- 1M token 上下文 + 顶级多模态能力
- 在多个排行榜上超越 GPT-4o 和 Claude 3.7

**关键基准**（Gemini 2.5 Pro vs 竞品）：

| 基准 | Gemini 2.5 Pro | GPT-4.1 | Claude 3.7 | DeepSeek-R1 |
|------|----------------|---------|------------|-------------|
| GPQA Diamond | 84.0% | 69.0% | 80.0% | 71.5% |
| AIME 2025 | 86.7% | — | 80.2% | 79.8% |
| SWE-Bench | 63.8% | 54.6% | 70.3% | — |
| Humanity's Last Exam | 18.8% | — | — | — |

### 3.2 超长上下文

Gemini 1.5 Pro 的 2M token 上下文是当前商业模型中最长的：

| 场景 | 等效内容量 |
|------|-----------|
| 2M tokens | ~1500 页 PDF |
| 2M tokens | ~100 万行代码 |
| 2M tokens | ~22 小时音频 |
| 2M tokens | ~2 小时高清视频 |

**Needle-in-Haystack 测试**：在 200 万 token 的文本中查找特定信息，准确率 >99%（业界最高）。

### 3.2 原生多模态能力

Gemini 支持的输入模态：

| 模态 | 支持内容 | 最大限制 |
|------|---------|---------|
| 文本 | 所有语言 | 2M tokens |
| 图像 | JPEG/PNG/WEBP/HEIC/HEIF | 每请求 16 张 |
| 视频 | MP4/MPEG/AVI/FLV/WMV | 最长 1 小时 |
| 音频 | WAV/MP3/AIFF/AAC/OGG | 最长 9.5 小时 |
| PDF | — | 最长 300 页 |
| 代码 | 所有语言 | — |

### 3.3 基准测试

| 基准 | Gemini 2.0 Flash | Gemini 1.5 Pro | GPT-4o | Claude 3.5 Sonnet |
|------|-----------------|----------------|--------|-------------------|
| MMLU | 85.0% | 85.9% | 88.7% | 88.7% |
| HumanEval | 89.2% | 84.1% | 90.2% | 92.0% |
| MATH | 82.6% | 67.7% | 76.6% | 71.1% |
| GPQA | 62.1% | 46.2% | 53.6% | 59.4% |
| VideoQA | — | 最强 | — | — |
| 长文档（SCROLLS）| — | 最强 | — | — |

## 4. 访问方式

### 4.1 Google AI Studio（开发测试）

- 免费访问 Gemini 1.5 Flash、Gemini 2.0
- 提供 API Key，可直接调用
- 有请求频率限制（免费版 15 RPM）

### 4.2 Vertex AI（生产部署）

- 企业级 SLA，支持 VPC 隔离
- 支持按量计费和预留实例
- 可与 Google Cloud 服务深度集成
- 支持调优（Supervised Fine-Tuning）

### 4.3 API 调用示例

```java
// 使用 Spring AI 集成 Gemini
// 依赖：spring-ai-vertex-ai-gemini-spring-boot-starter

// application.yml
// spring.ai.vertex.ai.gemini.project-id=your-project-id
// spring.ai.vertex.ai.gemini.location=us-central1
// spring.ai.vertex.ai.gemini.chat.options.model=gemini-2.0-flash-exp

@Autowired
private ChatClient chatClient;

public String analyzeDocument(String pdfContent) {
    return chatClient.prompt()
        .user(pdfContent)
        .call()
        .content();
}
```

## 5. Gemini 2.0 新特性

### 5.1 多模态实时 API

Gemini 2.0 支持实时双向流式对话，可以处理：
- 实时音频输入输出（语音对话）
- 实时视频流分析
- 屏幕共享理解

### 5.2 原生工具调用（Native Tool Use）

- Google Search 集成（实时网络搜索）
- Code Execution（代码解释器）
- 自定义函数（Function Calling）

### 5.3 Agentic 能力增强

Gemini 2.0 针对 Multi-step Agent 做了优化：
- 更强的计划制定和任务分解
- 更少的幻觉与错误工具调用

## 6. 适用场景矩阵

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 长文档分析（>128K）| Gemini 1.5 Pro | 2M 上下文 |
| 视频内容理解 | Gemini 1.5 Pro / 2.0 | 原生视频支持 |
| 实时多模态交互 | Gemini 2.0 Flash | 实时 API |
| 高频低成本调用 | Gemini 2.0 Flash-Lite | 最低价格 |
| 本地私有部署 | Gemma 2 27B | 开源授权 |
| 代码辅助（自托管）| CodeGemma 7B | 代码专项 |

## 7. 局限性与注意事项

| 限制 | 说明 |
|------|------|
| 中文能力 | 弱于国产模型（Qwen、DeepSeek）和 GPT-4o |
| 输出一致性 | 部分场景输出格式稳定性不如 Claude |
| API 稳定性 | 部分新功能仍为 Experimental，生产环境谨慎 |
| 数据隐私 | Google AI Studio 默认会用数据训练（需关闭）|
| Gemma 上下文 | 仅 8K，不适合长文档 |

## 8. 价格与竞品对比

| 模型 | 输入($/1M) | 输出($/1M) | 上下文 | 多模态 | 综合性价比 |
|------|-----------|-----------|--------|--------|-----------|
| Gemini 2.0 Flash | $0.10 | $0.40 | 1M | ✅ | ★★★★★ |
| Gemini 1.5 Flash | $0.075 | $0.30 | 1M | ✅ | ★★★★★ |
| Gemini 1.5 Pro | $1.25 | $5.00 | 2M | ✅ | ★★★★★ |
| GPT-4o mini | $0.15 | $0.60 | 128K | ✅ | ★★★★☆ |
| Claude 3.5 Haiku | $0.80 | $4.00 | 200K | ✅ | ★★★★☆ |

---

> 📌 相关文档：[02-openai-gpt-series.md](./02-openai-gpt-series.md) | [05-meta-llama.md](./05-meta-llama.md) | [09-model-comparison.md](./09-model-comparison.md)
