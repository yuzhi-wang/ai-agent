package com.yuzhi.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PgVectorVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorVectorStore;
    


    @Test
    void pgVectorStore() {
        List<Document> documents = List.of(
                new Document("loveapp有什么用？找对象。解决婚恋问题", Map.of("meta1", "meta1")),
                new Document("loveapp婚恋问题解决方案《恋爱23事》"),
                new Document("app是在苹果官方商城上架的", Map.of("meta2", "meta2")));
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("婚恋问题怎么办").topK(5).build());
        Assertions.assertNotNull(results);

    }
}