package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module5 单元测试
 */
class Module5Test {

    @Test
    @DisplayName("Module5 - 基本结构验证")
    void testModule5_exists() {
        Module5 module = new Module5();
        assertNotNull(module, "Module5 实例不应为 null");
    }

    @Test
    @DisplayName("Module5 - main 方法可正常调用")
    void testModule5_main() {
        assertDoesNotThrow(() -> Module5.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
