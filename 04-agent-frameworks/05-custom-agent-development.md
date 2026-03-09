# 自定义 Agent 开发

## 1. 为什么不使用框架？

| 场景 | 说明 |
|------|------|
| **性能敏感** | 框架 overhead  unacceptable |
| **深度定制** | 框架无法满足特殊需求 |
| **学习成本** | 团队不想学习新框架 |
| **已有架构** | 需要与现有系统深度集成 |

## 2. 最小 Agent 实现

```java
// 核心组件定义
public interface Agent {
    String execute(String userInput, Context context);
}

public interface Tool {
    String getName();
    String getDescription();
    String execute(Map<String, Object> params);
}

public class SimpleAgent implements Agent {
    private final ChatModel model;
    private final List<Tool> tools;
    private final PromptTemplate systemPrompt;
    
    @Override
    public String execute(String userInput, Context context) {
        // 1. 构建 Prompt
        String prompt = buildPrompt(userInput, context);
        
        // 2. 调用 LLM
        String response = model.generate(prompt);
        
        // 3. 解析响应（思考/行动/观察循环）
        Action action = parseAction(response);
        
        // 4. 执行工具
        if (action.isToolCall()) {
            String observation = executeTool(action);
            // 递归或循环继续
            return execute(userInput + "\n观察: " + observation, context);
        }
        
        // 5. 返回最终结果
        return response;
    }
}
```

## 3. ReAct 模式实现

```java
public class ReActAgent implements Agent {
    private static final String REACT_TEMPLATE = """
        你是一个智能助手，可以使用以下工具：
        {tools}
        
        请按以下格式思考：
        思考: 你需要做什么
        行动: 工具名称[参数]
        观察: 工具返回结果
        ...（可以重复多次）
        最终答案: 给用户回复
        
        用户问题: {question}
        """;
    
    @Override
    public String execute(String question) {
        StringBuilder history = new StringBuilder();
        
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // 构建当前 Prompt
            String prompt = REACT_TEMPLATE
                .replace("{tools}", formatTools())
                .replace("{question}", question)
                + history;
            
            // 调用 LLM
            String response = model.generate(prompt);
            
            // 解析
            if (response.contains("最终答案:")) {
                return extractFinalAnswer(response);
            }
            
            // 提取行动
            String action = extractAction(response);
            String observation = executeTool(action);
            
            // 记录历史
            history.append("思考: ").append(extractThought(response)).append("\n");
            history.append("行动: ").append(action).append("\n");
            history.append("观察: ").append(observation).append("\n");
        }
        
        return "无法在规定时间内完成";
    }
}
```

## 4. Function Calling 模式

```java
public class FunctionCallingAgent implements Agent {
    
    @Override
    public String execute(String userInput) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("你是一个智能助手"));
        messages.add(new UserMessage(userInput));
        
        while (true) {
            // 调用模型，传入工具定义
            ChatResponse response = model.generate(messages, tools);
            
            // 检查是否有工具调用
            if (response.hasToolCalls()) {
                // 执行工具
                for (ToolCall call : response.getToolCalls()) {
                    String result = executeTool(call);
                    messages.add(new ToolMessage(call.getId(), result));
                }
            } else {
                // 返回最终结果
                return response.getContent();
            }
        }
    }
}
```

## 5. 关键设计决策

| 决策点 | 选项 | 建议 |
|--------|------|------|
| **Prompt 管理** | 硬编码 / 模板引擎 / 数据库 | 模板引擎（Freemarker/Velocity）|
| **记忆存储** | 内存 / Redis / 数据库 | Redis（短期）+ 数据库（长期）|
| **工具注册** | 注解 / 配置文件 / 代码 | 注解扫描 + 配置覆盖 |
| **错误处理** | 重试 / 降级 / 中断 | 指数退避重试 + 降级 |
| **并发控制** | 同步 / 异步 / 流式 | 异步 + 流式（SSE）|

## 6. 生产环境 checklist

- [ ] 超时控制（模型调用、工具执行）
- [ ] 熔断降级（模型不可用时的 fallback）
- [ ] 限流防刷（Token 消耗控制）
- [ ] 日志追踪（全链路追踪 ID）
- [ ] 成本监控（按用户/按功能统计）
- [ ] 安全过滤（输入输出内容审核）
