# 04 - SQL 生成助手

## 1. 功能概述

Text2SQL 助手：
- 自然语言转 SQL
- Schema 理解
- SQL 验证
- 结果解释

## 2. Java 实现

```java
@Service
public class SQLAssistant {
    
    @Autowired
    private ChatClient chatClient;
    
    public String generateSQL(String question, String schema) {
        String prompt = String.format("""
            数据库Schema：
            %s
            
            用户问题：%s
            
            请生成对应的SQL查询：
            """, schema, question);
        
        return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
    }
}
```

---

> 更多实战案例见其他文档
