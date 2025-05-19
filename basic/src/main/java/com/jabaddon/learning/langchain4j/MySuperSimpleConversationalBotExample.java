package com.jabaddon.learning.langchain4j;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MySuperSimpleConversationalBotExample {
    public static void main(String[] args) {

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
                .modelName("gpt-4")
                .temperature(0.3)
                .logRequests(true)
                .logResponses(true)
                .build();

        // Mi Bot super sencillo con LangChain3jk
        List<ChatMessage> chatMemory = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[User]> ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("<exit>")) break;
            UserMessage newUserMessage = UserMessage.from(userInput);
            chatMemory.add(newUserMessage);
            Response<AiMessage> aiMessageResponse = chatModel.generate(chatMemory);
            chatMemory.add(aiMessageResponse.content());
            System.out.println("[AI]> " + aiMessageResponse.content().text());
        }
    }
}
