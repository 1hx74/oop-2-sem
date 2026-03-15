package gui;

import javax.swing.*;

public class ProgramExit {

    static private Localize localize;
    static private Runnable beforeExit;

    ProgramExit(Localize localize, Runnable beforeExit) {
        ProgramExit.localize = localize;
        ProgramExit.beforeExit = beforeExit;
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

        if (choose == 0) {
            if (beforeExit != null) {
                beforeExit.run();
            }
            System.exit(0);
        }
    }
}