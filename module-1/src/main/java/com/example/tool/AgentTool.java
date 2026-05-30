package com.example.tool;

import java.util.Map;

/** Agent 工具的统一接口 */
public interface AgentTool {
    String getName();
    String getDescription();
    String execute(Map<String, String> parameters);
}
