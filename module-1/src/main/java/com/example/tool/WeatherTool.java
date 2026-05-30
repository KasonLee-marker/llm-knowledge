package com.example.tool;

import java.util.HashMap;
import java.util.Map;

/** 天气查询工具（模拟实现） */
public class WeatherTool implements AgentTool {
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
