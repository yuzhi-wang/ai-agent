package com.yuzhi.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter mytokenTextSplitter;

    @Resource
    private MykeywordEnricher mykeywordEnricher;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加载文档
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        // 自主切分文档
//        List<Document> splitDocument = mytokenTextSplitter.splitCustomized(documentList);
        // 自主提取关键词
        List<Document> enrichedDocument = mykeywordEnricher.enrichDocuments(documentList);
        // 初始化向量数据库
        simpleVectorStore.add(enrichedDocument);
        return simpleVectorStore;

    }



}

