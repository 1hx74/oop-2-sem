package gui;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class AppState {

    private final Properties props = new Properties();

    private static Path getFile() {
        return Path.of("Robots", "src", "gui", "resources", "gui", "app_state.properties");
    }

    public static AppState load() {
        AppState state = new AppState();

        try (FileInputStream in = new FileInputStream(getFile().toFile())) {
            state.props.load(in);
        } catch (IOException ignored) {}

        return state;
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(getFile().toFile())) {
            props.store(out, "Application State");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLookAndFeel() {
        return props.getProperty("lookAndFeel", null);
    }

    public void setLookAndFeel(String laf) {
        props.setProperty("lookAndFeel", laf);
    }

    public String getLocale() {
        return props.getProperty("locale.current", "en");
    }

    public void setLocale(String locale) {
        props.setProperty("locale.current", locale);
    }

    public void saveFrame(String prefix, JInternalFrame frame) {
        props.setProperty(prefix + ".title", String.valueOf(frame.getTitle()));
        props.setProperty(prefix + ".x", String.valueOf(frame.getX()));
        props.setProperty(prefix + ".y", String.valueOf(frame.getY()));
        props.setProperty(prefix + ".width", String.valueOf(frame.getWidth()));
        props.setProperty(prefix + ".height", String.valueOf(frame.getHeight()));
        props.setProperty(prefix + ".iconified", String.valueOf(frame.isIcon()));
        props.setProperty(prefix + ".selected", String.valueOf(frame.isSelected()));
    }

    public void saveFrame(String prefix, JFrame frame) {
        props.setProperty(prefix + ".title", String.valueOf(frame.getTitle()));
        props.setProperty(prefix + ".x", String.valueOf(frame.getX()));
        props.setProperty(prefix + ".y", String.valueOf(frame.getY()));
        props.setProperty(prefix + ".width", String.valueOf(frame.getWidth()));
        props.setProperty(prefix + ".height", String.valueOf(frame.getHeight()));
        props.setProperty(prefix + ".visible", String.valueOf(frame.isVisible()));
    }

    public void loadFrameGeometry(String prefix, JInternalFrame frame, int defaultX, int defaultY, int defaultW, int defaultH) {
        frame.setLocation(
                Integer.parseInt(props.getProperty(prefix + ".x", String.valueOf(defaultX))),
                Integer.parseInt(props.getProperty(prefix + ".y", String.valueOf(defaultY)))
        );
        frame.setSize(
                Integer.parseInt(props.getProperty(prefix + ".width", String.valueOf(defaultW))),
                Integer.parseInt(props.getProperty(prefix + ".height", String.valueOf(defaultH)))
        );
    }

    public void restoreFrameState(String prefix, JInternalFrame frame) {
        boolean iconified = Boolean.parseBoolean(
                props.getProperty(prefix + ".iconified", "false")
        );

        if (iconified) {
            SwingUtilities.invokeLater(() -> {
                try {
                    frame.setIcon(true);
                    frame.getDesktopPane().revalidate();
                    frame.getDesktopPane().repaint();
                } catch (Exception ignored) {}
            });
        }
    }

    public void restoreSelectedFrame(String prefix, JInternalFrame frame) {
        boolean iconified = Boolean.parseBoolean(
                props.getProperty(prefix + ".iconified", "false")
        );
        boolean selected = Boolean.parseBoolean(
                props.getProperty(prefix + ".selected", "false")
        );

        if (!iconified && selected) {
            SwingUtilities.invokeLater(() -> {
                try {
                    frame.setSelected(true);
                } catch (Exception ignored) {}
            });
        }
    }
}