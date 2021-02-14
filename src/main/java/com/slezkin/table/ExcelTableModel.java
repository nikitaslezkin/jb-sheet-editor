package com.slezkin.table;

import com.slezkin.Constants;
import com.slezkin.сalculator.Parser;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class responsible for table's model. Contains main functionality for managing table data within application memory.
 */
public final class ExcelTableModel extends AbstractTableModel {

    private static class Cell {
        String data;
        String visual;
        String tooltip;

        public Cell() {
            this.data = "";
            this.visual = "";
        }
    }

    private final Map<Integer, Cell> table;
    private final int numRows;
    private final int numColumns;
    private Graph graph;

    public ExcelTableModel(int numRows, int numColumns) {
        this.table = new HashMap<>();
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.graph = new Graph();
    }

    @Override
    public int getRowCount() {
        return numRows;
    }

    @Override
    public int getColumnCount() {
        return numColumns;
    }

    @Override
    public String getValueAt(int row, int column) {
        int position = getPosition(row, column);
        if (table.containsKey(position))
            return table.get(position).visual;
        else return "";
    }

    public void setValueAt(Object value, int row, int column) {
        int position = getPosition(row, column);
        if (table.containsKey(position)) {
            Cell cell = table.get(position);
            cell.visual = (String) value;
        } else {
            Cell cell = new Cell();
            cell.visual = (String) value;
            table.put(position, cell);
        }
    }

    public String getDataAt(int row, int column) {
        int position = getPosition(row, column);
        if (table.containsKey(position))
            return table.get(position).data;
        else return "";
    }

    public void setDataAt(Object value, int row, int column) {
        int position = getPosition(row, column);
        if (table.containsKey(position)) {
            Cell cell = table.get(position);
            cell.data = (String) value;
        } else {
            Cell cell = new Cell();
            cell.data = (String) value;
            table.put(position, cell);
        }
    }

    public String getToolTipAt(int row, int column) {
        int position = getPosition(row, column);
        if (table.containsKey(position))
            return table.get(position).tooltip;
        else return "";
    }

    public void setToolTipAt(Object value, int row, int column) {
        int position = getPosition(row, column);
        table.get(position).tooltip = (String) value;
    }

    public Map<Integer, String> getAllData() {
        return table.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().data));
    }

    private void updateCellConnections(final String currentValue, CellCoordinates currentCell) {
        graph.clearVertexConnection(currentCell.getIntName());
        if (currentValue.isEmpty() || currentValue.charAt(0) != '=')
            return;
        List<String> varsName = Parser.getVars(currentValue.substring(1));
        for (String cell : varsName) {
            CellCoordinates cellCoordinates = new CellCoordinates(cell);
            graph.addEdge(currentCell.getIntName(), cellCoordinates.getIntName());
        }
    }

    private void insertValue(final String currentValue, final CellCoordinates currentCell) {
        String visualValue = currentValue;
        String toolTipValue = "";

        if (graph.isCellInCycle(currentCell.getIntName())) {
            visualValue = Constants.ERROR;
            toolTipValue = Constants.TOOLTIP_CYCLIC_CELLS;
        } else if (currentValue.length() >= 1 && currentValue.charAt(0) == '=') {
            visualValue = Parser.parseValue(this, currentValue.substring(1));
            try {
                Double.parseDouble(visualValue);
            } catch (NumberFormatException nfe) {
                toolTipValue = visualValue;
                visualValue = Constants.ERROR;
            }
        } else if (currentValue.length() >= 2 && currentValue.substring(0, 2).equals("'=")) {
            visualValue = visualValue.substring(1);
        }
        setValueAt(visualValue, currentCell.getRow(), currentCell.getColumn());
        setToolTipAt(toolTipValue, currentCell.getRow(), currentCell.getColumn());
    }

    public void insertValueRecursively(final String currentValue, final CellCoordinates currentCell) {
        setDataAt(currentValue, currentCell.getRow(), currentCell.getColumn());
        graph.addVertex(currentCell.getIntName());
        updateCellConnections(currentValue, currentCell);
        graph.getCellsInCycle();

        for (CellCoordinates cell : getTopologicalSort(currentCell)) {
            insertValue(getDataAt(cell.getRow(), cell.getColumn()), cell);
        }
    }

    private List<CellCoordinates> getTopologicalSort(CellCoordinates currentCell) {
        List<Integer> result = graph.getTopologicalSort(currentCell.getIntName());
        List<CellCoordinates> resultCoordinates = new LinkedList<>();
        for (Integer cell : result) {
            resultCoordinates.add(new CellCoordinates(cell));
        }
        Collections.reverse(resultCoordinates);
        return resultCoordinates;
    }

    private int getPosition(int row, int column) {
        return row * numColumns + column;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void clear() {
        table.clear();
        graph = new Graph();
    }

    private Set<TableModelListener> listeners = new HashSet<>();

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
}
