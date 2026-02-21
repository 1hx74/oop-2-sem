package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer m_visualizer;
    Localize localize;

    public GameWindow(Localize localize) {
        super(localize.tr("window.game.name"), true, true, true, true);
        this.localize = localize;
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void updateLocalization() {
        setTitle(localize.tr("window.game.name"));
    }
}
