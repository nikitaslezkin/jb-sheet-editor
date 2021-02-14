package com.slezkin.table.listener;

import com.slezkin.table.CellCoordinates;
import com.slezkin.table.ExcelTableModel;

import java.util.StringTokenizer;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;

/**
 * Class describing logic for processing hotkeys for copy-paste, deleting a cell or subtable.
 */
public final class CellHotKeyListener implements ActionListener {
    private Clipboard system;
    private final JTable table;

    public CellHotKeyListener(JTable table) {
        this.table = table;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, false);
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, false);
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false);

        this.table.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
        this.table.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        this.table.registerKeyboardAction(this, "Delete", delete, JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    private void actionCopy() {
        StringBuilder sbf = new StringBuilder();

        int numcols = table.getSelectedColumnCount();
        int numrows = table.getSelectedRowCount();
        int[] rowsselected = table.getSelectedRows();
        int[] colsselected = table.getSelectedColumns();

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                sbf.append(((ExcelTableModel) table.getModel()).getDataAt(rowsselected[i], colsselected[j]));
                if (j < numcols - 1) sbf.append("\t");
            }
            sbf.append("\n");
        }
        StringSelection stsel = new StringSelection(sbf.toString());
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
        system.setContents(stsel, stsel);
    }

    private void actionPaste() {
        int startRow = (table.getSelectedRows())[0];
        int startCol = (table.getSelectedColumns())[0];
        try {
            String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
            StringTokenizer st1 = new StringTokenizer(trstring, "\n");
            for (int i = 0; st1.hasMoreTokens(); i++) {
                String rowstring = st1.nextToken();
                String[] st2 = rowstring.split("\t", -1);
                for (int j = 0; j < st2.length; j++) {
                    String value = st2[j];
                    if (startRow + i < table.getRowCount() && startCol + j < table.getColumnCount()) {
                        CellCoordinates cell = new CellCoordinates(startRow + i, startCol + j);
                        ((ExcelTableModel) table.getModel()).insertValueRecursively(value, cell);
                    }
                }
            }
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            table.setRowSelectionInterval(startRow, startRow);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void actionDelete() {
        int numcols = table.getSelectedColumnCount();
        int numrows = table.getSelectedRowCount();
        int[] rowsselected = table.getSelectedRows();
        int[] colsselected = table.getSelectedColumns();

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                CellCoordinates cell = new CellCoordinates(rowsselected[i], colsselected[j]);
                ((ExcelTableModel) table.getModel()).insertValueRecursively("", cell);
            }
        }
        table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
        table.setRowSelectionInterval(rowsselected[0], rowsselected[numrows - 1]);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Copy":
                actionCopy();
                break;
            case "Paste":
                actionPaste();
                break;
            case "Delete":
                actionDelete();
                break;
        }
    }
}