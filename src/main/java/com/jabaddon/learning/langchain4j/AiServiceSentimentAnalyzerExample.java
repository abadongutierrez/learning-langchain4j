package com.jabaddon.learning.langchain4j;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

import java.time.Duration;
import java.util.List;

public class AiServiceSentimentAnalyzerExample {
    public static void main(String[] args) {
        List<ChatLanguageModel> models = List.of(
                createOllamaChatModel(),
                createOpenAIChatModel());

        List<SentimentAnalyzer> aiServices = models.stream().map(m -> AiServices.create(SentimentAnalyzer.class, m)).toList();

        for (int i = 0; i < aiServices.size(); i++) {
            SentimentAnalyzer sentimentAnalyzer = aiServices.get(i);
            ChatLanguageModel model = models.get(i);
            switch (model) {
                case OpenAiChatModel openAiChatModel -> System.out.println("OpenA:");
                case OllamaChatModel ollamaChatModel -> System.out.println("Ollama:");
                default -> System.out.println("Unknown");
            }
            List.of(
                    "This is great!",
                    "This sucks",
                    "Using langChain4j is awesome!",
                    "JavaScript is a popular language that is difficult to understand"
            ).forEach(str -> {
                try {
                    SentimentResponse sentiment = sentimentAnalyzer.analyzeSentimentOf(str);
                    System.out.printf("Text = [%s], Sentiment = [%s]\n", str, sentiment.sentiment);
                } catch (Exception ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            });
        }
    }

    private static OpenAiChatModel createOpenAIChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
//                .logRequests(true)
//                .logResponses(true)
                .responseFormat("json_object")
                .build();
    }

    private static OllamaChatModel createOllamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("llama2")
                .format("json")
                .build();
    }
}

enum Sentiment {
    POSITIVE, NEUTRAL, NEGATIVE
}

class SentimentResponse {
    Sentiment sentiment;
}

interface SentimentAnalyzer {
    @UserMessage("Analyze sentiment of {{it}}")
    SentimentResponse analyzeSentimentOf(String text);

    @UserMessage("Analyze sentiment of {{it}}")
    String asStringAnalyzeSentimentOf(String text);

    @UserMessage("Does {{it}} has a positive sentiment?")
    boolean isPositive(String text);
}
