# 05 - Meta LLaMA 系列深度调研

## 1. 系列概览

Meta 的 LLaMA（Large Language Model Meta AI）系列是目前最重要的开源 LLM 生态，从 2023 年 LLaMA 1 开始，已发展为与商业模型比肩的开源力量。LLaMA 3.1 405B 是目前开源/开放模型中综合能力最强的之一。

## 2. 主要模型版本演进

| 系列 | 发布时间 | 最大参数量 | 上下文 | 特色 | 许可证 |
|------|---------|-----------|--------|------|--------|
| LLaMA 1 | 2023-02 | 65B | 2K | 研究首发 | 研究仅限 |
| LLaMA 2 | 2023-07 | 70B | 4K | 商用开放 | Meta Llama 2 |
| LLaMA 3 | 2024-04 | 70B | 8K | 多语言 | Meta Llama 3 |
| LLaMA 3.1 | 2024-07 | 405B | 128K | 生态成熟 | Meta Llama 3.1 ⭐ |
| LLaMA 3.2 | 2024-09 | 90B | 128K | 多模态 | Meta Llama 3.2 |
| LLaMA 3.3 | 2024-12 | 70B | 128K | 能力升级 | Meta Llama 3.3 |
| **LLaMA 4 Scout** | **2025-04** | **109B MoE** | **10M** | **10M 超长上下文** | Meta Llama 4 ⭐ |
| **LLaMA 4 Maverick** | **2025-04** | **400B MoE** | **1M** | **多模态旗舰** | Meta Llama 4 ⭐ |

## 3. LLaMA 4 系列详解（2025 最新）

### 3.1 LLaMA 4 Scout

| 参数 | 规格 |
|------|------|
| 总参数量 | 109B（MoE，17B 激活）|
| 激活参数 | 17B |
| **上下文窗口** | **10,000,000 tokens（1000万！）** |
| 许可证 | Meta Llama 4 |
| 多模态 | 文本 + 图像 |
| 开放 API | ✅（Meta AI）|

**10M 上下文的实际意义**：
- 约等于 7500 页 PDF / 100 万行代码 / 10 小时视频帧
- 是 Gemini 2.5 Pro（1M）的 10 倍
- 可一次加载整个大型代码库进行分析

### 3.2 LLaMA 4 Maverick

| 参数 | 规格 |
|------|------|
| 总参数量 | ~400B（MoE，17B 激活）|
| 激活参数 | 17B |
| 上下文窗口 | 1M tokens |
| 多模态 | 文本 + 图像 |
| 定位 | 旗舰综合能力 |

**基准测试（LLaMA 4 Maverick vs 竞品）**：

| 基准 | LLaMA 4 Maverick | GPT-4o | Claude 3.5 Sonnet | DeepSeek-V3 |
|------|-----------------|--------|-------------------|-------------|
| MMLU | 86.5% | 88.7% | 88.7% | 88.5% |
| HumanEval | 87.8% | 90.2% | 92.0% | 82.6% |
| MATH-500 | 73.5% | 76.6% | 71.1% | 90.2% |
| IMAGE QA | 优秀 | 优秀 | — | — |

## 4. LLaMA 3.x 详细规格

### 3.1 LLaMA 3.1 系列（主力版本）

| 模型 | 参数量 | 上下文 | 量化版本 | GPU 内存需求 | 最佳用途 |
|------|--------|--------|---------|------------|---------|
| LLaMA 3.1 8B | 8B | 128K | GGUF Q4 (~4.5GB) | 6GB+ | 轻量本地推理 |
| LLaMA 3.1 70B | 70B | 128K | GGUF Q4 (~40GB) | 48GB+ | 高质量本地推理 |
| LLaMA 3.1 405B | 405B | 128K | GGUF Q2 (~230GB) | 多卡集群 | 顶级开源能力 |

### 3.2 LLaMA 3.2 系列（多模态 + 小型化）

| 模型 | 参数量 | 上下文 | 多模态 | 特色 |
|------|--------|--------|--------|------|
| LLaMA 3.2 1B | 1B | 128K | ❌ | 边缘部署，移动端 |
| LLaMA 3.2 3B | 3B | 128K | ❌ | 超轻量，离线场景 |
| LLaMA 3.2 11B | 11B | 128K | ✅（图像） | 视觉-语言轻量 |
| LLaMA 3.2 90B | 90B | 128K | ✅（图像） | 视觉-语言旗舰 |

### 3.3 LLaMA 3.3 70B（最新）

LLaMA 3.3 70B 是对 LLaMA 3.1 70B 的改进版本：
- 数学和推理能力显著提升
- 工具调用（Tool Use）更稳定
- 成本效益达到 LLaMA 3.1 405B 的 75%–80%

## 4. 基准测试

### 4.1 与商业模型对比

| 基准 | LLaMA 3.1 405B | LLaMA 3.3 70B | GPT-4o | Claude 3.5 Sonnet |
|------|----------------|---------------|--------|-------------------|
| MMLU | 88.6% | 86.0% | 88.7% | 88.7% |
| HumanEval | 89.0% | 88.4% | 90.2% | 92.0% |
| MATH | 73.8% | 77.0% | 76.6% | 71.1% |
| GPQA | 51.1% | 50.7% | 53.6% | 59.4% |
| MBPP | 84.8% | — | — | — |

### 4.2 开源模型横向对比

| 模型 | MMLU | HumanEval | 中文（C-Eval） | 上下文 | 开源协议 |
|------|------|-----------|--------------|--------|---------|
| LLaMA 3.1 405B | 88.6% | 89.0% | 一般 | 128K | Meta Llama 3.1 |
| LLaMA 3.3 70B | 86.0% | 88.4% | 一般 | 128K | Meta Llama 3.3 |
| Qwen2.5 72B | 86.1% | 86.6% | 优秀 | 128K | Apache 2.0 |
| DeepSeek-V3 | 88.5% | 82.6% | 优秀 | 128K | DeepSeek |
| Mistral Large 2 | 84.0% | 92.1% | 一般 | 128K | MRL |

## 5. 许可证说明

LLaMA 系列的商业使用许可证逐代开放：

| 版本 | 许可证类型 | 商业使用 | 月活限制 |
|------|-----------|---------|---------|
| LLaMA 1 | Meta Research License | ❌ | N/A |
| LLaMA 2 | Meta Llama 2 Community | ✅（月活 <7亿）| 7亿 MAU |
| LLaMA 3.x | Meta Llama 3 Community | ✅（月活 <7亿）| 7亿 MAU |

> ⚠️ **注意**：月活超过 7 亿的产品需要向 Meta 申请特别许可证。

## 6. 部署方案

### 6.1 本地部署（Ollama）

```bash
# 安装 Ollama
curl -fsSL https://ollama.ai/install.sh | sh

# 拉取并运行 LLaMA 3.3 70B（需 48GB+ 显存）
ollama run llama3.3:70b

# 拉取量化版本（更低显存需求）
ollama run llama3.3:70b-instruct-q4_K_M
```

### 6.2 云端托管（推荐 API 提供商）

| 提供商 | 支持模型 | 价格($/1M tok) | 特点 |
|--------|---------|---------------|------|
| Groq | LLaMA 3.3 70B | $0.59/$0.79 | 极高吞吐（280 tok/s）|
| Together AI | LLaMA 3.1 405B | $3.50/$3.50 | 全系列支持 |
| Fireworks AI | LLaMA 3.1 70B | $0.90/$0.90 | 低延迟 |
| AWS Bedrock | LLaMA 3.1 系列 | 按量计费 | 企业级 SLA |
| Azure AI | LLaMA 3.1 系列 | 按量计费 | 与 Azure 集成 |

### 6.3 量化部署（GGUF + llama.cpp）

```bash
# 下载量化模型
wget https://huggingface.co/bartowski/Meta-Llama-3.1-8B-Instruct-GGUF/resolve/main/Meta-Llama-3.1-8B-Instruct-Q4_K_M.gguf

# 使用 llama.cpp 推理服务器
./llama-server -m Meta-Llama-3.1-8B-Instruct-Q4_K_M.gguf --port 8080
```

### 6.4 Java 集成

```java
// Spring AI 通过 Ollama 集成 LLaMA
// application.yml
// spring.ai.ollama.base-url=http://localhost:11434
// spring.ai.ollama.chat.options.model=llama3.3:70b

// 或通过 OpenAI 兼容 API（Groq / Together AI）
// spring.ai.openai.base-url=https://api.groq.com/openai
// spring.ai.openai.api-key=your-groq-key
// spring.ai.openai.chat.options.model=llama-3.3-70b-versatile
```

## 7. 微调指南

LLaMA 是最受欢迎的微调基础模型之一：

| 微调方法 | 适用场景 | 工具 | GPU 需求 |
|---------|---------|------|---------|
| LoRA/QLoRA | 轻量领域适配 | Hugging Face PEFT | 8B: 1×RTX 3090 |
| SFT（全量） | 深度领域定制 | LLaMA-Factory | 8B: 4×A100 |
| RLHF | 对齐优化 | TRL | 多卡集群 |
| DPO | 偏好对齐 | TRL/Axolotl | 单卡可行 |

推荐工具：
- **LLaMA-Factory**：最易用的一站式微调框架
- **Axolotl**：灵活配置，支持多种微调方式
- **Unsloth**：速度最快，显存使用最少

## 8. 适用场景矩阵

| 场景 | 推荐版本 | 部署方式 | 成本 |
|------|---------|---------|------|
| 数据敏感私有部署 | LLaMA 3.1 70B | 自托管 | 硬件成本 |
| 边缘设备/移动端 | LLaMA 3.2 1B/3B | 本地推理 | 极低 |
| 高质量低成本 API | LLaMA 3.3 70B via Groq | 第三方 API | $0.59/1M |
| 企业微调定制 | LLaMA 3.1 8B | 微调后自托管 | 中等 |
| 最强开源能力 | LLaMA 3.1 405B | 集群部署 | 高 |

---

> 📌 相关文档：[06-alibaba-qwen.md](./06-alibaba-qwen.md) | [08-other-major-models.md](./08-other-major-models.md) | [11-fine-tuning-candidates.md](./11-fine-tuning-candidates.md)
