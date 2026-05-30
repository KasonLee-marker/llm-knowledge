package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module8 单元测试
 */
class Module8Test {

    @Test
    @DisplayName("Module8 - 基本结构验证")
    void testModule8_exists() {
        Module8 module = new Module8();
        assertNotNull(module, "Module8 实例不应为 null");
    }

    @Test
    @DisplayName("Module8 - main 方法可正常调用")
    void testModule8_main() {
        assertDoesNotThrow(() -> Module8.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
