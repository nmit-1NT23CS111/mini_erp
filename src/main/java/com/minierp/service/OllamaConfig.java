package com.minierp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OllamaConfig {

    @Value("${ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${ollama.model:llama3.2:3b}")
    private String model;

    @Value("${ollama.connect-timeout-ms:10000}")
    private long connectTimeoutMs;

    @Value("${ollama.timeout-ms:600000}")
    private long timeoutMs;

    public String getBaseUrl() {
        return baseUrl == null || baseUrl.trim().isEmpty() ? "http://localhost:11434" : baseUrl.trim();
    }

    public String getModel() {
        return model == null || model.trim().isEmpty() ? "llama3.2:3b" : model.trim();
    }

    public Duration getTimeout() {
        return Duration.ofMillis(Math.max(timeoutMs, 1000L));
    }

    public Duration getConnectTimeout() {
        return Duration.ofMillis(Math.max(connectTimeoutMs, 1000L));
    }

    public String getGenerateUrl() {
        String normalized = getBaseUrl();
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized + "/api/generate";
    }
}
