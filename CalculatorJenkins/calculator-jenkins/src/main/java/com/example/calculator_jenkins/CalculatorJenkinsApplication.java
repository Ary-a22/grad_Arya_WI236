package com.example.calculator_jenkins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalculatorJenkinsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculatorJenkinsApplication.class, args);

		Calculator calculator = new Calculator();
		int sum = calculator.add(5, 4);
		int difference = calculator.subtract(5, 4);
		int product = calculator.multiply(5, 4);
		System.out.println("Sum: " + sum);
		System.out.println("Difference: " + difference);
		System.out.println("Product: " + product);
	}

}
