package com.yuzhi.aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;


public class LangchainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel qwenChatModel  = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = qwenChatModel.chat("电脑打不开，如何找到问题");
        System.out.println(answer);
    }
}
