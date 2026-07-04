package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import com.example.crud.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductTool implements Tool {
    private final ProductAgent agent;

    public UpdateProductTool(ProductAgent agent) { this.agent = agent; }

    @Override
    public String name() { return "update-product"; }

    @Override
    public boolean matches(String message) {
        return message.contains("update") || message.contains("update product");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        Long id = req.getId();
        Product p = req.getProduct();
        if (id == null) throw new IllegalArgumentException("Missing id for update");
        if (p == null) throw new IllegalArgumentException("Missing product data for update");
        return agent.updateProduct(id, p);
    }
}
