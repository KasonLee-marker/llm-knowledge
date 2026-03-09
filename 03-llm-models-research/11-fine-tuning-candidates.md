# 11 - 微调候选模型指南

## 1. 为什么需要微调

| 场景 | 是否需要微调 | 原因 |
|------|------------|------|
| 通用对话、问答 | ❌ 通常不需要 | Prompt 工程 + RAG 足够 |
| 特定格式输出 | 可选 | Structured Output 可解决 |
| 领域知识注入 | 推荐 RAG 优先 | 微调成本高，RAG 更灵活 |
| 特定风格/语气 | ✅ 微调效果好 | 大量数据后对话风格一致 |
| 降低延迟/成本 | ✅ 推荐微调小模型 | 小模型微调后替代大模型 |
| 极度垂直场景 | ✅ 微调效果最佳 | 法律、医疗、金融专业知识 |

**优先顺序**：Prompt 工程 → RAG → 微调小模型 → 从头预训练（极少需要）

## 2. 微调方法对比

| 方法 | 参数比例 | GPU 需求 | 训练时间 | 效果 | 适用场景 |
|------|---------|---------|---------|------|---------|
| **Full Fine-tuning** | 100% | 极高（多卡）| 长 | 最强 | 大规模领域迁移 |
| **LoRA** | 0.1–1% | 中等 | 中等 | 良好 | 通用微调首选 ⭐ |
| **QLoRA** | 0.1–1% | 低（4bit量化）| 中等 | 良好 | 显存受限时 ⭐ |
| **DoRA** | 0.1–1% | 中等 | 中等 | 略好于 LoRA | LoRA 升级版 |
| **Adapter** | <1% | 低 | 快 | 一般 | 轻量插件式 |
| **Prompt Tuning** | 极少 | 极低 | 快 | 有限 | 资源极度受限 |

## 3. 推荐微调候选模型

### 3.1 轻量级（本地单卡可训练）

| 模型 | 参数量 | 许可证 | QLoRA 显存需求 | 推荐场景 |
|------|--------|--------|--------------|---------|
| **Qwen2.5-7B-Instruct** | 7B | Apache 2.0 | ~8GB | 中文垂直领域首选 ⭐ |
| **LLaMA 3.1 8B Instruct** | 8B | Meta | ~8GB | 英文垂直领域 |
| **Mistral 7B Instruct** | 7B | Apache 2.0 | ~6GB | 高性价比微调 |
| **Phi-3.5 mini** | 3.8B | MIT | ~4GB | 极轻量，资源受限 |
| **GLM-4-9B** | 9B | 授权 | ~10GB | 中文对话定制 |
| **DeepSeek-R1-Distill-Qwen-7B** | 7B | MIT | ~8GB | 推理增强 |

### 3.2 中等规模（多卡服务器训练）

| 模型 | 参数量 | 许可证 | LoRA 显存需求 | 推荐场景 |
|------|--------|--------|-------------|---------|
| **Qwen2.5-14B-Instruct** | 14B | Apache 2.0 | ~20GB | 高质量中文微调 ⭐ |
| **Qwen2.5-32B-Instruct** | 32B | Apache 2.0 | ~48GB | 企业旗舰中文 |
| **LLaMA 3.1 70B Instruct** | 70B | Meta | ~80GB×2 | 英文高质量 |
| **Mistral Small 3 24B** | 24B | Apache 2.0 | ~32GB | 欧美企业场景 |
| **Phi-4** | 14B | MIT | ~20GB | 高效小模型 |
| **InternLM2.5-7B** | 7B | Apache 2.0 | ~8GB | 长上下文微调 |

### 3.3 代码专项微调

| 基础模型 | 适合训练数据 | 目标效果 |
|---------|------------|---------|
| **Qwen2.5-Coder-7B** | 公司内部代码规范、API 文档 | 公司代码风格生成 |
| **DeepSeek-Coder-V2-Lite** | 领域代码库 | 特定语言/框架专家 |
| **Code LLaMA 7B/13B** | 多语言代码 + 注释 | 通用代码助手 |
| **Mistral 7B** | 代码 + 解释对 | 代码解释/审查 |

## 4. 微调数据要求

### 4.1 数据量参考

| 任务类型 | 最小数据量 | 推荐数据量 | 数据格式 |
|---------|-----------|-----------|---------|
| 风格定制（语气/格式）| 500–1K 条 | 5K 条 | instruction-output 对 |
| 领域知识注入 | 2K–5K 条 | 20K+ 条 | QA 对 |
| 功能扩展（新能力）| 5K–10K 条 | 50K+ 条 | 多轮对话 |
| 全面领域迁移 | 50K+ 条 | 200K+ 条 | 混合格式 |

### 4.2 数据格式（Chat Template）

```json
// ShareGPT 格式（最常用）
{
  "conversations": [
    {"role": "system", "content": "你是一个专业的 Java 开发助手"},
    {"role": "user", "content": "如何实现线程池？"},
    {"role": "assistant", "content": "以下是 Java 线程池的实现方式..."}
  ]
}

// Alpaca 格式
{
  "instruction": "解释 JVM 垃圾回收机制",
  "input": "",
  "output": "JVM 垃圾回收是..."
}
```

### 4.3 数据质量原则

- **质量 > 数量**：1000 条高质量数据 > 10000 条低质量数据
- **多样性**：覆盖目标场景的各种情况
- **一致性**：统一格式、语气、风格
- **正确性**：错误数据会直接影响模型行为

## 5. 主流微调工具链

### 5.1 工具对比

| 工具 | 特点 | 支持模型 | 上手难度 | 推荐场景 |
|------|------|---------|---------|---------|
| **LLaMA-Factory** | 最易用，Web UI | 100+ 模型 | ★★☆☆☆ | 快速上手 ⭐ |
| **Axolotl** | 高度可配置 | 主流模型 | ★★★☆☆ | 进阶定制 |
| **Unsloth** | 最快，最省显存 | LLaMA/Mistral/Qwen | ★★★☆☆ | 速度优先 ⭐ |
| **TRL (HuggingFace)** | 官方库，支持 DPO/PPO | 所有 HF 模型 | ★★★★☆ | 学术研究 |
| **torchtune** | PyTorch 原生 | Meta 系列 | ★★★★☆ | LLaMA 系列 |
| **Xtuner** | 国产，支持 InternLM | 国产系列 | ★★★☆☆ | 国产模型 |

### 5.2 LLaMA-Factory 快速上手

```bash
# 安装
pip install llamafactory

# 启动 Web UI
llamafactory-cli webui

# 命令行微调
llamafactory-cli train \
  --model_name_or_path Qwen/Qwen2.5-7B-Instruct \
  --stage sft \
  --do_train \
  --dataset alpaca_zh \
  --template qwen \
  --finetuning_type lora \
  --lora_rank 8 \
  --output_dir ./output \
  --num_train_epochs 3.0 \
  --per_device_train_batch_size 4 \
  --gradient_accumulation_steps 4
```

### 5.3 Unsloth 快速微调（显存最省）

```python
from unsloth import FastLanguageModel

# 加载模型（自动应用 2x 加速 + 60% 显存节省）
model, tokenizer = FastLanguageModel.from_pretrained(
    model_name="Qwen/Qwen2.5-7B-Instruct",
    max_seq_length=4096,
    load_in_4bit=True,  # QLoRA
)

# 应用 LoRA
model = FastLanguageModel.get_peft_model(
    model,
    r=16,
    target_modules=["q_proj", "k_proj", "v_proj", "o_proj"],
    lora_alpha=16,
    lora_dropout=0,
)
```

## 6. 微调流程

```
数据收集与清洗
      │
      ▼
选择基础模型（参见上文推荐）
      │
      ▼
选择微调方法（优先 QLoRA）
      │
      ▼
配置超参数（lr, batch, epochs, rank）
      │
      ▼
训练（监控 loss 曲线）
      │
      ▼
评估（人工评估 + 自动 benchmark）
      │
      ├── 不满意 → 调整数据/超参数，重新训练
      │
      └── 满意
              │
              ▼
         合并权重（LoRA merge）/ 量化（GGUF）
              │
              ▼
         部署（Ollama / vLLM / TGI）
              │
              ▼
         持续监控与迭代
```

## 7. 成本估算

### 7.1 云端 GPU 训练成本（参考）

| 模型 | 方法 | 数据量 | GPU | 训练时间 | 成本（A100 ~$3/h）|
|------|------|--------|-----|---------|-----------------|
| 7B QLoRA | LoRA r=16 | 10K 条 | 1×A100 | ~2h | ~$6 |
| 7B Full SFT | Full | 50K 条 | 4×A100 | ~8h | ~$96 |
| 70B QLoRA | LoRA r=16 | 10K 条 | 2×A100 | ~6h | ~$36 |
| 70B Full SFT | Full | 50K 条 | 16×A100 | ~24h | ~$1,152 |

### 7.2 ROI 分析

微调应当在以下情况下考虑：
1. Prompt 工程已无法提升质量
2. API 成本过高（大量请求），替换成本 < 微调成本
3. 需要特定领域知识或风格，且 RAG 不足够
4. 需要离线/私有部署

---

> 📌 相关文档：[09-model-comparison.md](./09-model-comparison.md) | [10-model-selection-guide.md](./10-model-selection-guide.md) | [12-model-trends.md](./12-model-trends.md)
