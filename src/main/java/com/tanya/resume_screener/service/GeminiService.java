package com.tanya.resume_screener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String analyzeResume(String resumeText, String jobDescription) {
        String prompt = "Analyze this resume against the job description and provide:\n" +
                "1. Match Score: (0-100)\n" +
                "2. Key Strengths (3 points)\n" +
                "3. Missing Skills (3 points)\n" +
                "4. Overall Recommendation\n\n" +
                "Job Description: " + jobDescription + "\n\n" +
                "Resume: " + resumeText;

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        return webClient.post()
                .uri("/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        var candidates = (List<Map>) response.get("candidates");
                        var contentMap = (Map) candidates.get(0).get("content");
                        var parts = (List<Map>) contentMap.get("parts");
                        return (String) parts.get(0).get("text");
                    } catch (Exception e) {
                        return "Analysis failed: " + e.getMessage();
                    }
                })
                .block();
    }
}