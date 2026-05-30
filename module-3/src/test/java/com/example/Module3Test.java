package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module3 单元测试
 */
class Module3Test {

    @Test
    @DisplayName("Module3 - 基本结构验证")
    void testModule3_exists() {
        Module3 module = new Module3();
        assertNotNull(module, "Module3 实例不应为 null");
    }

    @Test
    @DisplayName("Module3 - main 方法可正常调用")
    void testModule3_main() {
        assertDoesNotThrow(() -> Module3.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
