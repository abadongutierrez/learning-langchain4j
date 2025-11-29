package com.jabaddon.learning.langchain4j.chatnlanguagemodesl;

import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.ollama.OllamaLanguageModel;
import dev.langchain4j.model.output.Response;

public class LanguageModelExample {
    static void main(String[] args) {
        LanguageModel languageModel = OllamaLanguageModel.builder()
                .baseUrl("https://ollama.com")
                .modelName("gpt-oss:120b-cloud")
                .build();

        Response<String> response = languageModel.generate("Hi, how are you?");
        System.out.println(response.content());
    }
}
