package com.practice.raggeminiworking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeminiService {

    private final ChatClient chatClient;

    public GeminiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String askGeminiWithContext(String context, String question) {
        try {
            String prompt = """
Answer the question using only the provided context.
If the answer is not in the context say Not found in document.

Context:
%s

Question:
%s
""".formatted(context, question);

            // Spring AI handles everything!
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

        } catch (Exception e) {
            log.error("Exception while calling Gemini API", e);
            return "Error calling Gemini API: " + e.getMessage();
        }
    }
}