# 05 - 越狱攻击与防御

## 1. 越狱攻击类型

### 1.1 攻击分类

```mermaid
mindmap
  root((越狱攻击))
    角色扮演
      假扮开发者
      假扮系统管理员
      DAN模式
    提示注入
      指令覆盖
      上下文操控
      分隔符绕过
    编码绕过
      Base64编码
      凯撒密码
      十六进制
    混淆攻击
      同义词替换
      拼写错误
      多语言混合
    多轮诱导
      逐步引导
      假设场景
      角色切换
```

## 2. 防御策略

### 2.1 多层防御

```mermaid
flowchart TD
    A[输入] --> B{输入过滤}
    B --> C{意图识别}
    C --> D{越狱检测}
    D --> E{输出审查}
    E --> F[安全输出]
    
    B -->|可疑| X[拦截]
    C -->|恶意| X
    D -->|越狱| X
    E -->|违规| X
```

### 2.2 Java 实现

```java
@Service
public class JailbreakDetector {
    
    private final List<Pattern> jailbreakPatterns = List.of(
        Pattern.compile("ignore previous instructions", Pattern.CASE_INSENSITIVE),
        Pattern.compile("DAN|do anything now", Pattern.CASE_INSENSITIVE),
        Pattern.compile("system prompt|developer mode", Pattern.CASE_INSENSITIVE),
        Pattern.compile("base64|decode|encode", Pattern.CASE_INSENSITIVE)
    );
    
    public boolean detect(String input) {
        String normalized = normalize(input);
        
        for (Pattern pattern : jailbreakPatterns) {
            if (pattern.matcher(normalized).find()) {
                return true;
            }
        }
        
        // 检测编码内容
        if (isEncoded(normalized)) {
            return true;
        }
        
        return false;
    }
    
    private String normalize(String input) {
        return input.toLowerCase()
            .replaceAll("[^a-z0-9]", "")
            .replaceAll("(.)\\1+", "$1");
    }
    
    private boolean isEncoded(String input) {
        // Base64 检测
        if (input.matches("^[A-Za-z0-9+/]{20,}={0,2}$")) {
            return true;
        }
        return false;
    }
}
```

---

> 📌 下一步：[06-hallucination-mitigation.md](./06-hallucination-mitigation.md)
