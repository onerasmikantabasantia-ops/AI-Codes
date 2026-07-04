package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import org.springframework.stereotype.Component;

@Component
public class CountProductsTool implements Tool {
    private final ProductAgent agent;

    public CountProductsTool(ProductAgent agent) { this.agent = agent; }

    @Override
    public String name() { return "count-products"; }

    @Override
    public boolean matches(String message) {
        return message.contains("count");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        return agent.countProducts();
    }
}
