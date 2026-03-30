package gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RobotStateWindow extends AbstractWindow {
    private final RobotState m_robotState;
    private final JLabel m_label = new JLabel();
    private final FormatCombo formatCombo;

    public RobotStateWindow(Localize localize, RobotState robotState) {
        super(localize);
        this.m_robotState = robotState;
        formatCombo = new FormatCombo(localize, m_robotState, FormatCombo.Mode.STRING_BUILDER);

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
        m_label.setText(formatCombo.get());
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