package com.minierp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.ConnectException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OllamaClient {

    private final OllamaConfig ollamaConfig;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Autowired
    public OllamaClient(OllamaConfig ollamaConfig) {
        this.ollamaConfig = ollamaConfig;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(ollamaConfig.getConnectTimeout())
                .build();
    }

    public String generate(String prompt) throws Exception {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be empty.");
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("model", ollamaConfig.getModel());
        payload.put("prompt", prompt);
        payload.put("stream", false);

        String requestBody = objectMapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaConfig.getGenerateUrl()))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(ollamaConfig.getTimeout())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Ollama returned HTTP status " + response.statusCode() + ".");
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode responseNode = root == null ? null : root.get("response");
        if (responseNode == null || !responseNode.isTextual() || responseNode.asText().trim().isEmpty()) {
            throw new IllegalStateException("Ollama returned an empty or invalid response.");
        }
        return responseNode.asText().trim();
    }
}
