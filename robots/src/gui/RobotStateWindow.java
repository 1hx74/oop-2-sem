package gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RobotStateWindow extends AbstractWindow {
    private final RobotState m_robotState;
    private final JLabel m_label = new JLabel();

    public RobotStateWindow(Localize localize, RobotState robotState) {
        super(localize);
        this.m_robotState = robotState;

        m_robotState.addObserver(() ->
                SwingUtilities.invokeLater(this::updateLabel)
        );

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_label, BorderLayout.CENTER);
        getContentPane().add(panel);
        updateLabel();
        pack();
        // setSize(300, 60);
        setResizable(false);
    }

    private void updateLabel() {
        m_label.setText(String.format("%s: %.2f  %s: %.2f  %s: %.1f%s",
                localize.tr("window.robotstate.x"),
                m_robotState.getPositionX(),
                localize.tr("window.robotstate.y"),
                m_robotState.getPositionY(),
                localize.tr("window.robotstate.dir"),
                Math.toDegrees(m_robotState.getDirection()),
                localize.tr("window.robotstate.degrees")));
    }

    public void updateLocalization() {
        setTitle(localize.tr(getPathToName()));
        this.updateLabel();
    }

    @Override
    public String getPathToName() {
        return "window.robotstate.name";
    }

    @Override
    public String getPrefix() {
        return "robotstate";
    }
}