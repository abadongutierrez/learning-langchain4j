package com.jabaddon.learning.langchain4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.time.Duration;
import java.util.List;

public class EasyRAGExample {
    public static void main(String[] args) {
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(
                "documents",
                new TextDocumentParser());

        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.splitAll(documents);

        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2) // on each interaction we will retrieve the 2 most relevant segments
                .minScore(0.5) // we want to retrieve segments at least somewhat similar to user query
                .build();

        List.of(createOpenAIChatModel(), createOllamaChatModel()).forEach(chatModel -> {
            EasyRAGAssistant assistant = createEasyRAGAssistant(chatModel, contentRetriever);

            System.out.println(assistant.chat("What is Matt?"));
            System.out.println(assistant.chat("What is Diego?"));
            System.out.println(assistant.chat("To which persons do you think would like Sushi for lunch?"));
            System.out.println(assistant.chat("Which persons we should ask which kind of Tacos will be better for Dinner?"));
            System.out.println(assistant.chat("For the upcoming Physics tournament which Student we should send?"));
            System.out.println(assistant.chat("Which student do you think is urgent to call their parents to dicuss their bad grades?"));
        });
    }

    private static ChatLanguageModel createOpenAIChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
                .modelName("gpt-4o")
                .build();
    }

    private static ChatLanguageModel createOllamaChatModel() {
        return  OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("llama2")
                .temperature(0.0)
                .build();
    }

    private static EasyRAGAssistant createEasyRAGAssistant(ChatLanguageModel chatModel, ContentRetriever contentRetriever) {
        return AiServices.builder(EasyRAGAssistant.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(contentRetriever)
                .build();
    }
}

interface EasyRAGAssistant {
    String chat(String userMessage);
}
