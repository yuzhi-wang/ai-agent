package com.yuzhi.aiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bing 搜索引擎工具类
 * 提供简单的网络搜索功能
 */
public class WebSearchTool {

    // SearchAPI 接口地址
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;  // API 密钥

    /**
     * 构造函数
     * @param apiKey SearchAPI 的认证密钥
     */
    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 使用 Bing 搜索引擎进行网络搜索
     * @param query 搜索关键词
     * @return 格式化后的搜索结果字符串
     */
    @Tool(description = "Search for information from Bing Search Engine")
    public String searchBing(@ToolParam(description = "Search query keyword") String query) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "bing");
        paramMap.put("num", 5);
        paramMap.put("safe_search", "moderate");

        try {
            // 发送请求
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);

            // 👉 调试：打印原始响应，非常重要！
            System.out.println("SearchAPI Response: " + response);

            JSONObject jsonObject = JSONUtil.parseObj(response);

            // 👉 检查是否存在 organic_results 字段
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");

            if (organicResults == null || organicResults.isEmpty()) {
                return "No results found or 'organic_results' field missing in response.";
            }

            // 格式化结果
            String formattedResults = organicResults.stream()
                    .limit(5)
                    .map(obj -> {
                        JSONObject result = (JSONObject) obj;
                        return String.format("标题: %s\n链接: %s\n摘要: %s",
                                result.getStr("title", "N/A"),
                                result.getStr("link", "N/A"),
                                result.getStr("snippet", "N/A"));
                    })
                    .collect(Collectors.joining("\n\n"));

            return formattedResults.isEmpty() ? "No results to display." : formattedResults;

        } catch (Exception e) {
            // 👉 更详细的错误信息（包括响应内容）
            return "Error during search: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }

    }
}
