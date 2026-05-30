package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Module4 单元测试
 */
class Module4Test {

    @Test
    @DisplayName("Module4 - 基本结构验证")
    void testModule4_exists() {
        Module4 module = new Module4();
        assertNotNull(module, "Module4 实例不应为 null");
    }

    @Test
    @DisplayName("Module4 - main 方法可正常调用")
    void testModule4_main() {
        assertDoesNotThrow(() -> Module4.main(new String[]{}),
                "main 方法应正常执行不抛异常");
    }
}
