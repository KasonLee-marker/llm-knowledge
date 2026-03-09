# Semantic Kernel

## 1. 简介

Semantic Kernel（SK）是微软开源的 AI 应用开发 SDK，支持 .NET、Python、Java。

## 2. 核心概念

| 概念 | 说明 |
|------|------|
| **Kernel** | 核心协调器，管理所有组件 |
| **Plugins** | 功能插件，包含 Functions |
| **Functions** | 可调用单元（Prompt/Native）|
| **Memory** | 语义记忆，支持 RAG |
| **Planners** | 任务规划器 |

## 3. Java 快速开始

```java
// 创建 Kernel
Kernel kernel = Kernel.builder()
    .withAIService(OpenAIChatCompletion.builder()
        .withApiKey("your-key")
        .withModelId("gpt-4")
        .build())
    .build();

// 简单调用
ChatHistory history = new ChatHistory();
history.addUserMessage("你好");
ChatMessageContent<?> result = kernel.invokeAsync(chatFunction)
    .withArguments(new KernelArguments(history))
    .block();
```

## 4. Plugin 开发

```java
// 定义 Plugin
public class WeatherPlugin {
    @DefineKernelFunction(name = "getWeather", description = "获取天气")
    public String getWeather(
        @KernelFunctionParameter(name = "city", description = "城市") String city) {
        return "晴天";
    }
}

// 注册使用
kernel.importPluginFromObject(new WeatherPlugin(), "Weather");
```

## 5. 与 LangChain 对比

| 维度 | Semantic Kernel | LangChain |
|------|-----------------|-----------|
| 厂商背景 | 微软官方 | 社区主导 |
| 企业支持 | 强（Azure 集成）| 中等 |
| 跨语言 | .NET/Python/Java | Python/JS |
| 学习曲线 | 中等 | 较陡 |
| 生态成熟度 | 快速增长 | 非常成熟 |

## 6. 适用场景

- ✅ Azure/OpenAI 重度用户
- ✅ .NET 技术栈团队
- ✅ 企业级应用（微软服务集成）
- ✅ 需要 Planner 自动规划的场景
