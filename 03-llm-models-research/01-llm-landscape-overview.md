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
| 超大模型 | > 100B | GPT-5.4、Claude Opus 4.6、Gemini 3.1 Pro | 最高质量推理，高成本 |
| 大模型 | 30B–100B | LLaMA 4、Qwen3.5、DeepSeek-V4 | 高质量，可自托管 |
| 中型模型 | 7B–30B | Mistral 7B、LLaMA 3 8B、Qwen2.5 14B | 本地部署，低延迟 |
| 小模型 | < 7B | Phi-4、Gemma 3 2B、LLaMA 3.2 1B | 边缘设备，超低延迟 |

### 2.3 按能力类型分类

| 类别 | 代表模型 | 核心能力 |
|------|---------|---------|
| 通用对话 | GPT-5.4、Claude Sonnet 4.6、Gemini 3.1 | 指令跟随、创意写作、分析 |
| 推理专项 | o3、o4、DeepSeek-R1、GLM-Z1 | 数学、逻辑、复杂推理 |
| 代码专项 | GPT-5.3-Codex、Claude Opus 4.6、Gemini 3.1 Pro | 代码生成、调试、审查 |
| 多模态 | GPT-5.4、Gemini 3.1、Claude 4.6 | 图像/视频/音频理解、Computer Use |
| 长上下文 | Gemini 3.1 (1M)、Claude 4.6 (1M)、LLaMA 4 Scout (10M) | 超长文档分析 |
| 中文优化 | Qwen3.5、DeepSeek-V4、Kimi K2.5、GLM-5 | 中文理解与生成 |

## 3. 主流模型全景一览（2026年3月最新）

> 💡 **价格说明**：以下价格均为**每百万 token（¥/百万 tok 或 $/百万 tok）**，人民币与美元汇率约 7.3:1。价格随市场竞争频繁调整，以各厂商官方页面为准。

### 3.1 国际商业 API 模型

| 模型 | 厂商 | 发布时间 | 上下文 | 多模态 | 输入价格(¥/百万) | 输出价格(¥/百万) | 关键特性 |
|------|------|---------|--------|--------|----------------|----------------|---------|
| **GPT-5.4** | OpenAI | 2026-03 | 1M | ✅ (Computer Use) | ¥18 (Pro版¥216) | ¥108 (Pro版¥1296) | 原生Computer Use，OSWorld 75.0% |
| **GPT-5.3 Instant** | OpenAI | 2026-03 | 400K | ❌ | 未公开 | 未公开 | 幻觉率降低26.8%，快速响应 |
| **GPT-5.3-Codex** | OpenAI | 2026-02 | - | ❌ | - | - | 专业编程模型，编码能力领先 |
| **Claude Opus 4.6** | Anthropic | 2026-02 | 1M | ✅ | ¥36 | ¥180 | 最强编程能力，SWE-bench领先 |
| **Claude Sonnet 4.6** | Anthropic | 2026-02 | 1M (Beta) | ✅ | ¥36 | ¥180 | Agent规划增强，编码提升 |
| **Claude Haiku 4.5** | Anthropic | 2026-01 | - | ✅ | 低价 | 低价 | 轻量级快速响应 |
| **Gemini 3.1 Pro** | Google | 2026-02 | 1M | ✅ | ¥14-28 | ¥28-126 | ARC-AGI-2 77.1%，推理最强 |
| **Gemini 3.1 Flash-Lite** | Google | 2026-03 | 1M | ✅ | ¥1.8 | ¥10.5 | 性价比最高，可调思考层级 |
| **Gemini 3 Flash** | Google | 2026-02 | 1M | ✅ | ¥3.6 | ¥21 | 平衡速度与性能 |
| **Grok 4.2** | xAI | 2026-02 | 256K | ✅ | X Premium订阅 | X Premium订阅 | 500B参数，每周自迭代 |

### 3.2 国产商业 API 模型

| 模型 | 厂商 | 发布时间 | 上下文 | 多模态 | 输入(¥/百万) | 输出(¥/百万) | 关键特性 |
|------|------|---------|--------|--------|------------|------------|---------|
| **DeepSeek V4** | DeepSeek | 2026-03(预计) | 1M | ✅ | ~¥1 | ~¥2 | 原生多模态，编码专精 |
| **DeepSeek V3.2** | DeepSeek | 2025-12 | 1M | ❌ | ¥0.28 | ¥1.1 | 当前最新可用版本 |
| **Qwen3.5-397B-A17B** | 阿里 | 2026-02 | 256K | ✅ | 开源免费 | 开源免费 | MoE架构，Apache 2.0 |
| **Qwen3.5-Plus** | 阿里 | 2026-02 | 1M | ✅ | ¥0.8-4 | ¥4.8-24 | 阶梯计价，长文本强 |
| **Qwen3.5-Flash** | 阿里 | 2026-02 | 1M | ✅ | 更低价格 | 更低价格 | 快速响应版本 |
| **Kimi K2.5** | 月之暗面 | 2026-01 | 256K | ✅ | ~¥1.5 | ~¥4.5 | 万亿参数MoE，已开源 |
| **GLM-5** | 智谱 | 2026-02 | - | ✅ | 涨价30%起 | 涨价30%起 | Agentic Engineering最佳 |
| **豆包 2.0** | 字节跳动 | 2026年初 | - | ✅ | 低价策略 | 低价策略 | 多模态理解出色 |
| **M2.5** | MiniMax | 2026-02 | - | ✅ | ~¥17 (Lightning) | ~¥17 (Lightning) | OpenRouter霸榜 |

### 3.3 开源/可自部署模型

| 模型 | 机构 | 参数量 | 上下文 | 许可证 | 关键特性 |
|------|------|--------|--------|--------|---------|
| **DeepSeek V4** | DeepSeek | - | 1M | 预计开源 | 原生多模态，编码专精 |
| **DeepSeek V3.2** | DeepSeek | - | 1M | 开源 | 当前最新可用 |
| **Qwen3.5-397B-A17B** | 阿里 | 397B MoE (17B激活) | 256K | Apache 2.0 | 性能对标Qwen3-Max |
| **Qwen3.5-0.8B/2B/4B/9B** | 阿里 | 0.8B-9B | - | Apache 2.0 | 小尺寸开源系列 |
| **Kimi K2.5** | 月之暗面 | 1T MoE | 256K | 开源 | 万亿参数，训练成本460万美元 |
| **GLM-5** | 智谱 | - | - | 开源 | Agent与编程能力突出 |
| **LLaMA 4 Scout** | Meta | 109B MoE (17B激活) | 10M | Meta | 1000万token上下文 |
| **LLaMA 4 Maverick** | Meta | 400B MoE (17B激活) | 256K | Meta | 高效多模态 |
| **LLaMA 4 Behemoth** | Meta | 2T (训练中) | - | Meta | 性能最高基础模型预览 |

## 4. 关键技术演进时间线

```
2017 Transformer 架构（Google "Attention Is All You Need"）
  │
2020 GPT-3（OpenAI，175B 参数，few-shot 学习）
  │
2022 ChatGPT（RLHF 对齐，对话能力爆发）
  │
2023 GPT-4（多模态）/ LLaMA 2（Meta 开源）/ Claude 1/2 / Gemini
  │
2024 GPT-4o · Claude 3 系列 · LLaMA 3 · Qwen2 · DeepSeek-V2 · o1
  │
2025 ★ 全面爆发年 ★
  ├─ 2025-01 DeepSeek-R1（MIT 开源，媲美 o1，成本 1/30）
  ├─ 2025-01 Kimi k1.5（月之暗面，长思维链推理）
  ├─ 2025-02 Grok-3（xAI，强推理）
  ├─ 2025-02 Claude 3.7 Sonnet（Extended Thinking 128K 输出）
  ├─ 2025-03 Gemini 2.5 Pro（Google 新旗舰，顶级推理）
  ├─ 2025-04 GPT-4.1 系列（1M 上下文，大幅降价）
  ├─ 2025-04 o3 / o4-mini（OpenAI 推理新星）
  ├─ 2025-04 LLaMA 4 Scout/Maverick（10M 超长上下文）
  ├─ 2025-04 Qwen3 系列（思维/非思维双模式，MoE 旗舰）
  └─ 2025-12 DeepSeek V3.2（1M上下文，超低价）
  │
2026 ★ Agentic AI元年 ★
  ├─ 2026-01 Claude Haiku 4.5（Anthropic轻量模型）
  ├─ 2026-01 Kimi K2.5（月之暗面，万亿参数开源）
  ├─ 2026-02 GPT-5.3-Codex（OpenAI专业编程模型）
  ├─ 2026-02 Claude Opus 4.6 / Sonnet 4.6（编程最强）
  ├─ 2026-02 Gemini 3.1 Pro / Flash / Flash-Lite（Google新旗舰）
  ├─ 2026-02 Grok 4.2（xAI，500B参数，每周自迭代）
  ├─ 2026-02 Qwen3.5系列（阿里，397B MoE开源）
  ├─ 2026-02 GLM-5（智谱，Agentic Engineering最佳）
  ├─ 2026-02 MiniMax M2.5（OpenRouter霸榜）
  ├─ 2026-03 GPT-5.4 / 5.3 Instant（原生Computer Use）
  └─ 2026-03 DeepSeek V4（预计发布，原生多模态）
```

> **最新动态（2026年3月）**：
> - **2026-03**：OpenAI 发布 GPT-5.4，首个原生内置 Computer Use 能力的通用模型，OSWorld 测试 75.0% 超越人类
> - **2026-03**：OpenAI 发布 GPT-5.3 Instant，幻觉率降低 26.8%，反"尴尬"语气优化
> - **2026-03**：Google 发布 Gemini 3.1 Flash-Lite，性价比最高，输出速度比 Flash 快 45%
> - **2026-03**：DeepSeek V4 预计发布，原生多模态架构，编码能力专精
> - **2026-02**：Claude Opus 4.6 / Sonnet 4.6 发布，编程能力业界领先
> - **2026-02**：Gemini 3.1 Pro 发布，ARC-AGI-2 测试 77.1%，推理能力最强
> - **2026-02**：阿里 Qwen3.5 系列发布，397B MoE 开源，Apache 2.0 协议
> - **2026-02**：智谱 GLM-5 发布，"Agentic Engineering 时代最好的模型"
> - **2026-02**：xAI Grok 4.2 公测，500B 参数，每周自我迭代能力
> - **2026-01**：月之暗面 Kimi K2.5 开源，万亿参数 MoE，训练成本仅 460 万美元
> - **价格趋势**：OpenAI GPT-5.4 Pro 大幅涨价测试市场承受力；Gemini 3.1 Flash-Lite、DeepSeek 等持续压低价格
> - **技术趋势**：Agentic 能力成为竞争焦点，Computer Use、工具使用、Agent 规划成为标配

## 5. 选择模型的核心维度

| 维度 | 说明 | 推荐关注指标 |
|------|------|-------------|
| **性能/质量** | 模型在目标任务上的准确率与质量 | MMLU、HumanEval、MATH、Arena Elo、SWE-bench |
| **成本** | 每次推理的费用 | $/百万输入 token、$/百万输出 token |
| **延迟** | 响应速度 | TTFT（首字延迟）、TPS（生成速度）|
| **上下文窗口** | 单次处理的最大长度 | 最大 token 数 |
| **多模态** | 是否支持图像/音频/视频/Computer Use | 支持的模态类型 |
| **Agentic能力** | 工具使用、Computer Use、Agent规划 | 基准测试成绩、实际应用场景 |
| **隐私合规** | 数据是否可发往外部 | 是否可私有化部署 |
| **生态支持** | SDK、工具链、社区成熟度 | GitHub Stars、API 兼容性 |
| **中文能力** | 中文理解与生成质量 | C-Eval、CMMLU 分数 |

---

> 📌 各模型详细调研见后续章节（02–08），综合对比表格见 [09-model-comparison.md](./09-model-comparison.md)，选型指南见 [10-model-selection-guide.md](./10-model-selection-guide.md)。
