package com.example.crud.memory;

import com.example.crud.controller.dto.ChatRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ChatMemory {
    private final LinkedList<ChatRequest> history = new LinkedList<>();
    private final int capacity = 50;

    public synchronized void add(ChatRequest req) {
        history.addFirst(req);
        if (history.size() > capacity) history.removeLast();
    }

    public synchronized List<ChatRequest> getHistory() {
        return List.copyOf(history);
    }

    public synchronized void clear() { history.clear(); }
}
