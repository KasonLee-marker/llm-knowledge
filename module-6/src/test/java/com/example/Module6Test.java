package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module6 单元测试
 */
class Module6Test {

    @Test
    @DisplayName("Module6 - 基本结构验证")
    void testModule6_exists() {
        Module6 module = new Module6();
        assertNotNull(module, "Module6 实例不应为 null");
    }

    @Test
    @DisplayName("Module6 - main 方法可正常调用")
    void testModule6_main() {
        assertDoesNotThrow(() -> Module6.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
