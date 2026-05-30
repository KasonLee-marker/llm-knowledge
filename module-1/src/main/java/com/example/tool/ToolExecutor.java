package com.example.tool;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** 工具注册与执行中心 */
public class ToolExecutor {
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
