package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import com.example.crud.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CreateProductTool implements Tool {
    private final ProductAgent agent;

    public CreateProductTool(ProductAgent agent) {
        this.agent = agent;
    }

    @Override
    public String name() { return "create-product"; }

    @Override
    public boolean matches(String message) {
        return message.contains("create") || message.contains("create product");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        Product p = req.getProduct();
        if (p == null) throw new IllegalArgumentException("Missing product data for create");
        return agent.createProduct(p);
    }
}
