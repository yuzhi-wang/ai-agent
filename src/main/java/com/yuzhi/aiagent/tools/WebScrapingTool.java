package com.yuzhi.aiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapingTool {

    // 最大返回字符数（安全值，留出空间给其他上下文）
    private static final int MAX_CONTENT_LENGTH = 8000;

    @Tool(description = "Scrape the content of a web page")
    public String scrapeWebPage(
            @ToolParam(description = "URL of the webpage to scrape, must start with http:// or https://") String url) {

        try {
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .get();

            String title = doc.title();
            // 提取正文：优先 article, .content, main 等
            Elements content = doc.select("article, .content, main, .article-body, .post");
            if (content.isEmpty()) {
                content = doc.select("p");
            }

            StringBuilder text = new StringBuilder();
            text.append("【标题】").append(title).append("\n\n");
            text.append("【正文】\n");
            for (Element el : content) {
                String t = el.text().trim();
                if (!t.isEmpty()) {
                    text.append(t).append("\n\n");
                }
            }

            // 截断
            String result = text.toString();
            if (result.length() > MAX_CONTENT_LENGTH) {
                result = result.substring(0, MAX_CONTENT_LENGTH) + "\n...（内容过长，已截断）";
            }

            return result;

        } catch (IOException e) {
            return "无法访问网页: " + e.getMessage();
        } catch (Exception e) {
            return "解析网页时出错: " + e.getMessage();
        }
    }
}
