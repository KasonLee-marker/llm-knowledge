# 02 - Agent 通信机制

在多智能体系统中，Agent 之间的通信是实现协作的基础。有效的通信机制能够确保信息准确传递、任务协调顺畅、系统整体高效运作。

## 通信方式概览

```mermaid
graph TB
    subgraph "Agent 通信机制"
        direction TB
        
        CC[对话式通信<br/>Conversational]
        SM[共享内存<br/>Shared Memory]
        MQ[消息队列<br/>Message Queue]
        
        CC --> |自然语言交互| C1[点对点对话]
        CC --> |自然语言交互| C2[群组讨论]
        CC --> |自然语言交互| C3[广播通知]
        
        SM --> |数据共享| S1[键值存储]
        SM --> |数据共享| S2[黑板系统]
        SM --> |数据共享| S3[状态同步]
        
        MQ --> |异步消息| M1[发布/订阅]
        MQ --> |异步消息| M2[点对点队列]
        MQ --> |异步消息| M3[事件驱动]
    end
    
    style CC fill:#e3f2fd
    style SM fill:#fff3e0
    style MQ fill:#e8f5e9
```

## 对话式通信

对话式通信是最自然的 Agent 交互方式，Agent 使用自然语言进行信息交换和协商。

### 架构模式

```mermaid
graph LR
    subgraph "对话式通信"
        direction TB
        
        A1[Agent A] <-->|自然语言消息| A2[Agent B]
        A2 <-->|自然语言消息| A3[Agent C]
        A1 <-->|自然语言消息| A3
        
        subgraph "消息格式"
            M1["from: Agent A<br/>to: Agent B<br/>content: 请帮我分析<br/>timestamp: 1234567890"]
        end
    end
    
    style A1 fill:#e3f2fd
    style A2 fill:#e3f2fd
    style A3 fill:#e3f2fd
```

### 通信模式

#### 1. 点对点对话（Point-to-Point）

```mermaid
sequenceDiagram
    participant A as Agent A<br/>开发者
    participant B as Agent B<br/>测试员
    
    A->>B: 我完成了用户登录功能，请帮我测试
    Note over A,B: 包含：功能描述、测试要点、预期结果
    
    B->>B: 分析需求，设计测试用例
    B->>A: 收到，我需要以下信息：<br/>1. 测试环境配置<br/>2. 测试账号<br/>3. 边界条件
    
    A->>B: 提供测试所需信息
    B->>B: 执行测试
    B->>A: 测试结果：发现2个问题<br/>1. 密码长度未验证<br/>2. 登录失败提示不明确
    
    A->>A: 修复问题
    A->>B: 问题已修复，请重新测试
    B->>B: 回归测试
    B->>A: 测试通过，功能正常
```

#### 2. 群组讨论（Group Discussion）

```mermaid
sequenceDiagram
    participant PM as 项目经理
    participant Arch as 架构师
    participant Dev as 开发者
    participant QA as 测试员
    
    PM->>Arch: 新项目需求：构建电商系统
    PM->>Dev: 新项目需求：构建电商系统
    PM->>QA: 新项目需求：构建电商系统
    
    Arch->>PM: 建议采用微服务架构
    Arch->>Dev: 需要拆分订单、库存、支付服务
    
    Dev->>Arch: 微服务会增加复杂度，建议先单体
    Dev->>PM: 评估后建议 MVP 阶段用单体
    
    QA->>Arch: 微服务测试覆盖如何保障？
    QA->>Dev: 单体架构测试策略是什么？
    
    Arch->>PM: 同意 MVP 用单体，后续演进
    PM->>All: 决策：MVP 单体，预留微服务扩展点
    
    Note over PM,QA: 通过群组讨论达成共识
```

#### 3. 广播通知（Broadcast）

```mermaid
graph TB
    subgraph "广播通信"
        Sender[发送者Agent] -->|广播消息| R1[接收者Agent 1]
        Sender -->|广播消息| R2[接收者Agent 2]
        Sender -->|广播消息| R3[接收者Agent 3]
        Sender -->|广播消息| R4[接收者Agent 4]
        Sender -->|广播消息| R5[接收者Agent N...]
    end
    
    style Sender fill:#ffe0e0
    style R1 fill:#e3f2fd
    style R2 fill:#e3f2fd
    style R3 fill:#e3f2fd
    style R4 fill:#e3f2fd
    style R5 fill:#e3f2fd
```

### 消息结构设计

```java
/**
 * Agent 消息基类
 */
public class AgentMessage {
    private String messageId;        // 消息唯一标识
    private String fromAgent;        // 发送者
    private String toAgent;          // 接收者（null表示广播）
    private MessageType type;        // 消息类型
    private String content;          // 消息内容
    private long timestamp;          // 时间戳
    private String conversationId;   // 会话ID
    private Map<String, Object> metadata; // 元数据
    
    // 枚举：消息类型
    public enum MessageType {
        REQUEST,      // 请求
        RESPONSE,     // 响应
        NOTIFICATION, // 通知
        BROADCAST,    // 广播
        HEARTBEAT     // 心跳
    }
    
    // Getters and Setters
}

/**
 * 任务消息
 */
public class TaskMessage extends AgentMessage {
    private String taskId;           // 任务ID
    private TaskStatus status;       // 任务状态
    private String taskDescription;  // 任务描述
    private Object taskResult;       // 任务结果
    private int priority;            // 优先级
    
    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }
}
```

## 共享内存

共享内存是一种高效的 Agent 通信方式，Agent 通过读写共享数据空间来交换信息。

### 架构模式

```mermaid
graph TB
    subgraph "共享内存架构"
        direction TB
        
        subgraph "Agent 层"
            A1[Agent A]
            A2[Agent B]
            A3[Agent C]
        end
        
        subgraph "共享内存层"
            SM[共享内存管理器<br/>Shared Memory Manager]
            
            subgraph "存储区域"
                KV[键值存储<br/>Key-Value Store]
                State[状态存储<br/>State Store]
                Context[上下文存储<br/>Context Store]
            end
        end
        
        A1 <-->|读写| SM
        A2 <-->|读写| SM
        A3 <-->|读写| SM
        
        SM --> KV
        SM --> State
        SM --> Context
    end
    
    style A1 fill:#e3f2fd
    style A2 fill:#e3f2fd
    style A3 fill:#e3f2fd
    style SM fill:#fff3e0
```

### 黑板系统（Blackboard Pattern）

黑板系统是一种经典的共享内存模式，多个 Agent 共同读写一个共享的"黑板"。

```mermaid
graph TB
    subgraph "黑板系统"
        direction TB
        
        BB[黑板<br/>Blackboard<br/>共享数据空间]
        
        Controller[控制器<br/>Controller]
        
        subgraph "知识源"
            KS1[知识源 A<br/>语音识别]
            KS2[知识源 B<br/>语义分析]
            KS3[知识源 C<br/>知识推理]
            KS4[知识源 D<br/>结果生成]
        end
        
        KS1 <-->|读写| BB
        KS2 <-->|读写| BB
        KS3 <-->|读写| BB
        KS4 <-->|读写| BB
        
        Controller -->|调度| KS1
        Controller -->|调度| KS2
        Controller -->|调度| KS3
        Controller -->|调度| KS4
        
        Controller <-->|监控| BB
    end
    
    style BB fill:#fff3e0
    style Controller fill:#ffe0e0
    style KS1 fill:#e3f2fd
    style KS2 fill:#e3f2fd
    style KS3 fill:#e3f2fd
    style KS4 fill:#e3f2fd
```

### 共享内存工作流程

```mermaid
sequenceDiagram
    participant A as Agent A<br/>数据生产者
    participant SM as 共享内存
    participant B as Agent B<br/>数据消费者
    participant C as Agent C<br/>数据消费者
    
    A->>SM: 写入数据：key="task_result"
    Note over A,SM: 包含：任务ID、结果、状态、时间戳
    
    SM->>SM: 数据变更通知
    
    B->>SM: 订阅通知：key="task_result"
    C->>SM: 订阅通知：key="task_result"
    
    SM-->>B: 推送变更通知
    SM-->>C: 推送变更通知
    
    B->>SM: 读取数据：key="task_result"
    SM-->>B: 返回数据
    
    B->>B: 处理数据
    B->>SM: 写入处理结果：key="processed_result"
    
    C->>SM: 读取数据：key="task_result"
    SM-->>C: 返回数据
```

### Java 实现示例

```java
/**
 * 共享内存管理器
 */
@Component
public class SharedMemoryManager {
    
    private final ConcurrentHashMap<String, SharedEntry> memory = new ConcurrentHashMap<>();
    private final List<SharedMemoryListener> listeners = new CopyOnWriteArrayList<>();
    
    /**
     * 写入数据
     */
    public void write(String key, Object value, String agentId) {
        SharedEntry entry = new SharedEntry(value, agentId, System.currentTimeMillis());
        SharedEntry oldValue = memory.put(key, entry);
        
        // 通知监听器
        notifyListeners(key, oldValue != null ? oldValue.getValue() : null, value, agentId);
    }
    
    /**
     * 读取数据
     */
    public <T> T read(String key, Class<T> type) {
        SharedEntry entry = memory.get(key);
        return entry != null ? type.cast(entry.getValue()) : null;
    }
    
    /**
     * 订阅变更
     */
    public void subscribe(String keyPattern, SharedMemoryListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(String key, Object oldValue, Object newValue, String agentId) {
        for (SharedMemoryListener listener : listeners) {
            if (listener.matches(key)) {
                listener.onChange(key, oldValue, newValue, agentId);
            }
        }
    }
}

/**
 * 共享条目
 */
@Data
public class SharedEntry {
    private final Object value;
    private final String writtenBy;
    private final long timestamp;
    private final String version = UUID.randomUUID().toString();
}

/**
 * 共享内存监听器
 */
public interface SharedMemoryListener {
    boolean matches(String key);
    void onChange(String key, Object oldValue, Object newValue, String agentId);
}
```

## 消息队列

消息队列提供异步、可靠的 Agent 通信机制，适用于解耦和削峰场景。

### 架构模式

```mermaid
graph TB
    subgraph "消息队列架构"
        direction TB
        
        subgraph "生产者"
            P1[Agent A<br/>生产者]
            P2[Agent B<br/>生产者]
        end
        
        subgraph "消息队列"
            MQ[消息队列<br/>Message Queue]
            
            subgraph "队列类型"
                Q1[任务队列<br/>Task Queue]
                Q2[事件队列<br/>Event Queue]
                Q3[通知队列<br/>Notification Queue]
            end
        end
        
        subgraph "消费者"
            C1[Agent C<br/>消费者]
            C2[Agent D<br/>消费者]
            C3[Agent E<br/>消费者]
        end
        
        P1 -->|发布| Q1
        P2 -->|发布| Q2
        
        Q1 -->|消费| C1
        Q1 -->|消费| C2
        Q2 -->|消费| C3
        Q3 -->|广播| C1
        Q3 -->|广播| C2
        Q3 -->|广播| C3
    end
    
    style P1 fill:#e3f2fd
    style P2 fill:#e3f2fd
    style MQ fill:#fff3e0
    style C1 fill:#e8f5e9
    style C2 fill:#e8f5e9
    style C3 fill:#e8f5e9
```

### 发布/订阅模式（Pub/Sub）

```mermaid
graph LR
    subgraph "发布/订阅模式"
        direction TB
        
        P[发布者<br/>Publisher] -->|发布消息| Topic[Topic:<br/>agent.task.completed]
        
        Topic -->|推送| S1[订阅者 A]
        Topic -->|推送| S2[订阅者 B]
        Topic -->|推送| S3[订阅者 C]
    end
    
    style P fill:#ffe0e0
    style Topic fill:#fff3e0
    style S1 fill:#e3f2fd
    style S2 fill:#e3f2fd
    style S3 fill:#e3f2fd
```

### 消息队列工作流程

```mermaid
sequenceDiagram
    participant Producer as Agent A<br/>生产者
    participant Exchange as 交换机<br/>Exchange
    participant Queue as 队列<br/>Queue
    participant Consumer as Agent B<br/>消费者
    
    Producer->>Producer: 创建消息
    Producer->>Exchange: 发布消息<br/>routingKey: task.execute
    
    Exchange->>Exchange: 路由消息
    Exchange->>Queue: 存储消息
    
    Note over Queue: 消息持久化存储
    
    Queue-->>Consumer: 推送消息（推送模式）
    
    alt 拉取模式
        Consumer->>Queue: 拉取消息
        Queue-->>Consumer: 返回消息
    end
    
    Consumer->>Consumer: 处理消息
    
    alt 处理成功
        Consumer->>Queue: 发送 ACK
        Queue->>Queue: 删除消息
    else 处理失败
        Consumer->>Queue: 发送 NACK
        Queue->>Queue: 重新入队或进入死信队列
    end
```

### Java 实现示例（使用 Spring AMQP）

```java
/**
 * 消息队列配置
 */
@Configuration
public class AgentMessageQueueConfig {
    
    public static final String AGENT_EXCHANGE = "agent.exchange";
    public static final String TASK_QUEUE = "agent.task.queue";
    public static final String EVENT_QUEUE = "agent.event.queue";
    public static final String TASK_ROUTING_KEY = "agent.task.*";
    public static final String EVENT_ROUTING_KEY = "agent.event.*";
    
    @Bean
    public TopicExchange agentExchange() {
        return new TopicExchange(AGENT_EXCHANGE);
    }
    
    @Bean
    public Queue taskQueue() {
        return QueueBuilder.durable(TASK_QUEUE)
            .withArgument("x-dead-letter-exchange", "agent.dlx")
            .withArgument("x-dead-letter-routing-key", "agent.task.failed")
            .build();
    }
    
    @Bean
    public Queue eventQueue() {
        return QueueBuilder.durable(EVENT_QUEUE).build();
    }
    
    @Bean
    public Binding taskBinding(Queue taskQueue, TopicExchange agentExchange) {
        return BindingBuilder.bind(taskQueue).to(agentExchange).with(TASK_ROUTING_KEY);
    }
    
    @Bean
    public Binding eventBinding(Queue eventQueue, TopicExchange agentExchange) {
        return BindingBuilder.bind(eventQueue).to(agentExchange).with(EVENT_ROUTING_KEY);
    }
}

/**
 * 消息生产者
 */
@Service
public class AgentMessageProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendTask(String agentId, TaskMessage task) {
        String routingKey = "agent.task." + agentId;
        rabbitTemplate.convertAndSend(
            AgentMessageQueueConfig.AGENT_EXCHANGE,
            routingKey,
            task,
            message -> {
                message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                message.getMessageProperties().setTimestamp(new Date());
                return message;
            }
        );
    }
    
    public void broadcastEvent(EventMessage event) {
        rabbitTemplate.convertAndSend(
            AgentMessageQueueConfig.AGENT_EXCHANGE,
            "agent.event.broadcast",
            event
        );
    }
}

/**
 * 消息消费者
 */
@Service
public class AgentMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(AgentMessageConsumer.class);
    
    @Autowired
    private TaskExecutor taskExecutor;
    
    @RabbitListener(queues = AgentMessageQueueConfig.TASK_QUEUE)
    public void handleTask(TaskMessage task, Channel channel, 
                          @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            logger.info("收到任务: {} from {}", task.getTaskId(), task.getFromAgent());
            
            // 执行任务
            taskExecutor.execute(task);
            
            // 确认消息
            channel.basicAck(tag, false);
            
        } catch (Exception e) {
            logger.error("任务处理失败: {}", task.getTaskId(), e);
            // 拒绝消息，重新入队
            channel.basicNack(tag, false, true);
        }
    }
    
    @RabbitListener(queues = AgentMessageQueueConfig.EVENT_QUEUE)
    public void handleEvent(EventMessage event) {
        logger.info("收到事件: {} - {}", event.getEventType(), event.getContent());
        // 处理事件
    }
}
```

## 通信机制对比

```mermaid
graph TB
    subgraph "通信机制选择"
        direction TB
        
        Start{场景需求}
        
        Start -->|实时交互| CC[对话式通信]
        Start -->|数据共享| SM[共享内存]
        Start -->|异步解耦| MQ[消息队列]
        
        CC -->|优势| C1[直观自然<br/>灵活协商<br/>适合复杂讨论]
        CC -->|劣势| C2[开销较大<br/>需要解析<br/>可能歧义]
        
        SM -->|优势| S1[高效访问<br/>状态共享<br/>实时同步]
        SM -->|劣势| S2[一致性问题<br/>并发控制<br/>单点风险]
        
        MQ -->|优势| M1[异步解耦<br/>可靠传输<br/>削峰填谷]
        MQ -->|劣势| M2[延迟较高<br/>复杂度增加<br/>需要运维]
    end
    
    style CC fill:#e3f2fd
    style SM fill:#fff3e0
    style MQ fill:#e8f5e9
```

| 特性 | 对话式通信 | 共享内存 | 消息队列 |
|------|-----------|----------|----------|
| **实时性** | 高 | 极高 | 中 |
| **吞吐量** | 中 | 高 | 高 |
| **可靠性** | 中 | 低（需额外保障） | 高 |
| **复杂度** | 低 | 中 | 高 |
| **适用场景** | 协商讨论、决策 | 状态同步、数据共享 | 任务分发、事件通知 |
| **持久化** | 可选 | 可选 | 内置支持 |
| **扩展性** | 中 | 中 | 高 |

## 混合通信模式

在实际系统中，通常会组合使用多种通信机制：

```mermaid
graph TB
    subgraph "混合通信架构"
        direction TB
        
        subgraph "协调层"
            Orchestrator[协调器]
        end
        
        subgraph "Agent 集群"
            A1[Agent A]
            A2[Agent B]
            A3[Agent C]
        end
        
        subgraph "通信层"
            Conv[对话通道<br/>协商讨论]
            SM[共享内存<br/>状态同步]
            MQ[消息队列<br/>任务分发]
        end
        
        Orchestrator <-->|对话| A1
        Orchestrator <-->|对话| A2
        Orchestrator <-->|对话| A3
        
        A1 <-->|共享内存| SM
        A2 <-->|共享内存| SM
        A3 <-->|共享内存| SM
        
        Orchestrator -->|消息队列| MQ
        MQ -->|消费| A1
        MQ -->|消费| A2
        MQ -->|消费| A3
    end
    
    style Orchestrator fill:#ffe0e0
    style A1 fill:#e3f2fd
    style A2 fill:#e3f2fd
    style A3 fill:#e3f2fd
    style Conv fill:#f3e5f5
    style SM fill:#fff3e0
    style MQ fill:#e8f5e9
```

## 最佳实践

1. **选择合适的通信机制**：根据场景需求选择对话、共享内存或消息队列
2. **设计清晰的消息格式**：统一的消息结构便于解析和处理
3. **实现消息确认机制**：确保重要消息的可靠传递
4. **处理消息顺序**：考虑消息的顺序性和因果关系
5. **监控和日志**：记录通信日志便于调试和审计
6. **错误处理**：实现重试、死信队列等容错机制
