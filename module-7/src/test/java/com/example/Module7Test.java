package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module7 单元测试
 */
class Module7Test {

    @Test
    @DisplayName("Module7 - 基本结构验证")
    void testModule7_exists() {
        Module7 module = new Module7();
        assertNotNull(module, "Module7 实例不应为 null");
    }

    @Test
    @DisplayName("Module7 - main 方法可正常调用")
    void testModule7_main() {
        assertDoesNotThrow(() -> Module7.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
