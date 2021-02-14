package com.slezkin.сalculator;

/**
 * Class representing exceptions related to processing and calculation of arithmetic expressions.
 */
public class CalculatorException extends Exception {

    public CalculatorException() {
        super();
    }

    public CalculatorException(final String message) {
        super(message);
    }
}
