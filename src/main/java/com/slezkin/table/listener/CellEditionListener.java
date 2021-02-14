package com.slezkin.table.listener;

import com.slezkin.table.CellCoordinates;
import com.slezkin.table.ExcelTableModel;
import com.slezkin.table.Table;

import java.beans.*;

/**
 * Class describing logic for changing a table cell after an input to it.
 */
public final class CellEditionListener implements PropertyChangeListener {

    private final Table table;

    public CellEditionListener(Table table) {
        this.table = table;
        this.table.getTable().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("tableCellEditor".equals(e.getPropertyName()) && !table.getTable().isEditing()) {
            processEditingStopped();
        }
    }

    private void processEditingStopped() {
        CellCoordinates cell = new CellCoordinates(table.getTable().getEditingRow(), table.getTable().getEditingColumn());
        String currentValue = table.getValue(cell);
        ((ExcelTableModel) table.getTable().getModel()).insertValueRecursively(currentValue, cell);
        table.shiftDown(cell);
    }
}