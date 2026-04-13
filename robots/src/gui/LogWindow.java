package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends AbstractWindow implements LogChangeListener {
    private LogWindowSource m_logSource;
    private JTextArea m_logContent;
    private Localize localize;

    private JButton btnUp;
    private JButton btnDown;

    private long offset = 0;

    public LogWindow(Localize localize, LogWindowSource logSource) {
        super(localize);
        this.localize = localize;
        m_logSource = logSource;
        m_logSource.registerListener(this);

        m_logContent = new JTextArea("");
        m_logContent.setEditable(false);
        m_logContent.setFocusable(false);

        btnUp = new JButton("▲");
        btnDown = new JButton("▼");

        btnUp.addActionListener(this::scrollUp);
        btnDown.addActionListener(this::scrollDown);

        JPanel buttons = new JPanel(new GridLayout(2, 1));
        buttons.add(btnUp);
        buttons.add(btnDown);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);

        getContentPane().add(panel);
        setSize(400, 300);

        moveToBottom();

        updateLogContent();
    }

    private int getVisibleLineCount() {
        int height = m_logContent.getHeight();
        int lineHeight = m_logContent.getFontMetrics(m_logContent.getFont()).getHeight();

        if (lineHeight == 0) return 10;

        return Math.max(1, height / lineHeight);
    }

    private void moveToBottom() {
        int visible = getVisibleLineCount();
        long total = m_logSource.size();
        offset = Math.max(0, total - visible);
    }

    private void scrollUp(ActionEvent e) {
        int visible = getVisibleLineCount();
        int step = Math.max(1, visible / 2);
        offset = Math.max(0, offset - step);
        updateLogContent();
    }

    private void scrollDown(ActionEvent e) {
        int visible = getVisibleLineCount();
        int step = Math.max(1, visible / 2);
        long total = m_logSource.size();
        offset = Math.min(total - visible, offset + step);
        updateLogContent();
    }

    private void updateButtons() {
        int visible = getVisibleLineCount();
        long total = m_logSource.size();

        btnUp.setEnabled(offset > 0);
        btnDown.setEnabled(offset + visible < total);
    }

    private void updateLogContent() {
        int visibleLines = getVisibleLineCount();
        long total = m_logSource.size();

        // защита от выхода за границы
        if (offset > total) {
            offset = Math.max(0, total - visibleLines);
        }

        StringBuilder content = new StringBuilder();

        for (LogEntry entry : m_logSource.range(offset, visibleLines)) {
            content.append(entry.getMessage()).append("\n");
        }

        m_logContent.setText(content.toString());

        updateButtons();
    }

    public void updateLocalization() {
        setTitle(localize.tr("window.log.name"));
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(() -> {
            int visible = getVisibleLineCount();
            long total = m_logSource.size();

            if (offset + visible >= total - visible) {
                moveToBottom();
            }

            updateLogContent();
        });
    }

    @Override
    public String getPathToName() {
        return "window.log.name";
    }

    @Override
    public String getPrefix() {
        return "log";
    }
}