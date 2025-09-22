package com.yuzhi.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "测试.txt";
        String content = fileOperationTool.readFile(fileName);
        Assertions.assertNotNull(content);
    }

    @Test
    void writeFile() {

        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "测试.txt";
        String content = "这是对于spring ai读写工具的测试";
        String result = fileOperationTool.writeFile(fileName, content);
        Assertions.assertNotNull(result);
    }
}