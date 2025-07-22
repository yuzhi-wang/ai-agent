package com.yuzhi.aiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*
* 有Component即可启动run方法，去除注释即可
* */
// @Component
public class OllamaAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage message = ollamaChatModel.call(new Prompt("你好，你的名字"))
                .getResult()
                .getOutput();
        System.out.println(message.getText());
    }
}
