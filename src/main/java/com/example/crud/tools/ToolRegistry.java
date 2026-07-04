package com.example.crud.tools;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ToolRegistry {
    private final List<Tool> tools = new ArrayList<>();

    // Spring will inject all Tool beans automatically
    public ToolRegistry(List<Tool> availableTools) {
        if (availableTools != null) this.tools.addAll(availableTools);
    }

    public Optional<Tool> findTool(String message) {
        if (message == null) return Optional.empty();
        String lower = message.toLowerCase();
        return tools.stream().filter(t -> t.matches(lower)).findFirst();
    }

    public List<Tool> getTools() {
        return Collections.unmodifiableList(tools);
    }
}
