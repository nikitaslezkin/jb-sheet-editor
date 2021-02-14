package com.slezkin.menu;

import com.slezkin.Constants;
import com.slezkin.table.Table;

import javax.swing.JMenu;

/**
 * Class for creating application menu.
 */
final public class Menu {

    public static JMenu createFileMenu(Table table) {
        final JMenu file = new JMenu(Constants.FILE);

        file.add(NewMenuItem.makeItem(table));
        file.add(OpenMenuItem.makeItem(table));
        file.add(SaveMenuItem.makeItem(table));
        file.addSeparator();
        file.add(ExitMenuItem.makeItem());

        return file;
    }
}
