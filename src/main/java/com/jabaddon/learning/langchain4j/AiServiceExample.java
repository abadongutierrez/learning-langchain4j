package com.jabaddon.learning.langchain4j;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

public class AiServiceExample {
    public static void main(String[] args) {
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(String.format("http://localhost:11434/"))
                .modelName("llama2")
                .temperature(0.0)
                .build();

        Assistant assistant = AiServices.create(Assistant.class, chatModel);

        String answer = assistant.chat("What is the best programming language?");
        System.out.println(answer); // Hello, how can I help you?
    }
}

interface Assistant {
    String chat(String message);
}
