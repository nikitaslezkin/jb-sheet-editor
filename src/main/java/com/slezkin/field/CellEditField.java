package com.slezkin.field;

import com.slezkin.Constants;
import com.slezkin.table.CellCoordinates;
import com.slezkin.table.ExcelTableModel;
import com.slezkin.table.Table;

import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class responsible for cell's input field creation.
 */
final public class CellEditField {

    public static JTextField makeField(Table table) {
        final JTextField editField = new JTextField(Constants.CELL_EDIT_FIELD_SIZE);
        KeyListener tableKeyListener = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    CellCoordinates cell = new CellCoordinates(table.getTable().getSelectedRow(), table.getTable().getSelectedColumn());
                    String currentValue = editField.getText();
                    ((ExcelTableModel) table.getTable().getModel()).insertValueRecursively(currentValue, cell);
                    table.shiftDown(cell);
                }
            }
        };
        editField.addKeyListener(tableKeyListener);
        return editField;
    }
}
