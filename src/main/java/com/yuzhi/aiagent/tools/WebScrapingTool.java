package com.yuzhi.aiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapingTool {

    @Tool(description = "Scrape the content of a web page")
    public String scrapeWebPage(@ToolParam(description = "URL of the webpage to scrape") String url) {
        try {
            Document doc =  Jsoup.connect(url).get();
            return doc.html();
        } catch (IOException e) {
            return "Error scraping webpage" + e.getMessage();
        }
    }
}
