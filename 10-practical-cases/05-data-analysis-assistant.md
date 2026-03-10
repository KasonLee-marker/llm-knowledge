# 05 - 数据分析助手

## 1. 功能概述

数据分析助手：
- 数据洞察生成
- 可视化建议
- 报告生成
- 异常检测

## 2. Java 实现

```java
@Service
public class DataAnalysisAssistant {
    
    @Autowired
    private ChatClient chatClient;
    
    public String analyze(String dataDescription) {
        String prompt = String.format("""
            分析以下数据并提供洞察：
            %s
            """, dataDescription);
        
        return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
    }
}
```

---

> 至此，所有实战案例已完成
