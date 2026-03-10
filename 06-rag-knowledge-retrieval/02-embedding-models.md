# Embedding 模型选型指南

> 面向 Java 后端开发者的 Embedding 模型选型参考，涵盖主流云端 API 与开源模型对比。

---

## 目录

- [什么是 Embedding](#什么是-embedding)
- [主流 Embedding 模型概览](#主流-embedding-模型概览)
- [模型详细对比](#模型详细对比)
- [Java 集成示例](#java-集成示例)
- [选型建议](#选型建议)
- [模型部署与优化](#模型部署与优化)

---

## 什么是 Embedding

Embedding（嵌入向量）是将文本、图像等非结构化数据映射到高维数值向量的技术。语义相近的内容，其向量在高维空间中的距离也更近。

```
文本 → Embedding 模型 → 高维向量（如 1024 维）

"今天天气很好"    → [0.12, -0.45, 0.78, ...]
"今天阳光明媚"    → [0.11, -0.44, 0.79, ...]  ← 语义相近，向量接近
"明天要开会"     → [-0.32, 0.21, -0.15, ...]  ← 语义不同，向量差异大
```

### 核心应用场景

| 场景 | 说明 |
|------|------|
| **语义搜索** | 基于含义而非关键词匹配文档 |
| **RAG** | 检索增强生成，为 LLM 提供上下文 |
| **文本聚类** | 自动分组相似文档 |
| **推荐系统** | 基于内容相似度推荐 |
| **文本分类** | 通过向量相似度进行分类 |

---

## 主流 Embedding 模型概览

### 云端 API 模型

| 提供商 | 模型 | 特点 |
|--------|------|------|
| OpenAI | text-embedding-3-large | 最高精度，支持维度压缩 |
| OpenAI | text-embedding-3-small | 性价比最优，推荐首选 |
| OpenAI | text-embedding-ada-002 | 旧版，兼容性好 |
| Cohere | embed-multilingual-v3.0 | 多语言支持优秀 |
| Jina AI | jina-embeddings-v3 | 开源多语言，8192 上下文 |

### 开源本地模型

| 组织 | 模型 | 特点 |
|------|------|------|
| 智源研究院 | BGE-M3 | 中文效果最佳，多粒度检索 |
| 智源研究院 | BGE-large-zh | 中文语义理解强 |
| 智源研究院 | BGE-base-zh | 轻量版，推理快 |
| Moka AI | M3E | 中文开源，社区活跃 |
| Microsoft | E5 | MTEB 排行榜领先 |
| Jina AI | jina-embeddings-v2 | 多语言，小体积 |

---

## 模型详细对比

### 云端 API 模型对比

| 模型 | 维度 | 上下文长度 | 多语言 | 中文能力 | 价格（$/1M tokens） | 开源协议 |
|------|------|-----------|--------|----------|---------------------|----------|
| text-embedding-3-large | 256-3072 | 8191 | ✅ 优秀 | ✅ 良好 | $0.13 | 专有 |
| text-embedding-3-small | 512-1536 | 8191 | ✅ 优秀 | ✅ 良好 | $0.02 | 专有 |
| text-embedding-ada-002 | 1536 | 8191 | ✅ 良好 | ⚠️ 一般 | $0.10 | 专有 |
| Cohere embed-multilingual-v3 | 1024 | 512 | ✅ 优秀 | ✅ 良好 | $0.10 | 专有 |
| Jina jina-embeddings-v3 | 1024 | 8192 | ✅ 优秀 | ✅ 良好 | 免费/付费 | Apache 2.0 |

### 开源本地模型对比

| 模型 | 维度 | 上下文长度 | 多语言 | 中文能力 | 模型大小 | 开源协议 |
|------|------|-----------|--------|----------|----------|----------|
| BGE-M3 | 1024 | 8192 | ✅ 100+ 语言 | ✅ 优秀 | 2.27GB | MIT |
| BGE-large-zh-v1.5 | 1024 | 512 | ⚠️ 中英 | ✅ 优秀 | 1.3GB | MIT |
| BGE-base-zh-v1.5 | 768 | 512 | ⚠️ 中英 | ✅ 良好 | 500MB | MIT |
| M3E-base | 768 | 512 | ⚠️ 中英 | ✅ 良好 | 400MB | MIT |
| M3E-large | 1024 | 512 | ⚠️ 中英 | ✅ 良好 | 1.2GB | MIT |
| E5-large-v2 | 1024 | 512 | ✅ 多语言 | ⚠️ 一般 | 1.3GB | MIT |
| E5-base-v2 | 768 | 512 | ✅ 多语言 | ⚠️ 一般 | 438MB | MIT |
| jina-embeddings-v2-base-zh | 768 | 8192 | ⚠️ 中英 | ✅ 良好 | 320MB | Apache 2.0 |
| jina-embeddings-v2-small-zh | 512 | 8192 | ⚠️ 中英 | ✅ 良好 | 80MB | Apache 2.0 |

### 关键指标说明

| 指标 | 说明 | 影响 |
|------|------|------|
| **维度** | 向量长度 | 维度越高，表达能力越强，存储成本越大 |
| **上下文长度** | 最大输入 token 数 | 决定单条文本的处理能力 |
| **多语言** | 支持的语言数量 | 跨语言检索能力 |
| **中文能力** | 中文语义理解效果 | 中文场景的关键指标 |
| **模型大小** | 运行时内存占用 | 影响部署成本和推理速度 |

---

## Java 集成示例

### OpenAI Embedding API 集成

```java
@Service
public class OpenAIEmbeddingService {
    
    private final String apiKey;
    private final String apiUrl = "https://api.openai.com/v1/embeddings";
    private final HttpClient httpClient;
    
    public OpenAIEmbeddingService(@Value("${openai.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }
    
    /**
     * 单文本 Embedding
     */
    public double[] embed(String text) {
        return embed(List.of(text)).get(0);
    }
    
    /**
     * 批量 Embedding（推荐）
     */
    public List<double[]> embed(List<String> texts) {
        try {
            // 构建请求体
            Map<String, Object> requestBody = Map.of(
                "model", "text-embedding-3-small",
                "input", texts,
                "dimensions", 1536  // 可选：指定输出维度
            );
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                    new ObjectMapper().writeValueAsString(requestBody)))
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());
            
            // 解析响应
            JsonNode root = new ObjectMapper().readTree(response.body());
            JsonNode embeddings = root.get("data");
            
            List<double[]> results = new ArrayList<>();
            for (JsonNode item : embeddings) {
                JsonNode embedding = item.get("embedding");
                double[] vector = new double[embedding.size()];
                for (int i = 0; i < embedding.size(); i++) {
                    vector[i] = embedding.get(i).asDouble();
                }
                results.add(vector);
            }
            
            return results;
            
        } catch (Exception e) {
            throw new RuntimeException("Embedding 调用失败", e);
        }
    }
}
```

### Spring AI Embedding 集成

```java
@Configuration
public class EmbeddingConfig {
    
    @Bean
    public EmbeddingModel embeddingModel(
            @Value("${openai.api-key}") String apiKey) {
        
        return OpenAiEmbeddingModel.builder()
            .apiKey(apiKey)
            .modelName("text-embedding-3-small")
            .dimensions(1536)
            .build();
    }
}

@Service
public class DocumentEmbeddingService {
    
    @Autowired
    private EmbeddingModel embeddingModel;
    
    /**
     * 文档向量化并存储
     */
    public void embedAndStore(List<Document> documents) {
        for (Document doc : documents) {
            // Spring AI 自动处理 Embedding
            Embedding embedding = embeddingModel.embed(doc);
            
            // 存储到向量数据库
            vectorStore.add(List.of(new Document(
                doc.getText(),
                Map.of(
                    "embedding", embedding.getVector(),
                    "source", doc.getMetadata().get("source"),
                    "chunk_id", UUID.randomUUID().toString()
                )
            )));
        }
    }
}
```

### LangChain4j Embedding 集成

```java
@Component
public class LangChain4jEmbeddingService {
    
    private final EmbeddingModel embeddingModel;
    
    public LangChain4jEmbeddingService(
            @Value("${openai.api-key}") String apiKey) {
        
        this.embeddingModel = OpenAiEmbeddingModel.builder()
            .apiKey(apiKey)
            .modelName("text-embedding-3-small")
            .dimensions(1536)
            .build();
    }
    
    /**
     * 计算文本相似度
     */
    public double calculateSimilarity(String text1, String text2) {
        Response<Embedding> embedding1 = embeddingModel.embed(text1);
        Response<Embedding> embedding2 = embeddingModel.embed(text2);
        
        return CosineSimilarity.between(
            embedding1.content().vector(),
            embedding2.content().vector()
        );
    }
    
    /**
     * 批量 Embedding
     */
    public List<float[]> batchEmbed(List<String> texts) {
        return texts.stream()
            .map(text -> embeddingModel.embed(text).content().vector())
            .collect(Collectors.toList());
    }
}
```

### 本地模型部署（ONNX Runtime）

```java
@Service
public class LocalEmbeddingService {
    
    private final OrtEnvironment environment;
    private final OrtSession session;
    private final Tokenizer tokenizer;
    
    public LocalEmbeddingService() throws OrtException {
        // 加载 ONNX 模型（如 BGE-M3）
        this.environment = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions options = new OrtSession.SessionOptions();
        this.session = environment.createSession(
            "models/bge-m3/onnx/model.onnx", options);
        
        // 加载对应的分词器
        this.tokenizer = HuggingFaceTokenizer.newBuilder(
            "models/bge-m3/tokenizer", Map.of()).build();
    }
    
    /**
     * 本地模型推理
     */
    public float[] embed(String text) throws OrtException {
        // 1. Tokenize
        Encoding encoding = tokenizer.encode(text);
        long[] inputIds = encoding.getIds();
        long[] attentionMask = encoding.getAttentionMask();
        
        // 2. 构建 ONNX 输入张量
        OnnxTensor inputTensor = OnnxTensor.createTensor(
            environment, new long[][]{inputIds});
        OnnxTensor maskTensor = OnnxTensor.createTensor(
            environment, new long[][]{attentionMask});
        
        // 3. 运行推理
        OrtSession.Result results = session.run(Map.of(
            "input_ids", inputTensor,
            "attention_mask", maskTensor
        ));
        
        // 4. 提取向量（取 [CLS] token 或 mean pooling）
        float[][][] output = (float[][][]) results.get(0).getValue();
        return meanPooling(output[0], attentionMask);
    }
    
    private float[] meanPooling(float[][] tokenEmbeddings, long[] attentionMask) {
        int seqLen = tokenEmbeddings.length;
        int dim = tokenEmbeddings[0].length;
        float[] pooled = new float[dim];
        
        int validTokens = 0;
        for (int i = 0; i < seqLen; i++) {
            if (attentionMask[i] == 1) {
                for (int j = 0; j < dim; j++) {
                    pooled[j] += tokenEmbeddings[i][j];
                }
                validTokens++;
            }
        }
        
        // 归一化
        for (int j = 0; j < dim; j++) {
            pooled[j] /= validTokens;
        }
        
        return normalize(pooled);
    }
    
    private float[] normalize(float[] vector) {
        double norm = 0;
        for (float v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);
        
        float[] normalized = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = (float) (vector[i] / norm);
        }
        return normalized;
    }
}
```

---

## 选型建议

### 按场景推荐

| 场景 | 推荐方案 | 理由 |
|------|----------|------|
| **快速原型/MVP** | OpenAI text-embedding-3-small | 成本低，效果好，无需运维 |
| **中文生产环境（云端）** | OpenAI text-embedding-3-large | 精度最高，支持维度压缩 |
| **中文生产环境（本地）** | BGE-M3 | 中文效果最佳，开源免费 |
| **多语言场景** | BGE-M3 / Jina v3 | 支持 100+ 语言 |
| **资源受限环境** | BGE-base-zh / Jina small | 模型小，推理快 |
| **离线/内网环境** | BGE 系列本地部署 | 数据不出域 |
| **混合检索系统** | BGE-M3 | 支持稠密+稀疏+多向量 |

### 成本对比（估算）

| 方案 | 100万文档成本 | 运维成本 | 延迟 |
|------|--------------|----------|------|
| OpenAI 3-small | ~$20 | 低 | 100-300ms |
| OpenAI 3-large | ~$130 | 低 | 200-500ms |
| BGE-M3 本地 | GPU 电费 | 中 | 50-200ms |
| BGE-base 本地 | CPU 即可 | 低 | 20-100ms |

### 决策流程图

```
是否需要最高精度？
    ├── 是 → OpenAI text-embedding-3-large
    └── 否 → 是否中文场景为主？
              ├── 是 → 数据能否出域？
              │         ├── 能 → OpenAI 3-small
              │         └── 不能 → BGE-M3 本地部署
              └── 否 → 是否多语言？
                        ├── 是 → Jina v3 / BGE-M3
                        └── 否 → E5-large / BGE-large
```

---

## 模型部署与优化

### 本地模型部署方案

| 方案 | 适用模型 | 特点 |
|------|----------|------|
| **ONNX Runtime** | 所有 HuggingFace 模型 | 跨平台，性能好 |
| **Transformers.js** | 轻量模型 | Node.js 环境 |
| **Sentence Transformers** | Python 生态 | 功能丰富 |
| **Ollama** | 预打包模型 | 一键部署 |
| **vLLM** | 大模型批量推理 | 高吞吐 |

### 性能优化技巧

```java
@Service
public class OptimizedEmbeddingService {
    
    // 1. 批量处理减少 API 调用
    private static final int BATCH_SIZE = 100;
    
    // 2. 本地缓存避免重复计算
    private final Cache<String, double[]> embeddingCache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(Duration.ofHours(1))
        .build();
    
    /**
     * 带缓存的 Embedding
     */
    public double[] embedWithCache(String text) {
        return embeddingCache.get(text, this::computeEmbedding);
    }
    
    /**
     * 批量处理（自动分批次）
     */
    public List<double[]> batchEmbedOptimized(List<String> texts) {
        List<double[]> results = new ArrayList<>();
        
        // 按批次处理
        for (int i = 0; i < texts.size(); i += BATCH_SIZE) {
            List<String> batch = texts.subList(
                i, Math.min(i + BATCH_SIZE, texts.size()));
            results.addAll(embed(batch));
        }
        
        return results;
    }
    
    /**
     * 维度压缩（OpenAI 3 系列支持）
     */
    public double[] embedWithCompression(String text, int targetDimensions) {
        // 请求低维向量，减少存储和计算成本
        // dimensions 参数支持: 256, 512, 1024, 1536 (3-small) / 3072 (3-large)
        return callOpenAI(text, targetDimensions);
    }
}
```

### 监控指标

| 指标 | 说明 | 健康阈值 |
|------|------|----------|
| 平均延迟 | 单次 Embedding 耗时 | < 300ms |
| 成功率 | API 调用成功比例 | > 99.5% |
| 缓存命中率 | 本地缓存命中比例 | > 60% |
| 成本/千次 | 每千次调用成本 | 根据模型而定 |
| 向量质量 | 检索准确率 | > 80% |

---

## 参考资源

| 资源 | 链接 |
|------|------|
| OpenAI Embedding API | https://platform.openai.com/docs/guides/embeddings |
| BGE 模型仓库 | https://github.com/FlagOpen/FlagEmbedding |
| M3E 模型仓库 | https://github.com/moka-ai/m3e |
| Jina Embeddings | https://jina.ai/embeddings/ |
| MTEB Leaderboard | https://huggingface.co/spaces/mteb/leaderboard |
| Spring AI 文档 | https://docs.spring.io/spring-ai/reference/ |
| LangChain4j 文档 | https://docs.langchain4j.dev/ |

---

*最后更新：2026-03-10*
