package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Module1 单元测试
 *
 * 覆盖范围：
 * - WeatherTool：天气查询工具
 * - CalculatorTool：计算器工具
 * - ToolExecutor：工具注册与执行
 * - ShortTermMemory：短期记忆管理
 * - ReActAgent：ReAct Agent 运行流程
 */
class Module1Test {

    // ======================================================================
    // WeatherTool 测试
    // ======================================================================

    @Test
    @DisplayName("WeatherTool - 查询已知城市天气")
    void testWeatherTool_knownCity() {
        Module1.WeatherTool tool = new Module1.WeatherTool();
        Map<String, String> params = new HashMap<>();
        params.put("city", "北京");

        String result = tool.execute(params);

        assertNotNull(result);
        assertTrue(result.contains("北京"), "结果应包含城市名称");
        assertTrue(result.contains("°C"), "结果应包含温度信息");
    }

    @Test
    @DisplayName("WeatherTool - 查询未知城市时返回默认值")
    void testWeatherTool_unknownCity() {
        Module1.WeatherTool tool = new Module1.WeatherTool();
        Map<String, String> params = new HashMap<>();
        params.put("city", "小镇X");

        String result = tool.execute(params);

        assertNotNull(result);
        assertTrue(result.contains("小镇X"), "结果应包含查询的城市名");
    }

    @Test
    @DisplayName("WeatherTool - 不传城市参数时使用默认值")
    void testWeatherTool_defaultCity() {
        Module1.WeatherTool tool = new Module1.WeatherTool();
        Map<String, String> params = new HashMap<>(); // 不传 city 参数

        String result = tool.execute(params);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "结果不应为空");
    }

    @Test
    @DisplayName("WeatherTool - 工具名称和描述正确")
    void testWeatherTool_metadata() {
        Module1.WeatherTool tool = new Module1.WeatherTool();

        assertEquals("get_weather", tool.getName());
        assertNotNull(tool.getDescription());
        assertFalse(tool.getDescription().isEmpty());
    }

    // ======================================================================
    // CalculatorTool 测试
    // ======================================================================

    @Test
    @DisplayName("CalculatorTool - 加法运算")
    void testCalculatorTool_addition() {
        Module1.CalculatorTool tool = new Module1.CalculatorTool();
        Map<String, String> params = new HashMap<>();
        params.put("expression", "3+5");

        String result = tool.execute(params);

        assertTrue(result.contains("8"), "3+5 的结果应为 8");
    }

    @Test
    @DisplayName("CalculatorTool - 减法运算")
    void testCalculatorTool_subtraction() {
        Module1.CalculatorTool tool = new Module1.CalculatorTool();
        Map<String, String> params = new HashMap<>();
        params.put("expression", "10-3");

        String result = tool.execute(params);

        assertTrue(result.contains("7"), "10-3 的结果应为 7");
    }

    @Test
    @DisplayName("CalculatorTool - 乘法运算")
    void testCalculatorTool_multiplication() {
        Module1.CalculatorTool tool = new Module1.CalculatorTool();
        Map<String, String> params = new HashMap<>();
        params.put("expression", "6*7");

        String result = tool.execute(params);

        assertTrue(result.contains("42"), "6*7 的结果应为 42");
    }

    @Test
    @DisplayName("CalculatorTool - 除法运算")
    void testCalculatorTool_division() {
        Module1.CalculatorTool tool = new Module1.CalculatorTool();
        Map<String, String> params = new HashMap<>();
        params.put("expression", "15/3");

        String result = tool.execute(params);

        assertTrue(result.contains("5"), "15/3 的结果应为 5");
    }

    @Test
    @DisplayName("CalculatorTool - 除数为零时返回错误信息")
    void testCalculatorTool_divisionByZero() {
        Module1.CalculatorTool tool = new Module1.CalculatorTool();
        Map<String, String> params = new HashMap<>();
        params.put("expression", "10/0");

        String result = tool.execute(params);

        assertTrue(result.contains("失败") || result.contains("零"),
                "除数为零时应返回错误提示");
    }

    @Test
    @DisplayName("CalculatorTool - 工具名称和描述正确")
    void testCalculatorTool_metadata() {
        Module1.CalculatorTool tool = new Module1.CalculatorTool();

        assertEquals("calculate", tool.getName());
        assertNotNull(tool.getDescription());
        assertFalse(tool.getDescription().isEmpty());
    }

    // ======================================================================
    // ToolExecutor 测试
    // ======================================================================

    private Module1.ToolExecutor toolExecutor;

    @BeforeEach
    void setUp() {
        toolExecutor = new Module1.ToolExecutor();
        toolExecutor.registerTool(new Module1.WeatherTool());
        toolExecutor.registerTool(new Module1.CalculatorTool());
    }

    @Test
    @DisplayName("ToolExecutor - 成功执行已注册的工具")
    void testToolExecutor_executeRegisteredTool() {
        Map<String, String> params = new HashMap<>();
        params.put("city", "上海");

        String result = toolExecutor.execute("get_weather", params);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("ToolExecutor - 执行未注册的工具时返回错误信息")
    void testToolExecutor_executeUnregisteredTool() {
        Map<String, String> params = new HashMap<>();

        String result = toolExecutor.execute("non_existent_tool", params);

        assertTrue(result.contains("错误") || result.contains("不存在"),
                "应返回工具不存在的错误提示");
    }

    @Test
    @DisplayName("ToolExecutor - 检查工具是否存在")
    void testToolExecutor_hasTool() {
        assertTrue(toolExecutor.hasTool("get_weather"));
        assertTrue(toolExecutor.hasTool("calculate"));
        assertFalse(toolExecutor.hasTool("non_existent_tool"));
    }

    @Test
    @DisplayName("ToolExecutor - 获取工具描述列表")
    void testToolExecutor_getToolDescriptions() {
        Map<String, String> descriptions = toolExecutor.getToolDescriptions();

        assertEquals(2, descriptions.size());
        assertTrue(descriptions.containsKey("get_weather"));
        assertTrue(descriptions.containsKey("calculate"));
        assertNotNull(descriptions.get("get_weather"));
        assertNotNull(descriptions.get("calculate"));
    }

    // ======================================================================
    // ShortTermMemory 测试
    // ======================================================================

    @Test
    @DisplayName("ShortTermMemory - 添加消息并正确读取")
    void testShortTermMemory_addAndRetrieve() {
        Module1.ShortTermMemory memory = new Module1.ShortTermMemory(10);
        memory.add(new Module1.Message(Module1.MessageRole.USER, "你好"));
        memory.add(new Module1.Message(Module1.MessageRole.ASSISTANT, "你好！有什么可以帮助您？"));

        List<Module1.Message> messages = memory.getMessages();

        assertEquals(2, messages.size());
        assertEquals(Module1.MessageRole.USER, messages.get(0).getRole());
        assertEquals("你好", messages.get(0).getContent());
    }

    @Test
    @DisplayName("ShortTermMemory - 超出限制时自动淘汰旧消息")
    void testShortTermMemory_evictionWhenFull() {
        Module1.ShortTermMemory memory = new Module1.ShortTermMemory(3);
        // 添加 5 条用户消息，超出限制
        for (int i = 1; i <= 5; i++) {
            memory.add(new Module1.Message(Module1.MessageRole.USER, "消息 " + i));
        }

        // 消息数量应精确等于 maxMessages
        assertEquals(3, memory.size(), "记忆数量应恰好等于最大限制 3");
    }

    @Test
    @DisplayName("ShortTermMemory - clear 保留系统消息")
    void testShortTermMemory_clearKeepsSystemMessages() {
        Module1.ShortTermMemory memory = new Module1.ShortTermMemory(10);
        memory.add(new Module1.Message(Module1.MessageRole.SYSTEM, "系统提示"));
        memory.add(new Module1.Message(Module1.MessageRole.USER, "用户消息"));
        memory.add(new Module1.Message(Module1.MessageRole.ASSISTANT, "助手回复"));

        memory.clear();

        List<Module1.Message> remaining = memory.getMessages();
        assertEquals(1, remaining.size(), "清除后只应保留系统消息");
        assertEquals(Module1.MessageRole.SYSTEM, remaining.get(0).getRole());
    }

    @Test
    @DisplayName("ShortTermMemory - 消息列表不可修改（防御性拷贝）")
    void testShortTermMemory_immutableView() {
        Module1.ShortTermMemory memory = new Module1.ShortTermMemory(10);
        memory.add(new Module1.Message(Module1.MessageRole.USER, "test"));

        List<Module1.Message> messages = memory.getMessages();

        assertThrows(UnsupportedOperationException.class, () ->
                messages.add(new Module1.Message(Module1.MessageRole.USER, "hack")),
                "getMessages() 应返回不可修改的列表");
    }

    // ======================================================================
    // ReActAgent 测试
    // ======================================================================

    @Test
    @DisplayName("ReActAgent - 天气查询流程完成")
    void testReActAgent_weatherQuery() {
        Module1.ReActAgent agent = new Module1.ReActAgent(toolExecutor);

        String result = agent.run("帮我查询北京今天的天气");

        assertNotNull(result);
        assertFalse(result.isEmpty(), "Agent 应返回非空答案");
    }

    @Test
    @DisplayName("ReActAgent - 数学计算流程完成")
    void testReActAgent_calculation() {
        Module1.ReActAgent agent = new Module1.ReActAgent(toolExecutor);

        String result = agent.run("计算 6*7");

        assertNotNull(result);
        assertTrue(result.contains("42"), "计算结果应包含正确答案 42");
    }

    @Test
    @DisplayName("ReActAgent - 普通问答直接返回答案")
    void testReActAgent_generalQuestion() {
        Module1.ReActAgent agent = new Module1.ReActAgent(toolExecutor);

        String result = agent.run("你好，你能做什么？");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("ReActAgent - 执行日志非空")
    void testReActAgent_executionLogNotEmpty() {
        Module1.ReActAgent agent = new Module1.ReActAgent(toolExecutor);
        agent.run("查询上海天气");

        List<String> log = agent.getExecutionLog();

        assertNotNull(log);
        assertFalse(log.isEmpty(), "执行日志不应为空");
    }

    @Test
    @DisplayName("ReActAgent - 运行后记忆包含用户消息")
    void testReActAgent_memoryContainsUserMessage() {
        Module1.ReActAgent agent = new Module1.ReActAgent(toolExecutor);
        String userInput = "帮我查询广州的天气";

        agent.run(userInput);

        List<Module1.Message> messages = agent.getMemory().getMessages();
        boolean hasUserMessage = messages.stream()
                .anyMatch(m -> m.getRole() == Module1.MessageRole.USER &&
                               m.getContent().equals(userInput));
        assertTrue(hasUserMessage, "记忆中应包含用户输入消息");
    }

    // ======================================================================
    // AgentDecision 测试
    // ======================================================================

    @Test
    @DisplayName("AgentDecision - finish 方法创建完成决策")
    void testAgentDecision_finish() {
        Module1.AgentDecision decision = Module1.AgentDecision.finish("推理完成", "最终答案");

        assertTrue(decision.isFinished);
        assertEquals("最终答案", decision.finalAnswer);
        assertNull(decision.toolName);
    }

    @Test
    @DisplayName("AgentDecision - callTool 方法创建工具调用决策")
    void testAgentDecision_callTool() {
        Map<String, String> params = new HashMap<>();
        params.put("city", "北京");
        Module1.AgentDecision decision = Module1.AgentDecision.callTool("需要查询天气", "get_weather", params);

        assertFalse(decision.isFinished);
        assertEquals("get_weather", decision.toolName);
        assertEquals("北京", decision.toolParams.get("city"));
        assertNull(decision.finalAnswer);
    }

    // ======================================================================
    // Message 测试
    // ======================================================================

    @Test
    @DisplayName("Message - toString 格式正确")
    void testMessage_toString() {
        Module1.Message message = new Module1.Message(Module1.MessageRole.USER, "hello");

        String str = message.toString();

        assertTrue(str.contains("USER"));
        assertTrue(str.contains("hello"));
    }
}
