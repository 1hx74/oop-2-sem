package gui;

import javax.swing.*;

import static javax.swing.JOptionPane.showConfirmDialog;

public class ProgramExit {
    public static void exit() {
        int choose = showConfirmDialog(
                null, "EXIT?", "title", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

        switch (choose) {
            case 0:
                System.exit(0);
                break;
            case 1:
                break;
        }
    }
}
