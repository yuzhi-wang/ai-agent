package com.yuzhi.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "恋爱宝典.pdf";
        String content = "恋爱原创助手，请查看www.baidu.com";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
