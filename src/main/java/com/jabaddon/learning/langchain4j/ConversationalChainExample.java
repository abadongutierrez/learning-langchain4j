package com.jabaddon.learning.langchain4j;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversationalChainExample {
    public static void main(String[] args) {

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
                .build();

        UserMessage firstUserMessage = UserMessage.from("Hello, my name is Rafael");
        AiMessage firstAiMessage = chatModel.generate(firstUserMessage).content();
        System.out.println(firstAiMessage.text());
        UserMessage secondUserMessage = UserMessage.from("What is my name?");
        AiMessage secondAiMessage = chatModel.generate(firstUserMessage, firstAiMessage, secondUserMessage).content(); // Klaus
        System.out.println(secondAiMessage.text());

        UserMessage translate = UserMessage.from("Can you translate this text in english to spanish? 'you would write here instructions on what the LLM's role is in this conversation, how it should behave, in what style to answer, etc.'");
        Response<AiMessage> response = chatModel.generate(translate);
        System.out.println(response.tokenUsage().totalTokenCount());
        System.out.println(response.content().text());

        Scanner scanner = new Scanner(System.in);
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(chatModel)
                //.chatMemory(MessageWindowChatMemory.withMaxMessages(100)) // you can override default chat memory
                .build();

        while (true) {
            System.out.print("[User]> ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("<exit>")) break;
            String answer = chain.execute(userInput);
            System.out.println("[AI]> " + answer);
        }
    }
}
