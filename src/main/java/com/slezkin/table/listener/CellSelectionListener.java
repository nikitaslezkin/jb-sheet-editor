package com.slezkin.table.listener;

import com.slezkin.Main;
import com.slezkin.table.CellCoordinates;
import com.slezkin.table.Table;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class describing logic for behavior of table and its related elements upon selecting a certain cell.
 */
public final class CellSelectionListener implements ListSelectionListener {

    private final Table table;

    public CellSelectionListener(Table table) {
        this.table = table;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        int currentRow = table.getTable().getSelectedRow();
        int currentColumn = table.getTable().getSelectedColumn();
        if (currentRow == -1 || currentColumn == -1)
            return;
        CellCoordinates cell = new CellCoordinates(currentRow, currentColumn);
        String text = table.getData(cell);
        Main.cellEditField.setText(text);
        Main.cellNameField.setText(cell.getStringName());
    }
}