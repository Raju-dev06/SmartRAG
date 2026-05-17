package com.practice.raggeminiworking.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingService {

    @Value("${gemini.api.key}")
    private String apiKey;

//    public List<Double> createEmbedding(String text) {
//
//        try {
//
//            RestTemplate restTemplate = new RestTemplate();
//
//            String url =
//                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key="
//                            + apiKey;
//
//            String body = """
//                    {
//                      "content": {
//                        "parts":[
//                          {"text":"%s"}
//                        ]
//                      }
//                    }
//                    """.formatted(text);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//            String response = restTemplate.postForObject(url, request, String.class);
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(response);
//
//            JsonNode vector = root.path("embedding").path("values");
//
//            List<Double> embedding = new ArrayList<>();
//
//            for (JsonNode value : vector) {
//                embedding.add(value.asDouble());
//            }
//            System.out.println("Embedding size: " + embedding.size());
//
//            return embedding;
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    public List<Double> createEmbedding(String text) {

        try {

            RestTemplate restTemplate = new RestTemplate();

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key="
                            + apiKey;

            ObjectMapper mapper = new ObjectMapper();

            // Proper JSON creation
            String body = mapper.writeValueAsString(
                    java.util.Map.of(
                            "content",
                            java.util.Map.of(
                                    "parts",
                                    java.util.List.of(
                                            java.util.Map.of(
                                                    "text",
                                                    text
                                            )
                                    )
                            )
                    )
            );

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request =
                    new HttpEntity<>(body, headers);

            String response = restTemplate.postForObject(
                    url,
                    request,
                    String.class
            );

            JsonNode root = mapper.readTree(response);

            JsonNode vector =
                    root.path("embedding").path("values");

            List<Double> embedding = new ArrayList<>();

            for (JsonNode value : vector) {

                embedding.add(value.asDouble());
            }

            System.out.println(
                    "Embedding size: "
                            + embedding.size()
            );

            return embedding;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }
}