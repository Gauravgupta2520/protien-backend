package com.example.protien;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping
    public UserController.ApiResponse chat(
            @RequestBody ChatRequest chatRequest,
            Authentication authentication
    ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return new UserController.ApiResponse(false, "Unauthorized");
        }

        String reply = openAIService.chat(chatRequest.getMessages());

        return new UserController.ApiResponse(true, "Chat success", reply);
    }
}
