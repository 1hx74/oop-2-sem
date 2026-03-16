package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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

public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final String defaultLookAndFeel;
    private String nowLookAndFeel;
    private Localize localize;
    private GameWindow gameWindow;
    private LogWindow logWindow;
    private AppState state;

    public MainApplicationFrame() {

        state = AppState.load();
        localize = new Localize(state.getLocale());

        new ProgramExit(localize, this::saveState);

        defaultLookAndFeel = UIManager.getLookAndFeel().getClass().getName();
        nowLookAndFeel = defaultLookAndFeel;

        String savedLaf = state.getLookAndFeel();
        if (savedLaf != null) {
            nowLookAndFeel = savedLaf;
            setLookAndFeel();
        }

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);

        gameWindow = createGameWindow();
        state.loadFrameGeometry(gameWindow.getPrefix(), gameWindow, 230, 10, 300, 300);
        addWindow(gameWindow);

        logWindow = createLogWindow();
        state.loadFrameGeometry(logWindow.getPrefix(), logWindow, 10, 10, 210, 400);
        addWindow(logWindow);

        state.restoreFrameState(gameWindow.getPrefix(), gameWindow);
        SwingUtilities.invokeLater(() -> {
            state.restoreFrameState(logWindow.getPrefix(), logWindow);
            SwingUtilities.invokeLater(() -> {
                // вручную слева направо
                int x = 0;
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    if (frame.isIcon()) {
                        JInternalFrame.JDesktopIcon icon = frame.getDesktopIcon();
                        int iconWidth = icon.getWidth();
                        int iconHeight = icon.getHeight();
                        int y = desktopPane.getHeight() - iconHeight;
                        icon.setLocation(x, y);
                        x += iconWidth;
                    }
                }
                state.restoreSelectedFrame(gameWindow.getPrefix(), gameWindow);
                state.restoreSelectedFrame(logWindow.getPrefix(), logWindow);
            });
        });         //

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                ProgramExit.exit();
            }
        });
    }

    protected GameWindow createGameWindow() {
        GameWindow newGame = new GameWindow(localize);
        newGame.setLocation(230, 10);
        newGame.setSize(300, 300);
        setMinimumSize(newGame.getSize());
        return newGame;
    }

    protected LogWindow createLogWindow() {
        logWindow = new LogWindow(localize, Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(210, 400);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(localize.tr("log.started"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createMenu(localize.tr("menu.view"), KeyEvent.VK_V, localize.tr("menu.view"));
        addMenuItem(lookAndFeelMenu, localize.tr("menu.view.default"), KeyEvent.VK_S,
                e -> { nowLookAndFeel = defaultLookAndFeel; setLookAndFeel(); this.invalidate(); });
        addMenuItem(lookAndFeelMenu, localize.tr("menu.view.system"), KeyEvent.VK_S,
                e -> { nowLookAndFeel = UIManager.getSystemLookAndFeelClassName(); setLookAndFeel(); this.invalidate(); });
        addMenuItem(lookAndFeelMenu, localize.tr("menu.view.universal"), KeyEvent.VK_S,
                e -> { nowLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName(); setLookAndFeel(); this.invalidate(); });

        JMenu testMenu = createMenu(localize.tr("menu.tests"), KeyEvent.VK_T, localize.tr("menu.tests"));
        addMenuItem(testMenu, localize.tr("menu.tests.log"), KeyEvent.VK_S, e -> Logger.debug(localize.tr("menu.tests.log")));

        JMenu localMenu = createMenu(localize.tr("menu.locale"), KeyEvent.VK_T, localize.tr("menu.locale"));
        addMenuItem(localMenu, localize.tr("menu.locale.en"), KeyEvent.VK_E, e -> {
            localize.switchLocale("en");
            refreshUI();
        });
        addMenuItem(localMenu, localize.tr("menu.locale.ru"), KeyEvent.VK_R, e -> {
            localize.switchLocale("ru");
            refreshUI();
        });

        JMenu exitMenu = createMenu(localize.tr("menu.exit"), KeyEvent.VK_T, localize.tr("menu.exit"));
        addMenuItem(exitMenu, localize.tr("menu.exit.item"), KeyEvent.VK_S, e -> ProgramExit.exit());

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(localMenu);
        menuBar.add(exitMenu);

        return menuBar;
    }

    private JMenu createMenu(String title, int mnemonic_key, String accessibleDescription) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic_key);
        menu.getAccessibleContext().setAccessibleDescription(accessibleDescription);
        return menu;
    }

    private void addMenuItem(JMenu menu, String title, int mnemonic_key, ActionListener action) {
        JMenuItem item = new JMenuItem(title, mnemonic_key);
        item.addActionListener(action);
        menu.add(item);
    }

    private void saveState() {

        state.setLocale(localize.getLocaleCode());
        state.setLookAndFeel(nowLookAndFeel);
        state.saveFrame(this.getPrefix(), this);
        state.saveFrame(gameWindow.getPrefix(), gameWindow);
        state.saveFrame(logWindow.getPrefix(), logWindow);

        state.save();
    }

    private String getPrefix() {
        return "main";
    }

    private void refreshUI() {
        localize.localizeInternalFrame();
        setJMenuBar(generateMenuBar());
        SwingUtilities.updateComponentTreeUI(this);
        gameWindow.updateLocalization();
        logWindow.updateLocalization();
    }

    void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(nowLookAndFeel);
            localize.localizeInternalFrame();
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}