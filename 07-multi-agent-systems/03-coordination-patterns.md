# 03 - 协作模式

多智能体系统的核心在于 Agent 之间的协作。不同的协作模式适用于不同的场景，选择合适的协作模式对于系统效率和任务完成质量至关重要。

## 协作模式概览

```mermaid
graph TB
    subgraph "协作模式分类"
        direction TB
        
        CP[协作模式<br/>Coordination Patterns]
        
        CP --> Division[分工协作<br/>Division of Labor]
        CP --> Competition[竞争模式<br/>Competition]
        CP --> Hierarchy[层级结构<br/>Hierarchy]
        CP --> Market[市场模式<br/>Market-based]
        CP --> Team[团队模式<br/>Team Formation]
        
        Division --> D1[流水线]
        Division --> D2[任务分配]
        
        Competition --> C1[拍卖机制]
        Competition --> C2[投票机制]
        
        Hierarchy --> H1[树形结构]
        Hierarchy --> H2[主从结构]
        
        Market --> M1[竞价机制]
        Market --> M2[合同网]
        
        Team --> T1[动态组队]
        Team --> T2[固定团队]
    end
    
    style CP fill:#fff3e0
    style Division fill:#e3f2fd
    style Competition fill:#ffe0e0
    style Hierarchy fill:#e8f5e9
    style Market fill:#f3e5f5
    style Team fill:#fce4ec
```

## 分工协作

分工协作是最常见的多智能体协作模式，每个 Agent 负责特定的子任务，通过协作完成整体目标。

### 流水线模式（Pipeline）

```mermaid
graph LR
    subgraph "流水线协作模式"
        direction LR
        
        Input[输入] --> A1[Agent A<br/>需求分析]
        A1 --> A2[Agent B<br/>架构设计]
        A2 --> A3[Agent C<br/>代码开发]
        A3 --> A4[Agent D<br/>测试验证]
        A4 --> Output[输出]
        
        A1 -.->|反馈| Input
        A2 -.->|反馈| A1
        A3 -.->|反馈| A2
        A4 -.->|反馈| A3
    end
    
    style A1 fill:#e3f2fd
    style A2 fill:#fff3e0
    style A3 fill:#e8f5e9
    style A4 fill:#f3e5f5
```

**工作流程：**

```mermaid
sequenceDiagram
    participant User as 用户
    participant A as Agent A<br/>分析师
    participant B as Agent B<br/>设计师
    participant C as Agent C<br/>开发者
    participant D as Agent D<br/>测试员
    
    User->>A: 提交需求：开发用户系统
    A->>A: 分析需求
    A->>B: 传递分析结果<br/>用户故事、功能列表
    
    B->>B: 设计架构
    B->>C: 传递设计文档<br/>API设计、数据库设计
    
    C->>C: 编写代码
    C->>D: 提交代码<br/>功能实现
    
    D->>D: 执行测试
    
    alt 测试通过
        D-->>User: 交付功能
    else 发现问题
        D->>C: 反馈Bug
        C->>C: 修复
        C->>D: 重新提交
    end
```

### 任务分配模式（Task Assignment）

```mermaid
graph TB
    subgraph "任务分配模式"
        direction TB
        
        Coordinator[协调器<br/>Coordinator]
        
        subgraph "任务池"
            T1[任务1: 前端开发]
            T2[任务2: 后端API]
            T3[任务3: 数据库设计]
            T4[任务4: 测试用例]
        end
        
        subgraph "Agent 池"
            A1[Agent A<br/>前端专家]
            A2[Agent B<br/>后端专家]
            A3[Agent C<br/>数据库专家]
            A4[Agent D<br/>测试专家]
        end
        
        Coordinator -->|分配| T1
        Coordinator -->|分配| T2
        Coordinator -->|分配| T3
        Coordinator -->|分配| T4
        
        T1 -->|执行| A1
        T2 -->|执行| A2
        T3 -->|执行| A3
        T4 -->|执行| A4
        
        A1 -->|汇报| Coordinator
        A2 -->|汇报| Coordinator
        A3 -->|汇报| Coordinator
        A4 -->|汇报| Coordinator
    end
    
    style Coordinator fill:#ffe0e0
    style A1 fill:#e3f2fd
    style A2 fill:#e3f2fd
    style A3 fill:#e3f2fd
    style A4 fill:#e3f2fd
```

### Java 实现示例

```java
/**
 * 任务分配器
 */
@Service
public class TaskDistributor {
    
    @Autowired
    private AgentRegistry agentRegistry;
    
    /**
     * 分配任务给最适合的 Agent
     */
    public Agent assignTask(Task task) {
        List<Agent> availableAgents = agentRegistry.getAvailableAgents();
        
        // 基于能力匹配选择 Agent
        Agent bestAgent = availableAgents.stream()
            .filter(agent -> agent.hasCapability(task.getRequiredCapabilities()))
            .max(Comparator.comparingInt(agent -> 
                calculateMatchScore(agent, task)))
            .orElseThrow(() -> new NoSuitableAgentException("没有合适的Agent"));
        
        // 分配任务
        bestAgent.assignTask(task);
        return bestAgent;
    }
    
    /**
     * 流水线模式执行
     */
    public void executePipeline(Task task, List<String> agentRoles) {
        Task currentTask = task;
        
        for (String role : agentRoles) {
            Agent agent = agentRegistry.findByRole(role);
            TaskResult result = agent.execute(currentTask);
            
            if (!result.isSuccess()) {
                throw new PipelineException("流水线执行失败: " + role);
            }
            
            currentTask = createNextTask(result);
        }
    }
    
    private int calculateMatchScore(Agent agent, Task task) {
        int score = 0;
        // 能力匹配度
        score += agent.getCapabilities().stream()
            .filter(task.getRequiredCapabilities()::contains)
            .count() * 10;
        // 当前负载
        score -= agent.getCurrentLoad() * 5;
        // 历史成功率
        score += agent.getSuccessRate() * 20;
        return score;
    }
}

/**
 * Agent 注册表
 */
@Component
public class AgentRegistry {
    
    private final Map<String, Agent> agents = new ConcurrentHashMap<>();
    
    public void register(Agent agent) {
        agents.put(agent.getId(), agent);
    }
    
    public List<Agent> getAvailableAgents() {
        return agents.values().stream()
            .filter(Agent::isAvailable)
            .collect(Collectors.toList());
    }
    
    public Agent findByRole(String role) {
        return agents.values().stream()
            .filter(agent -> agent.getRole().equals(role))
            .findFirst()
            .orElseThrow(() -> new AgentNotFoundException(role));
    }
}
```

## 竞争模式

竞争模式适用于资源有限或需要最优解的场景，Agent 通过竞争获得任务执行权。

### 拍卖机制（Auction）

```mermaid
graph TB
    subgraph "拍卖机制"
        direction TB
        
        Auctioneer[拍卖师<br/>Auctioneer]
        
        subgraph "竞标者"
            B1[Agent A<br/>出价: 50元]
            B2[Agent B<br/>出价: 45元]
            B3[Agent C<br/>出价: 55元]
        end
        
        Task[待分配任务]
        
        Task --> Auctioneer
        Auctioneer -->|宣布拍卖| B1
        Auctioneer -->|宣布拍卖| B2
        Auctioneer -->|宣布拍卖| B3
        
        B1 -->|提交出价| Auctioneer
        B2 -->|提交出价| Auctioneer
        B3 -->|提交出价| Auctioneer
        
        Auctioneer -->|宣布获胜者| B2
        B2 -->|执行任务| Result[任务完成]
    end
    
    style Auctioneer fill:#ffe0e0
    style B1 fill:#e3f2fd
    style B2 fill:#e8f5e9
    style B3 fill:#e3f2fd
```

**拍卖流程：**

```mermaid
sequenceDiagram
    participant Auctioneer as 拍卖师
    participant A as Agent A
    participant B as Agent B
    participant C as Agent C
    
    Auctioneer->>A: 宣布拍卖：任务X<br/>起拍价: 100
    Auctioneer->>B: 宣布拍卖：任务X<br/>起拍价: 100
    Auctioneer->>C: 宣布拍卖：任务X<br/>起拍价: 100
    
    Note over A,C: 评估任务成本
    
    A->>Auctioneer: 出价: 80
    B->>Auctioneer: 出价: 70
    C->>Auctioneer: 出价: 75
    
    Note over Auctioneer: 比较出价<br/>选择最低出价
    
    Auctioneer->>B: 中标通知
    Auctioneer->>A: 未中标通知
    Auctioneer->>C: 未中标通知
    
    B->>B: 执行任务
    B->>Auctioneer: 任务完成，提交结果
```

### 投票机制（Voting）

```mermaid
graph TB
    subgraph "投票机制"
        direction TB
        
        Proposal[提案]
        
        subgraph "投票者"
            V1[Agent A<br/>赞成]
            V2[Agent B<br/>反对]
            V3[Agent C<br/>赞成]
            V4[Agent D<br/>赞成]
        end
        
        Result[结果: 通过<br/>3票赞成 vs 1票反对]
        
        Proposal --> V1
        Proposal --> V2
        Proposal --> V3
        Proposal --> V4
        
        V1 --> Result
        V2 --> Result
        V3 --> Result
        V4 --> Result
    end
    
    style Proposal fill:#fff3e0
    style V1 fill:#e8f5e9
    style V2 fill:#ffe0e0
    style V3 fill:#e8f5e9
    style V4 fill:#e8f5e9
    style Result fill:#e8f5e9
```

### Java 实现示例

```java
/**
 * 拍卖管理器
 */
@Service
public class AuctionManager {
    
    /**
     * 执行拍卖
     */
    public AuctionResult conductAuction(Task task, List<Agent> bidders) {
        // 创建拍卖
        Auction auction = new Auction(task);
        
        // 收集出价
        List<Bid> bids = new ArrayList<>();
        for (Agent bidder : bidders) {
            Bid bid = bidder.submitBid(task);
            if (bid != null) {
                bids.add(bid);
            }
        }
        
        // 选择获胜者（最低出价）
        Bid winningBid = bids.stream()
            .min(Comparator.comparingDouble(Bid::getAmount))
            .orElseThrow(() -> new NoBidException("没有出价"));
        
        // 通知结果
        notifyWinner(winningBid.getAgent(), task);
        notifyLosers(bids.stream()
            .filter(b -> !b.equals(winningBid))
            .map(Bid::getAgent)
            .collect(Collectors.toList()), task);
        
        return new AuctionResult(winningBid.getAgent(), winningBid.getAmount());
    }
}

/**
 * 投票管理器
 */
@Service
public class VotingManager {
    
    public VotingResult conductVote(Proposal proposal, List<Agent> voters, VotingStrategy strategy) {
        Map<Agent, Vote> votes = new HashMap<>();
        
        // 收集投票
        for (Agent voter : voters) {
            Vote vote = voter.vote(proposal);
            votes.put(voter, vote);
        }
        
        // 计算结果
        long approveCount = votes.values().stream()
            .filter(v -> v == Vote.APPROVE)
            .count();
        long rejectCount = votes.values().stream()
            .filter(v -> v == Vote.REJECT)
            .count();
        
        boolean passed = strategy.evaluate(approveCount, rejectCount, voters.size());
        
        return new VotingResult(passed, approveCount, rejectCount, votes);
    }
}

/**
 * 投票策略
 */
public interface VotingStrategy {
    boolean evaluate(long approveCount, long rejectCount, long totalVoters);
}

/**
 * 简单多数策略
 */
public class SimpleMajorityStrategy implements VotingStrategy {
    @Override
    public boolean evaluate(long approveCount, long rejectCount, long totalVoters) {
        return approveCount > rejectCount;
    }
}

/**
 * 绝对多数策略
 */
public class AbsoluteMajorityStrategy implements VotingStrategy {
    @Override
    public boolean evaluate(long approveCount, long rejectCount, long totalVoters) {
        return approveCount > totalVoters / 2;
    }
}
```

## 层级结构

层级结构通过明确的上下级关系组织 Agent，适合需要统一指挥的场景。

### 树形结构（Tree Structure）

```mermaid
graph TB
    subgraph "树形层级结构"
        direction TB
        
        Root[CEO Agent<br/>战略决策]
        
        M1[经理 A<br/>产品部门]
        M2[经理 B<br/>技术部门]
        M3[经理 C<br/>运营部门]
        
        E1[员工 A1]
        E2[员工 A2]
        E3[员工 B1]
        E4[员工 B2]
        E5[员工 C1]
        E6[员工 C2]
        
        Root --> M1
        Root --> M2
        Root --> M3
        
        M1 --> E1
        M1 --> E2
        M2 --> E3
        M2 --> E4
        M3 --> E5
        M3 --> E6
    end
    
    style Root fill:#ffe0e0
    style M1 fill:#fff3e0
    style M2 fill:#fff3e0
    style M3 fill:#fff3e0
    style E1 fill:#e3f2fd
    style E2 fill:#e3f2fd
    style E3 fill:#e3f2fd
    style E4 fill:#e3f2fd
    style E5 fill:#e3f2fd
    style E6 fill:#e3f2fd
```

### 主从结构（Master-Slave）

```mermaid
graph TB
    subgraph "主从结构"
        direction TB
        
        Master[主节点<br/>Master<br/>任务分配与协调]
        
        subgraph "从节点"
            S1[Slave 1<br/>执行任务]
            S2[Slave 2<br/>执行任务]
            S3[Slave 3<br/>执行任务]
            S4[Slave N<br/>执行任务]
        end
        
        Master <-->|分配任务| S1
        Master <-->|分配任务| S2
        Master <-->|分配任务| S3
        Master <-->|分配任务| S4
        
        S1 -->|汇报结果| Master
        S2 -->|汇报结果| Master
        S3 -->|汇报结果| Master
        S4 -->|汇报结果| Master
    end
    
    style Master fill:#ffe0e0
    style S1 fill:#e3f2fd
    style S2 fill:#e3f2fd
    style S3 fill:#e3f2fd
    style S4 fill:#e3f2fd
```

### Java 实现示例

```java
/**
 * 层级节点
 */
public abstract class HierarchicalAgent implements Agent {
    
    private String id;
    private String role;
    private HierarchicalAgent parent;
    private List<HierarchicalAgent> children = new ArrayList<>();
    
    public void setParent(HierarchicalAgent parent) {
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }
    
    public void addChild(HierarchicalAgent child) {
        children.add(child);
    }
    
    /**
     * 向下委派任务
     */
    public void delegateDown(Task task, String targetRole) {
        HierarchicalAgent target = findChildByRole(targetRole);
        if (target != null) {
            target.receiveTask(task);
        }
    }
    
    /**
     * 向上汇报
     */
    public void reportUp(TaskResult result) {
        if (parent != null) {
            parent.receiveReport(this, result);
        }
    }
    
    /**
     * 广播给所有子节点
     */
    public void broadcastToChildren(Message message) {
        for (HierarchicalAgent child : children) {
            child.receiveMessage(message);
        }
    }
    
    private HierarchicalAgent findChildByRole(String role) {
        return children.stream()
            .filter(c -> c.getRole().equals(role))
            .findFirst()
            .orElse(null);
    }
}

/**
 * 主节点
 */
@Service
public class MasterAgent extends HierarchicalAgent {
    
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    
    /**
     * 分配任务给从节点
     */
    public void distributeTask(Task task) {
        // 选择负载最低的从节点
        SlaveAgent slave = findLeastLoadedSlave();
        
        CompletableFuture.supplyAsync(() -> {
            slave.execute(task);
            return slave.getResult();
        }, executor).thenAccept(result -> {
            collectResult(task.getId(), result);
        });
    }
    
    /**
     * 收集结果
     */
    private void collectResult(String taskId, TaskResult result) {
        // 聚合结果
        // 触发下一步操作
    }
    
    private SlaveAgent findLeastLoadedSlave() {
        return getChildren().stream()
            .filter(a -> a instanceof SlaveAgent)
            .map(a -> (SlaveAgent) a)
            .min(Comparator.comparingInt(SlaveAgent::getLoad))
            .orElseThrow();
    }
}

/**
 * 从节点
 */
@Service
public class SlaveAgent extends HierarchicalAgent {
    
    private int load = 0;
    private TaskResult lastResult;
    
    @Override
    public TaskResult execute(Task task) {
        load++;
        try {
            // 执行任务
            TaskResult result = doExecute(task);
            lastResult = result;
            return result;
        } finally {
            load--;
        }
    }
    
    private TaskResult doExecute(Task task) {
        // 具体执行逻辑
        return new TaskResult(true, "执行完成");
    }
    
    public int getLoad() {
        return load;
    }
    
    public TaskResult getResult() {
        return lastResult;
    }
}
```

## 市场模式

市场模式模拟经济系统中的交易行为，Agent 通过合同和竞价进行协作。

### 合同网协议（Contract Net Protocol）

```mermaid
sequenceDiagram
    participant Manager as 管理者
    participant A as Agent A
    participant B as Agent B
    participant C as Agent C
    
    Note over Manager,C: 合同网协议流程
    
    Manager->>A: 招标公告<br/>任务描述、要求、截止时间
    Manager->>B: 招标公告
    Manager->>C: 招标公告
    
    Note over A,C: 评估任务可行性
    
    A->>Manager: 投标<br/>报价、能力证明、计划
    B->>Manager: 投标
    Note over C: 放弃投标
    
    Note over Manager: 评估投标<br/>选择最优承包商
    
    Manager->>B: 中标通知<br/>合同条款
    Manager->>A: 未中标通知
    
    B->>Manager: 合同确认
    
    Note over Manager,B: 合同执行阶段
    
    B->>B: 执行任务
    B->>Manager: 进度报告
    B->>Manager: 任务完成报告<br/>交付成果
    
    Manager->>B: 验收确认<br/>支付（如有）
```

### Java 实现示例

```java
/**
 * 合同网协议实现
 */
@Service
public class ContractNetProtocol {
    
    /**
     * 管理者发起招标
     */
    public void announceTask(TaskAnnouncement announcement, List<Agent> contractors) {
        for (Agent contractor : contractors) {
            contractor.receiveAnnouncement(announcement);
        }
    }
    
    /**
     * 收集投标并选择承包商
     */
    public Contract awardContract(TaskAnnouncement announcement, List<Bid> bids) {
        // 评估投标
        Bid winningBid = evaluateBids(bids);
        
        // 创建合同
        Contract contract = new Contract(
            announcement.getTaskId(),
            winningBid.getAgent(),
            winningBid.getProposal(),
            winningBid.getPrice()
        );
        
        // 通知中标者
        winningBid.getAgent().receiveAward(contract);
        
        // 通知未中标者
        bids.stream()
            .filter(b -> !b.equals(winningBid))
            .forEach(b -> b.getAgent().receiveReject(announcement.getTaskId()));
        
        return contract;
    }
    
    private Bid evaluateBids(List<Bid> bids) {
        return bids.stream()
            .max(Comparator.comparingDouble(this::calculateBidScore))
            .orElseThrow();
    }
    
    private double calculateBidScore(Bid bid) {
        // 综合考虑价格、能力、历史表现
        double priceScore = 1.0 / bid.getPrice();
        double capabilityScore = bid.getAgent().getCapabilityScore();
        double reputationScore = bid.getAgent().getReputation();
        
        return priceScore * 0.4 + capabilityScore * 0.4 + reputationScore * 0.2;
    }
}

/**
 * 任务公告
 */
@Data
public class TaskAnnouncement {
    private String taskId;
    private String description;
    private List<String> requiredCapabilities;
    private LocalDateTime deadline;
    private double maxBudget;
}

/**
 * 投标
 */
@Data
public class Bid {
    private Agent agent;
    private String taskId;
    private double price;
    private String proposal;
    private LocalDateTime estimatedCompletion;
}

/**
 * 合同
 */
@Data
public class Contract {
    private String contractId;
    private String taskId;
    private Agent contractor;
    private String terms;
    private double price;
    private ContractStatus status;
    
    public enum ContractStatus {
        PENDING, ACTIVE, COMPLETED, CANCELLED
    }
}
```

## 协作模式对比

```mermaid
graph TB
    subgraph "协作模式选择决策"
        direction TB
        
        Start{任务特点}
        
        Start -->|线性依赖| Division[分工协作<br/>流水线]
        Start -->|可并行| Division2[分工协作<br/>任务分配]
        Start -->|资源竞争| Competition[竞争模式<br/>拍卖/投票]
        Start -->|需要统一指挥| Hierarchy[层级结构]
        Start -->|动态协商| Market[市场模式<br/>合同网]
        
        Division -->|优势| D1[清晰流程<br/>易于追踪]
        Division2 -->|优势| D2[并行执行<br/>提高效率]
        Competition -->|优势| C1[资源优化<br/>公平竞争]
        Hierarchy -->|优势| H1[统一协调<br/>快速决策]
        Market -->|优势| M1[灵活协商<br/>动态组队]
    end
```

| 协作模式 | 适用场景 | 优势 | 劣势 |
|---------|---------|------|------|
| **流水线** | 任务有明确步骤依赖 | 流程清晰、责任明确 | 存在等待时间、单点瓶颈 |
| **任务分配** | 任务可并行、Agent 专业化 | 并行执行、效率高 | 需要协调、可能有负载不均 |
| **拍卖** | 资源竞争、成本敏感 | 资源优化、公平 | 拍卖开销、可能非理性竞价 |
| **投票** | 需要集体决策 | 民主、容错 | 决策慢、可能僵局 |
| **层级** | 需要统一指挥 | 决策快、责任明确 | 单点故障、信息传递失真 |
| **市场** | 动态环境、灵活协商 | 灵活、自适应 | 复杂度高、协商开销 |

## 混合协作模式

实际系统中通常会组合使用多种协作模式：

```mermaid
graph TB
    subgraph "混合协作架构示例"
        direction TB
        
        Root[总协调者<br/>层级结构]
        
        subgraph "产品团队"
            PM[产品经理<br/>层级]
            PA1[分析师]
            PA2[设计师]
        end
        
        subgraph "开发团队"
            subgraph "任务分配"
                TL[技术负责人]
                Dev1[开发Agent A]
                Dev2[开发Agent B]
                Dev3[开发Agent C]
            end
        end
        
        subgraph "测试团队"
            subgraph "流水线"
                Test1[单元测试]
                Test2[集成测试]
                Test3[验收测试]
            end
        end
        
        Root --> PM
        Root --> TL
        Root --> Test1
        
        PM --> PA1
        PM --> PA2
        
        TL -->|分配| Dev1
        TL -->|分配| Dev2
        TL -->|分配| Dev3
        
        Test1 --> Test2
        Test2 --> Test3
    end
    
    style Root fill:#ffe0e0
    style PM fill:#fff3e0
    style TL fill:#fff3e0
    style Test1 fill:#e8f5e9
```

## 最佳实践

1. **根据任务特点选择模式**：线性任务用流水线，可并行任务用任务分配
2. **考虑 Agent 能力**：专业化 Agent 适合分工，通用 Agent 适合竞争
3. **平衡效率与公平**：竞争模式公平但开销大，分工模式高效但可能不均
4. **设计容错机制**：单点故障时能够重新分配任务
5. **监控与调整**：实时监控协作效率，动态调整策略
