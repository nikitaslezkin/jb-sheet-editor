package com.slezkin.table;

import com.slezkin.Constants;
import com.slezkin.table.listener.CellEditionListener;
import com.slezkin.table.listener.CellHotKeyListener;
import com.slezkin.table.listener.CellSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *  Class responsible for visualization of the main table in the application.
 */
public final class Table {

    private final JTable table;
    private final JScrollPane scrollPane;

    public Table() {
        ExcelTableModel excelTableModel = new ExcelTableModel(Constants.TABLE_ROW_COUNT, Constants.TABLE_COLUMN_COUNT);
        table = new JTable(excelTableModel) {
            public String getToolTipText(MouseEvent e) {
                Point point = e.getPoint();
                return ((ExcelTableModel) getModel()).getToolTipAt(rowAtPoint(point), columnAtPoint(point));
            }

            public Point getToolTipLocation(MouseEvent e) {
                Point point = e.getPoint();
                point.y += 15;
                return point;
            }
        };

        table.setFillsViewportHeight(true);
        table.setRowHeight(Constants.CELL_HEIGHT);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        scrollPane = new JScrollPane(table);
        JTable rowTable = new RowNumberTable(table);
        scrollPane.setRowHeaderView(rowTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());
        table.setRowSelectionInterval(0, 0);
        table.setColumnSelectionInterval(0, 0);
        table.getSelectionModel().addListSelectionListener(new CellSelectionListener(this));
        table.getColumnModel().getSelectionModel().addListSelectionListener(new CellSelectionListener(this));
        new CellEditionListener(this);
        new CellHotKeyListener(table);
        setSelection(new CellCoordinates(0, 0));
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTable getTable() {
        return table;
    }

    public String getValue(CellCoordinates cell) {
        return ((ExcelTableModel) table.getModel()).getValueAt(cell.getRow(), cell.getColumn());
    }

    public String getData(CellCoordinates cell) {
        return ((ExcelTableModel) table.getModel()).getDataAt(cell.getRow(), cell.getColumn());
    }

    public void clear() {
        ((ExcelTableModel) table.getModel()).clear();
        update();
    }

    public void setSelection(CellCoordinates cell) {
        table.setRowSelectionInterval(cell.getRow(), cell.getRow());
        table.setColumnSelectionInterval(cell.getColumn(), cell.getColumn());
    }

    public void update() {
        setSelection(new CellCoordinates(Constants.TABLE_ROW_COUNT - 1, Constants.TABLE_COLUMN_COUNT - 1));
        setSelection(new CellCoordinates(0, 0));
    }

    public void shiftDown(CellCoordinates cell) {
        update();
        setSelection(new CellCoordinates(cell.getRow() + 1, cell.getColumn()));
    }
}
