package com.example;

import java.util.*;

/**
 * Module 1: Agent Basics - 示例代码
 *
 * 本模块演示 LLM Agent 的核心概念：
 * 1. Agent 接口定义
 * 2. 工具（Tool）封装
 * 3. 简化版 ReAct Agent 实现
 * 4. 简单记忆管理（短期记忆）
 */
public class Module1 {

    // ======================================================================
    // 1. 消息类型定义
    // ======================================================================

    /** 消息角色枚举 */
    public enum MessageRole {
        SYSTEM, USER, ASSISTANT, TOOL
    }

    /** 对话消息 */
    public static class Message {
        private final MessageRole role;
        private final String content;
        private final String toolCallId; // 工具调用结果时使用

        public Message(MessageRole role, String content) {
            this(role, content, null);
        }

        public Message(MessageRole role, String content, String toolCallId) {
            this.role = role;
            this.content = content;
            this.toolCallId = toolCallId;
        }

        public MessageRole getRole() { return role; }
        public String getContent() { return content; }
        public String getToolCallId() { return toolCallId; }

        @Override
        public String toString() {
            return "[" + role + "] " + content;
        }
    }

    // ======================================================================
    // 2. 工具接口与实现
    // ======================================================================

    /** Agent 工具的统一接口 */
    public interface AgentTool {
        String getName();
        String getDescription();
        String execute(Map<String, String> parameters);
    }

    /** 天气查询工具（模拟实现） */
    public static class WeatherTool implements AgentTool {
        @Override
        public String getName() {
            return "get_weather";
        }

        @Override
        public String getDescription() {
            return "查询指定城市的当前天气情况，参数：city（城市名称）";
        }

        @Override
        public String execute(Map<String, String> parameters) {
            String city = parameters.getOrDefault("city", "未知城市");
            // 模拟天气数据（实际应调用天气 API）
            Map<String, String> mockWeather = new HashMap<>();
            mockWeather.put("北京", "北京：晴天，气温 22°C，湿度 45%，风速 10km/h");
            mockWeather.put("上海", "上海：多云，气温 25°C，湿度 68%，风速 15km/h");
            mockWeather.put("广州", "广州：阵雨，气温 28°C，湿度 80%，风速 8km/h");
            return mockWeather.getOrDefault(city, city + "：晴天，气温 20°C，湿度 50%");
        }
    }

    /** 计算器工具 */
    public static class CalculatorTool implements AgentTool {
        @Override
        public String getName() {
            return "calculate";
        }

        @Override
        public String getDescription() {
            return "执行数学计算，参数：expression（数学表达式，支持加减乘除）";
        }

        @Override
        public String execute(Map<String, String> parameters) {
            String expression = parameters.getOrDefault("expression", "0");
            try {
                double result = evaluateExpression(expression);
                return String.format("计算结果：%s = %.2f", expression, result);
            } catch (Exception e) {
                return "计算失败：" + e.getMessage();
            }
        }

        /** 简单的表达式求值（仅支持两个操作数的四则运算） */
        private double evaluateExpression(String expression) {
            expression = expression.trim().replaceAll("\\s+", "");
            if (expression.contains("+")) {
                String[] parts = expression.split("\\+", 2);
                return Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
            } else if (expression.contains("-")) {
                String[] parts = expression.split("-", 2);
                return Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
            } else if (expression.contains("*")) {
                String[] parts = expression.split("\\*", 2);
                return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
            } else if (expression.contains("/")) {
                String[] parts = expression.split("/", 2);
                double divisor = Double.parseDouble(parts[1]);
                if (divisor == 0) throw new ArithmeticException("除数不能为零");
                return Double.parseDouble(parts[0]) / divisor;
            }
            return Double.parseDouble(expression);
        }
    }

    // ======================================================================
    // 3. 工具执行器
    // ======================================================================

    /** 工具注册与执行中心 */
    public static class ToolExecutor {
        private final Map<String, AgentTool> tools = new HashMap<>();

        public void registerTool(AgentTool tool) {
            tools.put(tool.getName(), tool);
        }

        public String execute(String toolName, Map<String, String> parameters) {
            AgentTool tool = tools.get(toolName);
            if (tool == null) {
                return "错误：工具 '" + toolName + "' 不存在。可用工具：" + tools.keySet();
            }
            return tool.execute(parameters);
        }

        public Map<String, String> getToolDescriptions() {
            Map<String, String> descriptions = new LinkedHashMap<>();
            tools.forEach((name, tool) -> descriptions.put(name, tool.getDescription()));
            return descriptions;
        }

        public boolean hasTool(String name) {
            return tools.containsKey(name);
        }
    }

    // ======================================================================
    // 4. 短期记忆管理
    // ======================================================================

    /** Agent 短期记忆（对话历史） */
    public static class ShortTermMemory {
        private final List<Message> messages = new ArrayList<>();
        private final int maxMessages;

        public ShortTermMemory(int maxMessages) {
            this.maxMessages = maxMessages;
        }

        public void add(Message message) {
            messages.add(message);
            // 超出限制时删除最早的非系统消息；若全为系统消息则直接退出
            while (messages.size() > maxMessages) {
                boolean removed = false;
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getRole() != MessageRole.SYSTEM) {
                        messages.remove(i);
                        removed = true;
                        break;
                    }
                }
                if (!removed) {
                    break; // 只有系统消息时不做淘汰，防止无限循环
                }
            }
        }

        public List<Message> getMessages() {
            return Collections.unmodifiableList(messages);
        }

        public void clear() {
            // 保留系统消息
            messages.removeIf(m -> m.getRole() != MessageRole.SYSTEM);
        }

        public int size() {
            return messages.size();
        }
    }

    // ======================================================================
    // 5. 简化版 ReAct Agent
    // ======================================================================

    /**
     * 简化版 ReAct Agent
     *
     * ReAct 模式：Reasoning（推理）+ Acting（行动）交替进行
     * 本实现模拟 Agent 的决策循环，使用规则替代真实的 LLM 调用
     */
    public static class ReActAgent {
        private static final int MAX_STEPS = 5;
        private final ToolExecutor toolExecutor;
        private final ShortTermMemory memory;
        private final List<String> executionLog = new ArrayList<>();

        public ReActAgent(ToolExecutor toolExecutor) {
            this.toolExecutor = toolExecutor;
            this.memory = new ShortTermMemory(20);
        }

        /**
         * 运行 Agent，处理用户输入
         *
         * @param userInput 用户的自然语言输入
         * @return Agent 的最终回答
         */
        public String run(String userInput) {
            executionLog.clear();
            memory.clear();

            log("=== Agent 开始运行 ===");
            log("用户输入：" + userInput);
            memory.add(new Message(MessageRole.USER, userInput));

            for (int step = 1; step <= MAX_STEPS; step++) {
                log("\n--- 步骤 " + step + " ---");

                // 模拟推理：决定下一步行动
                AgentDecision decision = think(userInput, step);
                log("推理：" + decision.thought);

                if (decision.isFinished) {
                    log("行动：生成最终答案");
                    log("=== Agent 完成 ===");
                    memory.add(new Message(MessageRole.ASSISTANT, decision.finalAnswer));
                    return decision.finalAnswer;
                }

                // 执行工具调用
                log("行动：调用工具 " + decision.toolName + "，参数：" + decision.toolParams);
                String toolResult = toolExecutor.execute(decision.toolName, decision.toolParams);
                log("观察：" + toolResult);

                // 将工具结果加入记忆
                memory.add(new Message(MessageRole.TOOL, toolResult, "call_" + step));
            }

            String fallback = "抱歉，我在 " + MAX_STEPS + " 步内未能完成任务，请重新描述您的需求。";
            log("=== Agent 达到最大步骤限制 ===");
            return fallback;
        }

        /**
         * 模拟推理过程（真实场景中这里会调用 LLM API）
         */
        private AgentDecision think(String userInput, int step) {
            String lowerInput = userInput.toLowerCase();

            // 检测天气查询意图
            if (lowerInput.contains("天气")) {
                if (step == 1) {
                    String city = extractCity(userInput);
                    Map<String, String> params = new HashMap<>();
                    params.put("city", city);
                    return AgentDecision.callTool(
                            "我需要查询 " + city + " 的天气信息",
                            "get_weather", params);
                } else {
                    // 获取上一步工具结果
                    String lastToolResult = getLastToolResult();
                    return AgentDecision.finish(
                            "已获取天气信息，可以回答用户了",
                            "根据查询结果：" + lastToolResult);
                }
            }

            // 检测计算意图
            if (lowerInput.contains("计算") || lowerInput.contains("等于") || hasNumbers(userInput)) {
                if (step == 1) {
                    String expression = extractExpression(userInput);
                    Map<String, String> params = new HashMap<>();
                    params.put("expression", expression);
                    return AgentDecision.callTool(
                            "用户需要计算数学表达式：" + expression,
                            "calculate", params);
                } else {
                    String lastToolResult = getLastToolResult();
                    return AgentDecision.finish(
                            "计算完成，返回结果",
                            lastToolResult);
                }
            }

            // 默认：直接回答
            return AgentDecision.finish(
                    "这个问题不需要调用工具，可以直接回答",
                    "您好！我是一个 AI Agent 示例。您的问题是：\"" + userInput + "\"。" +
                    "我可以帮您查询天气或进行数学计算，请尝试说'北京天气'或'计算 3+5'。");
        }

        private String extractCity(String input) {
            String[] cities = {"北京", "上海", "广州", "深圳", "杭州"};
            for (String city : cities) {
                if (input.contains(city)) return city;
            }
            return "北京"; // 默认城市
        }

        private boolean hasNumbers(String input) {
            return input.matches(".*\\d+.*[+\\-*/].*\\d+.*");
        }

        private String extractExpression(String input) {
            // 提取数字和运算符组成的表达式
            return input.replaceAll("[^0-9+\\-*/.]", "").trim();
        }

        private String getLastToolResult() {
            List<Message> messages = memory.getMessages();
            for (int i = messages.size() - 1; i >= 0; i--) {
                if (messages.get(i).getRole() == MessageRole.TOOL) {
                    return messages.get(i).getContent();
                }
            }
            return "";
        }

        private void log(String message) {
            executionLog.add(message);
            System.out.println(message);
        }

        public List<String> getExecutionLog() {
            return Collections.unmodifiableList(executionLog);
        }

        public ShortTermMemory getMemory() {
            return memory;
        }
    }

    /** Agent 决策结果 */
    public static class AgentDecision {
        public final String thought;        // 推理过程
        public final boolean isFinished;    // 是否完成任务
        public final String finalAnswer;    // 最终答案（isFinished=true 时有效）
        public final String toolName;       // 工具名称（isFinished=false 时有效）
        public final Map<String, String> toolParams; // 工具参数

        private AgentDecision(String thought, boolean isFinished, String finalAnswer,
                              String toolName, Map<String, String> toolParams) {
            this.thought = thought;
            this.isFinished = isFinished;
            this.finalAnswer = finalAnswer;
            this.toolName = toolName;
            this.toolParams = toolParams;
        }

        public static AgentDecision finish(String thought, String answer) {
            return new AgentDecision(thought, true, answer, null, null);
        }

        public static AgentDecision callTool(String thought, String toolName, Map<String, String> params) {
            return new AgentDecision(thought, false, null, toolName, params);
        }
    }

    // ======================================================================
    // 6. 主程序演示
    // ======================================================================

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("Module 1: Agent Basics 示例演示");
        System.out.println("======================================\n");

        // 初始化工具
        ToolExecutor toolExecutor = new ToolExecutor();
        toolExecutor.registerTool(new WeatherTool());
        toolExecutor.registerTool(new CalculatorTool());

        // 创建 ReAct Agent
        ReActAgent agent = new ReActAgent(toolExecutor);

        // 演示场景 1：天气查询
        System.out.println("场景 1：天气查询");
        String result1 = agent.run("帮我查询北京今天的天气");
        System.out.println("\n最终答案：" + result1);

        System.out.println("\n" + "=".repeat(40) + "\n");

        // 演示场景 2：数学计算
        System.out.println("场景 2：数学计算");
        String result2 = agent.run("计算 125*8");
        System.out.println("\n最终答案：" + result2);

        System.out.println("\n" + "=".repeat(40) + "\n");

        // 演示场景 3：普通问答
        System.out.println("场景 3：普通问答");
        String result3 = agent.run("你好，你能做什么？");
        System.out.println("\n最终答案：" + result3);
    }
}
