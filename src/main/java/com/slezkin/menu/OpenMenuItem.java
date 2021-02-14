package com.slezkin.menu;

import com.slezkin.Constants;
import com.slezkin.table.CellCoordinates;
import com.slezkin.table.ExcelTableModel;
import com.slezkin.table.Table;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

/**
 * Class responsible for opening the file of a new table.
 */
final public class OpenMenuItem {

    static JMenuItem makeItem(Table table) {
        final JMenuItem openItem = new JMenuItem(Constants.OPEN, UIManager.getIcon("FileChooser.upFolderIcon"));

        openItem.addActionListener(arg0 -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Table (.tbl)", "tbl"));
            String path = Objects.requireNonNull(OpenMenuItem.class.getClassLoader().getResource("tblfiles/")).toString().substring(5);
            fc.setCurrentDirectory(new File(path));
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File file2 = fc.getSelectedFile();
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }

                    FileReader fr = new FileReader(file2.getAbsoluteFile());
                    BufferedReader br = new BufferedReader(fr);

                    String line;
                    table.clear();
                    while ((line = br.readLine()) != null && line.length() != 0) {
                        String[] line2 = line.split("\t");
                        int currentRow = Integer.parseInt(line2[0]);
                        int currentColumn = Integer.parseInt(line2[1]);
                        String currentValue = line2[2];
                        CellCoordinates cell = new CellCoordinates(currentRow, currentColumn);
                        ((ExcelTableModel) table.getTable().getModel()).insertValueRecursively(currentValue, cell);
                        table.update();
                    }

                    br.close();
                    fr.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return openItem;
    }
}
