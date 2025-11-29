package com.jabaddon.learning.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.time.Duration;
import java.util.List;

public class ToolsHighLevelApiExample {
    public static void main(String[] args) {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
//                .logResponses(true)
//                .logRequests(true)
                .modelName("gpt-3.5-turbo")
                .build();

        HighLevelToolAssistant assistant =
            AiServices.builder(HighLevelToolAssistant.class).tools(new LowLevelTool()).chatModel(chatModel).build();

        List.of(
                "1 + 10",
                "230302 + 12323123",
                "What is the square root of 9",
                "What is the square root of 9000000",
                "What is the weather in Londong in Celcius",
                "What is the weather in Mexico City in Fahrenheit"
        ).forEach(msg -> {
            String response = assistant.ask(msg);
            System.out.printf("Response: %s", response);
        });
    }
}

interface HighLevelToolAssistant {
    String ask(String question);
}
