package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import org.springframework.stereotype.Component;

@Component
public class FindByNameTool implements Tool {
    private final ProductAgent agent;

    public FindByNameTool(ProductAgent agent) { this.agent = agent; }

    @Override
    public String name() { return "find-by-name"; }

    @Override
    public boolean matches(String message) {
        return message.contains("find by name") || message.contains("by name") || message.contains("find name");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        String name = req.getName();
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Missing name for find by name");
        return agent.findByName(name);
    }
}
