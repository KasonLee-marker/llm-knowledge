# 07 - DeepSeek 系列深度调研

## 1. 系列概览

DeepSeek（深度求索）是深圳人工智能公司幻方科技旗下的 AI 研究机构。2024 年底 DeepSeek-V3 和 DeepSeek-R1 的发布在全球引起强烈关注——以极低的训练成本实现了接近乃至超越 GPT-4o 的性能，彻底颠覆了"大模型需要巨额算力"的认知。

## 2. 主要模型版本对比

### 2.1 基础/对话模型

| 模型 | 发布时间 | 参数量 | 架构 | 上下文 | 许可证 | MMLU | 特点 |
|------|---------|--------|------|--------|--------|------|------|
| DeepSeek-LLM 7B/67B | 2023-11 | 7B/67B | Dense | 4K | DeepSeek | 71.3/79.8 | 首代 |
| DeepSeek-V2 | 2024-05 | 236B (21B激活) | MoE | 128K | DeepSeek | 78.5 | MoE 突破 |
| DeepSeek-V2.5 | 2024-09 | 236B (21B激活) | MoE | 128K | DeepSeek | 80.4 | 更强对话 |
| DeepSeek-V3 | 2024-12 | 671B (37B激活) | MoE | 128K | DeepSeek | **88.5** | 媲美 GPT-4o ⭐ |

### 2.2 推理专项模型

| 模型 | 发布时间 | 参数量 | 架构 | 推理方式 | AIME24 | MATH-500 | 许可证 |
|------|---------|--------|------|---------|--------|---------|--------|
| DeepSeek-R1-Zero | 2025-01 | 671B | MoE | 纯 RL，无 SFT | 71.0% | 95.9% | MIT |
| DeepSeek-R1 | 2025-01 | 671B | MoE | RL + SFT | **79.8%** | **97.3%** | MIT ⭐ |
| DeepSeek-R1-Distill-Qwen-1.5B | 2025-01 | 1.5B | Dense | 蒸馏 | 28.9% | 83.9% | MIT |
| DeepSeek-R1-Distill-Qwen-7B | 2025-01 | 7B | Dense | 蒸馏 | 55.5% | 92.8% | MIT |
| DeepSeek-R1-Distill-Qwen-14B | 2025-01 | 14B | Dense | 蒸馏 | 69.7% | 93.9% | MIT |
| DeepSeek-R1-Distill-Llama-70B | 2025-01 | 70B | Dense | 蒸馏 | 70.0% | 94.5% | MIT |

### 2.3 代码专项模型

| 模型 | 参数量 | HumanEval | MBPP | 特点 |
|------|--------|-----------|------|------|
| DeepSeek-Coder-V2-Lite | 16B (2.4B激活) | 81.1% | 72.0% | 轻量代码 MoE |
| DeepSeek-Coder-V2 | 236B (21B激活) | 90.2% | 76.2% | 旗舰代码模型 |
| DeepSeek-V3（通用）| 671B | 82.6% | — | 通用代码能力强 |

## 3. DeepSeek-V3 技术深度

### 3.1 核心创新

**Multi-head Latent Attention（MLA）：**
- 将 KV Cache 压缩 94%，显著降低显存占用
- 同等质量下 GPU 内存需求仅为传统 MHA 的 1/10

**MoE 架构（专家混合）：**
- 671B 总参数，推理时仅激活 37B（约 5.5%）
- 256 个路由专家 + 1 个共享专家
- 每 token 激活 8 个专家

**多 Token 预测（Multi-Token Prediction）：**
- 每次预测多个 token，大幅提升推理吞吐

### 3.2 训练成本突破

| 对比 | DeepSeek-V3 | GPT-4（估计） | LLaMA 3.1 405B（估计） |
|------|------------|--------------|----------------------|
| 训练 GPU 时 | 278 万 H800 小时 | 数千万 A100 时 | 数百万 H100 时 |
| 估算训练成本 | ~$560 万 | ~$1 亿+ | ~$3000 万 |
| 模型参数 | 671B (MoE) | 未公开 | 405B |

> 这一成本效率创下行业记录，引发全球 AI 圈广泛关注。

### 3.3 性能基准

| 基准 | DeepSeek-V3 | GPT-4o | Claude 3.5 Sonnet | Qwen2.5-72B |
|------|------------|--------|-------------------|-------------|
| MMLU | 88.5% | 88.7% | 88.7% | 86.1% |
| HumanEval | 82.6% | 90.2% | 92.0% | 86.6% |
| MATH-500 | 90.2% | 76.6% | 71.1% | — |
| LiveCodeBench | **40.6%** | 34.2% | 36.3% | — |
| C-Eval | **90.1%** | 87.5% | 86.4% | 90.1% |
| CLUEWSC | — | — | — | — |

## 4. DeepSeek-R1 技术深度

### 4.1 训练方法创新

DeepSeek-R1 的革命性在于用**纯强化学习**训练出推理能力（R1-Zero），不依赖人工标注的思维链数据：

```
训练流程：
1. DeepSeek-V3-Base（基础预训练）
2. 冷启动 SFT（少量长 CoT 数据）
3. 推理导向强化学习（GRPO 算法）
4. 拒绝采样 SFT（过滤高质量推理样本）
5. 混合 RL（推理 + 通用能力）
```

### 4.2 与 OpenAI o1 对比

| 基准 | DeepSeek-R1 | OpenAI o1 | o3-mini (high) |
|------|------------|-----------|----------------|
| AIME 2024 | **79.8%** | 74.4% | 86.5% |
| MATH-500 | **97.3%** | 96.4% | 97.9% |
| Codeforces | 96.3% | 96.6% | 99.5% |
| GPQA Diamond | **71.5%** | 75.7% | 79.7% |
| HumanEval | **92.6%** | 92.4% | 93.7% |

**结论**：DeepSeek-R1 基本与 OpenAI o1 持平，远低于 o1 的成本（API 价格约 1/30）。

### 4.3 蒸馏版本性能

小模型蒸馏版本的突破性表现：

| 蒸馏模型 | 参数量 | AIME24 | MATH-500 | 超越对象 |
|---------|--------|--------|---------|---------|
| R1-Distill-Qwen-7B | 7B | 55.5% | 92.8% | 超越 Claude 3.5 Sonnet 数学 |
| R1-Distill-Qwen-14B | 14B | 69.7% | 93.9% | 接近 o1-mini |
| R1-Distill-Llama-70B | 70B | 70.0% | 94.5% | 接近 o1 |

## 5. API 访问

### 5.1 官方 API 定价

| 模型 | 输入(¥/1M tok) | 输出(¥/1M tok) | 缓存命中(¥/1M) |
|------|--------------|--------------|--------------|
| deepseek-chat (V3) | ¥0.002 | ¥0.008 | ¥0.0001 |
| deepseek-reasoner (R1) | ¥0.004 | ¥0.016 | ¥0.001 |

> 与 GPT-4o 相比，DeepSeek-V3 价格约为其 **1/100**，DeepSeek-R1 约为 o1 的 **1/30**。

### 5.2 第三方 API 平台

| 平台 | 支持模型 | 优势 |
|------|---------|------|
| DeepSeek 官方 | V3、R1 | 最低价格，直连 |
| SiliconFlow | V3、R1 | 国内加速，低延迟 |
| 火山引擎（字节）| V3、R1 | 企业级 SLA |
| 阿里云百炼 | V3 | 与阿里云集成 |
| AWS Bedrock | V3 | 企业级，合规 |

### 5.3 Java 集成

```java
// DeepSeek API 完全兼容 OpenAI API 格式
// Spring AI 配置
// spring.ai.openai.base-url=https://api.deepseek.com
// spring.ai.openai.api-key=your-deepseek-api-key
// spring.ai.openai.chat.options.model=deepseek-chat

// 使用推理模型 R1
// spring.ai.openai.chat.options.model=deepseek-reasoner
```

## 6. 本地部署

```bash
# Ollama 部署
ollama pull deepseek-v3:671b  # 需要多卡
ollama pull deepseek-r1:7b    # 单卡可用
ollama pull deepseek-r1:14b   # 16GB+ 显存

# 运行 R1 蒸馏版（推荐本地推理）
ollama run deepseek-r1:14b
```

## 7. 适用场景矩阵

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 通用对话（成本优先）| DeepSeek-V3 API | 极低成本，媲美 GPT-4o |
| 数学/科学推理 | DeepSeek-R1 | 接近 o1 水平 |
| 代码复杂推理 | DeepSeek-R1 Distill 14B | 本地部署，接近 o1-mini |
| 中文企业应用 | DeepSeek-V3 | 顶级中文能力，超低成本 |
| 私有本地推理 | R1-Distill-Qwen-7B | 7B 实现强推理 |
| 微调定制 | DeepSeek-R1-Distill 系列 | MIT 协议，可商用 |

---

> 📌 相关文档：[06-alibaba-qwen.md](./06-alibaba-qwen.md) | [09-model-comparison.md](./09-model-comparison.md) | [11-fine-tuning-candidates.md](./11-fine-tuning-candidates.md)
