package com.example.calculator_jenkins.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.calculator_jenkins.Calculator;

@RestController
public class CalculatorController {

    Calculator calculator = new Calculator();

    @GetMapping("/")
    public String getMethodName() {
        Integer sum = calculator.add(10, 4);
        Integer difference = calculator.subtract(5, 4);
        Integer product = calculator.multiply(5, 4);
        return "Welcome to the Calculator Application!" + "\nSum: " + sum + "\nDifference: " + difference + "\nProduct: " + product;
    }
}
