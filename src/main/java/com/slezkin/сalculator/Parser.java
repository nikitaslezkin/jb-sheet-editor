package com.slezkin.сalculator;

import com.slezkin.Constants;
import com.slezkin.table.CellCoordinates;
import com.slezkin.table.ExcelTableModel;

import java.util.LinkedList;
import java.util.List;

/**
 * "Middleman" class for table and equation parser. It contains extra functions, necessary for the correct parsing of arithmetic equations.
 */
public class Parser {

    public static String eval(final String function, final List<String> vars, final List<Double> values) {

        double result = 0;
        if ((function != null) && !function.isEmpty()) {
            try {
                if (vars.size() != 0 && values.size() == 0)
                    throw new Exception();
                Function fun = new Function(function);
                result = fun.getValue(values, vars);

            } catch (final CalculatorException e) {
                return e.getMessage();
            } catch (Exception e) {
                return Constants.TOOLTIP_RELATED_CELLS;
            }
        }

        return Double.toString(result);
    }

    public static List<String> getVars(String function) {
        StringBuilder cell = new StringBuilder();
        function += "!";
        List<String> varsList = new LinkedList<>();
        for (char ch : function.toCharArray()) {
            if ('A' <= ch && ch <= 'Z' || '0' <= ch && ch <= '9') {
                cell.append(ch);
            } else {
                boolean isNumber = false;
                boolean isLetter = false;
                boolean isGood = true;
                for (char chCell : cell.toString().toCharArray()) {
                    if (!isNumber && !isLetter && Character.isLetter(chCell)) {
                        isLetter = true;
                    } else if (isLetter && !isNumber && Character.isDigit(chCell)) {
                        isNumber = true;
                    } else if (isLetter && isNumber && Character.isDigit(chCell)) {
                        isNumber = true;
                    } else {
                        isGood = false;
                        break;
                    }
                }
                if (isGood && isLetter) {
                    if (Integer.parseInt(cell.substring(1)) <= Constants.TABLE_ROW_COUNT && Integer.parseInt(cell.substring(1)) >= 1)
                        varsList.add(cell.toString());
                }
                cell = new StringBuilder();
            }
        }

        return varsList;
    }

    private static List<Double> getValuesOfVars(ExcelTableModel table, List<String> varsList) {
        List<Double> valuesList = new LinkedList<>();
        try {
            for (String var : varsList) {
                CellCoordinates cell = new CellCoordinates(var);
                String stringValue = table.getValueAt(cell.getRow(), cell.getColumn());
                double value;
                if (stringValue == null || stringValue.equals("")) {
                    value = 0.0;
                } else {
                    value = Double.parseDouble(stringValue);
                }
                valuesList.add(value);
            }
        } catch (Exception e) {
            valuesList = new LinkedList<>();
        }
        return valuesList;
    }

    public static String parseValue(ExcelTableModel table, String formula) {
        List<String> varsName = getVars(formula);
        List<Double> valuesList = getValuesOfVars(table, varsName);
        return eval(formula, varsName, valuesList);
    }
}
