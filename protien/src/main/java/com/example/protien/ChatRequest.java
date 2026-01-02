package com.example.protien;

import java.util.List;

import lombok.Data;

@Data
public class ChatRequest {
    private List<ChatMessage> messages;
}
