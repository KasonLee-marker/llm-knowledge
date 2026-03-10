# 07 - RAG 评估与优化

## 目录

1. [RAG 评估指标](#1-rag-评估指标)
2. [检索质量评估](#2-检索质量评估)
3. [生成质量评估](#3-生成质量评估)
4. [常见问题与优化](#4-常见问题与优化)
5. [Java 实现示例](#5-java-实现示例)

---

## 1. RAG 评估指标

### 1.1 评估维度

```
RAG 评估
    ├── 检索质量 (Retrieval Quality)
    │       ├── 相关性 (Relevance)
    │       ├── 召回率 (Recall)
    │       └── 精确率 (Precision)
    │
    ├── 生成质量 (Generation Quality)
    │       ├── 忠实性 (Faithfulness)
    │       ├── 答案相关性 (Answer Relevance)
    │       └── 上下文利用率 (Context Utilization)
    │
    └── 端到端质量 (End-to-End Quality)
            ├── 正确性 (Correctness)
            ├── 完整性 (Completeness)
            └── 有用性 (Helpfulness)
```

### 1.2 指标对比表

| 指标 | 评估对象 | 说明 | 计算方法 |
|------|---------|------|---------|
| **Hit Rate** | 检索 | Top-K 中是否包含相关文档 | 二元判断 |
| **MRR** | 检索 | 第一个相关文档的排名倒数 | 1/rank |
| **NDCG** | 检索 | 考虑排序的相关性加权 | 折扣累积增益 |
| **Precision@K** | 检索 | Top-K 中相关文档比例 | TP/(TP+FP) |
| **Recall@K** | 检索 | 相关文档被检索到的比例 | TP/(TP+FN) |
| **Faithfulness** | 生成 | 回答是否忠实于上下文 | LLM/规则判断 |
| **Answer Relevance** | 生成 | 回答是否针对问题 | LLM/嵌入相似度 |
| **Context Precision** | 生成 | 上下文中有用信息比例 | LLM判断 |
| **Context Recall** | 生成 | 回答问题所需信息被检索比例 | LLM判断 |

---

## 2. 检索质量评估

### 2.1 基于标注的评估

```java
// 检索评估数据集
@Data
public class RetrievalSample {
    private String query;           // 查询
    private List<String> relevantDocIds;  // 相关文档ID
    private List<String> candidateDocIds; // 候选文档ID（可选）
}

// 检索评估器
@Service
public class RetrievalEvaluator {
    
    /**
     * 计算 Hit Rate@K
     */
    public double hitRateAtK(
        List<RetrievalSample> samples, 
        int k
    ) {
        int hits = 0;
        
        for (RetrievalSample sample : samples) {
            // 执行检索
            List<Document> retrieved = vectorStore.search(
                embeddingModel.embed(sample.getQuery()), 
                k
            );
            
            // 检查是否命中
            Set<String> retrievedIds = retrieved.stream()
                .map(Document::getId)
                .collect(Collectors.toSet());
            
            boolean hit = sample.getRelevantDocIds().stream()
                .anyMatch(retrievedIds::contains);
            
            if (hit) hits++;
        }
        
        return (double) hits / samples.size();
    }
    
    /**
     * 计算 MRR (Mean Reciprocal Rank)
     */
    public double mrr(List<RetrievalSample> samples) {
        double sum = 0;
        
        for (RetrievalSample sample : samples) {
            List<Document> retrieved = vectorStore.search(
                embeddingModel.embed(sample.getQuery()), 
                100  // 检索较多结果
            );
            
            // 找到第一个相关文档的排名
            for (int i = 0; i < retrieved.size(); i++) {
                if (sample.getRelevantDocIds().contains(
                    retrieved.get(i).getId()
                )) {
                    sum += 1.0 / (i + 1);
                    break;
                }
            }
        }
        
        return sum / samples.size();
    }
    
    /**
     * 计算 Precision@K 和 Recall@K
     */
    public Map<String, Double> precisionRecallAtK(
        List<RetrievalSample> samples,
        int k
    ) {
        double totalPrecision = 0;
        double totalRecall = 0;
        
        for (RetrievalSample sample : samples) {
            List<Document> retrieved = vectorStore.search(
                embeddingModel.embed(sample.getQuery()), 
                k
            );
            
            Set<String> retrievedIds = retrieved.stream()
                .map(Document::getId)
                .collect(Collectors.toSet());
            
            Set<String> relevantIds = new HashSet<>(
                sample.getRelevantDocIds()
            );
            
            // 计算交集
            Set<String> intersection = new HashSet<>(retrievedIds);
            intersection.retainAll(relevantIds);
            
            double precision = (double) intersection.size() / k;
            double recall = (double) intersection.size() / relevantIds.size();
            
            totalPrecision += precision;
            totalRecall += recall;
        }
        
        return Map.of(
            "precision@" + k, totalPrecision / samples.size(),
            "recall@" + k, totalRecall / samples.size()
        );
    }
}
```

### 2.2 无标注评估（LLM-as-Judge）

```java
// LLM 评估检索相关性
@Service
public class LLMRetrievalEvaluator {
    
    private static final String RELEVANCE_PROMPT = """
        请评估以下检索结果与查询的相关性。
        
        查询：{query}
        
        检索到的文档：
        {document}
        
        请判断该文档与查询的相关性等级：
        - HIGH：文档直接回答了查询
        - MEDIUM：文档部分相关，包含有用信息
        - LOW：文档与查询无关
        - NONE：完全无关
        
        只输出等级，不要解释。
        """;
    
    public RelevanceLevel evaluateRelevance(
        String query, 
        Document document
    ) {
        String prompt = RELEVANCE_PROMPT
            .replace("{query}", query)
            .replace("{document}", document.getContent());
        
        String result = llm.generate(prompt).trim().toUpperCase();
        
        return switch (result) {
            case "HIGH" -> RelevanceLevel.HIGH;
            case "MEDIUM" -> RelevanceLevel.MEDIUM;
            case "LOW" -> RelevanceLevel.LOW;
            default -> RelevanceLevel.NONE;
        };
    }
    
    /**
     * 批量评估并计算平均相关性分数
     */
    public double evaluateBatch(
        List<RetrievalSample> samples
    ) {
        double totalScore = 0;
        int count = 0;
        
        for (RetrievalSample sample : samples) {
            List<Document> retrieved = vectorStore.search(
                embeddingModel.embed(sample.getQuery()), 
                5
            );
            
            for (Document doc : retrieved) {
                RelevanceLevel level = evaluateRelevance(
                    sample.getQuery(), 
                    doc
                );
                totalScore += level.getScore();
                count++;
            }
        }
        
        return count > 0 ? totalScore / count : 0;
    }
}

enum RelevanceLevel {
    HIGH(1.0),
    MEDIUM(0.5),
    LOW(0.2),
    NONE(0.0);
    
    private final double score;
    
    RelevanceLevel(double score) {
        this.score = score;
    }
    
    public double getScore() {
        return score;
    }
}
```

---

## 3. 生成质量评估

### 3.1 忠实性评估（Faithfulness）

```java
// 忠实性评估
@Service
public class FaithfulnessEvaluator {
    
    private static final String FAITHFULNESS_PROMPT = """
        请评估以下回答是否忠实于提供的上下文。
        
        上下文：
        {context}
        
        回答：
        {answer}
        
        请判断：
        1. 回答中的每个事实是否都能在上下文中找到依据？
        2. 回答是否包含上下文中没有的信息（幻觉）？
        
        输出格式：
        忠实性分数：0-1（1表示完全忠实）
        幻觉内容：如果有，列出幻觉部分
        """;
    
    public FaithfulnessResult evaluate(
        String context,
        String answer
    ) {
        String prompt = FAITHFULNESS_PROMPT
            .replace("{context}", context)
            .replace("{answer}", answer);
        
        String result = llm.generate(prompt);
        
        // 解析结果
        return parseFaithfulnessResult(result);
    }
    
    /**
     * 基于 NLI（自然语言推理）的忠实性评估
     */
    public double evaluateByNLI(String context, String answer) {
        // 将回答拆分为声明
        List<String> claims = extractClaims(answer);
        
        int faithfulCount = 0;
        for (String claim : claims) {
            // 使用 NLI 模型判断：上下文是否蕴含该声明
            NLIResult nli = nliModel.predict(context, claim);
            
            if (nli == NLIResult.ENTAILMENT) {
                faithfulCount++;
            }
        }
        
        return (double) faithfulCount / claims.size();
    }
}

@Data
public class FaithfulnessResult {
    private double score;           // 0-1
    private List<String> hallucinations;
    private List<String> supportedFacts;
}
```

### 3.2 答案相关性评估

```java
// 答案相关性评估
@Service
public class AnswerRelevanceEvaluator {
    
    /**
     * 基于嵌入相似度的相关性
     */
    public double evaluateByEmbedding(String query, String answer) {
        float[] queryVec = embeddingModel.embed(query);
        float[] answerVec = embeddingModel.embed(answer);
        
        return cosineSimilarity(queryVec, answerVec);
    }
    
    /**
     * 基于 LLM 的判断
     */
    public RelevanceScore evaluateByLLM(String query, String answer) {
        String prompt = """
            请评估以下回答是否针对问题。
            
            问题：{query}
            回答：{answer}
            
            评分标准：
            - 5分：完全回答了问题
            - 4分：基本回答了问题，略有遗漏
            - 3分：部分相关，但不够完整
            - 2分：相关性较低
            - 1分：几乎无关
            
            输出：分数（1-5）
            """
            .replace("{query}", query)
            .replace("{answer}", answer);
        
        String result = llm.generate(prompt).trim();
        int score = Integer.parseInt(result.replaceAll("[^0-9]", ""));
        
        return new RelevanceScore(score / 5.0, "LLM judged");
    }
    
    /**
     * 基于问题生成的相关性（逆向评估）
     */
    public double evaluateByQuestionGeneration(String answer) {
        // 让 LLM 基于回答生成可能的问题
        String genPrompt = """
            基于以下回答，生成3个可能的问题：
            {answer}
            """.replace("{answer}", answer);
        
        String generatedQuestions = llm.generate(genPrompt);
        
        // 检查生成的问题与原问题的相似度
        // ...
        
        return similarity;
    }
}
```

### 3.3 上下文利用率评估

```java
// 上下文利用率评估
@Service
public class ContextUtilizationEvaluator {
    
    /**
     * 评估使用了上下文的哪些部分
     */
    public ContextUtilizationResult evaluate(
        String context,
        String answer
    ) {
        // 将上下文分句
        List<String> contextSentences = splitSentences(context);
        
        List<String> utilizedSentences = new ArrayList<>();
        List<String> unusedSentences = new ArrayList<>();
        
        for (String sentence : contextSentences) {
            // 检查回答是否使用了该句信息
            boolean utilized = checkUtilization(answer, sentence);
            
            if (utilized) {
                utilizedSentences.add(sentence);
            } else {
                unusedSentences.add(sentence);
            }
        }
        
        double utilizationRate = (double) utilizedSentences.size() 
            / contextSentences.size();
        
        return ContextUtilizationResult.builder()
            .utilizationRate(utilizationRate)
            .utilizedSentences(utilizedSentences)
            .unusedSentences(unusedSentences)
            .build();
    }
    
    private boolean checkUtilization(String answer, String sentence) {
        // 方法1：嵌入相似度
        float[] answerVec = embeddingModel.embed(answer);
        float[] sentenceVec = embeddingModel.embed(sentence);
        double similarity = cosineSimilarity(answerVec, sentenceVec);
        
        // 方法2：关键词重叠
        Set<String> answerWords = extractKeywords(answer);
        Set<String> sentenceWords = extractKeywords(sentence);
        
        Set<String> intersection = new HashSet<>(answerWords);
        intersection.retainAll(sentenceWords);
        
        double overlap = (double) intersection.size() / sentenceWords.size();
        
        // 综合判断
        return similarity > 0.7 || overlap > 0.5;
    }
}
```

---

## 4. 常见问题与优化

### 4.1 检索问题

| 问题 | 症状 | 解决方案 |
|------|------|---------|
| **检索不到** | 相关文档未出现在 Top-K | 优化 Embedding、混合检索、查询改写 |
| **检索不准** | 返回大量无关文档 | 重排序、调整相似度阈值、元数据过滤 |
| **上下文过长** | 检索文档超出 LLM 限制 | 上下文压缩、Rerank 后截断、分层检索 |
| **多跳推理** | 需要多个文档联合推理 | Graph RAG、Agentic RAG、多步检索 |

### 4.2 生成问题

| 问题 | 症状 | 解决方案 |
|------|------|---------|
| **幻觉** | 生成上下文外的信息 | 严格提示工程、忠实性约束、Self-RAG |
| **不相关** | 回答与问题无关 | 查询改写、答案相关性过滤 |
| **不完整** | 遗漏关键信息 | 多文档融合、上下文扩展 |
| **冗长** | 回答过于啰嗦 | 输出长度限制、摘要优化 |

### 4.3 优化策略

#### 检索优化

```java
// 1. 查询改写优化
public String optimizeQuery(String originalQuery) {
    // HyDE：生成假设回答
    String hypothetical = llm.generate(
        "基于问题生成一个简短回答：" + originalQuery
    );
    
    // 融合原始查询和假设回答
    return originalQuery + " " + hypothetical;
}

// 2. 混合检索权重调优
public List<Document> optimizedHybridSearch(String query) {
    // 动态调整权重
    double denseWeight = query.contains("概念") ? 0.7 : 0.5;
    double sparseWeight = 1 - denseWeight;
    
    return hybridSearch(query, denseWeight, sparseWeight);
}

// 3. 重排序优化
public List<Document> optimizedRerank(
    String query, 
    List<Document> candidates
) {
    // 粗排：快速筛选
    List<Document> coarse = candidates.stream()
        .filter(d -> fastScore(query, d) > 0.5)
        .limit(20)
        .collect(Collectors.toList());
    
    // 精排：Cross-Encoder
    return reranker.rerank(query, coarse, 5);
}
```

#### 生成优化

```java
// 1. 提示工程优化
public String buildOptimizedPrompt(String context, String query) {
    return String.format("""
        你是一个专业的问答助手。请严格基于以下上下文回答问题。
        如果上下文中没有相关信息，请明确说明"根据提供的资料无法回答"。
        不要编造信息。
        
        上下文：
        %s
        
        问题：%s
        
        请用简洁的语言回答，并引用上下文中的相关信息。
        """, context, query);
}

// 2. 忠实性约束
public String generateWithFaithfulnessConstraint(
    String context, 
    String query
) {
    String prompt = buildOptimizedPrompt(context, query);
    String answer = llm.generate(prompt);
    
    // 后处理：检查忠实性
    FaithfulnessResult check = faithfulnessEvaluator.evaluate(
        context, answer
    );
    
    if (check.getScore() < 0.8) {
        // 重新生成或标记
        answer = answer + "\n\n[注意：部分内容可能超出上下文范围]";
    }
    
    return answer;
}
```

### 4.4 持续优化流程

```
收集反馈
    ↓
分析问题（检索/生成）
    ↓
定位原因
    ↓
实施优化
    ↓
A/B 测试
    ↓
评估效果
    ↓
部署上线
```

---

## 5. Java 实现示例

### 5.1 完整评估 Pipeline

```java
@Service
public class RAGEvaluationPipeline {
    
    @Autowired
    private RetrievalEvaluator retrievalEvaluator;
    
    @Autowired
    private FaithfulnessEvaluator faithfulnessEvaluator;
    
    @Autowired
    private AnswerRelevanceEvaluator relevanceEvaluator;
    
    /**
     * 端到端评估
     */
    public RAGEvaluationResult evaluate(RAGTestDataset dataset) {
        List<TestSample> samples = dataset.getSamples();
        
        // 1. 检索评估
        RetrievalMetrics retrievalMetrics = evaluateRetrieval(samples);
        
        // 2. 生成评估
        GenerationMetrics generationMetrics = evaluateGeneration(samples);
        
        // 3. 综合评估
        return RAGEvaluationResult.builder()
            .retrievalMetrics(retrievalMetrics)
            .generationMetrics(generationMetrics)
            .overallScore(calculateOverallScore(
                retrievalMetrics, 
                generationMetrics
            ))
            .build();
    }
    
    private RetrievalMetrics evaluateRetrieval(List<TestSample> samples) {
        double hitRateAt5 = retrievalEvaluator.hitRateAtK(samples, 5);
        double hitRateAt10 = retrievalEvaluator.hitRateAtK(samples, 10);
        double mrr = retrievalEvaluator.mrr(samples);
        Map<String, Double> pr = retrievalEvaluator.precisionRecallAtK(samples, 5);
        
        return RetrievalMetrics.builder()
            .hitRateAt5(hitRateAt5)
            .hitRateAt10(hitRateAt10)
            .mrr(mrr)
            .precisionAt5(pr.get("precision@5"))
            .recallAt5(pr.get("recall@5"))
            .build();
    }
    
    private GenerationMetrics evaluateGeneration(List<TestSample> samples) {
        double totalFaithfulness = 0;
        double totalRelevance = 0;
        
        for (TestSample sample : samples) {
            // 执行 RAG
            RAGResult result = ragService.query(sample.getQuery());
            
            // 评估忠实性
            FaithfulnessResult faith = faithfulnessEvaluator.evaluate(
                result.getContext(),
                result.getAnswer()
            );
            totalFaithfulness += faith.getScore();
            
            // 评估相关性
            double relevance = relevanceEvaluator.evaluateByEmbedding(
                sample.getQuery(),
                result.getAnswer()
            );
            totalRelevance += relevance;
        }
        
        int n = samples.size();
        return GenerationMetrics.builder()
            .avgFaithfulness(totalFaithfulness / n)
            .avgRelevance(totalRelevance / n)
            .build();
    }
}

// 评估结果
@Data
@Builder
public class RAGEvaluationResult {
    private RetrievalMetrics retrievalMetrics;
    private GenerationMetrics generationMetrics;
    private double overallScore;
    private LocalDateTime evaluatedAt;
}
```

### 5.2 评估报告生成

```java
@Service
public class EvaluationReportService {
    
    public String generateReport(RAGEvaluationResult result) {
        StringBuilder report = new StringBuilder();
        
        report.append("# RAG 评估报告\n\n");
        report.append("评估时间：").append(result.getEvaluatedAt()).append("\n\n");
        
        // 检索指标
        report.append("## 检索指标\n\n");
        RetrievalMetrics rm = result.getRetrievalMetrics();
        report.append(String.format("- Hit Rate@5: %.2f%%\n", rm.getHitRateAt5() * 100));
        report.append(String.format("- Hit Rate@10: %.2f%%\n", rm.getHitRateAt10() * 100));
        report.append(String.format("- MRR: %.3f\n", rm.getMrr()));
        report.append(String.format("- Precision@5: %.2f%%\n", rm.getPrecisionAt5() * 100));
        report.append(String.format("- Recall@5: %.2f%%\n", rm.getRecallAt5() * 100));
        
        // 生成指标
        report.append("\n## 生成指标\n\n");
        GenerationMetrics gm = result.getGenerationMetrics();
        report.append(String.format("- 平均忠实性: %.2f%%\n", gm.getAvgFaithfulness() * 100));
        report.append(String.format("- 平均相关性: %.2f%%\n", gm.getAvgRelevance() * 100));
        
        // 综合评分
        report.append("\n## 综合评分\n\n");
        report.append(String.format("**%.1f/100**\n", result.getOverallScore() * 100));
        
        // 建议
        report.append("\n## 优化建议\n\n");
        report.append(generateRecommendations(result));
        
        return report.toString();
    }
    
    private String generateRecommendations(RAGEvaluationResult result) {
        List<String> recommendations = new ArrayList<>();
        
        if (result.getRetrievalMetrics().getHitRateAt5() < 0.8) {
            recommendations.add("- 检索命中率较低，建议优化 Embedding 模型或尝试混合检索");
        }
        
        if (result.getGenerationMetrics().getAvgFaithfulness() < 0.8) {
            recommendations.add("- 生成忠实性较低，建议加强提示工程约束或引入 Self-RAG");
        }
        
        if (recommendations.isEmpty()) {
            return "各项指标良好，继续保持！";
        }
        
        return String.join("\n", recommendations);
    }
}
```

---

> 📌 下一步学习：
> - [08-java-rag-practice.md](./08-java-rag-practice.md) - Java 实战：构建完整 RAG 系统
