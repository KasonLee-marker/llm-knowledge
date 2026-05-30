package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module10 单元测试
 */
class Module10Test {

    @Test
    @DisplayName("Module10 - 基本结构验证")
    void testModule10_exists() {
        Module10 module = new Module10();
        assertNotNull(module, "Module10 实例不应为 null");
    }

    @Test
    @DisplayName("Module10 - main 方法可正常调用")
    void testModule10_main() {
        assertDoesNotThrow(() -> Module10.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
