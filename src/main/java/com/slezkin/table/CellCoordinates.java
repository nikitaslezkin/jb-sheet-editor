package com.slezkin.table;

import com.slezkin.Constants;

/**
 *  Class, instance of which can be used to keeping cell's name in several ways.
 */
public final class CellCoordinates {

    private final String stringName;
    private final int intName;
    private final int row, column;

    public CellCoordinates(String stringName) {
        this.stringName = stringName;
        this.row = Integer.parseInt(stringName.substring(1)) - 1;
        this.column = (int) stringName.charAt(0) - 65;
        this.intName = row * Constants.TABLE_COLUMN_COUNT + column;
    }

    public CellCoordinates(int intName) {
        this.intName = intName;
        this.column = intName % Constants.TABLE_COLUMN_COUNT;
        this.row = intName / Constants.TABLE_COLUMN_COUNT;
        this.stringName = String.valueOf((char) (column + 65)) + (row + 1);
    }

    public CellCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
        this.intName = row * Constants.TABLE_COLUMN_COUNT + column;
        this.stringName = String.valueOf((char) (column + 65)) + (row + 1);
    }

    public String getStringName() {
        return stringName;
    }

    public int getIntName() {
        return intName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
