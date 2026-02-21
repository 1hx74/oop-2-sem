package gui;

import javax.swing.*;

public class ProgramExit {
    public static void exit() {
        Object[] options = {"ОК", "Отмена"};
        int choose = JOptionPane.showOptionDialog(
                null,
                "Выйти?",
                "Подтверждение",
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