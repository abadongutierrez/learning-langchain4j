package com.jabaddon.learning.langchain4j;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class AiServiceWithSystemMessageExample {
    public static void main(String[] args) {
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(String.format("http://localhost:11434/"))
                .modelName("llama2")
                .temperature(0.0)
                .build();

        Friend assistant = AiServices.create(Friend.class, chatModel);

        String answer = assistant.chat("hello");
        System.out.println(answer); // Hello, how can I help you?
    }
}
interface Friend {
    @SystemMessage("You are a good friend of mine. Answer using slang.")
    String chat(String message);
}
