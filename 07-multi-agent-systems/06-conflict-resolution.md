# 06 - 冲突解决

在多智能体系统中，由于 Agent 的自主性、资源竞争、目标差异等因素，冲突不可避免。有效的冲突解决机制是确保系统稳定运行的关键。

## 冲突类型

```mermaid
graph TB
    subgraph "冲突类型分类"
        direction TB
        
        Conflict[多智能体冲突]
        
        Conflict --> RC[资源冲突<br/>Resource Conflict]
        Conflict --> GC[目标冲突<br/>Goal Conflict]
        Conflict --> IC[信息冲突<br/>Information Conflict]
        Conflict --> TC[任务冲突<br/>Task Conflict]
        
        RC --> RC1[资源竞争]
        RC --> RC2[资源死锁]
        
        GC --> GC1[目标不一致]
        GC --> GC2[优先级冲突]
        
        IC --> IC1[数据不一致]
        IC --> IC2[知识冲突]
        
        TC --> TC1[任务依赖冲突]
        TC --> TC2[执行顺序冲突]
    end
    
    style Conflict fill:#ffe0e0
    style RC fill:#e3f2fd
    style GC fill:#fff3e0
    style IC fill:#e8f5e9
    style TC fill:#f3e5f5
```

### 1. 资源冲突

当多个 Agent 竞争有限资源时产生的冲突。

```mermaid
graph TB
    subgraph "资源冲突示例"
        direction TB
        
        Resource[数据库连接池<br/>剩余: 2个连接]
        
        Agent1[Agent A<br/>请求: 3个连接]
        Agent2[Agent B<br/>请求: 2个连接]
        Agent3[Agent C<br/>请求: 1个连接]
        
        Agent1 -->|冲突| Resource
        Agent2 -->|冲突| Resource
        Agent3 -->|冲突| Resource
        
        Conflict{资源不足<br/>总需求: 6 > 可用: 2}
        
        Resource --> Conflict
    end
    
    style Resource fill:#fff3e0
    style Agent1 fill:#e3f2fd
    style Agent2 fill:#e3f2fd
    style Agent3 fill:#e3f2fd
    style Conflict fill:#ffe0e0
```

### 2. 目标冲突

当 Agent 的目标相互矛盾或不一致时产生的冲突。

```mermaid
graph TB
    subgraph "目标冲突示例"
        direction TB
        
        Agent1[Agent A<br/>目标: 最大化性能]
        Agent2[Agent B<br/>目标: 最小化成本]
        
        Conflict{冲突点}
        
        Agent1 -->|高性能方案<br/>成本高| Conflict
        Agent2 -->|低成本方案<br/>性能差| Conflict
        
        Conflict --> Result[需要协调<br/>找到平衡点]
    end
    
    style Agent1 fill:#e3f2fd
    style Agent2 fill:#fff3e0
    style Conflict fill:#ffe0e0
    style Result fill:#e8f5e9
```

### 3. 信息冲突

当 Agent 对同一信息有不同的认知时产生的冲突。

```mermaid
graph TB
    subgraph "信息冲突示例"
        direction TB
        
        Data[订单数据]
        
        Agent1[Agent A<br/>读取: 状态=待处理]
        Agent2[Agent B<br/>读取: 状态=已发货]
        
        Conflict{数据不一致}
        
        Data --> Agent1
        Data --> Agent2
        Agent1 --> Conflict
        Agent2 --> Conflict
        
        Conflict --> Resolution[需要同步<br/>确定正确状态]
    end
    
    style Agent1 fill:#e3f2fd
    style Agent2 fill:#fff3e0
    style Conflict fill:#ffe0e0
    style Resolution fill:#f3e5f5
```

### 4. 任务冲突

当任务之间存在依赖或执行顺序矛盾时产生的冲突。

```mermaid
graph TB
    subgraph "任务冲突示例"
        direction TB
        
        TaskA[任务A: 修改API接口]
        TaskB[任务B: 编写API文档]
        TaskC[任务C: 前端集成]
        
        Conflict1{冲突: B依赖A完成}
        Conflict2{冲突: C依赖A完成}
        
        TaskA --> Conflict1
        TaskB --> Conflict1
        
        TaskA --> Conflict2
        TaskC --> Conflict2
        
        Note: 如果并行执行会导致问题
    end
    
    style TaskA fill:#ffe0e0
    style TaskB fill:#e3f2fd
    style TaskC fill:#fff3e0
```

## 冲突检测

```mermaid
graph TB
    subgraph "冲突检测机制"
        direction TB
        
        Monitor[监控器]
        
        subgraph "检测策略"
            Static[静态检测<br/>规划阶段]
            Dynamic[动态检测<br/>执行阶段]
        end
        
        subgraph "检测类型"
            ResourceCheck[资源检查]
            DependencyCheck[依赖检查]
            ConsistencyCheck[一致性检查]
        end
        
        Monitor --> Static
        Monitor --> Dynamic
        
        Static --> ResourceCheck
        Static --> DependencyCheck
        
        Dynamic --> ResourceCheck
        Dynamic --> ConsistencyCheck
        
        ResourceCheck --> Conflict[冲突识别]
        DependencyCheck --> Conflict
        ConsistencyCheck --> Conflict
    end
    
    style Monitor fill:#e3f2fd
    style Conflict fill:#ffe0e0
```

### Java 实现示例

```java
/**
 * 冲突检测器
 */
@Service
public class ConflictDetector {
    
    @Autowired
    private ResourceManager resourceManager;
    
    @Autowired
    private TaskManager taskManager;
    
    /**
     * 静态冲突检测（规划阶段）
     */
    public List<Conflict> detectStaticConflicts(ExecutionPlan plan) {
        List<Conflict> conflicts = new ArrayList<>();
        
        // 检测资源冲突
        conflicts.addAll(detectResourceConflicts(plan));
        
        // 检测依赖冲突
        conflicts.addAll(detectDependencyConflicts(plan));
        
        // 检测任务冲突
        conflicts.addAll(detectTaskConflicts(plan));
        
        return conflicts;
    }
    
    /**
     * 动态冲突检测（执行阶段）
     */
    public List<Conflict> detectDynamicConflicts() {
        List<Conflict> conflicts = new ArrayList<>();
        
        // 检测运行时资源冲突
        conflicts.addAll(detectRuntimeResourceConflicts());
        
        // 检测数据一致性冲突
        conflicts.addAll(detectConsistencyConflicts());
        
        // 检测死锁
        conflicts.addAll(detectDeadlocks());
        
        return conflicts;
    }
    
    /**
     * 检测资源冲突
     */
    private List<Conflict> detectResourceConflicts(ExecutionPlan plan) {
        List<Conflict> conflicts = new ArrayList<>();
        Map<String, Integer> resourceDemand = new HashMap<>();
        
        // 统计各时段资源需求
        for (SubTask task : plan.getSubTasks()) {
            for (String resource : task.getRequiredResources()) {
                resourceDemand.merge(resource, 1, Integer::sum);
            }
        }
        
        // 检查是否超过可用资源
        for (Map.Entry<String, Integer> entry : resourceDemand.entrySet()) {
            String resourceId = entry.getKey();
            int demand = entry.getValue();
            int available = resourceManager.getAvailableCapacity(resourceId);
            
            if (demand > available) {
                conflicts.add(Conflict.builder()
                    .type(ConflictType.RESOURCE)
                    .severity(ConflictSeverity.HIGH)
                    .description(String.format("资源 %s 不足: 需求 %d, 可用 %d", 
                        resourceId, demand, available))
                    .involvedResources(Collections.singletonList(resourceId))
                    .build());
            }
        }
        
        return conflicts;
    }
    
    /**
     * 检测依赖冲突
     */
    private List<Conflict> detectDependencyConflicts(ExecutionPlan plan) {
        List<Conflict> conflicts = new ArrayList<>();
        
        // 检查循环依赖
        DependencyGraph graph = buildDependencyGraph(plan);
        List<List<SubTask>> cycles = graph.findCycles();
        
        for (List<SubTask> cycle : cycles) {
            conflicts.add(Conflict.builder()
                .type(ConflictType.DEPENDENCY)
                .severity(ConflictSeverity.CRITICAL)
                .description("检测到循环依赖: " + 
                    cycle.stream().map(SubTask::getId).collect(Collectors.joining(" -> ")))
                .involvedTasks(cycle.stream().map(SubTask::getId).collect(Collectors.toList()))
                .build());
        }
        
        return conflicts;
    }
    
    /**
     * 检测死锁
     */
    private List<Conflict> detectDeadlocks() {
        List<Conflict> conflicts = new ArrayList<>();
        
        // 构建资源分配图
        ResourceAllocationGraph graph = buildResourceAllocationGraph();
        
        // 检测循环等待
        List<List<String>> waitCycles = graph.findWaitCycles();
        
        for (List<String> cycle : waitCycles) {
            conflicts.add(Conflict.builder()
                .type(ConflictType.DEADLOCK)
                .severity(ConflictSeverity.CRITICAL)
                .description("检测到死锁: " + String.join(" -> ", cycle))
                .build());
        }
        
        return conflicts;
    }
}

/**
 * 冲突定义
 */
@Data
@Builder
public class Conflict {
    private String id;
    private ConflictType type;
    private ConflictSeverity severity;
    private String description;
    private List<String> involvedAgents;
    private List<String> involvedTasks;
    private List<String> involvedResources;
    private LocalDateTime detectedAt;
    private ConflictStatus status;
    
    public enum ConflictType {
        RESOURCE,      // 资源冲突
        GOAL,          // 目标冲突
        INFORMATION,   // 信息冲突
        TASK,          // 任务冲突
        DEPENDENCY,    // 依赖冲突
        DEADLOCK       // 死锁
    }
    
    public enum ConflictSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum ConflictStatus {
        DETECTED, RESOLVING, RESOLVED, ESCALATED
    }
}
```

## 冲突解决策略

### 1. 协商（Negotiation）

通过对话协商达成共识。

```mermaid
sequenceDiagram
    participant A as Agent A
    participant B as Agent B
    participant Mediator as 调解者
    
    Note over A,Mediator: 协商解决冲突
    
    A->>Mediator: 报告冲突<br/>我需要使用资源X
    B->>Mediator: 报告冲突<br/>我也需要资源X
    
    Mediator->>A: 询问优先级和截止时间
    Mediator->>B: 询问优先级和截止时间
    
    A-->>Mediator: 优先级: 高<br/>截止时间: 今天
    B-->>Mediator: 优先级: 中<br/>截止时间: 明天
    
    Mediator->>Mediator: 评估方案
    
    Mediator->>A: 方案: 你先用2小时
    Mediator->>B: 方案: 2小时后你使用
    
    A-->>Mediator: 同意
    B-->>Mediator: 同意
    
    Mediator->>A: 确认协议
    Mediator->>B: 确认协议
```

### 2. 仲裁（Arbitration）

由第三方仲裁者做出决策。

```mermaid
graph TB
    subgraph "仲裁流程"
        direction TB
        
        Conflict[冲突发生]
        
        Submit[提交仲裁]
        
        Arbitrator[仲裁者]
        
        Evaluation[评估证据]
        
        Decision{决策}
        
        ResultA[支持 Agent A]
        ResultB[支持 Agent B]
        Compromise[折中方案]
        
        Conflict --> Submit
        Submit --> Arbitrator
        Arbitrator --> Evaluation
        Evaluation --> Decision
        Decision --> ResultA
        Decision --> ResultB
        Decision --> Compromise
        
        ResultA --> Enforce[执行决策]
        ResultB --> Enforce
        Compromise --> Enforce
    end
    
    style Arbitrator fill:#ffe0e0
    style Decision fill:#fff3e0
```

### 3. 投票（Voting）

通过集体投票决定。

```mermaid
graph TB
    subgraph "投票解决冲突"
        direction TB
        
        Proposal[提案:<br/>如何分配资源X]
        
        subgraph "投票"
            A[Agent A<br/>赞成方案1]
            B[Agent B<br/>赞成方案2]
            C[Agent C<br/>赞成方案1]
            D[Agent D<br/>弃权]
        end
        
        Count[计票]
        
        Result[结果:<br/>方案1获胜<br/>2票 vs 1票]
        
        Proposal --> A
        Proposal --> B
        Proposal --> C
        Proposal --> D
        
        A --> Count
        B --> Count
        C --> Count
        D --> Count
        
        Count --> Result
    end
    
    style Proposal fill:#e3f2fd
    style Result fill:#e8f5e9
```

### 4. 优先级（Priority）

基于优先级规则自动解决。

```mermaid
graph TB
    subgraph "优先级解决"
        direction TB
        
        Conflict[资源冲突]
        
        Check{检查优先级}
        
        AgentA[Agent A<br/>优先级: 10<br/>截止时间: 今天]
        AgentB[Agent B<br/>优先级: 5<br/>截止时间: 明天]
        
        Decision[决策:<br/>Agent A 获得资源]
        
        Conflict --> Check
        Check --> AgentA
        Check --> AgentB
        
        AgentA --> Decision
        AgentB --> Decision
    end
    
    style Conflict fill:#ffe0e0
    style Decision fill:#e8f5e9
```

### 5. 资源重分配（Resource Reallocation）

动态调整资源分配。

```mermaid
graph TB
    subgraph "资源重分配"
        direction TB
        
        Conflict[资源不足]
        
        Analyze[分析资源使用]
        
        Find[寻找可释放资源]
        
        Preempt[抢占低优先级<br/>任务资源]
        
        Allocate[重新分配]
        
        Conflict --> Analyze
        Analyze --> Find
        Find --> Preempt
        Preempt --> Allocate
    end
    
    style Conflict fill:#ffe0e0
    style Allocate fill:#e8f5e9
```

## Java 实现示例

### 冲突解决器

```java
/**
 * 冲突解决器接口
 */
public interface ConflictResolver {
    Resolution resolve(Conflict conflict);
}

/**
 * 协商解决器
 */
@Service
public class NegotiationResolver implements ConflictResolver {
    
    @Autowired
    private CommunicationService communicationService;
    
    @Override
    public Resolution resolve(Conflict conflict) {
        // 获取冲突各方
        List<Agent> parties = getInvolvedAgents(conflict);
        
        // 收集各方诉求
        Map<String, Proposal> proposals = new HashMap<>();
        for (Agent agent : parties) {
            Proposal proposal = agent.makeProposal(conflict);
            proposals.put(agent.getId(), proposal);
        }
        
        // 寻找共同接受的方案
        Resolution resolution = findMutuallyAcceptableSolution(proposals);
        
        if (resolution == null) {
            // 无法协商一致，升级处理
            return escalate(conflict);
        }
        
        // 记录协议
        recordAgreement(conflict, parties, resolution);
        
        return resolution;
    }
    
    private Resolution findMutuallyAcceptableSolution(Map<String, Proposal> proposals) {
        // 实现协商逻辑
        // 可以尝试妥协、轮流、时间分割等策略
        return null;
    }
    
    private Resolution escalate(Conflict conflict) {
        // 升级到仲裁
        return null;
    }
}

/**
 * 仲裁解决器
 */
@Service
public class ArbitrationResolver implements ConflictResolver {
    
    @Autowired
    private ArbitrationPolicy policy;
    
    @Override
    public Resolution resolve(Conflict conflict) {
        // 根据仲裁策略做出决策
        switch (policy.getStrategy()) {
            case PRIORITY:
                return resolveByPriority(conflict);
            case FIRST_COME_FIRST_SERVED:
                return resolveByFCFS(conflict);
            case FAIR_SHARE:
                return resolveByFairShare(conflict);
            case ADMINISTRATOR_DECISION:
                return resolveByAdministrator(conflict);
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    private Resolution resolveByPriority(Conflict conflict) {
        // 按优先级解决
        List<Agent> sortedAgents = conflict.getInvolvedAgents().stream()
            .map(this::getAgent)
            .sorted(Comparator.comparingInt(Agent::getPriority).reversed())
            .collect(Collectors.toList());
        
        Agent winner = sortedAgents.get(0);
        
        return Resolution.builder()
            .conflictId(conflict.getId())
            .winner(winner.getId())
            .reason("优先级最高: " + winner.getPriority())
            .build();
    }
    
    private Resolution resolveByFCFS(Conflict conflict) {
        // 按先到先得解决
        Agent winner = conflict.getInvolvedAgents().stream()
            .map(this::getAgent)
            .min(Comparator.comparing(Agent::getRequestTime))
            .orElseThrow();
        
        return Resolution.builder()
            .conflictId(conflict.getId())
            .winner(winner.getId())
            .reason("最先请求")
            .build();
    }
    
    private Resolution resolveByFairShare(Conflict conflict) {
        // 公平分配
        // 实现资源按比例或时间分割
        return Resolution.builder()
            .conflictId(conflict.getId())
            .solution("时间分割")
            .allocation(calculateFairAllocation(conflict))
            .build();
    }
    
    private Map<String, Double> calculateFairAllocation(Conflict conflict) {
        // 计算公平分配方案
        return Collections.emptyMap();
    }
}

/**
 * 投票解决器
 */
@Service
public class VotingResolver implements ConflictResolver {
    
    @Autowired
    private AgentRegistry agentRegistry;
    
    @Override
    public Resolution resolve(Conflict conflict) {
        // 提出候选方案
        List<Proposal> candidates = generateCandidates(conflict);
        
        // 收集投票
        Map<String, String> votes = new HashMap<>();
        for (Agent agent : agentRegistry.getAllAgents()) {
            String vote = agent.vote(candidates, conflict);
            votes.put(agent.getId(), vote);
        }
        
        // 计票
        Map<String, Integer> voteCount = new HashMap<>();
        for (String vote : votes.values()) {
            voteCount.merge(vote, 1, Integer::sum);
        }
        
        // 选出获胜方案
        String winner = voteCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
        
        return Resolution.builder()
            .conflictId(conflict.getId())
            .winningProposal(winner)
            .voteCount(voteCount)
            .build();
    }
    
    private List<Proposal> generateCandidates(Conflict conflict) {
        // 生成候选解决方案
        List<Proposal> candidates = new ArrayList<>();
        
        // 方案1: 优先级
        candidates.add(new Proposal("priority", "按优先级分配"));
        
        // 方案2: 轮流
        candidates.add(new Proposal("round_robin", "轮流使用"));
        
        // 方案3: 时间分割
        candidates.add(new Proposal("time_slice", "时间片分配"));
        
        return candidates;
    }
}

/**
 * 冲突解决管理器
 */
@Service
public class ConflictResolutionManager {
    
    @Autowired
    private ConflictDetector detector;
    
    private final Map<ConflictType, ConflictResolver> resolvers = new HashMap<>();
    
    public ConflictResolutionManager() {
        // 注册解决器
        resolvers.put(ConflictType.RESOURCE, new ArbitrationResolver());
        resolvers.put(ConflictType.GOAL, new NegotiationResolver());
        resolvers.put(ConflictType.INFORMATION, new VotingResolver());
        resolvers.put(ConflictType.TASK, new ArbitrationResolver());
    }
    
    /**
     * 处理冲突
     */
    public void handleConflicts() {
        List<Conflict> conflicts = detector.detectDynamicConflicts();
        
        for (Conflict conflict : conflicts) {
            if (conflict.getStatus() == ConflictStatus.DETECTED) {
                resolve(conflict);
            }
        }
    }
    
    /**
     * 解决单个冲突
     */
    public Resolution resolve(Conflict conflict) {
        conflict.setStatus(ConflictStatus.RESOLVING);
        
        try {
            ConflictResolver resolver = resolvers.get(conflict.getType());
            if (resolver == null) {
                resolver = new ArbitrationResolver(); // 默认使用仲裁
            }
            
            Resolution resolution = resolver.resolve(conflict);
            
            // 执行解决方案
            executeResolution(resolution);
            
            conflict.setStatus(ConflictStatus.RESOLVED);
            
            return resolution;
            
        } catch (Exception e) {
            conflict.setStatus(ConflictStatus.ESCALATED);
            throw new ConflictResolutionException("冲突解决失败", e);
        }
    }
    
    private void executeResolution(Resolution resolution) {
        // 执行解决方案
        // 例如：重新分配资源、调整任务等
    }
}

/**
 * 解决方案
 */
@Data
@Builder
public class Resolution {
    private String conflictId;
    private String winner;
    private String winningProposal;
    private String solution;
    private String reason;
    private Map<String, Object> allocation;
    private Map<String, Integer> voteCount;
    private LocalDateTime resolvedAt;
}
```

## 冲突预防

```mermaid
graph TB
    subgraph "冲突预防策略"
        direction TB
        
        Prevention[冲突预防]
        
        Prevention --> P1[资源预留]
        Prevention --> P2[任务调度优化]
        Prevention --> P3[信息共享]
        Prevention --> P4[协议设计]
        
        P1 --> R1[预分配资源<br/>避免竞争]
        P2 --> R2[合理安排任务<br/>避免冲突]
        P3 --> R3[及时同步信息<br/>避免不一致]
        P4 --> R4[明确使用协议<br/>避免误解]
    end
    
    style Prevention fill:#e8f5e9
```

### 预防策略

1. **资源预留**：提前预留关键资源
2. **任务调度优化**：合理安排任务执行顺序
3. **信息共享**：及时同步状态信息
4. **协议设计**：制定明确的使用协议
5. **监控预警**：实时监控潜在冲突

## 总结

```mermaid
graph TB
    subgraph "冲突管理流程"
        direction TB
        
        Detect[冲突检测]
        Analyze[冲突分析]
        Select[策略选择]
        Resolve[冲突解决]
        Verify[结果验证]
        
        Detect --> Analyze
        Analyze --> Select
        Select --> Resolve
        Resolve --> Verify
        
        Verify -->|成功| Done[完成]
        Verify -->|失败| Escalate[升级处理]
        Escalate --> Resolve
    end
    
    style Detect fill:#e3f2fd
    style Resolve fill:#fff3e0
    style Done fill:#e8f5e9
    style Escalate fill:#ffe0e0
```

有效的冲突管理需要：
1. **及时检测**：快速发现冲突
2. **合理分析**：准确判断冲突类型和严重程度
3. **策略选择**：根据情况选择合适的解决策略
4. **公平解决**：确保解决方案公平合理
5. **持续优化**：总结经验，预防未来冲突
