package com.yuzhi.aiagent.app;


import com.yuzhi.aiagent.advisor.ReReadingAdvisor;
import com.yuzhi.aiagent.advisor.YuLoggerAdvisor;
import com.yuzhi.aiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    public LoveApp(ChatModel dashscopeChatModel) {
        //初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/temp/chatmemory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
//        //初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        //会话记忆拦截器
                        new MessageChatMemoryAdvisor(chatMemory),

                        //自定义日志Advisor，按需开启
                        new YuLoggerAdvisor()

                        //自定义重读推理增强Advisor，按需开启
//                        ,new ReReadingAdvisor()

                )
                .build();


    }

    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();

        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;
    }

    //Java 21特性，使用record快速定义一个类
    record LoveReport(String title, List<String> suggestions) {

    }

    /**
     * AI 恋爱报告，结构化输出
     * @param message
     * @param chatId
     * @return
     */

    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport  = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);


        log.info("lovereport:{}", loveReport);
        return loveReport;
    }

    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    @Resource
    private VectorStore pgVectorVectorStore;

    /**
     * RAG 知识库对话
     * @param message
     * @param chatId
     * @return
     */
    public String  doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                //开启日志
                .advisors(new YuLoggerAdvisor())
                //应用RAG知识库问答
//                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
//                //应用RAG检索增强服务（基于云服务器）
//                .advisors(loveAppRagCloudAdvisor)
                //应用 RAG 检索增强 （基于PgVector向量存储）
                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                .call()
                .chatResponse();


        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;

        

    }
}
