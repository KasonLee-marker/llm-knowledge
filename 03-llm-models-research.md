# 03 - LLM 模型研究（LLM Models Research）

本模块对市面主流 LLM 模型进行全面调研，包含商业 API 模型与开源模型的技术规格、基准测试、成本分析与选型指南，重点面向 Java 后端开发者的生产实践需求。

## 目录

| # | 文档 | 简介 |
|---|------|------|
| 1 | [LLM 市场全景概览](./03-llm-models-research/01-llm-landscape-overview.md) | 全局分类、市场格局、主流模型一览与选型维度 |
| 2 | [OpenAI GPT 系列](./03-llm-models-research/02-openai-gpt-series.md) | GPT-4o、GPT-4o mini、o1、o3-mini 详细调研与对比 |
| 3 | [Anthropic Claude 系列](./03-llm-models-research/03-anthropic-claude.md) | Claude 3/3.5/3.7 系列特性、安全机制与集成指南 |
| 4 | [Google Gemini 系列](./03-llm-models-research/04-google-gemini.md) | Gemini 2.0 Flash、1.5 Pro、Gemma 开源模型 |
| 5 | [Meta LLaMA 系列](./03-llm-models-research/05-meta-llama.md) | LLaMA 3.1/3.2/3.3 规格、部署与微调指南 |
| 6 | [阿里通义千问（Qwen）](./03-llm-models-research/06-alibaba-qwen.md) | Qwen2.5 全系列、代码/数学专项、中文优势 |
| 7 | [DeepSeek 系列](./03-llm-models-research/07-deepseek.md) | DeepSeek-V3、R1 技术突破、超低成本分析 |
| 8 | [其他主要模型](./03-llm-models-research/08-other-major-models.md) | Mistral、Phi、ERNIE、混元、ChatGLM、Yi、InternLM 等 |
| 9 | [全面模型对比表格](./03-llm-models-research/09-model-comparison.md) | 多维度横向对比：性能、成本、延迟、上下文、合规 |
| 10 | [模型选型指南](./03-llm-models-research/10-model-selection-guide.md) | 场景化决策树、Java 集成策略与成本控制 |
| 11 | [微调候选模型](./03-llm-models-research/11-fine-tuning-candidates.md) | 微调方法对比、候选模型推荐、工具链指南 |
| 12 | [模型发展趋势](./03-llm-models-research/12-model-trends.md) | 推理革命、MoE 架构、多模态演进与市场预测 |

## 核心对比速览

| 模型 | 综合能力 | 代码 | 中文 | 成本 | 上下文 | 可私有部署 |
|------|---------|------|------|------|--------|-----------|
| GPT-4o | ★★★★★ | ★★★★★ | ★★★★☆ | ★★★☆☆ | 128K | ❌ |
| Claude 3.5 Sonnet | ★★★★★ | ★★★★★ | ★★★☆☆ | ★★★☆☆ | 200K | ❌ |
| Gemini 1.5 Pro | ★★★★☆ | ★★★★☆ | ★★★☆☆ | ★★★★☆ | 2M | ❌ |
| DeepSeek-V3 | ★★★★★ | ★★★★☆ | ★★★★★ | ★★★★★ | 128K | ✅ |
| DeepSeek-R1 | ★★★★★ | ★★★★★ | ★★★★★ | ★★★★★ | 128K | ✅ |
| Qwen2.5-72B | ★★★★☆ | ★★★★★ | ★★★★★ | ★★★★☆ | 128K | ✅ |
| LLaMA 3.1 70B | ★★★★☆ | ★★★★☆ | ★★★☆☆ | ★★★★☆ | 128K | ✅ |
| Mistral Small 3 | ★★★★☆ | ★★★★☆ | ★★★☆☆ | ★★★★☆ | 128K | ✅ |

## 学习路径建议

```
LLM 市场概览（01）
       │
       ├── 主流模型调研（02–08）
       │       ├── 国际商业：OpenAI (02) → Claude (03) → Gemini (04)
       │       └── 中国/开源：Meta (05) → Qwen (06) → DeepSeek (07) → 其他 (08)
       │
       ├── 横向对比（09）← 快速查表
       │
       └── 选型决策（10）
               │
               ├── 确定方案后 → 微调指南（11）
               └── 持续关注 → 趋势展望（12）
```

## 与其他模块的关系

- 本模块为 [05 - LLM APIs 与服务提供商](./05-llm-apis-providers.md) 提供模型选型基础
- [04 - Agent 框架](./04-agent-frameworks.md) 中的框架均依赖本模块选定的模型
- 模型的 Function Calling 能力详见 [02 - LLM 基础](./02-llm-fundamentals.md) 中的相关章节