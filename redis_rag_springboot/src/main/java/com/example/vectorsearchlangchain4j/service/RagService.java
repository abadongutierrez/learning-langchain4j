package com.example.vectorsearchlangchain4j.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import dev.langchain4j.model.openai.OpenAiChatModel;

@Service
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;
    private final ContentRetriever contentRetriever;
    private final DocumentSplitter documentSplitter;
    private final ChatModel chatModel;

    @Autowired
    public RagService(
            EmbeddingModel embeddingModel,
            EmbeddingStore embeddingStore,
            ContentRetriever contentRetriever,
            DocumentSplitter documentSplitter,
            OpenAiChatModel chatModel) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.contentRetriever = contentRetriever;
        this.documentSplitter = documentSplitter;
        this.chatModel = chatModel;
    }

    public void ingestDocument(MultipartFile file) throws IOException {
        Document document = new TextDocumentParser().parse(file.getInputStream());
        List<TextSegment> segments = documentSplitter.split(document).stream()
                .map(doc -> TextSegment.from(doc.text()))
                .toList();

        for (TextSegment segment : segments) {
            embeddingStore.add(embeddingModel.embed(segment.text()).content(), segment);
        }
    }

    public String query(String question) {
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(contentRetriever)
                .build();

        return assistant.chat(question);
    }

    interface Assistant {
        String chat(String userMessage);
    }
} 