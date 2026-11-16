package com.minierp.rest;

import com.minierp.dto.AiAssistantRequest;
import com.minierp.dto.AiAssistantResponse;
import com.minierp.dto.ApiResponse;
import com.minierp.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AiAssistantRestController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/assistant/chat")
    public ResponseEntity<ApiResponse<AiAssistantResponse>> chat(@RequestBody AiAssistantRequest request) {
        if (request == null || request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Please enter a question for the AI Business Assistant."));
        }

        AiAssistantResponse response = aiAssistantService.askQuestion(request.getQuestion());
        return ResponseEntity.ok(ApiResponse.ok("Assistant response ready.", response));
    }
}
