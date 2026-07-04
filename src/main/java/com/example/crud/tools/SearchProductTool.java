package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import com.example.crud.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchProductTool implements Tool {
    private final ProductAgent agent;

    public SearchProductTool(ProductAgent agent) { this.agent = agent; }

    @Override
    public String name() { return "search-product"; }

    @Override
    public boolean matches(String message) {
        return message.contains("search") || message.contains("get all") || message.contains("list");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        return agent.searchProducts();
    }
}
