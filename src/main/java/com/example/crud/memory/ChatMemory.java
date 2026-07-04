package com.example.crud.memory;

import com.example.crud.controller.dto.ChatRequest;
import com.example.crud.entity.ChatMessage;
import com.example.crud.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMemory {
    private final LinkedList<ChatRequest> history = new LinkedList<>();
    private final int capacity = 50;
    private final ChatMessageRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatMemory(ChatMessageRepository repo) {
        this.repo = repo;
        // Load chat history from database on startup
        loadFromDatabase();
    }

    public synchronized void add(ChatRequest req) {
        history.addFirst(req);
        if (history.size() > capacity) history.removeLast();

        // Persist to database
        try {
            String productJson = req.getProduct() != null ? mapper.writeValueAsString(req.getProduct()) : null;
            ChatMessage msg = new ChatMessage(
                req.getMessage(),
                productJson,
                req.getId(),
                req.getName(),
                req.getPrice()
            );
            repo.save(msg);
        } catch (Exception e) {
            System.err.println("Failed to persist chat message: " + e.getMessage());
        }
    }

    public synchronized List<ChatRequest> getHistory() {
        return List.copyOf(history);
    }

    public synchronized void clear() {
        history.clear();
        repo.deleteAll();
    }

    private void loadFromDatabase() {
        try {
            List<ChatMessage> messages = repo.findAllByOrderByTimestampDesc();
            messages.stream()
                .limit(capacity)
                .forEach(msg -> {
                    ChatRequest req = new ChatRequest();
                    req.setMessage(msg.getMessage());
                    req.setId(msg.getProductId());
                    req.setName(msg.getProductName());
                    req.setPrice(msg.getProductPrice());
                    if (msg.getProductJson() != null) {
                        try {
                            req.setProduct(mapper.readValue(msg.getProductJson(), com.example.crud.entity.Product.class));
                        } catch (Exception e) {
                            System.err.println("Failed to deserialize product JSON: " + e.getMessage());
                        }
                    }
                    history.addLast(req);
                });
        } catch (Exception e) {
            System.err.println("Failed to load chat history from database: " + e.getMessage());
        }
    }
}
