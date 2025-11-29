package com.jabaddon.learning.langchain4j;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.time.LocalDate;
import java.util.List;

import dev.langchain4j.model.chat.request.ResponseFormat;

public class AiServiceWithOutputParsingExample {
    public static void main(String[] args) {
        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl(String.format("http://localhost:11434/"))
                .modelName("llama2")
                .temperature(0.0)
                .responseFormat(ResponseFormat.JSON)
                .build();

        AssistantWithOutputParsing assistant = AiServices.create(AssistantWithOutputParsing.class, chatModel);

        String answer = assistant.chat("What is the best programming language?");
        System.out.println(answer); // Hello, how can I help you?
        DateResponse today = assistant.today();
        System.out.println("Today: " + today.date);
        List<String> persons = List.of(
                "Benjamin Franklin",
                "Benito Juarez",
                "Steve Jobs",
                "Nikola Tesla",
                "Michael Jordan");
        persons.forEach(p -> {
            DateResponse b1 = assistant.birthDayOf(p);
            System.out.printf("Birthday of %s = %s\n", p, b1.date);
        });
    }
}

interface AssistantWithOutputParsing {

    String chat(String s);

    @UserMessage("What day is today? date text must be YYYY-MM-DD format")
    DateResponse today();

    @UserMessage("What is the birthday of {{personName}}? date text must be YYYY-MM-DD format")
    DateResponse birthDayOf(@V("personName") String personName);
}

class DateResponse {
    LocalDate date;
}
