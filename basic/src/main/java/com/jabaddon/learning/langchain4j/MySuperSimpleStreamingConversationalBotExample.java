package com.jabaddon.learning.langchain4j;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import java.util.Scanner;

import dev.langchain4j.model.chat.response.ChatResponse;

public class MySuperSimpleStreamingConversationalBotExample {
    public static void main(String[] args) {

//        StreamingChatLanguageModel chatModel = OpenAiStreamingChatModel.builder()
//                .apiKey(System.getenv("OPENAI_API_KEY"))
//                .timeout(Duration.ofSeconds(60))
//                .modelName("gpt-3.5-turbo")
//                .temperature(0.3)
////                .logRequests(true)
////                .logResponses(true)
//                .build();

        OllamaStreamingChatModel chatModel = OllamaStreamingChatModel.builder()
                .baseUrl(String.format("http://localhost:11434/"))
                .modelName("llama2")
                .temperature(0.0)
                .build();

        // Mi Bot super sencillo con LangChain4j
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenCountEstimator("gpt-3.5-turbo"));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[User]> ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("<exit>")) break;
            chatMemory.add(UserMessage.from(userInput));
            System.out.print("[AI]> ");
            WaitingThread thread = new WaitingThread();
            thread.start();
            chatModel.chat(chatMemory.messages(), new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String token) {
                    System.out.print(token);
                }

                @Override
                public void onError(Throwable error) {
                    System.err.println(error.getMessage());
                }

                @Override
                public void onCompleteResponse(ChatResponse response) {
                    chatMemory.add(response.aiMessage());
                    System.out.println();
                    thread.finish();
                }
            });
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

class WaitingThread extends Thread {
    private volatile boolean wait = true;

    @Override
    public void run() {
        while (wait) {
//            System.out.print(".");
        }
    }

    public void finish() {
        wait = false;
    }
}
