package com.yuzhi.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 基于AI的文档元信息增强器（用ai为文档补充元信息）
 */

@Configuration
public class MykeywordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;

    List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.dashscopeChatModel, 5);
        return enricher.apply(documents);
    }
}
