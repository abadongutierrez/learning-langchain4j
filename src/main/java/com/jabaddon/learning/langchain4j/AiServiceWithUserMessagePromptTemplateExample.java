package com.jabaddon.learning.langchain4j;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AiServiceWithUserMessagePromptTemplateExample {
    public static void main(String[] args) {
        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl(String.format("http://localhost:11434/"))
                .modelName("llama2")
                .temperature(0.0)
                .build();

        FriendWithTemplate assistant = AiServices.create(FriendWithTemplate.class, chatModel);

        String answer = assistant.chat("hello");
        System.out.println(answer); // Hello, how can I help you?
    }
}

interface FriendWithTemplate {
    @UserMessage("You are a good friend of mine. Answer using slang. {{message}}")
    String chat(@V("message") String message);
}
