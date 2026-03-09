# 06 - 阿里通义千问（Qwen）系列深度调研

## 1. 系列概览

通义千问（Qwen）是阿里云推出的大语言模型系列，由阿里 DAMO Academy 研发。自 2023 年发布以来快速迭代，Qwen2.5 系列在多项国际基准上超越同等规模开源模型，是目前综合能力最强的中英双语开源模型之一。

## 2. 主要模型版本演进

| 系列 | 发布时间 | 核心特点 |
|------|---------|---------|
| Qwen1.0 | 2023-09 | 首代，中英双语，7B/14B |
| Qwen1.5 | 2024-02 | 增强多语言，0.5B–110B |
| Qwen2 | 2024-06 | 架构升级，GQA，128K 上下文 |
| Qwen2.5 | 2024-09 | 全系列升级，全球开源最强之一 ⭐ |
| Qwen2.5-Max | 2025-01 | 商业版旗舰，超越 GPT-4o |

## 3. Qwen2.5 系列详细规格

### 3.1 通用对话模型

| 模型 | 参数量 | 上下文 | 许可证 | MMLU | C-Eval | 代码 | 数学 |
|------|--------|--------|--------|------|--------|------|------|
| Qwen2.5-0.5B | 0.5B | 32K | Apache 2.0 | 47.0 | — | — | — |
| Qwen2.5-1.5B | 1.5B | 32K | Apache 2.0 | 60.9 | — | — | — |
| Qwen2.5-3B | 3B | 32K | Apache 2.0 | 65.6 | — | — | — |
| Qwen2.5-7B | 7B | 128K | Apache 2.0 | 74.2 | 80.2 | 良好 | 优秀 |
| Qwen2.5-14B | 14B | 128K | Apache 2.0 | 79.7 | 84.3 | 良好 | 优秀 |
| Qwen2.5-32B | 32B | 128K | Apache 2.0 | 83.5 | 87.7 | 优秀 | 优秀 |
| Qwen2.5-72B | 72B | 128K | Apache 2.0 | **86.1** | **90.1** | 优秀 | 卓越 |

### 3.2 代码专项模型（Qwen2.5-Coder）

| 模型 | 参数量 | HumanEval | EvalPlus | MBPP | 特点 |
|------|--------|-----------|----------|------|------|
| Qwen2.5-Coder-1.5B | 1.5B | 69.5% | — | — | 轻量代码助手 |
| Qwen2.5-Coder-7B | 7B | 88.4% | — | — | 高性价比 |
| Qwen2.5-Coder-14B | 14B | 89.0% | — | — | 商用代码主力 |
| Qwen2.5-Coder-32B | 32B | **92.7%** | — | — | 顶级开源代码模型 ⭐ |

> Qwen2.5-Coder-32B 在 HumanEval 上超越 GPT-4o，是目前最强开源代码模型之一。

### 3.3 数学专项模型（Qwen2.5-Math）

| 模型 | MATH-500 | AMC23 | AIME24 | 特点 |
|------|---------|-------|--------|------|
| Qwen2.5-Math-1.5B | 85.4% | — | — | 超小数学模型 |
| Qwen2.5-Math-7B | 91.6% | — | — | 数学竞赛级 |
| Qwen2.5-Math-72B | **95.9%** | — | — | 接近 o1 推理水平 |

### 3.4 多模态模型（Qwen-VL 系列）

| 模型 | 参数量 | 支持输入 | 特点 |
|------|--------|---------|------|
| Qwen2-VL-2B | 2B | 图像/视频 | 极轻量视觉 |
| Qwen2-VL-7B | 7B | 图像/视频 | 高性价比视觉 |
| Qwen2-VL-72B | 72B | 图像/视频 | 顶级开源视觉 ⭐ |

Qwen2-VL-72B 在多个视觉基准上媲美 GPT-4o Vision，支持动态分辨率和视频理解。

## 4. 商业版 API（阿里云）

### 4.1 通义千问 API 定价

| 模型 | 输入(¥/1M tok) | 输出(¥/1M tok) | 上下文 | 定位 |
|------|--------------|--------------|--------|------|
| qwen-max | ¥0.04 | ¥0.12 | 32K | 最强商业版 |
| qwen-plus | ¥0.008 | ¥0.02 | 131K | 均衡 |
| qwen-turbo | ¥0.003 | ¥0.006 | 1M | 速度优先 |
| qwen-long | ¥0.0005 | ¥0.002 | 10M | 超长文档 ⭐ |
| qwen2.5-max | ¥0.02 | ¥0.06 | 128K | 新旗舰 |

> `qwen-long` 支持 1000 万 token 上下文，价格极低，适合超长文档分析。

### 4.2 基准测试（qwen2.5-max vs 竞品）

| 基准 | Qwen2.5-Max | GPT-4o | Claude 3.5 Sonnet | DeepSeek-V3 |
|------|------------|--------|-------------------|-------------|
| MMLU | 85.9% | 88.7% | 88.7% | 88.5% |
| LiveCodeBench | 43.4% | 34.2% | 36.3% | **40.6%** |
| MATH-500 | **92.1%** | 76.6% | 71.1% | 90.2% |
| C-Eval | **95.9%** | 87.5% | 86.4% | 90.1% |

## 5. 中文能力优势

Qwen 系列在中文场景具有系统性优势：

| 能力维度 | Qwen2.5-72B | GPT-4o | Claude 3.5 Sonnet | DeepSeek-V3 |
|---------|------------|--------|-------------------|-------------|
| C-Eval | **90.1%** | 87.5% | 86.4% | 90.1% |
| CMMLU | **89.5%** | 87.2% | 85.1% | 88.8% |
| 中文写作 | ★★★★★ | ★★★★☆ | ★★★☆☆ | ★★★★★ |
| 中文指令遵循 | ★★★★★ | ★★★★☆ | ★★★☆☆ | ★★★★★ |
| 行业知识（中文）| ★★★★★ | ★★★★☆ | ★★★☆☆ | ★★★★☆ |

## 6. 部署方案

### 6.1 阿里云 API（推荐生产）

```java
// Spring AI + 通义千问（OpenAI 兼容接口）
// application.yml
// spring.ai.openai.base-url=https://dashscope.aliyuncs.com/compatible-mode
// spring.ai.openai.api-key=your-dashscope-api-key
// spring.ai.openai.chat.options.model=qwen-max

@Autowired
private ChatClient chatClient;

public String chat(String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
}
```

### 6.2 本地部署（Ollama）

```bash
# 下载并运行 Qwen2.5
ollama pull qwen2.5:7b
ollama pull qwen2.5:14b
ollama pull qwen2.5:72b  # 需要 48GB+ 显存

# 运行
ollama run qwen2.5:7b
```

### 6.3 Hugging Face 部署

```python
from transformers import AutoModelForCausalLM, AutoTokenizer

model_name = "Qwen/Qwen2.5-7B-Instruct"
model = AutoModelForCausalLM.from_pretrained(model_name, device_map="auto")
tokenizer = AutoTokenizer.from_pretrained(model_name)
```

## 7. 适用场景矩阵

| 场景 | 推荐模型 | 原因 |
|------|---------|------|
| 中文企业对话 | qwen-max / Qwen2.5-72B | 最强中文能力 |
| 代码生成（开源）| Qwen2.5-Coder-32B | 超越 GPT-4o 代码 |
| 数学/理科推理 | Qwen2.5-Math-72B | 接近 o1 水平 |
| 超长文档处理 | qwen-long（API）| 1000 万 token |
| 视觉分析 | Qwen2-VL-72B | 媲美 GPT-4o Vision |
| 本地轻量部署 | Qwen2.5-3B/7B | Apache 2.0，免费 |
| 边缘/移动设备 | Qwen2.5-0.5B/1.5B | 极低参数量 |

## 8. 生态与工具支持

- **Ollama**：原生支持所有 Qwen2.5 版本
- **vLLM**：高吞吐推理，支持 GQA 优化
- **llama.cpp**：CPU/GPU 量化推理
- **LLaMA-Factory**：一键微调 Qwen
- **Hugging Face**：所有版本公开下载（Apache 2.0）
- **阿里云 DashScope**：官方商业 API，支持 OpenAI 兼容接口

---

> 📌 相关文档：[07-deepseek.md](./07-deepseek.md) | [08-other-major-models.md](./08-other-major-models.md) | [09-model-comparison.md](./09-model-comparison.md)
