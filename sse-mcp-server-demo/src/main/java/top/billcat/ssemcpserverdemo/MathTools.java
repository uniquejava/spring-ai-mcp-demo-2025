package top.billcat.ssemcpserverdemo;

import org.springframework.ai.tool.annotation.Tool;

public class MathTools {
    @Tool(description = "Add two numbers.")
    public int add(int a, int b) {
        return a + b;
    }

    @Tool(description = "Multiply two numbers")
    public int multiply(int a, int b) {
        return a * b;
    }
}
