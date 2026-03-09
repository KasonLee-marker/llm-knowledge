# 本地 LLM 部署

## 1. 简介

本地部署 LLM 可实现数据完全自主可控，适合隐私敏感场景。

## 2. 主流方案

| 方案 | 定位 | 性能 | 易用性 |
|------|------|------|--------|
| **Ollama** | 本地运行，快速开始 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **vLLM** | 高吞吐推理服务 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| **LM Studio** | 桌面 GUI 工具 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **llama.cpp** | 边缘设备优化 | ⭐⭐⭐ | ⭐⭐⭐ |
| **TensorRT-LLM** | NVIDIA GPU 优化 | ⭐⭐⭐⭐⭐ | ⭐⭐ |

## 3. Ollama（推荐入门）

### 安装
```bash
# macOS/Linux
curl -fsSL https://ollama.com/install.sh | sh

# Windows: 官网下载安装包
```

### 使用
```bash
# 拉取模型
ollama pull llama3.2

# 运行
ollama run llama3.2

# API 服务（自动启动在 11434 端口）
curl http://localhost:11434/api/generate -d '{
    "model": "llama3.2",
    "prompt": "你好"
}'
```

### Java 集成
```java
// 与 OpenAI API 兼容
@RestController
public class LocalLLMController {
    
    @Value("${ollama.base-url:http://localhost:11434}")
    private String baseUrl;
    
    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        // 使用 OpenAI 客户端，指向 Ollama
        // Ollama 提供 OpenAI 兼容 API
        return restTemplate.postForObject(
            baseUrl + "/v1/chat/completions",
            buildRequest(message),
            String.class
        );
    }
}
```

## 4. vLLM（生产推荐）

### 特点
- 高吞吐：PagedAttention 优化
- 兼容 OpenAI API
- 支持多卡、张量并行

### 部署
```bash
# 安装
pip install vllm

# 启动服务
python -m vllm.entrypoints.openai.api_server \
    --model Qwen/Qwen2.5-72B-Instruct \
    --tensor-parallel-size 4 \
    --max-model-len 32768
```

### Java 调用
```java
// 与 OpenAI API 完全兼容
OpenAiApi api = new OpenAiApi("http://localhost:8000/v1", "dummy-key");
// 后续与 OpenAI 相同
```

## 5. 硬件要求

| 模型规模 | 显存需求 | 推荐 GPU |
|---------|---------|---------|
| 7B | 8-16GB | RTX 3090/4090 |
| 13B | 16-24GB | RTX 4090/A5000 |
| 70B | 80-160GB | A100/H100 |
| 400B MoE | 多卡 | 多卡 H100 |

### 量化优化
```bash
# Ollama 自动量化
ollama pull llama3.2:4b  # 4-bit 量化

# vLLM 量化
--quantization awq  # 或 gptq
```

## 6. 方案选择

| 场景 | 推荐方案 |
|------|---------|
| 个人开发测试 | Ollama |
| 生产环境高吞吐 | vLLM |
| 非技术人员使用 | LM Studio |
| 边缘设备部署 | llama.cpp |
| NVIDIA 集群 | TensorRT-LLM |

## 7. 与云端 API 对比

| 维度 | 本地部署 | 云端 API |
|------|---------|---------|
| **数据隐私** | ✅ 完全可控 | ❌ 上传云端 |
| **成本** | 硬件一次性 | 按量付费 |
| **延迟** | 低（本地）| 网络依赖 |
| **维护** | 需自行维护 | 托管 |
| **模型选择** | 有限 | 丰富 |
| **弹性扩展** | 困难 | 容易 |

## 8. 最佳实践

1. **开发阶段**：Ollama 本地测试
2. **生产阶段**：vLLM + 多卡部署
3. **混合架构**：敏感数据本地，通用任务云端
4. **监控**：显存、温度、推理延迟
