package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.Logger;

/**
 * Главная рамка приложения, содержащая рабочую область для внутренних окон (JInternalFrame)
 * и панель меню. Позволяет добавлять окна и управлять внешним видом приложения
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    private final String defaultLookAndFeel;

    /**
     * Конструктор главного окна приложения
     * Настраивает размеры окна с отступами от экрана, создаёт внутренние окна
     * (лог и игровое), создаёт меню и настраивает обработку закрытия окна
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.

        defaultLookAndFeel = UIManager.getLookAndFeel().getClass().getName();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                ProgramExit.exit();
            }
        });

    }

    /**
     * Создаёт окно логирования приложения
     * Настраивает его размер, положение и минимальный размер окна.
     * @return созданное окно логирования
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет внутреннее окно (JInternalFrame) в рабочую область
     * Делает окно видимым после добавления
     * @param frame внутреннее окно для добавления
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Создаёт панель меню приложения с пунктами
     * @return объект JMenuBar для установки в JFrame
     */
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");
        addMenuItem(lookAndFeelMenu, "Исходная схема", KeyEvent.VK_S,e -> { setLookAndFeel(defaultLookAndFeel); this.invalidate(); });
        addMenuItem(lookAndFeelMenu, "Системная схема", KeyEvent.VK_S, e -> { setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); this.invalidate(); });
        addMenuItem(lookAndFeelMenu, "Универсальная схема", KeyEvent.VK_S, e -> { setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); this.invalidate(); });
        JMenu testMenu = createMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        addMenuItem(testMenu, "Сообщение в лог", KeyEvent.VK_S, e -> Logger.debug("Новая строка"));

        JMenu exitMenu = createMenu("Выход", KeyEvent.VK_T, "Меню выхода");
        addMenuItem(exitMenu, "Выйти", KeyEvent.VK_S, e -> ProgramExit.exit());

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);

        return menuBar;
    }

    /**
     * Создаёт меню с заданным названием, мнемоникой и доступным описанием
     * @param title название меню
     * @param mnemonic_key клавиша-мнемоник для быстрого доступа
     * @param accessibleDescription описание для вспомогательных технологий
     * @return объект JMenu с заданными параметрами
     */
    private JMenu createMenu(String title, int mnemonic_key, String accessibleDescription) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic_key);
        menu.getAccessibleContext().setAccessibleDescription(accessibleDescription);
        return menu;
    }

    /**
     * Добавляет пункт меню к существующему JMenu
     * @param menu меню, к которому добавляется пункт
     * @param title название пункта меню
     * @param mnemonic_key клавиша-мнемоник для быстрого доступа
     * @param action обработчик события при выборе пункта
     */
    private void addMenuItem(JMenu menu, String title, int mnemonic_key, ActionListener action) {
        JMenuItem item = new JMenuItem(title, mnemonic_key);
        item.addActionListener(action);
        menu.add(item);
    }

    void localizeInternalFrame() {
        UIManager.put("InternalFrameTitlePane.closeButtonText", "Закрыть");
        UIManager.put("InternalFrameTitlePane.iconifyButtonText", "Свернуть");
        UIManager.put("InternalFrameTitlePane.restoreButtonText", "Восстановить");
        UIManager.put("InternalFrameTitlePane.maximizeButtonText", "Развернуть");
        UIManager.put("InternalFrameTitlePane.minimizeButtonText", "Свернуть");
        UIManager.put("InternalFrameTitlePane.moveButtonText", "Переместить");
        UIManager.put("InternalFrameTitlePane.sizeButtonText", "Изменить размер");
    }

    /**
     * Устанавливает Look and Feel приложения
     * @param className полное имя класса Look and Feel
     */
    void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            localizeInternalFrame();
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}