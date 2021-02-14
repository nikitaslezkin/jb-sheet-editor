package com.slezkin.menu;

import com.slezkin.Constants;
import com.slezkin.table.Table;

import javax.swing.JMenuItem;
import javax.swing.UIManager;

/**
 * Class responsible for making a new table sheet.
 */
final public class NewMenuItem {

    static JMenuItem makeItem(Table table) {
        final JMenuItem newItem = new JMenuItem(Constants.NEW, UIManager.getIcon("FileChooser.newFolderIcon"));
        newItem.addActionListener(arg0 -> table.clear());
        return newItem;
    }
}
