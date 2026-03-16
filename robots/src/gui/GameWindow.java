package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class GameWindow extends AbstractWindow {
    private final GameVisualizer m_visualizer;

    public GameWindow(Localize localize) {
        super(localize);
        m_visualizer = new GameVisualizer();

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void updateLocalization() {
        setTitle(localize.tr(getPathToName()));
    }

    @Override
    public String getPathToName() {
        return "window.game.name";
    }

    @Override
    public String getPrefix() {
        return "game";
    }
}