# 08 - Java 实战：构建 RAG 系统

本章节通过完整的实战项目，演示如何使用 Java 技术栈构建一个企业级 RAG 系统。

## 目录

1. [技术栈选型](#1-技术栈选型)
2. [项目架构](#2-项目架构)
3. [核心代码实现](#3-核心代码实现)
4. [完整示例](#4-完整示例)
5. [部署与运维](#5-部署与运维)

---

## 1. 技术栈选型

### 1.1 技术栈概览

| 层级 | 技术 | 版本 | 说明 |
|------|------|------|------|
| **框架** | Spring Boot | 3.2.x | 主框架 |
| **AI 框架** | Spring AI | 1.0.x | AI 抽象层 |
| **向量数据库** | Milvus | 2.3.x | 向量存储 |
| **Embedding** | OpenAI / BGE | - | 文本嵌入 |
| **LLM** | OpenAI / Qwen | - | 大语言模型 |
| **文档处理** | Apache PDFBox / POI | 2.x | 文档解析 |
| **缓存** | Redis | 7.x | 结果缓存 |
| **监控** | Micrometer + Prometheus | - | 可观测性 |

### 1.2 Maven 依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>rag-system</artifactId>
    <version>1.0.0</version>
    
    <properties>
        <java.version>21</java.version>
        <spring-ai.version>1.0.0-M2</spring-ai.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
        <!-- Spring AI -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-milvus-store-spring-boot-starter</artifactId>
        </dependency>
        
        <!-- Milvus Client -->
        <dependency>
            <groupId>io.milvus</groupId>
            <artifactId>milvus-sdk-java</artifactId>
            <version>2.3.5</version>
        </dependency>
        
        <!-- Document Processing -->
        <dependency>
            <groupId>org.apache.pdfbox</groupId>
            <artifactId>pdfbox</artifactId>
            <version>3.0.1</version>
        </dependency>
        
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.5</version>
        </dependency>
        
        <!-- Utilities -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>${spring-ai.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### 1.3 配置文件

```yaml
# application.yml
server:
  port: 8080

spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4
          temperature: 0.7
      embedding:
        options:
          model: text-embedding-3-large
    
    vectorstore:
      milvus:
        host: localhost
        port: 19530
        collection-name: document_chunks
        embedding-dimension: 3072
  
  redis:
    host: localhost
    port: 6379
    database: 0

rag:
  chunk:
    size: 512
    overlap: 50
  retrieval:
    top-k: 5
    score-threshold: 0.7
  cache:
    ttl: 3600  # 缓存1小时

logging:
  level:
    com.example.rag: DEBUG
```

---

## 2. 项目架构

### 2.1 模块结构

```
rag-system/
├── src/main/java/com/example/rag/
│   ├── RagApplication.java
│   ├── config/
│   │   ├── MilvusConfig.java
│   │   └── RAGConfig.java
│   ├── controller/
│   │   └── RAGController.java
│   ├── service/
│   │   ├── DocumentService.java
│   │   ├── RetrievalService.java
│   │   ├── GenerationService.java
│   │   └── RAGPipeline.java
│   ├── domain/
│   │   ├── Document.java
│   │   ├── Chunk.java
│   │   └── RAGRequest.java
│   ├── repository/
│   │   └── VectorStore.java
│   └── util/
│       ├── DocumentLoader.java
│       └── TextSplitter.java
├── src/main/resources/
│   └── application.yml
└── docker-compose.yml
```

### 2.2 核心组件交互

```
┌─────────────────────────────────────────────────────────────┐
│                      RAG System Architecture                 │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌──────────────┐                                          │
│   │  RAGController│ ←── REST API                            │
│   └──────┬───────┘                                          │
│          │                                                  │
│          ↓                                                  │
│   ┌──────────────┐     ┌──────────────┐                    │
│   │  RAGPipeline │────→│ DocumentService│                   │
│   │   (编排)     │     │   (文档处理)   │                   │
│   └──────┬───────┘     └──────────────┘                    │
│          │                                                  │
│          ├────────────────┬────────────────┐               │
│          ↓                ↓                ↓               │
│   ┌──────────────┐ ┌──────────────┐ ┌──────────────┐      │
│   │RetrievalService│ │GenerationService│ │  VectorStore  │      │
│   │   (检索)      │ │   (生成)       │ │  (Milvus)    │      │
│   └──────┬───────┘ └──────┬───────┘ └──────┬───────┘      │
│          │                │                │               │
│          └────────────────┴────────────────┘               │
│                           ↓                                │
│                    ┌──────────────┐                       │
│                    │ Spring AI    │                       │
│                    │ OpenAI/Milvus│                       │
│                    └──────────────┘                       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. 核心代码实现

### 3.1 实体类

```java
// Document.java
@Data
@Builder
public class Document {
    private String id;
    private String content;
    private String source;
    private String title;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
}

// Chunk.java
@Data
@Builder
public class Chunk {
    private String id;
    private String documentId;
    private String content;
    private int chunkIndex;
    private Map<String, Object> metadata;
}

// RAGRequest.java
@Data
public class RAGRequest {
    private String query;
    private int topK = 5;
    private double scoreThreshold = 0.7;
    private Map<String, Object> filters;
}

// RAGResponse.java
@Data
@Builder
public class RAGResponse {
    private String answer;
    private List<String> sources;
    private List<Double> scores;
    private long retrievalTimeMs;
    private long generationTimeMs;
}
```

### 3.2 文档处理服务

```java
@Service
@Slf4j
public class DocumentService {
    
    @Autowired
    private TextSplitter textSplitter;
    
    @Autowired
    private EmbeddingClient embeddingClient;
    
    @Autowired
    private VectorStore vectorStore;
    
    /**
     * 处理并索引文档
     */
    public void processDocument(MultipartFile file) {
        try {
            // 1. 加载文档
            String content = loadDocument(file);
            
            // 2. 分块
            List<String> chunks = textSplitter.split(content);
            log.info("Document split into {} chunks", chunks.size());
            
            // 3. 生成嵌入并存储
            for (int i = 0; i < chunks.size(); i++) {
                String chunk = chunks.get(i);
                
                // 创建 Document 对象
                org.springframework.ai.document.Document doc = 
                    new org.springframework.ai.document.Document(
                        chunk,
                        Map.of(
                            "source", file.getOriginalFilename(),
                            "chunkIndex", i,
                            "totalChunks", chunks.size()
                        )
                    );
                
                // 存储到向量数据库
                vectorStore.add(List.of(doc));
            }
            
            log.info("Document indexed successfully: {}", file.getOriginalFilename());
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to process document", e);
        }
    }
    
    /**
     * 加载不同格式的文档
     */
    private String loadDocument(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        
        return switch (extension.toLowerCase()) {
            case "pdf" -> loadPdf(file);
            case "docx" -> loadDocx(file);
            case "txt", "md" -> new String(file.getBytes(), StandardCharsets.UTF_8);
            default -> throw new UnsupportedOperationException(
                "Unsupported file type: " + extension
            );
        };
    }
    
    private String loadPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    
    private String loadDocx(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            return document.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining("\n"));
        }
    }
}
```

### 3.3 检索服务

```java
@Service
@Slf4j
public class RetrievalService {
    
    @Autowired
    private VectorStore vectorStore;
    
    @Autowired
    private EmbeddingClient embeddingClient;
    
    @Value("${rag.retrieval.top-k:5}")
    private int defaultTopK;
    
    @Value("${rag.retrieval.score-threshold:0.7}")
    private double scoreThreshold;
    
    /**
     * 检索相关文档
     */
    public List<Document> retrieve(String query, int topK) {
        long startTime = System.currentTimeMillis();
        
        // 构建搜索请求
        SearchRequest searchRequest = SearchRequest.builder()
            .query(query)
            .topK(topK)
            .similarityThreshold(scoreThreshold)
            .build();
        
        // 执行检索
        List<Document> results = vectorStore.similaritySearch(searchRequest);
        
        long duration = System.currentTimeMillis() - startTime;
        log.debug("Retrieved {} documents in {}ms", results.size(), duration);
        
        return results;
    }
    
    /**
     * 带过滤条件的检索
     */
    public List<Document> retrieveWithFilter(
        String query, 
        int topK,
        Map<String, Object> filters
    ) {
        // 构建过滤表达式
        String filterExpression = buildFilterExpression(filters);
        
        SearchRequest searchRequest = SearchRequest.builder()
            .query(query)
            .topK(topK)
            .similarityThreshold(scoreThreshold)
            .filterExpression(filterExpression)
            .build();
        
        return vectorStore.similaritySearch(searchRequest);
    }
    
    private String buildFilterExpression(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return null;
        }
        
        return filters.entrySet().stream()
            .map(e -> String.format("%s == '%s'", e.getKey(), e.getValue()))
            .collect(Collectors.joining(" && "));
    }
}
```

### 3.4 生成服务

```java
@Service
@Slf4j
public class GenerationService {
    
    @Autowired
    private ChatClient chatClient;
    
    private static final String RAG_PROMPT_TEMPLATE = """
        你是一个专业的问答助手。请严格基于以下上下文回答问题。
        如果上下文中没有相关信息，请明确说明"根据提供的资料无法回答"。
        不要编造信息。
        
        上下文：
        {context}
        
        问题：{question}
        
        请用简洁的语言回答，并在回答末尾列出参考来源。
        """;
    
    /**
     * 生成回答
     */
    public String generate(String query, List<Document> contextDocuments) {
        long startTime = System.currentTimeMillis();
        
        // 构建上下文
        String context = contextDocuments.stream()
            .map(doc -> String.format("[%s]: %s", 
                doc.getMetadata().get("source"), 
                doc.getContent()))
            .collect(Collectors.joining("\n\n"));
        
        // 构建 Prompt
        String prompt = RAG_PROMPT_TEMPLATE
            .replace("{context}", context)
            .replace("{question}", query);
        
        // 调用 LLM
        String answer = chatClient.prompt()
            .user(prompt)
            .call()
            .content();
        
        long duration = System.currentTimeMillis() - startTime;
        log.debug("Generated answer in {}ms", duration);
        
        return answer;
    }
}
```

### 3.5 RAG Pipeline

```java
@Service
@Slf4j
public class RAGPipeline {
    
    @Autowired
    private RetrievalService retrievalService;
    
    @Autowired
    private GenerationService generationService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${rag.cache.ttl:3600}")
    private long cacheTtl;
    
    /**
     * 执行完整的 RAG 流程
     */
    public RAGResponse query(RAGRequest request) {
        String query = request.getQuery();
        
        // 1. 检查缓存
        String cacheKey = "rag:" + hashQuery(query);
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.debug("Cache hit for query: {}", query);
            return fromJson(cached);
        }
        
        // 2. 检索
        long retrievalStart = System.currentTimeMillis();
        List<Document> documents = retrievalService.retrieve(
            query, 
            request.getTopK()
        );
        long retrievalTime = System.currentTimeMillis() - retrievalStart;
        
        if (documents.isEmpty()) {
            return RAGResponse.builder()
                .answer("未找到相关文档，无法回答问题。")
                .sources(List.of())
                .retrievalTimeMs(retrievalTime)
                .generationTimeMs(0)
                .build();
        }
        
        // 3. 生成
        long generationStart = System.currentTimeMillis();
        String answer = generationService.generate(query, documents);
        long generationTime = System.currentTimeMillis() - generationStart;
        
        // 4. 构建响应
        List<String> sources = documents.stream()
            .map(doc -> doc.getMetadata().get("source").toString())
            .distinct()
            .collect(Collectors.toList());
        
        RAGResponse response = RAGResponse.builder()
            .answer(answer)
            .sources(sources)
            .retrievalTimeMs(retrievalTime)
            .generationTimeMs(generationTime)
            .build();
        
        // 5. 缓存结果
        redisTemplate.opsForValue().set(
            cacheKey, 
            toJson(response), 
            cacheTtl, 
            TimeUnit.SECONDS
        );
        
        log.info("RAG query completed: retrieval={}ms, generation={}ms", 
            retrievalTime, generationTime);
        
        return response;
    }
    
    private String hashQuery(String query) {
        return DigestUtils.md5DigestAsHex(query.getBytes());
    }
}
```

### 3.6 REST Controller

```java
@RestController
@RequestMapping("/api/rag")
@Slf4j
public class RAGController {
    
    @Autowired
    private RAGPipeline ragPipeline;
    
    @Autowired
    private DocumentService documentService;
    
    /**
     * 问答接口
     */
    @PostMapping("/query")
    public ResponseEntity<RAGResponse> query(@RequestBody RAGRequest request) {
        log.info("Received query: {}", request.getQuery());
        
        RAGResponse response = ragPipeline.query(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 文档上传接口
     */
    @PostMapping("/documents/upload")
    public ResponseEntity<String> uploadDocument(
        @RequestParam("file") MultipartFile file
    ) {
        log.info("Received document: {}", file.getOriginalFilename());
        
        documentService.processDocument(file);
        
        return ResponseEntity.ok("Document uploaded and indexed successfully");
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "rag-system",
            "version", "1.0.0"
        ));
    }
}
```

---

## 4. 完整示例

### 4.1 Docker Compose 配置

```yaml
# docker-compose.yml
version: '3.8'

services:
  rag-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - SPRING_AI_MILVUS_HOST=milvus
      - SPRING_REDIS_HOST=redis
    depends_on:
      - milvus
      - redis
    networks:
      - rag-network

  milvus:
    image: milvusdb/milvus:v2.3.5
    ports:
      - "19530:19530"
      - "9091:9091"
    environment:
      ETCD_ENDPOINTS: etcd:2379
      MINIO_ADDRESS: minio:9000
    depends_on:
      - etcd
      - minio
    networks:
      - rag-network

  etcd:
    image: quay.io/coreos/etcd:v3.5.5
    environment:
      - ETCD_AUTO_COMPACTION_MODE=revision
      - ETCD_AUTO_COMPACTION_RETENTION=1000
      - ETCD_QUOTA_BACKEND_BYTES=4294967296
    networks:
      - rag-network

  minio:
    image: minio/minio:RELEASE.2023-03-20T20-16-18Z
    environment:
      MINIO_ACCESS_KEY: minioadmin
      MINIO_SECRET_KEY: minioadmin
    command: minio server /minio_data
    networks:
      - rag-network

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks:
      - rag-network

networks:
  rag-network:
    driver: bridge
```

### 4.2 Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 4.3 测试示例

```java
@SpringBootTest
@AutoConfigureMockMvc
public class RAGIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private VectorStore vectorStore;
    
    @BeforeEach
    void setUp() {
        // 清理测试数据
        vectorStore.delete(List.of());
    }
    
    @Test
    void testDocumentUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Spring Boot is a Java framework.".getBytes()
        );
        
        mockMvc.perform(multipart("/api/rag/documents/upload")
                .file(file))
            .andExpect(status().isOk());
    }
    
    @Test
    void testQuery() throws Exception {
        // 先上传文档
        // ...
        
        // 测试查询
        RAGRequest request = new RAGRequest();
        request.setQuery("What is Spring Boot?");
        request.setTopK(3);
        
        mockMvc.perform(post("/api/rag/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answer").exists())
            .andExpect(jsonPath("$.sources").isArray());
    }
}
```

---

## 5. 部署与运维

### 5.1 性能优化建议

| 优化点 | 方案 | 效果 |
|--------|------|------|
| **Embedding 批处理** | 批量生成嵌入 | 提升 5-10 倍 |
| **向量索引** | 使用 HNSW 索引 | 检索速度提升 10 倍 |
| **结果缓存** | Redis 缓存常见查询 | 减少 80% 重复计算 |
| **异步处理** | 文档处理异步化 | 提升用户体验 |
| **连接池** | 复用 Milvus/LLM 连接 | 减少连接开销 |

### 5.2 监控指标

```java
@Component
public class RAGMetrics {
    
    private final MeterRegistry meterRegistry;
    
    public void recordQuery(RAGResponse response) {
        // 记录检索时间
        meterRegistry.timer("rag.retrieval.time")
            .record(response.getRetrievalTimeMs(), TimeUnit.MILLISECONDS);
        
        // 记录生成时间
        meterRegistry.timer("rag.generation.time")
            .record(response.getGenerationTimeMs(), TimeUnit.MILLISECONDS);
        
        // 记录来源数量
        meterRegistry.distributionSummary("rag.sources.count")
            .record(response.getSources().size());
    }
}
```

### 5.3 关键指标

| 指标 | 说明 | 告警阈值 |
|------|------|---------|
| rag.retrieval.time | 检索耗时 | > 500ms |
| rag.generation.time | 生成耗时 | > 3000ms |
| rag.query.rate | 查询速率 | 根据容量 |
| rag.error.rate | 错误率 | > 1% |

---

## 6. 总结

本章节演示了如何使用 Spring Boot + Spring AI + Milvus 构建完整的企业级 RAG 系统。

### 核心要点

1. **分层架构**：Controller → Service → Repository 清晰分层
2. **配置驱动**：通过 YAML 灵活配置参数
3. **缓存优化**：Redis 缓存提升性能
4. **可观测性**：Micrometer 指标监控
5. **容器化**：Docker Compose 一键部署

### 扩展方向

- 集成更多向量数据库（Pinecone、Weaviate）
- 添加重排序（Reranking）模块
- 实现混合检索（Dense + Sparse）
- 引入 Agentic RAG 能力

---

> 📌 至此，RAG 模块全部内容已完成。
> 
> 建议学习路径：
> 1. [01-rag-basics.md](./01-rag-basics.md) - 理解核心概念
> 2. [02-embedding-models.md](./02-embedding-models.md) - 选型 Embedding
> 3. [03-vector-databases.md](./03-vector-databases.md) - 选型向量数据库
> 4. [04-retrieval-strategies.md](./04-retrieval-strategies.md) - 优化检索
> 5. [05-rag-architectures.md](./05-rag-architectures.md) - 架构演进
> 6. [06-document-processing.md](./06-document-processing.md) - 文档处理
> 7. [07-rag-evaluation.md](./07-rag-evaluation.md) - 评估优化
> 8. [08-java-rag-practice.md](./08-java-rag-practice.md) - 实战部署
