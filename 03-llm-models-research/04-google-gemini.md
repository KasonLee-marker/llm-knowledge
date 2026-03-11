# 04 - Google Gemini 系列深度调研

## 1. 系列概览

Google DeepMind 于 2023 年 12 月发布 Gemini 系列，作为 PaLM 2 的继任者。Gemini 从设计之初就是原生多模态架构（非视觉适配器），并以超长上下文和多模态能力著称。2026 年 Gemini 3 系列的发布进一步巩固了其在多模态和推理领域的领先地位。

## 2. 主要模型版本对比

> 价格单位：每百万 token

### 2.1 Gemini 3 系列（2026年旗舰）

| 模型 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 输入($/M) | 输出($/M) | 定位 |
|------|---------|--------|--------|------------|------------|---------|---------|------|
| **Gemini 3.1 Pro** | 2026-02 | 1M | ✅（图/视/音）| ¥14.6-29.2 | ¥87.6-131 | $2.00-$4.00 | $12.00-$18.00 | 编程旗舰 ⭐ |
| **Gemini 3 Pro** | 2025-12 | 1M | ✅（图/视/音）| ¥14.6-29.2 | ¥87.6-131 | $2.00-$4.00 | $12.00-$18.00 | 推理旗舰 ⭐ |
| **Gemini 3 Flash** | 2025-12 | 1M | ✅ | ¥3.6 | ¥21.9 | $0.50 | $3.00 | 性价比首选 ⭐ |
| **Gemini 3 Flash-Lite** | 2026-02 | 1M | ✅ | ¥1.8 | ¥11 | $0.25 | $1.50 | 极速低价 ⭐ |

> 💡 **上下文分级定价**：Gemini 3 系列采用上下文分级定价，≤200K 为标准价格，>200K 输入价格翻倍

### 2.2 Gemini 2.5 系列（2025年主力）

| 模型 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 输入($/M) | 输出($/M) | 定位 |
|------|---------|--------|--------|------------|------------|---------|---------|------|
| **Gemini 2.5 Pro** | 2025-06 | 1M | ✅（图/视/音）| ¥9.1-18.3 | ¥36.5-109.5 | $1.25-$2.50 | $5.00-$15.00 | 推理旗舰 |
| **Gemini 2.5 Flash** | 2025-05 | 1M | ✅ | ¥1.1 | ¥4.4 | $0.15 | $0.60 | 性价比首选 |
| Gemini 2.5 Flash-Lite | 2025-02 | 1M | ✅ | ¥0.55 | ¥2.2 | $0.075 | $0.30 | 极低成本 |

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

### 3.1 Gemini 3.1 Pro：2026年编程和多模态旗舰

**核心突破**：
- 在编程任务上超越 GPT-5.2 和 Claude 4.6
- 原生多模态理解（图像/视频/音频）
- 1M token 上下文 + 深度推理能力
- 支持 Agentic 工作流

**关键基准**（Gemini 3.1 Pro vs 竞品）：

| 基准 | Gemini 3.1 Pro | GPT-5.2 | Claude 4.6 | DeepSeek-V3.2 |
|------|----------------|---------|------------|---------------|
| GPQA Diamond | 88.0% | 75.0% | 82.0% | 78.0% |
| AIME 2025 | 90.0% | 85.0% | 88.0% | 86.0% |
| SWE-Bench | 72.0% | 70.0% | 79.6% | 65.0% |
| MMLU | 89.0% | 89.5% | 90.5% | 88.5% |

### 3.2 Gemini 3 Flash / Flash-Lite：极致性价比

**Gemini 3 Flash**：
- 价格：$0.50/$3.00 per MTok
- 1M 上下文窗口
- 支持多模态（文本/图像/视频）
- 适合高频调用场景

**Gemini 3 Flash-Lite**：
- 价格：$0.25/$1.50 per MTok
- 1M 上下文窗口
- 速度最快
- 适合大批量处理

### 3.3 超长上下文

Gemini 系列支持业界领先的上下文窗口：

| 场景 | 等效内容量 |
|------|-----------|
| 1M tokens | ~750 页 PDF |
| 1M tokens | ~50 万行代码 |
| 1M tokens | ~11 小时音频 |
| 1M tokens | ~1 小时高清视频 |
| 2M tokens (Gemini 1.5 Pro) | ~1500 页 PDF |

**Needle-in-Haystack 测试**：在 200 万 token 的文本中查找特定信息，准确率 >99%（业界最高）。

### 3.4 原生多模态能力

Gemini 支持的输入模态：

| 模态 | 支持内容 | 最大限制 |
|------|---------|---------|
| 文本 | 所有语言 | 1M-2M tokens |
| 图像 | JPEG/PNG/WEBP/HEIC/HEIF | 每请求 16 张 |
| 视频 | MP4/MPEG/AVI/FLV/WMV | 最长 1 小时 |
| 音频 | WAV/MP3/AIFF/AAC/OGG | 最长 9.5 小时 |
| PDF | — | 最长 300 页 |
| 代码 | 所有语言 | — |

## 4. 访问方式

### 4.1 Google AI Studio（开发测试）

- 免费访问 Gemini 2.5 Flash、Gemini 3 Flash
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
// spring.ai.vertex.ai.gemini.chat.options.model=gemini-3.0-pro

@Autowired
private ChatClient chatClient;

public String analyzeDocument(String content) {
    return chatClient.prompt()
        .user(content)
        .call()
        .content();
}
```

## 5. Gemini 3 新特性

### 5.1 多模态实时 API

Gemini 3 支持实时双向流式对话，可以处理：
- 实时音频输入输出（语音对话）
- 实时视频流分析
- 屏幕共享理解

### 5.2 原生工具调用（Native Tool Use）

- Google Search 集成（实时网络搜索）
- Code Execution（代码解释器）
- 自定义函数（Function Calling）
- Google Maps 集成

### 5.3 Agentic 能力增强

Gemini 3 针对 Multi-step Agent 做了优化：
- 更强的计划制定和任务分解
- 更少的幻觉与错误工具调用
- 支持 Deep Research 模式

### 5.4 图像生成

Gemini 3 Pro 支持原生图像生成：
- 价格：$0.039-$0.24 / 张（根据分辨率）
- 支持 1024x1024 到 4096x4096
- Batch API 50% 折扣

## 6. 适用场景矩阵

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 长文档分析（>128K）| Gemini 2.5 Pro | 1M 上下文，价格较低 |
| 视频内容理解 | Gemini 3 Pro | 原生视频支持 |
| 实时多模态交互 | Gemini 2.5 Flash Live | 实时 API |
| 高频低成本调用 | Gemini 3 Flash-Lite | 最低价格 |
| 编程任务 | Gemini 3.1 Pro | 编程能力最强 |
| 本地私有部署 | Gemma 3 27B | 开源授权 |
| 代码辅助（自托管）| CodeGemma 7B | 代码专项 |

## 7. 价格与竞品对比

| 模型 | 输入($/1M) | 输出($/1M) | 上下文 | 多模态 | 综合性价比 |
|------|-----------|-----------|--------|--------|-----------|
| Gemini 3 Flash-Lite | $0.25 | $1.50 | 1M | ✅ | ★★★★★ |
| Gemini 3 Flash | $0.50 | $3.00 | 1M | ✅ | ★★★★★ |
| Gemini 2.5 Flash | $0.15 | $0.60 | 1M | ✅ | ★★★★★ |
| Gemini 2.5 Pro | $1.25 | $5.00 | 1M | ✅ | ★★★★★ |
| Gemini 3.1 Pro | $2.00 | $12.00 | 1M | ✅ | ★★★★★ |
| GPT-5-mini | $0.25 | $2.00 | 400K | ✅ | ★★★★☆ |
| Claude Haiku 4.5 | $1.00 | $5.00 | 200K | ✅ | ★★★★☆ |

## 8. 局限性与注意事项

| 限制 | 说明 |
|------|------|
| 中文能力 | 弱于国产模型（Qwen、DeepSeek）和 GPT-5 |
| 输出一致性 | 部分场景输出格式稳定性不如 Claude |
| API 稳定性 | 部分新功能仍为 Preview，生产环境谨慎 |
| 数据隐私 | Google AI Studio 默认会用数据训练（需关闭）|
| 区域限制 | 部分模型在特定区域不可用 |

## 9. 成本优化策略

| 策略 | 说明 | 节省 |
|------|------|------|
| 使用 Flash-Lite | 最低价格档位 | 80%+ |
| Batch API | 50% 折扣 | 50% |
| 上下文缓存 | 75% 成本降低 | 75% |
| 控制上下文长度 | 避免 >200K 触发高价 | 50% |

---

> 📌 相关文档：[02-openai-gpt-series.md](./02-openai-gpt-series.md) | [05-meta-llama.md](./05-meta-llama.md) | [09-model-comparison.md](./09-model-comparison.md)

> 📅 最后更新：2026年3月
