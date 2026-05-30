package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module2 单元测试
 */
class Module2Test {

    @Test
    @DisplayName("Module2 - 基本结构验证")
    void testModule2_exists() {
        Module2 module = new Module2();
        assertNotNull(module, "Module2 实例不应为 null");
    }

    @Test
    @DisplayName("Module2 - main 方法可正常调用")
    void testModule2_main() {
        assertDoesNotThrow(() -> Module2.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
