package com.slezkin.сalculator;

import com.slezkin.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing main logic for calculation of arithmetic expressions.
 */
public class Function {

    private final String f;
    private List<Double> values;
    private List<String> variables;

    static class ParseData {
        double value;
        StringBuilder number;
        String function;
        boolean hasNumber;
        boolean hasFunction;

        public ParseData(double value, StringBuilder number, String function, boolean hasNumber, boolean hasFunction) {
            this.value = value;
            this.number = number;
            this.function = function;
            this.hasNumber = hasNumber;
            this.hasFunction = hasFunction;
        }
    }

    public Function(final String f) {
        this.f = f.trim().replaceAll(" ", "").toLowerCase();
    }

    public double getValue(final List<Double> values, final List<String> variables) throws CalculatorException {
        final List<String> vars = new ArrayList<>();
        for (final String string : variables) {
            vars.add(string.trim().replaceAll(" ", "").toLowerCase());
        }
        this.values = values;
        this.variables = vars;
        return eval(f);
    }

    private int sum(String f, int i, ParseData data) throws CalculatorException {
        final String new_f = f.substring(i + 1);

        if (data.hasNumber) {
            double numb = Double.parseDouble(data.number.toString());
            data.value = numb + eval(new_f);
            data.hasNumber = false;
            data.number = new StringBuilder();
        } else if (data.hasFunction) {
            data.value = eval(data.function) + eval(new_f);
            data.hasFunction = false;
            data.function = "";
        } else {
            data.value = data.value + eval(new_f);
        }
        return i + new_f.length();
    }

    private int mul(String f, int i, ParseData data) throws CalculatorException {
        final String new_f = nextFunction(f.substring(i + 1));
        if (data.hasNumber) {
            double numb = Double.parseDouble(data.number.toString());
            data.value = numb * eval(new_f);
            data.hasNumber = false;
            data.number = new StringBuilder();
        } else if (data.hasFunction) {
            data.value = eval(data.function) * eval(new_f);
            data.hasFunction = false;
            data.function = "";
        } else {
            data.value = data.value * eval(new_f);
        }
        return i + new_f.length();
    }

    private int sub(String f, int i, ParseData data) throws CalculatorException {
        final String new_f = nextMinusFunction(f.substring(i + 1));
        if (data.hasNumber) {
            final double numb = Double.parseDouble(data.number.toString());
            data.value = numb - eval(new_f);
            data.hasNumber = false;
            data.number = new StringBuilder();
        } else if (data.hasFunction) {
            data.value = eval(data.function) - eval(new_f);
            data.hasFunction = false;
            data.function = "";
        } else {
            data.value = data.value - eval(new_f);
        }
        return i + new_f.length();
    }

    private int div(String f, int i, ParseData data) throws CalculatorException {
        final String new_f = nextFunction(f.substring(i + 1));
        if (data.hasNumber) {
            double numb = Double.parseDouble(data.number.toString());
            double evaluatedValue = eval(new_f);
            if (evaluatedValue == 0.0)
                throw new CalculatorException(Constants.TOOLTIP_DIVISION_BY_ZERO);
            data.value = numb / evaluatedValue;
            data.hasNumber = false;
            data.number = new StringBuilder();
        } else if (data.hasFunction) {
            double evaluatedValue = eval(new_f);
            if (evaluatedValue == 0.0)
                throw new CalculatorException(Constants.TOOLTIP_DIVISION_BY_ZERO);
            data.value = eval(data.function) / evaluatedValue;
            data.hasFunction = false;
            data.function = "";
        } else {
            data.value = data.value / eval(new_f);
        }
        return i + new_f.length();
    }

    private int pow(String f, int i, ParseData data) throws CalculatorException {
        final String new_f = nextFunction(f.substring(i + 1));

        if (data.hasNumber) {
            double numb = Double.parseDouble(data.number.toString());
            data.value = Math.pow(numb, eval(new_f));
            data.hasNumber = false;
            data.number = new StringBuilder();
        } else if (data.hasFunction) {
            data.value = Math.pow(eval(data.function), eval(new_f));
            data.hasFunction = false;
            data.function = "";
        } else {
            data.value = Math.pow(data.value, eval(new_f));
        }

        return i + new_f.length();
    }

    private int leftBracket(String f, int i, ParseData data) throws CalculatorException {
        if (i == (f.length() - 1)) {
            throw new CalculatorException(Constants.TOOLTIP_NOT_WELL_FORMED);
        }

        final String new_f = f.substring(i + 1, nextBracket(f));
        if (data.hasFunction) {
            switch (data.function) {
                case "sin":
                    data.value = Math.sin(eval(new_f));
                    break;
                case "cos":
                    data.value = Math.cos(eval(new_f));
                    break;
                case "tan":
                    data.value = Math.tan(eval(new_f));
                    break;
                case "ln":
                    data.value = Math.log(eval(new_f));
                    break;
                case "sqrt":
                    data.value = Math.sqrt(eval(new_f));
                    break;
                case "pow":
                    String[] functions = new_f.split(",");
                    if (functions.length != 2)
                        throw new CalculatorException(Constants.TOOLTIP_NOT_WELL_FORMED);
                    data.value = Math.pow(eval(functions[0]), eval(functions[1]));
                    break;
                default:
                    throw new CalculatorException(Constants.TOOLTIP_NOT_WELL_FORMED);
            }

            data.hasFunction = false;
            data.function = "";

        } else {
            data.value = eval(new_f);
        }
        return i + new_f.length() + 1;
    }

    private void getDigit(String f, int i, ParseData data, char character) throws CalculatorException {
        data.hasNumber = true;
        data.number.append(character);
        if (i == (f.length() - 1)) {
            try {
                data.value = Double.parseDouble(data.number.toString());
            } catch (Exception ex) {
                throw new CalculatorException(Constants.TOOLTIP_NOT_WELL_FORMED);
            }
            data.number = new StringBuilder();
            data.hasNumber = false;
        }
    }

    private void getCharacter(String f, int i, ParseData data, char character) throws CalculatorException {
        data.function = data.function + character;
        data.hasFunction = true;

        if (i == (f.length() - 1)) {
            if (data.function.equals("e")) {
                data.value = Math.E;
            } else if (data.function.equals("pi")) {
                data.value = Math.PI;
            } else {
                final int n = variables.indexOf(data.function);
                if (n >= 0) {
                    data.value = values.get(n);
                } else {
                    throw new CalculatorException(Constants.TOOLTIP_NOT_WELL_FORMED);
                }
            }
        }
    }

    private void dot(String f, int i, ParseData data, char character) throws CalculatorException {
        if (i == (f.length() - 1)) {
            throw new CalculatorException(Constants.TOOLTIP_NOT_WELL_FORMED);
        }
        if (data.hasNumber && (data.number.length() > 0)) {
            data.number.append(character);
        }
    }

    private double eval(final String f) throws CalculatorException {
        ParseData data = new ParseData(0, new StringBuilder(), "", false, false);

        for (int i = 0; i < f.length(); i++) {
            final char character = f.charAt(i);

            if (character >= '0' && character <= '9' && !data.hasFunction) {
                getDigit(f, i, data, character);
            } else if (character == '+') {
                i = sum(f, i, data);
            } else if (character == '*') {
                i = mul(f, i, data);
            } else if (character == '-') {
                i = sub(f, i, data);
            } else if (character == '/') {
                i = div(f, i, data);
            } else if (character == '^') {
                i = pow(f, i, data);
            } else if (character == '.') {
                dot(f, i, data, character);
            } else if (character == '(') {
                i = leftBracket(f, i, data);
            } else if (character == ')') {
                throw new CalculatorException(Constants.TOOLTIP_BRACKETS);
            } else if (isValidNumericAndCharacter(character)) {
                getCharacter(f, i, data, character);
            } else {
                throw new CalculatorException(Constants.TOOLTIP_INVALID_CHARACTER + character);
            }
        }
        return data.value;
    }

    private String nextFunction(final String f) throws CalculatorException {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < f.length(); i++) {
            final char character = f.charAt(i);

            if (character == '+' || character == '*' || character == '-' || character == '/') {
                i = f.length();
            } else if (character == '^' || character == '.' || isValidNumericAndCharacter(character)) {
                result.append(character);
            } else if (character == '(') {
                final String new_f = f.substring(i, nextBracket(f) + 1);
                result.append(new_f);
                i = (i + new_f.length()) - 1;
            } else if (character == ')') {
                throw new CalculatorException(Constants.TOOLTIP_BRACKETS);
            } else {
                throw new CalculatorException(Constants.TOOLTIP_INVALID_CHARACTER + character);
            }
        }
        return result.toString();
    }

    private String nextMinusFunction(String f) throws CalculatorException {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < f.length(); i++) {
            final char character = f.charAt(i);

            if (character == '+' || character == '-') {
                i = f.length();
            } else if (character == '*' || character == '/' || character == '^' || character == '.' || isValidNumericAndCharacter(character)) {
                result.append(character);
            } else if (character == '(') {
                final String new_f = f.substring(i, nextBracket(f) + 1);
                result.append(new_f);
                i = (i + new_f.length()) - 1;
            } else if (character == ')') {
                throw new CalculatorException(Constants.TOOLTIP_BRACKETS);
            } else {
                throw new CalculatorException(Constants.TOOLTIP_INVALID_CHARACTER + character);
            }
        }
        return result.toString();

    }

    private boolean isValidNumericAndCharacter(final char character) {
        boolean result = false;
        if ((character >= 'a' && character <= 'z') || (character >= '0' && character <= '9')) {
            result = true;
        }
        return result;
    }

    private int nextBracket(final String f) throws CalculatorException {
        int result = 0;
        int count = 0;
        for (int i = 0; i < f.length(); i++) {
            final char character = f.charAt(i);
            if (character == '(') {
                result = i;
                count++;
            } else if (character == ')') {
                result = i;
                count--;
                if (count == 0) {
                    return i;
                }
            } else {
                result = i;
            }
        }

        if (count != 0) {
            throw new CalculatorException(Constants.TOOLTIP_BRACKETS);
        }
        return result;
    }
}
