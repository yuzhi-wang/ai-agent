package com.yuzhi.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.yuzhi.aiagent.constant.FileDirConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.FileInputStream;

public class FileOperationTool {

    private final String FILE_DIR = FileDirConstant.FILE_SAVE_DIR + "/file";

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName) {
        String filepath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filepath);
        } catch (Exception e) {
            return "File reading error" + e.getMessage();
        }
    }


    public String writeFile(@ToolParam(description = "Name of a file to write") String fileName,
                            @ToolParam(description = "Content to write to this file") String content){

        String filepath = FILE_DIR + "/" + fileName;
        try {
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filepath);
            return "File writing successfully" + filepath;
        } catch (Exception e) {
            return "File writing error" + e.getMessage();
        }
    }
}
