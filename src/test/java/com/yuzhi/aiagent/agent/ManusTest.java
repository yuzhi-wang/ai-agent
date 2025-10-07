package com.yuzhi.aiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManusTest {

    @Resource
    private YuManus yuManus;

    @Test
    void run() {
        String userPrompt = """  
                我的另一半居住在杭州市上城区，请帮我找到 5 公里内合适的约会地点，  
                并结合一些网络图片，制定一份详细的约会计划，  
                并以 PDF 格式输出""";
        String answer = yuManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}

