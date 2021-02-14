package com.slezkin.menu;

import com.slezkin.Constants;

import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Class describing application exit logic.
 */
final public class ExitMenuItem {

    static class ExitAction extends AbstractAction {
        ExitAction() {
            putValue(NAME, Constants.EXIT);
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    static JMenuItem makeItem() {
        return new JMenuItem(new ExitAction());
    }
}
