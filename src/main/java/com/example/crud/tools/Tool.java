package com.example.crud.tools;

import com.example.crud.controller.dto.ChatRequest;

public interface Tool {
    String name();
    boolean matches(String message);
    Object execute(ChatRequest req) throws Exception;
}
