package com.minierp.dto;

public class AiAssistantResponse {
    private String question;
    private String answer;
    private String model;
    private String source;

    public AiAssistantResponse() {
    }

    public AiAssistantResponse(String question, String answer, String model, String source) {
        this.question = question;
        this.answer = answer;
        this.model = model;
        this.source = source;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
