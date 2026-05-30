package com.example.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Agent 短期记忆（对话历史） */
public class ShortTermMemory {
    private final List<Message> messages = new ArrayList<>();
    private final int maxMessages;

    public ShortTermMemory(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public void add(Message message) {
        messages.add(message);
        // 超出限制时删除最早的非系统消息；若全为系统消息则直接退出
        while (messages.size() > maxMessages) {
            boolean removed = false;
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getRole() != MessageRole.SYSTEM) {
                    messages.remove(i);
                    removed = true;
                    break;
                }
            }
            if (!removed) {
                break; // 只有系统消息时不做淘汰，防止无限循环
            }
        }
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void clear() {
        // 保留系统消息
        messages.removeIf(m -> m.getRole() != MessageRole.SYSTEM);
    }

    public int size() {
        return messages.size();
    }
}
