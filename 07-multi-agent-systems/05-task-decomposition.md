# 05 - 任务分解与规划

任务分解与规划是多智能体系统的核心能力。将复杂任务分解为可管理的子任务，并制定合理的执行计划，是确保多智能体系统高效运作的关键。

## 任务分解概述

```mermaid
graph TB
    subgraph "任务分解过程"
        direction TB
        
        CT[复杂任务<br/>Complex Task]
        
        subgraph "分解策略"
            DS1[功能分解<br/>Functional]
            DS2[层次分解<br/>Hierarchical]
            DS3[数据分解<br/>Data-driven]
            DS4[时间分解<br/>Temporal]
        end
        
        ST1[子任务 1]
        ST2[子任务 2]
        ST3[子任务 3]
        ST4[子任务 N]
        
        CT --> DS1
        CT --> DS2
        CT --> DS3
        CT --> DS4
        
        DS1 --> ST1
        DS2 --> ST2
        DS3 --> ST3
        DS4 --> ST4
    end
    
    style CT fill:#ffe0e0
    style ST1 fill:#e3f2fd
    style ST2 fill:#e3f2fd
    style ST3 fill:#e3f2fd
    style ST4 fill:#e3f2fd
```

## 任务分解策略

### 1. 功能分解（Functional Decomposition）

按功能模块将任务分解为独立的子任务。

```mermaid
graph TB
    subgraph "功能分解示例：开发电商系统"
        CT[开发电商系统]
        
        CT --> F1[用户模块]
        CT --> F2[商品模块]
        CT --> F3[订单模块]
        CT --> F4[支付模块]
        CT --> F5[物流模块]
        
        F1 --> F1_1[用户注册]
        F1 --> F1_2[用户登录]
        F1 --> F1_3[个人信息管理]
        
        F2 --> F2_1[商品展示]
        F2 --> F2_2[商品搜索]
        F2 --> F2_3[库存管理]
        
        F3 --> F3_1[购物车]
        F3 --> F3_2[订单创建]
        F3 --> F3_3[订单查询]
        
        F4 --> F4_1[支付方式]
        F4 --> F4_2[支付回调]
        F4 --> F4_3[退款处理]
        
        F5 --> F5_1[物流跟踪]
        F5 --> F5_2[配送管理]
    end
    
    style CT fill:#ffe0e0
    style F1 fill:#e3f2fd
    style F2 fill:#e3f2fd
    style F3 fill:#e3f2fd
    style F4 fill:#e3f2fd
    style F5 fill:#e3f2fd
```

### 2. 层次分解（Hierarchical Decomposition）

按抽象层次将任务分解为不同级别的子任务。

```mermaid
graph TB
    subgraph "层次分解示例：撰写研究报告"
        L0[撰写研究报告]
        
        L0 --> L1_1[确定主题]
        L0 --> L1_2[收集资料]
        L0 --> L1_3[撰写内容]
        L0 --> L1_4[审校发布]
        
        L1_1 --> L2_1[市场调研]
        L1_1 --> L2_2[确定研究问题]
        
        L1_2 --> L2_3[文献检索]
        L1_2 --> L2_4[数据收集]
        L1_2 --> L2_5[资料整理]
        
        L1_3 --> L2_6[撰写摘要]
        L1_3 --> L2_7[撰写正文]
        L1_3 --> L2_8[撰写结论]
        
        L1_4 --> L2_9[内容审校]
        L1_4 --> L2_10[格式调整]
        L1_4 --> L2_11[发布分享]
        
        L2_7 --> L3_1[引言]
        L2_7 --> L3_2[方法]
        L2_7 --> L3_3[结果]
        L2_7 --> L3_4[讨论]
    end
    
    style L0 fill:#ffe0e0
    style L1_1 fill:#fff3e0
    style L1_2 fill:#fff3e0
    style L1_3 fill:#fff3e0
    style L1_4 fill:#fff3e0
    style L2_1 fill:#e3f2fd
    style L2_2 fill:#e3f2fd
    style L2_3 fill:#e3f2fd
    style L2_4 fill:#e3f2fd
    style L2_5 fill:#e3f2fd
    style L2_6 fill:#e3f2fd
    style L2_7 fill:#e3f2fd
    style L2_8 fill:#e3f2fd
    style L2_9 fill:#e3f2fd
    style L2_10 fill:#e3f2fd
    style L2_11 fill:#e3f2fd
```

### 3. 数据分解（Data-driven Decomposition）

按数据维度将任务分解为可并行处理的子任务。

```mermaid
graph TB
    subgraph "数据分解示例：批量数据处理"
        DT[处理100万条用户数据]
        
        DT --> D1[分区1<br/>0-25万条]
        DT --> D2[分区2<br/>25-50万条]
        DT --> D3[分区3<br/>50-75万条]
        DT --> D4[分区4<br/>75-100万条]
        
        D1 --> P1[并行处理]
        D2 --> P1
        D3 --> P1
        D4 --> P1
        
        P1 --> M[合并结果]
    end
    
    style DT fill:#ffe0e0
    style D1 fill:#e3f2fd
    style D2 fill:#e3f2fd
    style D3 fill:#e3f2fd
    style D4 fill:#e3f2fd
    style P1 fill:#fff3e0
    style M fill:#e8f5e9
```

### 4. 时间分解（Temporal Decomposition）

按时间顺序将任务分解为阶段性的子任务。

```mermaid
graph LR
    subgraph "时间分解示例：产品发布"
        direction LR
        
        T1[阶段1<br/>需求分析<br/>Week 1-2]
        T2[阶段2<br/>设计开发<br/>Week 3-6]
        T3[阶段3<br/>测试优化<br/>Week 7-8]
        T4[阶段4<br/>发布上线<br/>Week 9]
        T5[阶段5<br/>运营维护<br/>Week 10+]
        
        T1 --> T2 --> T3 --> T4 --> T5
    end
    
    style T1 fill:#e3f2fd
    style T2 fill:#fff3e0
    style T3 fill:#e8f5e9
    style T4 fill:#f3e5f5
    style T5 fill:#fce4ec
```

## 任务依赖关系

### 依赖类型

```mermaid
graph TB
    subgraph "任务依赖类型"
        direction TB
        
        subgraph "顺序依赖"
            S1[任务A] --> S2[任务B]
            %% 任务B必须在任务A完成后开始
        end
        
        subgraph "并行依赖"
            P0[任务A] --> P1[任务B]
            P0 --> P2[任务C]
            %% 任务B和C可以并行执行
        end
        
        subgraph "汇聚依赖"
            C1[任务A] --> C3[任务C]
            C2[任务B] --> C3
            %% 任务C需要等待A和B都完成
        end
        
        subgraph "条件依赖"
            CD1{条件判断}
            CD1 -->|条件1| CD2[任务A]
            CD1 -->|条件2| CD3[任务B]
            %% 根据条件选择执行路径
        end
    end
```

### 依赖关系图示例

```mermaid
graph TB
    subgraph "软件开发任务依赖图"
        T1[需求分析]
        T2[架构设计]
        T3[数据库设计]
        T4[API开发]
        T5[前端开发]
        T6[单元测试]
        T7[集成测试]
        T8[部署上线]
        
        T1 --> T2
        T2 --> T3
        T2 --> T4
        T2 --> T5
        T3 --> T4
        T4 --> T6
        T5 --> T6
        T6 --> T7
        T7 --> T8
    end
    
    style T1 fill:#e3f2fd
    style T2 fill:#fff3e0
    style T3 fill:#fff3e0
    style T4 fill:#e8f5e9
    style T5 fill:#e8f5e9
    style T6 fill:#f3e5f5
    style T7 fill:#f3e5f5
    style T8 fill:#ffe0e0
```

## 规划算法

### 1. 基于规则的规划

使用预定义规则进行任务规划。

```mermaid
flowchart TD
    Start[开始规划] --> Analyze[分析任务]
    
    Analyze --> Type{任务类型}
    
    Type -->|开发任务| DevRule[开发规则]
    Type -->|分析任务| AnalysisRule[分析规则]
    Type -->|写作任务| WritingRule[写作规则]
    
    DevRule --> DevPlan[设计→开发→测试→部署]
    AnalysisRule --> AnalysisPlan[收集→分析→报告]
    WritingRule --> WritingPlan[大纲→草稿→修订→发布]
    
    DevPlan --> End[输出计划]
    AnalysisPlan --> End
    WritingPlan --> End
```

### 2. 基于 LLM 的规划

利用大语言模型的推理能力进行任务规划。

```mermaid
sequenceDiagram
    participant Planner as 规划Agent
    participant LLM as LLM
    participant Executor as 执行Agent
    
    Planner->>Planner: 接收复杂任务
    
    Planner->>LLM: 提示词：<br/>"请将以下任务分解为子任务：<br/>[任务描述]"
    
    LLM->>LLM: 推理分析
    LLM-->>Planner: 返回子任务列表<br/>1. 子任务A<br/>2. 子任务B<br/>3. 子任务C
    
    Planner->>Planner: 解析并验证子任务
    
    loop 迭代优化
        Planner->>LLM: "请检查并优化任务分解"
        LLM-->>Planner: 优化后的子任务
    end
    
    Planner->>Planner: 构建执行计划
    Planner->>Executor: 分发子任务
```

### 3. ReAct 规划

结合推理（Reasoning）和行动（Acting）的规划方法。

```mermaid
graph LR
    subgraph "ReAct 循环"
        direction TB
        
        Thought[思考<br/>Thought]
        Action[行动<br/>Action]
        Observation[观察<br/>Observation]
        
        Thought --> Action
        Action --> Observation
        Observation --> Thought
    end
    
    style Thought fill:#e3f2fd
    style Action fill:#fff3e0
    style Observation fill:#e8f5e9
```

### 4. 树状搜索规划

使用树状搜索算法（如 MCTS）进行任务规划。

```mermaid
graph TB
    subgraph "树状搜索规划"
        Root[初始状态]
        
        Root --> A1[动作A]
        Root --> A2[动作B]
        Root --> A3[动作C]
        
        A1 --> B1[动作A1]
        A1 --> B2[动作A2]
        
        A2 --> B3[动作B1]
        A2 --> B4[动作B2]
        
        B1 --> C1[目标状态<br/>奖励: 100]
        B2 --> C2[目标状态<br/>奖励: 80]
        B3 --> C3[目标状态<br/>奖励: 90]
        B4 --> C4[中间状态]
        
        C4 --> D1[动作...]
    end
    
    style Root fill:#ffe0e0
    style C1 fill:#e8f5e9
    style C2 fill:#e8f5e9
    style C3 fill:#e8f5e9
```

## Java 实现示例

### 任务分解器

```java
/**
 * 任务分解器接口
 */
public interface TaskDecomposer {
    List<SubTask> decompose(Task task);
}

/**
 * 基于 LLM 的任务分解器
 */
@Service
public class LLMTaskDecomposer implements TaskDecomposer {
    
    @Autowired
    private ChatClient chatClient;
    
    private static final String DECOMPOSITION_PROMPT = """
        请将以下复杂任务分解为具体的子任务。
        
        任务描述：%s
        
        要求：
        1. 每个子任务应该是可独立执行的
        2. 子任务之间如果有依赖关系，请明确说明
        3. 为每个子任务分配预估工作量（小时）
        4. 指定每个子任务需要的技能
        
        请以 JSON 格式返回：
        {
            "subTasks": [
                {
                    "id": "任务ID",
                    "description": "任务描述",
                    "estimatedHours": 预估工时,
                    "requiredSkills": ["技能1", "技能2"],
                    "dependencies": ["依赖任务ID"]
                }
            ]
        }
        """;
    
    @Override
    public List<SubTask> decompose(Task task) {
        String prompt = String.format(DECOMPOSITION_PROMPT, task.getDescription());
        
        String response = chatClient.call(prompt);
        
        // 解析 JSON 响应
        DecompositionResult result = parseJson(response);
        
        // 构建子任务列表
        return result.getSubTasks().stream()
            .map(this::convertToSubTask)
            .collect(Collectors.toList());
    }
    
    private DecompositionResult parseJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, DecompositionResult.class);
        } catch (JsonProcessingException e) {
            throw new DecompositionException("解析分解结果失败", e);
        }
    }
}

/**
 * 基于规则的分解器
 */
@Component
public class RuleBasedDecomposer implements TaskDecomposer {
    
    private final Map<String, DecompositionRule> rules = new HashMap<>();
    
    public RuleBasedDecomposer() {
        // 初始化规则
        rules.put("software_development", new SoftwareDevelopmentRule());
        rules.put("data_analysis", new DataAnalysisRule());
        rules.put("content_creation", new ContentCreationRule());
    }
    
    @Override
    public List<SubTask> decompose(Task task) {
        DecompositionRule rule = rules.get(task.getType());
        if (rule == null) {
            throw new UnsupportedTaskTypeException(task.getType());
        }
        return rule.apply(task);
    }
}

/**
 * 软件开发分解规则
 */
public class SoftwareDevelopmentRule implements DecompositionRule {
    
    @Override
    public List<SubTask> apply(Task task) {
        List<SubTask> subTasks = new ArrayList<>();
        
        // 需求分析
        subTasks.add(SubTask.builder()
            .id(task.getId() + "_1")
            .description("需求分析和文档编写")
            .estimatedHours(8)
            .requiredSkills(Arrays.asList("需求分析", "文档编写"))
            .dependencies(Collections.emptyList())
            .build());
        
        // 架构设计
        subTasks.add(SubTask.builder()
            .id(task.getId() + "_2")
            .description("系统架构设计")
            .estimatedHours(16)
            .requiredSkills(Arrays.asList("架构设计", "技术选型"))
            .dependencies(Arrays.asList(task.getId() + "_1"))
            .build());
        
        // 开发实现
        subTasks.add(SubTask.builder()
            .id(task.getId() + "_3")
            .description("代码开发和单元测试")
            .estimatedHours(40)
            .requiredSkills(Arrays.asList("编程", "测试"))
            .dependencies(Arrays.asList(task.getId() + "_2"))
            .build());
        
        // 集成测试
        subTasks.add(SubTask.builder()
            .id(task.getId() + "_4")
            .description("集成测试和Bug修复")
            .estimatedHours(16)
            .requiredSkills(Arrays.asList("测试", "调试"))
            .dependencies(Arrays.asList(task.getId() + "_3"))
            .build());
        
        // 部署上线
        subTasks.add(SubTask.builder()
            .id(task.getId() + "_5")
            .description("部署和上线")
            .estimatedHours(8)
            .requiredSkills(Arrays.asList("DevOps", "运维"))
            .dependencies(Arrays.asList(task.getId() + "_4"))
            .build());
        
        return subTasks;
    }
}
```

### 任务规划器

```java
/**
 * 任务规划器
 */
@Service
public class TaskPlanner {
    
    @Autowired
    private TaskDecomposer decomposer;
    
    @Autowired
    private AgentRegistry agentRegistry;
    
    /**
     * 创建执行计划
     */
    public ExecutionPlan createPlan(Task task) {
        // 1. 任务分解
        List<SubTask> subTasks = decomposer.decompose(task);
        
        // 2. 构建依赖图
        DependencyGraph graph = buildDependencyGraph(subTasks);
        
        // 3. 拓扑排序
        List<SubTask> orderedTasks = topologicalSort(graph);
        
        // 4. 分配 Agent
        Map<String, Agent> assignments = assignAgents(orderedTasks);
        
        // 5. 生成执行计划
        return ExecutionPlan.builder()
            .taskId(task.getId())
            .subTasks(orderedTasks)
            .assignments(assignments)
            .estimatedDuration(calculateDuration(orderedTasks))
            .build();
    }
    
    /**
     * 构建依赖图
     */
    private DependencyGraph buildDependencyGraph(List<SubTask> subTasks) {
        DependencyGraph graph = new DependencyGraph();
        
        // 添加节点
        for (SubTask subTask : subTasks) {
            graph.addNode(subTask);
        }
        
        // 添加边
        for (SubTask subTask : subTasks) {
            for (String depId : subTask.getDependencies()) {
                SubTask dependency = findSubTask(subTasks, depId);
                if (dependency != null) {
                    graph.addEdge(dependency, subTask);
                }
            }
        }
        
        return graph;
    }
    
    /**
     * 拓扑排序
     */
    private List<SubTask> topologicalSort(DependencyGraph graph) {
        List<SubTask> result = new ArrayList<>();
        Set<SubTask> visited = new HashSet<>();
        Set<SubTask> visiting = new HashSet<>();
        
        for (SubTask node : graph.getNodes()) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, visiting, result);
            }
        }
        
        return result;
    }
    
    private void dfs(SubTask node, DependencyGraph graph, 
                     Set<SubTask> visited, Set<SubTask> visiting,
                     List<SubTask> result) {
        if (visiting.contains(node)) {
            throw new CircularDependencyException("检测到循环依赖");
        }
        
        if (visited.contains(node)) {
            return;
        }
        
        visiting.add(node);
        
        for (SubTask neighbor : graph.getNeighbors(node)) {
            dfs(neighbor, graph, visited, visiting, result);
        }
        
        visiting.remove(node);
        visited.add(node);
        result.add(0, node);
    }
    
    /**
     * 分配 Agent
     */
    private Map<String, Agent> assignAgents(List<SubTask> subTasks) {
        Map<String, Agent> assignments = new HashMap<>();
        
        for (SubTask subTask : subTasks) {
            Agent bestAgent = findBestAgent(subTask);
            assignments.put(subTask.getId(), bestAgent);
        }
        
        return assignments;
    }
    
    private Agent findBestAgent(SubTask subTask) {
        return agentRegistry.getAvailableAgents().stream()
            .filter(agent -> hasRequiredSkills(agent, subTask.getRequiredSkills()))
            .min(Comparator.comparingInt(this::getAgentLoad))
            .orElseThrow(() -> new NoSuitableAgentException(subTask.getId()));
    }
    
    private boolean hasRequiredSkills(Agent agent, List<String> requiredSkills) {
        return agent.getSkills().containsAll(requiredSkills);
    }
    
    private int getAgentLoad(Agent agent) {
        return agent.getAssignedTasks().size();
    }
    
    private Duration calculateDuration(List<SubTask> subTasks) {
        int totalHours = subTasks.stream()
            .mapToInt(SubTask::getEstimatedHours)
            .sum();
        return Duration.ofHours(totalHours);
    }
}

/**
 * 执行计划
 */
@Data
@Builder
public class ExecutionPlan {
    private String taskId;
    private List<SubTask> subTasks;
    private Map<String, Agent> assignments;
    private Duration estimatedDuration;
    private LocalDateTime createdAt;
    
    public List<SubTask> getExecutableTasks() {
        return subTasks.stream()
            .filter(task -> task.getStatus() == TaskStatus.PENDING)
            .filter(task -> task.getDependencies().stream()
                .allMatch(depId -> isTaskCompleted(depId)))
            .collect(Collectors.toList());
    }
    
    private boolean isTaskCompleted(String taskId) {
        return subTasks.stream()
            .filter(t -> t.getId().equals(taskId))
            .anyMatch(t -> t.getStatus() == TaskStatus.COMPLETED);
    }
}
```

### 动态规划调整

```java
/**
 * 动态规划器
 */
@Service
public class DynamicPlanner {
    
    @Autowired
    private TaskPlanner taskPlanner;
    
    /**
     * 根据执行反馈调整计划
     */
    public ExecutionPlan adjustPlan(ExecutionPlan currentPlan, ExecutionFeedback feedback) {
        if (feedback.isOnTrack()) {
            return currentPlan;
        }
        
        // 重新评估剩余任务
        List<SubTask> remainingTasks = currentPlan.getSubTasks().stream()
            .filter(t -> t.getStatus() != TaskStatus.COMPLETED)
            .collect(Collectors.toList());
        
        // 根据反馈调整
        for (SubTask task : remainingTasks) {
            if (feedback.hasIssue(task.getId())) {
                // 增加预估时间
                task.setEstimatedHours((int) (task.getEstimatedHours() * 1.5));
                
                // 重新分配 Agent
                if (feedback.needsReassignment(task.getId())) {
                    Agent newAgent = findAlternativeAgent(task, currentPlan);
                    currentPlan.getAssignments().put(task.getId(), newAgent);
                }
            }
        }
        
        // 重新计算时间线
        recalculateTimeline(currentPlan);
        
        return currentPlan;
    }
    
    /**
     * 处理任务失败
     */
    public ExecutionPlan handleFailure(ExecutionPlan plan, String failedTaskId, String reason) {
        SubTask failedTask = plan.getSubTasks().stream()
            .filter(t -> t.getId().equals(failedTaskId))
            .findFirst()
            .orElseThrow();
        
        // 标记失败
        failedTask.setStatus(TaskStatus.FAILED);
        
        // 分析失败原因
        if (isRetryable(reason)) {
            // 重新分配并重试
            Agent newAgent = findAlternativeAgent(failedTask, plan);
            plan.getAssignments().put(failedTaskId, newAgent);
            failedTask.setStatus(TaskStatus.PENDING);
        } else {
            // 需要重新分解
            List<SubTask> replacementTasks = decomposeAlternative(failedTask, reason);
            
            // 更新计划
            plan.getSubTasks().remove(failedTask);
            plan.getSubTasks().addAll(replacementTasks);
            
            // 重新规划
            return taskPlanner.createPlan(new Task(plan.getTaskId(), "调整后的任务"));
        }
        
        return plan;
    }
    
    private boolean isRetryable(String reason) {
        // 判断失败是否可重试
        return !reason.contains("逻辑错误") && !reason.contains("需求不清");
    }
    
    private List<SubTask> decomposeAlternative(SubTask task, String reason) {
        // 根据失败原因重新分解任务
        // 实现逻辑...
        return Collections.emptyList();
    }
    
    private Agent findAlternativeAgent(SubTask task, ExecutionPlan plan) {
        Agent currentAgent = plan.getAssignments().get(task.getId());
        // 寻找其他可用 Agent
        return null;
    }
    
    private void recalculateTimeline(ExecutionPlan plan) {
        // 重新计算时间线
        // 实现逻辑...
    }
}
```

## 规划策略选择

```mermaid
flowchart TD
    Start[选择规划策略] --> Analyze{任务特点}
    
    Analyze -->|结构化| RuleBased[基于规则]
    Analyze -->|不确定性高| LLMBased[基于LLM]
    Analyze -->|需要优化| SearchBased[基于搜索]
    Analyze -->|动态环境| Hybrid[混合策略]
    
    RuleBased --> R1[预定义流程<br/>可预测]
    LLMBased --> L1[灵活适应<br/>智能推理]
    SearchBased --> S1[最优解<br/>复杂决策]
    Hybrid --> H1[规则+LLM<br/>动态调整]
    
    style RuleBased fill:#e3f2fd
    style LLMBased fill:#fff3e0
    style SearchBased fill:#e8f5e9
    style Hybrid fill:#f3e5f5
```

## 最佳实践

1. **分层规划**：高层规划使用 LLM，低层规划使用规则
2. **迭代优化**：根据执行反馈持续优化计划
3. **依赖管理**：明确任务依赖，避免循环依赖
4. **资源预估**：合理预估任务资源需求
5. **容错设计**：设计失败重试和替代方案
6. **可视化**：提供规划结果的可视化展示
