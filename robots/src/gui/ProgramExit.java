package gui;

import javax.swing.*;

public class ProgramExit {

    static private Localize localize;

    ProgramExit(Localize localize) {
        this.localize = localize;
    }

    public static void exit() {
        Object[] options = {localize.tr("window.exit.ok"), localize.tr("window.exit.cancel")};
        int choose = JOptionPane.showOptionDialog(
                null,
                localize.tr("window.exit.exit?"),
                localize.tr("window.exit.confirm"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );
        switch (choose) {
            case 0:
                System.exit(0);
                break;
            case 1:
                break;
        }
    }
}