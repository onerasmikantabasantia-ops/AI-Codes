package com.example.crud.tools;

import com.example.crud.agent.ProductAgent;
import com.example.crud.controller.dto.ChatRequest;
import org.springframework.stereotype.Component;

@Component
public class DeleteProductTool implements Tool {
    private final ProductAgent agent;

    public DeleteProductTool(ProductAgent agent) { this.agent = agent; }

    @Override
    public String name() { return "delete-product"; }

    @Override
    public boolean matches(String message) {
        return message.contains("delete") || message.contains("delete product");
    }

    @Override
    public Object execute(ChatRequest req) throws Exception {
        Long id = req.getId();
        if (id == null) throw new IllegalArgumentException("Missing id for delete");
        agent.deleteProduct(id);
        return null;
    }
}
