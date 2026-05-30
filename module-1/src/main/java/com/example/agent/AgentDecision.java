package com.example.agent;

import java.util.Map;

/** Agent 决策结果 */
public class AgentDecision {
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
