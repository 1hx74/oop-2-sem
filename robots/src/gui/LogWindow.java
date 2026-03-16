package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends AbstractWindow implements LogChangeListener {
    private LogWindowSource m_logSource;
    private JTextArea m_logContent;
    private Localize localize;

    public LogWindow(Localize localize, LogWindowSource logSource) {
        super(localize);
        this.localize = localize;
        m_logSource = logSource;
        m_logSource.registerListener(this);

        m_logContent = new JTextArea("");
        m_logContent.setEditable(false);
        m_logContent.setFocusable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(m_logContent), BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
    }

    public void updateLocalization() {
        setTitle(localize.tr("window.log.name"));
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public String getPathToName() {
        return "window.log.name";
    }
}
