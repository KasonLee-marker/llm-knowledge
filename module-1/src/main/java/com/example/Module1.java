package com.example;

import com.example.agent.ReActAgent;
import com.example.tool.CalculatorTool;
import com.example.tool.ToolExecutor;
import com.example.tool.WeatherTool;

/**
 * Module 1: Agent Basics - 示例演示
 *
 * 本模块演示 LLM Agent 的核心概念：
 * 1. Agent 接口定义
 * 2. 工具（Tool）封装
 * 3. 简化版 ReAct Agent 实现
 * 4. 简单记忆管理（短期记忆）
 */
public class Module1 {

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
