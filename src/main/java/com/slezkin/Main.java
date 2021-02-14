package com.slezkin;

import com.slezkin.field.CellEditField;
import com.slezkin.field.CellNameField;
import com.slezkin.menu.Menu;
import com.slezkin.table.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

/**
 * Main class where the form is initialized.
 */
public class Main extends JFrame {

    final Table mainTable;
    final JPanel tablePanel;
    public static JTextField cellNameField;
    public static JTextField cellEditField;

    JLabel getImage() {
        try {
            BufferedImage myPicture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("function.png")));
            return new JLabel(new ImageIcon(myPicture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Main(String title) {
        super(title);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setLocation(Constants.WINDOW_LOCATION_X, Constants.WINDOW_LOCATION_Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ToolTipManager.sharedInstance().setInitialDelay(0);

        Container mainContainer = this.getContentPane();
        mainContainer.setLayout(new BorderLayout(0, 0));

        mainTable = new Table();
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(mainTable.getScrollPane());

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        cellNameField = CellNameField.makeField(mainTable);
        cellEditField = CellEditField.makeField(mainTable);
        editPanel.add(cellNameField);
        editPanel.add(getImage());
        editPanel.add(cellEditField);

        mainContainer.add(editPanel, BorderLayout.NORTH);
        mainContainer.add(tablePanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(Menu.createFileMenu(mainTable));
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        Main gui = new Main(Constants.WINDOW_NAME);
        gui.setVisible(true);
    }
}
