package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module9 单元测试
 */
class Module9Test {

    @Test
    @DisplayName("Module9 - 基本结构验证")
    void testModule9_exists() {
        Module9 module = new Module9();
        assertNotNull(module, "Module9 实例不应为 null");
    }

    @Test
    @DisplayName("Module9 - main 方法可正常调用")
    void testModule9_main() {
        assertDoesNotThrow(() -> Module9.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
