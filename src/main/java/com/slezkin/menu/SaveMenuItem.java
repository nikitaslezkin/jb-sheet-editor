package com.slezkin.menu;

import com.slezkin.Constants;
import com.slezkin.table.CellCoordinates;
import com.slezkin.table.ExcelTableModel;
import com.slezkin.table.Table;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * Class responsible for saving created table into a file.
 */
final public class SaveMenuItem {

    static JMenuItem makeItem(Table table) {
        final JMenuItem save = new JMenuItem(Constants.SAVE, UIManager.getIcon("FileView.floppyDriveIcon"));

        save.addActionListener(arg0 -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Table (.tbl)", "tbl"));
            String path = Objects.requireNonNull(OpenMenuItem.class.getClassLoader().getResource("tblfiles/")).toString().substring(5);
            fc.setCurrentDirectory(new File(path));
            int returnVal = fc.showSaveDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                try {
                    File file = fc.getSelectedFile();
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);

                    Map<Integer, String> map = ((ExcelTableModel)table.getTable().getModel()).getAllData();
                    for (Map.Entry<Integer, String> entry : map.entrySet()) {
                        CellCoordinates cell = new CellCoordinates(entry.getKey());
                        bw.write(cell.getRow() + "\t" + cell.getColumn() + "\t" + entry.getValue() + "\n");
                    }

                    bw.close();
                    fw.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return save;
    }
}
