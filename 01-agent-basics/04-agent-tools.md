# Agent 工具（Tools / Function Calling）

## 什么是 Agent 工具

工具（Tool）是 Agent 能够调用的**外部能力**，扩展了 LLM 的能力边界。LLM 本身只能生成文本，通过工具调用，Agent 可以：
- 获取实时信息（天气、股价、新闻）
- 执行计算和代码
- 操作文件和数据库
- 调用第三方 API 和服务
- 与用户界面交互

## Function Calling 机制

现代 LLM（OpenAI GPT-4、Claude 等）原生支持 Function Calling，工作流程：

```
1. 开发者注册工具（提供工具名称、描述、参数 Schema）
   ↓
2. 用户发送请求
   ↓
3. LLM 决定是否需要调用工具，并生成结构化的调用参数
   ↓
4. 应用程序执行实际的工具逻辑
   ↓
5. 将执行结果返回给 LLM
   ↓
6. LLM 生成最终回答
```

## 工具定义（JSON Schema）

每个工具需要提供清晰的描述和参数规范：

```json
{
  "name": "search_orders",
  "description": "根据用户ID或订单号查询订单信息，包括订单状态、商品详情和支付信息",
  "parameters": {
    "type": "object",
    "properties": {
      "user_id": {
        "type": "string",
        "description": "用户的唯一标识符"
      },
      "order_id": {
        "type": "string",
        "description": "订单号（可选，如果提供则精确匹配）"
      },
      "status": {
        "type": "string",
        "enum": ["pending", "shipped", "delivered", "cancelled"],
        "description": "按订单状态过滤（可选）"
      }
    },
    "required": ["user_id"]
  }
}
```

## 常见工具类型

| 类型 | 示例 | 用途 |
|------|------|------|
| **信息检索** | 搜索引擎、RAG 检索 | 获取实时或知识库信息 |
| **数据库操作** | SQL 查询、NoSQL 读写 | 业务数据查询和更新 |
| **代码执行** | Python 沙箱、计算器 | 执行计算和数据分析 |
| **外部 API** | 天气、地图、支付 API | 调用第三方服务 |
| **文件操作** | 读写文件、生成文档 | 处理文件系统 |
| **通信工具** | 发送邮件、发送通知 | 触发通知和通信 |

## 工具设计最佳实践

### 1. 工具描述要清晰准确
- 描述应明确说明工具的**用途**、**输入**、**输出**和**适用场景**
- 避免歧义，防止 LLM 误用工具

### 2. 参数设计要合理
- 必填参数尽量少，提供合理默认值
- 使用枚举（enum）限制有效值范围
- 参数名称和描述用自然语言，便于模型理解

### 3. 错误处理
- 工具应返回明确的错误信息，而不是抛出异常
- 错误信息应对 LLM 友好（说明原因，建议重试方式）

### 4. 安全控制
- 对敏感操作（写入、删除、支付）进行权限验证
- 实现速率限制，防止 Agent 过度调用
- 记录所有工具调用日志，用于审计

## Java 工具接口设计

```java
/**
 * Agent 工具的统一接口
 */
public interface AgentTool {
    /** 工具名称（唯一标识） */
    String getName();

    /** 工具描述（供 LLM 理解工具用途） */
    String getDescription();

    /** 参数 Schema（JSON Schema 格式） */
    String getParameterSchema();

    /**
     * 执行工具
     * @param parameters JSON 格式的参数
     * @return 执行结果（JSON 格式）
     */
    String execute(String parameters);
}

/**
 * 订单查询工具示例
 */
public class OrderSearchTool implements AgentTool {
    private final OrderRepository orderRepository;

    @Override
    public String getName() {
        return "search_orders";
    }

    @Override
    public String getDescription() {
        return "根据用户ID查询订单列表，支持按状态过滤";
    }

    @Override
    public String execute(String parameters) {
        // 解析参数
        JsonObject params = JsonParser.parseString(parameters).getAsJsonObject();
        String userId = params.get("user_id").getAsString();

        // 执行查询
        List<Order> orders = orderRepository.findByUserId(userId);

        // 返回 JSON 结果
        return new Gson().toJson(orders);
    }
}
```

---
