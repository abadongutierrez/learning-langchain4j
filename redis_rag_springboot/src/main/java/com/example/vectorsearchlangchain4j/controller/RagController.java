package com.example.vectorsearchlangchain4j.controller;

import com.example.vectorsearchlangchain4j.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    @Autowired
    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestDocument(@RequestParam("file") MultipartFile file) {
        try {
            ragService.ingestDocument(file);
            return ResponseEntity.ok("Document ingested successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error ingesting document: " + e.getMessage());
        }
    }

    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody String question) {
        try {
            String response = ragService.query(question);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing query: " + e.getMessage());
        }
    }
} 