package com.slezkin.field;

import com.slezkin.Constants;
import com.slezkin.table.CellCoordinates;
import com.slezkin.table.Table;
import com.slezkin.сalculator.Parser;

import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class responsible for making field for showing or entering cell's name.
 */
final public class CellNameField {

    public static JTextField makeField(Table table) {
        final JTextField cellField = new JTextField(Constants.CELL_NAME_FIELD_SIZE);
        cellField.setText("A1");
        KeyListener cellKeyListener = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && Parser.getVars(cellField.getText()).size() == 1) {
                    CellCoordinates cell = new CellCoordinates(cellField.getText());
                    table.getTable().scrollRectToVisible(table.getTable().getCellRect(cell.getRow(), cell.getColumn(), true));
                    table.setSelection(cell);
                }
            }
        };
        cellField.addKeyListener(cellKeyListener);

        return cellField;
    }
}
