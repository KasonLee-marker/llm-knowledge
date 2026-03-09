# 05 - LLM APIs 与服务提供商

> 本章介绍主流 LLM 服务商的 API 接入方式，以及 Java 后端的最佳实践。

---

## 目录

| # | 文档 | 简介 |
|---|------|------|
| 1 | [OpenAI API](./05-llm-apis-providers/01-openai-api.md) | ChatCompletion、Embedding、Fine-tuning API |
| 2 | [Azure OpenAI](./05-llm-apis-providers/02-azure-openai.md) | 企业级 Azure 部署与鉴权 |
| 3 | [Anthropic Claude API](./05-llm-apis-providers/03-anthropic-claude.md) | Messages API 与 Claude 特性 |
| 4 | [Google Gemini API](./05-llm-apis-providers/04-google-gemini.md) | Vertex AI 与 Gemini API |
| 5 | [本地 LLM 部署](./05-llm-apis-providers/05-local-llms.md) | Ollama、vLLM、LM Studio |
| 6 | [API 集成模式](./05-llm-apis-providers/06-api-integration-patterns.md) | 统一客户端抽象、熔断、限流、缓存 |

---

## 服务商对比速查

| 服务商 | 特点 | 推荐场景 |
|--------|------|---------|
| **OpenAI** | 生态最全，模型领先 | 通用场景，快速开始 |
| **Azure OpenAI** | 企业合规，数据驻留 | 企业级应用 |
| **Anthropic** | 长上下文，安全性 | 长文档处理 |
| **Google** | 超长上下文，多模态 | 视频/长文档分析 |
| **DeepSeek** | 开源，中文强，便宜 | 国内部署，成本敏感 |
| **阿里云** | 中文优化，合规 | 国内业务 |
| **字节火山** | 超低价格 | 大规模调用 |

---

## Java 集成方案

| 方案 | 适用场景 | 复杂度 |
|------|---------|--------|
| **Spring AI** | Spring Boot 项目 | 低 |
| **LangChain4j** | 非 Spring 项目 | 中 |
| **HttpClient** | 简单场景 | 低 |
| **OpenAI SDK** | 标准 OpenAI | 低 |
| **Azure SDK** | Azure 服务 | 中 |

---

## 关键决策点

### 选择云服务商
1. **数据合规** → Azure / 国内云
2. **成本控制** → DeepSeek / Doubao
3. **中文能力** → Qwen / DeepSeek
4. **长上下文** → Claude / Gemini
5. **多模态** → GPT-4o / Gemini

### 选择部署方式
1. **快速开始** → 云 API
2. **数据敏感** → 本地部署
3. **混合架构** → 敏感数据本地，通用任务云端

---

## API 兼容性说明

> OpenAI API 格式已成为事实标准，大多数服务商提供兼容接口。

```java
// 统一配置
spring:
  ai:
    openai:
      base-url: https://api.openai.com/v1  # 或兼容端点
      api-key: ${API_KEY}
```

**兼容 OpenAI 格式的服务商：**
- DeepSeek
- 阿里云（百炼）
- 字节火山
- 智谱 AI
- Ollama
- vLLM
