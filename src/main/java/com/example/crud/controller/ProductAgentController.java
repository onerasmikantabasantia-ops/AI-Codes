package com.example.crud.controller;

import com.example.crud.agent.ProductAgent;
import com.example.crud.entity.Product;
import com.example.crud.controller.dto.ChatRequest;
import com.example.crud.controller.dto.ChatResponse;
import com.example.crud.tools.ToolRegistry;
import com.example.crud.tools.Tool;
import com.example.crud.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class ProductAgentController {
    private final ProductAgent agent;
    private final ToolRegistry tools;
    private final ChatMemory memory;

    public ProductAgentController(ProductAgent agent, ToolRegistry tools, ChatMemory memory) {
        this.agent = agent;
        this.tools = tools;
        this.memory = memory;
    }

    /**
     * Accepts a chat-like request and performs the requested product operation.
     * Expected JSON shape (examples):
     * { "message": "create product", "product": { "name":"A", "price":10.0 } }
     * { "message": "update product", "id": 1, "product": { "name":"B", "price":12.0 } }
     * { "message": "delete product", "id": 1 }
     * { "message": "search product" }
     * { "message": "count products" }
     * { "message": "find by name", "name": "Foo" }
     * { "message": "find by price", "price": 10.0 }
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> handleChat(@RequestBody ChatRequest req) {
        String msg = req.getMessage();
        if (msg == null || msg.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Message is required", null));
        }

        String lower = msg.trim().toLowerCase();
        // store chat in memory
        memory.add(req);

        try {
            // prefer tools registered in ToolRegistry
            return tools.findTool(lower)
                    .map(tool -> {
                        try {
                            Object result = tool.execute(req);
                            return ResponseEntity.ok(new ChatResponse("OK via tool: " + tool.name(), result));
                        } catch (IllegalArgumentException ex) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ChatResponse(ex.getMessage(), null));
                        } catch (Exception ex) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ChatResponse("Tool error: " + ex.getMessage(), null));
                        }
                    })
                    .orElseGet(() -> {
                        // fallback to legacy parsing
                        try {
                            if (lower.contains("create product") || lower.startsWith("create")) {
                                Product p = req.getProduct();
                                if (p == null) return ResponseEntity.badRequest().body(new ChatResponse("Missing product data for create", null));
                                Product created = agent.createProduct(p);
                                return ResponseEntity.ok(new ChatResponse("Product created", created));
                            }

                            if (lower.contains("update product") || lower.startsWith("update")) {
                                Long id = req.getId();
                                Product p = req.getProduct();
                                if (id == null) return ResponseEntity.badRequest().body(new ChatResponse("Missing id for update", null));
                                if (p == null) return ResponseEntity.badRequest().body(new ChatResponse("Missing product data for update", null));
                                Product updated = agent.updateProduct(id, p);
                                return ResponseEntity.ok(new ChatResponse("Product updated", updated));
                            }

                            if (lower.contains("delete product") || lower.startsWith("delete")) {
                                Long id = req.getId();
                                if (id == null) return ResponseEntity.badRequest().body(new ChatResponse("Missing id for delete", null));
                                agent.deleteProduct(id);
                                return ResponseEntity.ok(new ChatResponse("Product deleted", null));
                            }

                            if (lower.contains("search product") || lower.contains("search") || lower.contains("get all")) {
                                List<Product> list = agent.searchProducts();
                                return ResponseEntity.ok(new ChatResponse("Search results", list));
                            }

                            if (lower.contains("count products") || lower.contains("count")) {
                                long c = agent.countProducts();
                                return ResponseEntity.ok(new ChatResponse("Count result", c));
                            }

                            if (lower.contains("find by name") || lower.contains("find name") || lower.contains("by name")) {
                                String name = req.getName();
                                if (name == null || name.trim().isEmpty()) return ResponseEntity.badRequest().body(new ChatResponse("Missing name for find by name", null));
                                List<Product> res = agent.findByName(name);
                                return ResponseEntity.ok(new ChatResponse("Find by name results", res));
                            }

                            if (lower.contains("find by price") || lower.contains("by price") || lower.contains("find price")) {
                                Double price = req.getPrice();
                                if (price == null) return ResponseEntity.badRequest().body(new ChatResponse("Missing price for find by price", null));
                                List<Product> res = agent.findByPrice(price);
                                return ResponseEntity.ok(new ChatResponse("Find by price results", res));
                            }

                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ChatResponse("Unknown command: " + msg, null));
                        } catch (IllegalArgumentException ex) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ChatResponse(ex.getMessage(), null));
                        } catch (Exception ex) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ChatResponse("Server error: " + ex.getMessage(), null));
                        }
                    });
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ChatResponse("Server error: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatRequest>> history() {
        return ResponseEntity.ok(memory.getHistory());
    }

    @DeleteMapping("/history")
    public ResponseEntity<String> clearHistory() {
        memory.clear();
        return ResponseEntity.ok("Chat history cleared");
    }
}