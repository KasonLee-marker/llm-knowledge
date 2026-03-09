# 01 - LLM 市场全景概览

## 1. 什么是大语言模型（LLM）

大语言模型（Large Language Model，LLM）是基于 Transformer 架构、通过海量文本数据预训练的神经网络模型，具备理解和生成自然语言的能力。自 2022 年 ChatGPT 发布以来，LLM 已成为 AI 产业最核心的基础设施。

## 2. 市场格局分类

### 2.1 按来源地域分类

| 类别 | 代表厂商/模型 | 特点 |
|------|-------------|------|
| 美国商业模型 | OpenAI GPT、Anthropic Claude、Google Gemini | 综合能力强，生态成熟，API 计费 |
| 美国开源模型 | Meta LLaMA、Mistral、Microsoft Phi | 开源可私有化部署 |
| 中国商业模型 | 阿里通义千问、百度文心、腾讯混元 | 中文优化，合规友好，国内 API |
| 中国开源模型 | DeepSeek、ChatGLM、Yi、InternLM | 开源中英双语，适合国内私有部署 |

### 2.2 按规模分类

| 规模 | 参数量 | 代表模型 | 适用场景 |
|------|--------|---------|---------|
| 超大模型 | > 100B | GPT-4（估计 ~1.8T MoE）、Claude 3 Opus、Gemini Ultra | 最高质量推理，高成本 |
| 大模型 | 30B–100B | LLaMA 3.1 70B、Qwen2.5 72B、DeepSeek-V3 | 高质量，可自托管 |
| 中型模型 | 7B–30B | Mistral 7B、LLaMA 3 8B、Qwen2.5 14B | 本地部署，低延迟 |
| 小模型 | < 7B | Phi-3.5 mini、Gemma 2 2B、LLaMA 3.2 1B | 边缘设备，超低延迟 |

### 2.3 按能力类型分类

| 类别 | 代表模型 | 核心能力 |
|------|---------|---------|
| 通用对话 | GPT-4o、Claude 3.5 Sonnet | 指令跟随、创意写作、分析 |
| 推理专项 | o1、o3、DeepSeek-R1 | 数学、逻辑、复杂推理 |
| 代码专项 | GPT-4o、DeepSeek-Coder | 代码生成、调试、审查 |
| 多模态 | GPT-4o、Gemini 2.0、Claude 3.5 | 图像/视频/音频理解 |
| 长上下文 | Gemini 1.5 Pro (2M)、Claude 3.5 (200K) | 超长文档分析 |
| 中文优化 | Qwen2.5、DeepSeek-V3、ChatGLM-4 | 中文理解与生成 |

## 3. 主流模型全景一览（2025年）

### 3.1 商业 API 模型

| 模型 | 厂商 | 发布时间 | 参数量 | 上下文窗口 | 多模态 | 输入价格($/1M tok) | 输出价格($/1M tok) |
|------|------|---------|--------|-----------|--------|--------------------|--------------------|
| GPT-4o | OpenAI | 2024-05 | 未公开 | 128K | ✅ | $2.50 | $10.00 |
| GPT-4o mini | OpenAI | 2024-07 | 未公开 | 128K | ✅ | $0.15 | $0.60 |
| o1 | OpenAI | 2024-09 | 未公开 | 200K | ✅ | $15.00 | $60.00 |
| o3-mini | OpenAI | 2025-01 | 未公开 | 200K | ❌ | $1.10 | $4.40 |
| Claude 3.5 Sonnet | Anthropic | 2024-10 | 未公开 | 200K | ✅ | $3.00 | $15.00 |
| Claude 3.5 Haiku | Anthropic | 2024-11 | 未公开 | 200K | ✅ | $0.80 | $4.00 |
| Claude 3 Opus | Anthropic | 2024-03 | 未公开 | 200K | ✅ | $15.00 | $75.00 |
| Gemini 2.0 Flash | Google | 2025-02 | 未公开 | 1M | ✅ | $0.10 | $0.40 |
| Gemini 1.5 Pro | Google | 2024-05 | 未公开 | 2M | ✅ | $1.25 | $5.00 |
| Gemini 1.5 Flash | Google | 2024-05 | 未公开 | 1M | ✅ | $0.075 | $0.30 |
| Qwen2.5-Max | 阿里 | 2025-01 | 未公开 | 128K | ✅ | ¥0.02 | ¥0.06 |
| ERNIE 4.0 Turbo | 百度 | 2024-10 | 未公开 | 128K | ✅ | ¥0.03 | ¥0.09 |
| Hunyuan-Turbo | 腾讯 | 2024-11 | 未公开 | 256K | ✅ | ¥0.015 | ¥0.05 |
| DeepSeek-V3 | DeepSeek | 2024-12 | 671B MoE | 128K | ❌ | ¥0.002 | ¥0.008 |

### 3.2 开源/可自部署模型

| 模型 | 机构 | 参数量 | 上下文窗口 | 许可证 | MMLU | 代码能力 | 中文能力 |
|------|------|--------|-----------|--------|------|---------|---------|
| LLaMA 3.3 70B | Meta | 70B | 128K | Meta Llama | 86.0 | 优秀 | 良好 |
| LLaMA 3.1 405B | Meta | 405B | 128K | Meta Llama | 88.6 | 优秀 | 良好 |
| Qwen2.5 72B | 阿里 | 72B | 128K | Apache 2.0 | 86.1 | 优秀 | 优秀 |
| Qwen2.5-Coder 32B | 阿里 | 32B | 128K | Apache 2.0 | - | 卓越 | 优秀 |
| DeepSeek-V3 | DeepSeek | 671B MoE | 128K | DeepSeek | 88.5 | 优秀 | 优秀 |
| DeepSeek-R1 | DeepSeek | 671B MoE | 128K | MIT | 90.8 | 卓越 | 优秀 |
| Mistral Large 2 | Mistral | ~123B | 128K | MRL | 84.0 | 优秀 | 一般 |
| Mistral Small 3 | Mistral | 24B | 128K | Apache 2.0 | 81.0 | 良好 | 一般 |
| ChatGLM-4 | 智谱 | 9B | 128K | 商业授权 | - | 良好 | 优秀 |
| Yi-34B | 零一万物 | 34B | 200K | Yi License | 76.3 | 良好 | 优秀 |
| InternLM-2.5 | 上海AI | 7B/20B | 1M | Apache 2.0 | 79.6 | 良好 | 优秀 |
| Phi-3.5 mini | Microsoft | 3.8B | 128K | MIT | 69.0 | 良好 | 一般 |
| Gemma 2 27B | Google | 27B | 8K | Gemma | 75.2 | 良好 | 一般 |

## 4. 关键技术演进时间线

```
2017 Transformer 架构（Google "Attention Is All You Need"）
  │
2019 GPT-2（OpenAI，1.5B 参数，文本生成突破）
  │
2020 GPT-3（OpenAI，175B 参数，few-shot 学习）
  │
2022 ChatGPT（RLHF 对齐，对话能力爆发）
  │   InstructGPT / RLHF 对齐技术成熟
  │
2023 GPT-4（多模态、更强推理）
  │   LLaMA 1/2（Meta 开源，生态爆发）
  │   Claude 1/2（Anthropic，安全对齐）
  │   Gemini（Google 多模态）
  │
2024 GPT-4o（低延迟多模态）
  │   Claude 3 系列（Haiku/Sonnet/Opus）
  │   LLaMA 3（Meta 开源最强）
  │   Qwen2 / DeepSeek-V2（国产崛起）
  │   o1（OpenAI 推理专项）
  │
2025 DeepSeek-R1/V3（超强推理+超低价格）
     Gemini 2.0（多模态全面升级）
     GPT-4.5 / o3（下一代推理）
     Qwen2.5（开源最强综合）
```

## 5. 选择模型的核心维度

| 维度 | 说明 | 推荐关注指标 |
|------|------|-------------|
| **性能/质量** | 模型在目标任务上的准确率与质量 | MMLU、HumanEval、MATH、Arena Elo |
| **成本** | 每次推理的费用 | $/百万输入 token、$/百万输出 token |
| **延迟** | 响应速度 | TTFT（首字延迟）、TPS（生成速度）|
| **上下文窗口** | 单次处理的最大长度 | 最大 token 数 |
| **多模态** | 是否支持图像/音频/视频 | 支持的模态类型 |
| **隐私合规** | 数据是否可发往外部 | 是否可私有化部署 |
| **生态支持** | SDK、工具链、社区成熟度 | GitHub Stars、API 兼容性 |
| **中文能力** | 中文理解与生成质量 | C-Eval、CMMLU 分数 |

---

> 📌 各模型详细调研见后续章节（02–08），综合对比表格见 [09-model-comparison.md](./09-model-comparison.md)，选型指南见 [10-model-selection-guide.md](./10-model-selection-guide.md)。
