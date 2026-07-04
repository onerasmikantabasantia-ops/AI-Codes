package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import org.springframework.stereotype.Component;

@Component
public class FindByPriceTool implements Tool {
    private final ProductAgent agent;

    public FindByPriceTool(ProductAgent agent) { this.agent = agent; }

    @Override
    public String name() { return "find-by-price"; }

    @Override
    public boolean matches(String message) {
        return message.contains("find by price") || message.contains("by price") || message.contains("find price");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        Double price = req.getPrice();
        if (price == null) throw new IllegalArgumentException("Missing price for find by price");
        return agent.findByPrice(price);
    }
}
