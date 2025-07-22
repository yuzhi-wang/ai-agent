package com.yuzhi.aiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

/**
 * 阿里云DashScope API客户端
 * 使用Hutool工具类发送HTTP请求
 * Http方式调用阿里云灵积
 */
public class HttpAiInvoke {
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private final String apiKey;
    
    public HttpAiInvoke(String apiKey) {
        this.apiKey = apiKey;
    }
    
    /**
     * 发送聊天请求到DashScope API
     * 
     * @param model 模型名称，如 "qwen-plus"
     * @param systemMessage 系统消息
     * @param userMessage 用户消息
     * @return API响应结果
     */
    public String sendChatRequest(String model, String systemMessage, String userMessage) {
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", model);
        
        // 构建消息数组
        JSONArray messages = new JSONArray();
        
        // 添加系统消息
        JSONObject systemMsg = new JSONObject();
        systemMsg.set("role", "system");
        systemMsg.set("content", systemMessage);
        messages.add(systemMsg);
        
        // 添加用户消息
        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", userMessage);
        messages.add(userMsg);
        
        requestBody.set("messages", messages);
        
        // 发送HTTP请求
        HttpResponse response = HttpRequest.post(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .timeout(30000) // 30秒超时
                .execute();
        
        // 检查响应状态
        if (response.isOk()) {
            return response.body();
        } else {
            throw new RuntimeException("API请求失败: " + response.getStatus() + " - " + response.body());
        }
    }
    
    /**
     * 解析API响应，提取AI回复内容
     * 
     * @param responseBody API响应体
     * @return AI回复的内容
     */
    public String parseResponse(String responseBody) {
        try {
            JSONObject response = JSONUtil.parseObj(responseBody);
            JSONArray choices = response.getJSONArray("choices");
            if (choices != null && choices.size() > 0) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                return message.getStr("content");
            }
            return "无法解析响应内容";
        } catch (Exception e) {
            return "解析响应失败: " + e.getMessage();
        }
    }
    
    /**
     * 使用示例方法
     */
    public static void main(String[] args) {
        // 请替换为您的实际API密钥
        String apiKey = TestApiKey.API_KEY;

        HttpAiInvoke client = new HttpAiInvoke(apiKey);
        
        try {
            // 发送请求
            String response = client.sendChatRequest(
                "qwen-plus",
                "You are a helpful assistant.",
                "你能做什么？？"
            );
            
            System.out.println("API响应: " + response);
            
            // 解析并显示AI回复
            String aiReply = client.parseResponse(response);
            System.out.println("AI回复: " + aiReply);
            
        } catch (Exception e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 