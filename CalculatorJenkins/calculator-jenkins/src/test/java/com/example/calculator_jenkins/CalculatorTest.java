package com.example.calculator_jenkins;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalculatorTest {
    private Calculator calculator = new Calculator();

    @org.junit.jupiter.api.Test
    public void testAdd() {
        int result = calculator.add(10, 4);
        org.junit.jupiter.api.Assertions.assertEquals(14, result);
    }

    @org.junit.jupiter.api.Test
    public void testSubtract() {
        int result = calculator.subtract(5, 4);
        org.junit.jupiter.api.Assertions.assertEquals(1, result);
    }

    @org.junit.jupiter.api.Test
    public void testMultiply() {
        int result = calculator.multiply(5, 4);
        org.junit.jupiter.api.Assertions.assertEquals(20, result);
    }
}
