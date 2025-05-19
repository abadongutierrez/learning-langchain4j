# Vector Search with LangChain4j

This project implements a simple RAG (Retrieval Augmented Generation) system using Spring Boot and LangChain4j.

## Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose (for Redis)

## Setup

1. Start Redis using Docker Compose:
   ```bash
   docker-compose up -d
   ```
   This will start Redis on localhost:6379

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Ingest Document
```http
POST /api/rag/ingest
Content-Type: multipart/form-data

file: <your-document-file>
```

### Query
```http
POST /api/rag/query
Content-Type: text/plain

What is the capital of France?
```

## Features

- Document ingestion and chunking
- Vector storage using Redis
- Semantic search using all-MiniLM-L6-v2 embeddings
- RAG-based question answering

## Configuration

The application uses the following default configurations:
- Redis host: localhost
- Redis port: 6379
- Embedding dimension: 384 (for all-MiniLM-L6-v2)
- Maximum results per query: 2
- Minimum similarity score: 0.6

You can modify these settings in the `LangChainConfig` class.

## Docker Commands

- Start Redis: `docker-compose up -d`
- Stop Redis: `docker-compose down`
- View Redis logs: `docker-compose logs -f redis`
- Restart Redis: `docker-compose restart redis` 