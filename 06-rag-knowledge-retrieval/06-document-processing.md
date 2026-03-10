# 06 - 文档处理与分块

## 目录

1. [文档加载](#1-文档加载)
2. [分块策略](#2-分块策略)
3. [元数据管理](#3-元数据管理)
4. [Java 实现示例](#4-java-实现示例)

---

## 1. 文档加载

### 1.1 支持的文档格式

| 格式 | 说明 | Java 库 |
|------|------|---------|
| **PDF** | 学术论文、报告 | Apache PDFBox、iText |
| **Word** | 合同、文档 | Apache POI |
| **Excel** | 表格数据 | Apache POI |
| **Markdown** | 技术文档、笔记 | 原生支持 |
| **HTML** | 网页内容 | Jsoup |
| **TXT** | 纯文本 | 原生支持 |
| **JSON/XML** | 结构化数据 | Jackson、JAXB |
| **代码文件** | 源码 | 原生支持 |

### 1.2 文档加载器设计

```java
// 文档加载器接口
public interface DocumentLoader {
    List<Document> load(String source);
}

// 基础文档类
@Data
public class Document {
    private String id;
    private String content;
    private String source;
    private Map<String, Object> metadata;
    private long timestamp;
}

// PDF 加载器实现
@Component
public class PDFLoader implements DocumentLoader {
    
    @Override
    public List<Document> load(String filePath) {
        List<Document> documents = new ArrayList<>();
        
        try (PDDocument pdf = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            
            Document doc = new Document();
            doc.setId(UUID.randomUUID().toString());
            doc.setContent(text);
            doc.setSource(filePath);
            doc.setMetadata(Map.of(
                "format", "pdf",
                "pages", pdf.getNumberOfPages(),
                "filename", new File(filePath).getName()
            ));
            doc.setTimestamp(System.currentTimeMillis());
            
            documents.add(doc);
            
        } catch (IOException e) {
            log.error("Failed to load PDF: {}", filePath, e);
        }
        
        return documents;
    }
}

// Markdown 加载器
@Component
public class MarkdownLoader implements DocumentLoader {
    
    @Override
    public List<Document> load(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));
            
            // 解析 Markdown 元数据（Front Matter）
            Map<String, Object> metadata = extractFrontMatter(content);
            String body = removeFrontMatter(content);
            
            Document doc = new Document();
            doc.setId(UUID.randomUUID().toString());
            doc.setContent(body);
            doc.setSource(filePath);
            doc.setMetadata(metadata);
            
            return List.of(doc);
            
        } catch (IOException e) {
            log.error("Failed to load Markdown: {}", filePath, e);
            return List.of();
        }
    }
}
```

### 1.3 批量文档加载

```java
@Service
public class DocumentLoadingService {
    
    private final Map<String, DocumentLoader> loaders;
    
    public DocumentLoadingService() {
        this.loaders = Map.of(
            "pdf", new PDFLoader(),
            "docx", new WordLoader(),
            "md", new MarkdownLoader(),
            "txt", new TextLoader()
        );
    }
    
    public List<Document> loadDirectory(String dirPath) {
        List<Document> allDocs = new ArrayList<>();
        
        try (Stream<Path> paths = Files.walk(Path.of(dirPath))) {
            paths.filter(Files::isRegularFile)
                .forEach(path -> {
                    String ext = getExtension(path.toString());
                    DocumentLoader loader = loaders.get(ext);
                    
                    if (loader != null) {
                        allDocs.addAll(loader.load(path.toString()));
                    }
                });
        } catch (IOException e) {
            log.error("Failed to load directory: {}", dirPath, e);
        }
        
        return allDocs;
    }
}
```

---

## 2. 分块策略

### 2.1 为什么需要分块？

```
问题：
- 文档太长 → 超出 Embedding 模型上下文限制
- 文档太长 → 向量表示稀释，检索精度下降
- 无关内容过多 → 噪声干扰

解决：
- 将长文档切分为语义完整的短片段
- 每个片段独立嵌入和检索
```

### 2.2 分块策略对比

| 策略 | 原理 | 优点 | 缺点 | 适用场景 |
|------|------|------|------|---------|
| **固定长度** | 按字符/Token 数切分 | 简单、可预测 | 可能切断语义 | 简单场景、快速验证 |
| **递归分块** | 按层级结构递归切分 | 保持结构完整 | 实现复杂 | 结构化文档 |
| **语义分块** | 按句子/段落边界切分 | 语义完整 | 块大小不均 | 高质量检索 |
| **Agentic 分块** | 模型智能决策切分点 | 最优切分 | 成本高、慢 | 高精度场景 |

### 2.3 固定长度分块

```java
// 固定长度分块器
public class FixedSizeSplitter implements TextSplitter {
    
    private final int chunkSize;
    private final int overlap;
    
    public FixedSizeSplitter(int chunkSize, int overlap) {
        this.chunkSize = chunkSize;
        this.overlap = overlap;
    }
    
    @Override
    public List<String> split(String text) {
        List<String> chunks = new ArrayList<>();
        
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            String chunk = text.substring(start, end);
            chunks.add(chunk);
            
            // 滑动窗口（考虑重叠）
            start = end - overlap;
            if (start < 0) start = end;
        }
        
        return chunks;
    }
}

// 使用示例
TextSplitter splitter = new FixedSizeSplitter(512, 50);
List<String> chunks = splitter.split(longDocument);
```

### 2.4 递归分块

```java
// 递归分块器（按层级结构）
public class RecursiveSplitter implements TextSplitter {
    
    private final List<String> separators;
    private final int chunkSize;
    
    public RecursiveSplitter() {
        // 按优先级排序的分隔符
        this.separators = List.of(
            "\n\n",  // 段落
            "\n",    // 换行
            ".",     // 句子
            " "      // 单词
        );
        this.chunkSize = 512;
    }
    
    @Override
    public List<String> split(String text) {
        return splitRecursive(text, 0);
    }
    
    private List<String> splitRecursive(String text, int separatorIndex) {
        if (text.length() <= chunkSize) {
            return List.of(text);
        }
        
        if (separatorIndex >= separators.size()) {
            // 强制切分
            return forceSplit(text);
        }
        
        String separator = separators.get(separatorIndex);
        List<String> parts = Arrays.asList(text.split(separator));
        
        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for (String part : parts) {
            if (current.length() + part.length() > chunkSize) {
                if (current.length() > 0) {
                    chunks.add(current.toString());
                    current = new StringBuilder();
                }
                
                // 递归处理超长的部分
                if (part.length() > chunkSize) {
                    chunks.addAll(splitRecursive(part, separatorIndex + 1));
                } else {
                    current.append(part);
                }
            } else {
                if (current.length() > 0) {
                    current.append(separator);
                }
                current.append(part);
            }
        }
        
        if (current.length() > 0) {
            chunks.add(current.toString());
        }
        
        return chunks;
    }
}
```

### 2.5 语义分块

```java
// 语义分块器（基于句子）
public class SemanticSplitter implements TextSplitter {
    
    private final SentenceDetector sentenceDetector;
    private final int maxChunkSize;
    
    public SemanticSplitter() {
        this.sentenceDetector = new SentenceDetectorME(
            new SentenceModel(new FileInputStream("en-sent.bin"))
        );
        this.maxChunkSize = 512;
    }
    
    @Override
    public List<String> split(String text) {
        // 检测句子边界
        String[] sentences = sentenceDetector.sentDetect(text);
        
        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for (String sentence : sentences) {
            if (current.length() + sentence.length() > maxChunkSize) {
                // 当前块已满，保存并开始新块
                chunks.add(current.toString().trim());
                current = new StringBuilder();
            }
            
            current.append(sentence).append(" ");
        }
        
        // 添加最后一个块
        if (current.length() > 0) {
            chunks.add(current.toString().trim());
        }
        
        return chunks;
    }
}
```

### 2.6 分块大小建议

| Embedding 模型 | 推荐块大小 | 重叠大小 |
|---------------|-----------|---------|
| text-embedding-3-small | 256-512 | 50 |
| text-embedding-3-large | 512-1024 | 100 |
| BGE-large | 512 | 50 |
| M3E-base | 256-512 | 50 |

---

## 3. 元数据管理

### 3.1 元数据的作用

```
元数据用途：
1. 过滤检索范围（按时间、类别、作者等）
2. 增强上下文（提供文档来源、标题等）
3. 结果展示（显示来源、时间等）
4. 权限控制（按用户可见范围过滤）
```

### 3.2 常见元数据字段

```java
// 元数据结构
@Data
public class DocumentMetadata {
    // 基础信息
    private String source;           // 来源文件/URL
    private String title;            // 文档标题
    private String author;           // 作者
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    
    // 内容信息
    private String category;         // 分类/标签
    private String language;         // 语言
    private int pageNumber;          // 页码
    private String section;          // 章节
    
    // 技术信息
    private String format;           // 格式：pdf/docx/md
    private long wordCount;          // 字数
    private int chunkIndex;          // 分块序号
    private int totalChunks;         // 总分块数
    
    // 业务信息
    private String department;       // 部门
    private String project;          // 项目
    private List<String> tags;       // 标签
    private String accessLevel;      // 访问级别
}
```

### 3.3 元数据过滤检索

```java
// 带元数据过滤的检索
public List<Document> searchWithFilter(
    String query, 
    Map<String, Object> filters
) {
    // 1. 向量检索
    float[] queryVector = embeddingModel.embed(query);
    
    // 2. 构建过滤条件
    Filter filter = Filter.builder()
        .eq("category", filters.get("category"))
        .gte("createdAt", filters.get("startDate"))
        .lte("createdAt", filters.get("endDate"))
        .in("department", filters.get("departments"))
        .build();
    
    // 3. 执行过滤检索
    return vectorDB.search(queryVector, topK, filter);
}

// 使用示例
Map<String, Object> filters = Map.of(
    "category", "技术文档",
    "department", List.of("研发部", "架构组"),
    "startDate", LocalDate.of(2024, 1, 1)
);

List<Document> results = searchWithFilter("RAG是什么", filters);
```

---

## 4. Java 实现示例

### 4.1 完整的文档处理 Pipeline

```java
@Service
public class DocumentProcessingPipeline {
    
    @Autowired
    private DocumentLoadingService loadingService;
    
    @Autowired
    private TextSplitter textSplitter;
    
    @Autowired
    private EmbeddingModel embeddingModel;
    
    @Autowired
    private VectorStore vectorStore;
    
    /**
     * 处理文档并索引
     */
    public void processAndIndex(String filePath) {
        // 1. 加载文档
        List<Document> documents = loadingService.load(filePath);
        
        for (Document doc : documents) {
            // 2. 分块
            List<String> chunks = textSplitter.split(doc.getContent());
            
            // 3. 处理每个块
            for (int i = 0; i < chunks.size(); i++) {
                String chunk = chunks.get(i);
                
                // 4. 生成嵌入
                float[] embedding = embeddingModel.embed(chunk);
                
                // 5. 构建块文档
                Document chunkDoc = new Document();
                chunkDoc.setId(doc.getId() + "_chunk_" + i);
                chunkDoc.setContent(chunk);
                chunkDoc.setSource(doc.getSource());
                
                // 6. 添加元数据
                Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
                metadata.put("chunkIndex", i);
                metadata.put("totalChunks", chunks.size());
                metadata.put("wordCount", chunk.split("\\s+").length);
                chunkDoc.setMetadata(metadata);
                
                // 7. 存储到向量数据库
                vectorStore.add(chunkDoc, embedding);
            }
        }
    }
    
    /**
     * 批量处理目录
     */
    public void processDirectory(String dirPath) {
        List<Document> documents = loadingService.loadDirectory(dirPath);
        
        documents.parallelStream().forEach(doc -> {
            List<String> chunks = textSplitter.split(doc.getContent());
            
            List<float[]> embeddings = embeddingModel.embedBatch(chunks);
            
            for (int i = 0; i < chunks.size(); i++) {
                // ... 构建和存储逻辑
            }
        });
    }
}
```

### 4.2 配置类

```java
@Configuration
public class RAGConfiguration {
    
    @Bean
    public TextSplitter textSplitter(
        @Value("${rag.chunk.size:512}") int chunkSize,
        @Value("${rag.chunk.overlap:50}") int overlap
    ) {
        return new RecursiveSplitter(chunkSize, overlap);
    }
    
    @Bean
    public DocumentLoadingService documentLoadingService() {
        return new DocumentLoadingService();
    }
    
    @Bean
    public DocumentProcessingPipeline documentProcessingPipeline() {
        return new DocumentProcessingPipeline();
    }
}
```

### 4.3 REST API

```java
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    
    @Autowired
    private DocumentProcessingPipeline pipeline;
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
        @RequestParam("file") MultipartFile file
    ) {
        try {
            // 保存临时文件
            Path tempPath = Files.createTempFile("upload_", 
                getExtension(file.getOriginalFilename()));
            file.transferTo(tempPath);
            
            // 处理并索引
            pipeline.processAndIndex(tempPath.toString());
            
            // 清理临时文件
            Files.delete(tempPath);
            
            return ResponseEntity.ok("Document processed successfully");
            
        } catch (IOException e) {
            return ResponseEntity.status(500)
                .body("Failed to process document: " + e.getMessage());
        }
    }
    
    @PostMapping("/batch")
    public ResponseEntity<String> batchProcess(
        @RequestParam("directory") String directory
    ) {
        pipeline.processDirectory(directory);
        return ResponseEntity.ok("Batch processing started");
    }
}
```

---

## 5. 最佳实践

### 5.1 分块策略选择

```
文档类型 → 推荐策略

技术文档（Markdown） → 递归分块（按标题层级）
论文（PDF）         → 语义分块（按段落）
代码               → 按函数/类分块
表格数据           → 按行分块 + 表头上下文
聊天记录           → 按会话分块
```

### 5.2 元数据设计原则

1. **必需字段**：source、chunkIndex、timestamp
2. **过滤字段**：category、department、accessLevel
3. **展示字段**：title、author、pageNumber
4. **避免冗余**：不存储大文本、二进制数据

### 5.3 性能优化

```java
// 批量处理优化
public void batchProcessOptimized(List<Document> documents) {
    // 并行加载
    List<Document> loadedDocs = documents.parallelStream()
        .map(loadingService::load)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    
    // 批量分块
    List<String> allChunks = loadedDocs.stream()
        .flatMap(doc -> textSplitter.split(doc.getContent()).stream())
        .collect(Collectors.toList());
    
    // 批量嵌入（利用批处理 API）
    List<float[]> embeddings = embeddingModel.embedBatch(allChunks);
    
    // 批量写入向量数据库
    vectorStore.addBatch(loadedDocs, embeddings);
}
```

---

> 📌 下一步学习：
> - [07-rag-evaluation.md](./07-rag-evaluation.md) - RAG 评估与优化
> - [08-java-rag-practice.md](./08-java-rag-practice.md) - Java 实战
