# 01 - 代码生成助手

## 1. 功能概述

代码生成助手帮助开发者：
- 代码补全
- 代码审查
- 代码重构
- 生成单元测试

## 2. 架构设计

```mermaid
flowchart TD
    A[用户输入] --> B[意图识别]
    B --> C{任务类型}
    C -->|补全| D[代码补全]
    C -->|审查| E[代码审查]
    C -->|重构| F[代码重构]
    C -->|测试| G[生成测试]
    D --> H[输出结果]
    E --> H
    F --> H
    G --> H
```

## 3. Java 实现

```java
@Service
public class CodeAssistantService {
    
    @Autowired
    private ChatClient chatClient;
    
    public String completeCode(String context, String partial) {
        String prompt = String.format("""
            基于以下上下文代码，补全代码：
            %s
            
            需要补全的部分：
            %s
            """, context, partial);
        
        return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
    }
    
    public String reviewCode(String code) {
        String prompt = String.format("""
            审查以下代码，找出潜在问题：
            ```java
            %s
            ```
            """, code);
        
        return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
    }
}
```

---

> 更多实战案例见其他文档
