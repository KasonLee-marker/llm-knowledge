# LangChain 概述

## 1. 什么是 LangChain

LangChain 是一个用于开发 LLM 应用程序的 Python/JS 框架，提供模块化组件来构建复杂的 AI 应用。

## 2. 核心概念

### 2.1 核心组件

| 组件 | 说明 | 用途 |
|------|------|------|
| **Models** | LLM 接口封装 | 统一调用不同模型 |
| **Prompts** | 提示词管理 | 模板化、变量替换 |
| **Chains** | 链式调用 | 串联多个操作 |
| **Agents** | 智能代理 | 自主决策、工具调用 |
| **Memory** | 记忆管理 | 上下文持久化 |
| **Retrievers** | 检索器 | RAG 文档检索 |

### 2.2 架构图

```
User Input → Prompt Template → LLM → Output Parser → Result
                ↓
           Memory (可选)
                ↓
           Tools (Agent 模式)
```

## 3. 快速示例

```python
from langchain import OpenAI, PromptTemplate, LLMChain

# 定义模板
template = """将以下中文翻译成英文：
{text}"""
prompt = PromptTemplate(input_variables=["text"], template=template)

# 创建链
llm = OpenAI(temperature=0)
chain = LLMChain(llm=llm, prompt=prompt)

# 执行
result = chain.predict(text="你好，世界")
```

## 4. 优缺点

| 优点 | 缺点 |
|------|------|
| 生态丰富，组件齐全 | 学习曲线较陡 |
| 社区活跃，文档完善 | 版本迭代快，API 变化大 |
| 支持多种模型和工具 | 对生产环境支持有限 |
| 快速原型开发 | 性能开销较大 |

## 5. 适用场景

- ✅ 快速原型开发
- ✅ 复杂多步骤任务
- ✅ RAG 应用
- ✅ Agent 原型
- ❌ 高性能生产环境（考虑 LangGraph/LangServe）
