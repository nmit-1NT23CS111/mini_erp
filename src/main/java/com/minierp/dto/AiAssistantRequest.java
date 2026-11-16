package com.minierp.dto;

public class AiAssistantRequest {
    private String question;

    public AiAssistantRequest() {
    }

    public AiAssistantRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
