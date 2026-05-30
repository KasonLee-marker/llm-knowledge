package com.example.agent;

import com.example.memory.Message;
import com.example.memory.MessageRole;
import com.example.memory.ShortTermMemory;
import com.example.tool.ToolExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简化版 ReAct Agent
 *
 * ReAct 模式：Reasoning（推理）+ Acting（行动）交替进行
 * 本实现模拟 Agent 的决策循环，使用规则替代真实的 LLM 调用
 */
public class ReActAgent {
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
