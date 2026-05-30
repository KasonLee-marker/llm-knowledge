package com.example.memory;

/** 对话消息 */
public class Message {
    private final MessageRole role;
    private final String content;
    private final String toolCallId; // 工具调用结果时使用

    public Message(MessageRole role, String content) {
        this(role, content, null);
    }

    public Message(MessageRole role, String content, String toolCallId) {
        this.role = role;
        this.content = content;
        this.toolCallId = toolCallId;
    }

    public MessageRole getRole() { return role; }
    public String getContent() { return content; }
    public String getToolCallId() { return toolCallId; }

    @Override
    public String toString() {
        return "[" + role + "] " + content;
    }
}
