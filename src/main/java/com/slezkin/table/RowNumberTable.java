package com.slezkin.table;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Class for including row names in the table. By default the table is made only with column names.
 */
public final class RowNumberTable extends JTable {
    private final JTable main;

    public RowNumberTable(JTable table) {
        main = table;

        setFocusable(false);
        setAutoCreateColumnsFromModel(false);
        setSelectionModel(main.getSelectionModel());

        TableColumn column = new TableColumn();
        column.setHeaderValue(" ");
        addColumn(column);
        column.setCellRenderer(new RowNumberRenderer());
        getColumnModel().getColumn(0).setPreferredWidth(50);
        setRowHeight(20);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    @Override
    public int getRowCount() {
        return main.getRowCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return Integer.toString(row + 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
    }

    private static class RowNumberRenderer extends DefaultTableCellRenderer {
        public RowNumberRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JTableHeader header = table.getTableHeader();
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
            setText(value.toString());
            return this;
        }
    }
}
