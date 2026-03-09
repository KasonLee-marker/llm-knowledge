# OpenAI API 使用指南（概要）

- 接入点：REST API / 官方 SDK（多语言支持）。
- 常用功能：聊天接口、completion、function-calling、embeddings。
- 实践要点：密钥管理、速率限制、请求分层（路由到不同模型）。

示例：在 Java 中使用 OkHttp/HttpClient 对 OpenAI 进行封装，统一 error handling 与重试策略。

---