package com.example.tool;

import java.util.Map;

/** 计算器工具 */
public class CalculatorTool implements AgentTool {
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
