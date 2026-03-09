# 本地部署 LLM（Local LLMs）

常见方案：Ollama、Llama.cpp、LMStudio、VLLM 等。

优点：数据可控、无持续 API 成本、可调参数更细粒度。

挑战：运维成本、硬件投入（GPU/TPU）、线上扩缩容复杂度。

建议：对敏感数据使用本地模型；对于高并发推理，可采用混合架构（本地 + 云模型）。

---