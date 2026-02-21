package gui;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Localize {
    private ResourceBundle bundle;
    private String language;

    public Localize(String language) {
        this.language = language;
        loadBundle();
    }

    public void switchLocale(String language) {
        this.language = language;
        loadBundle();
    }

    private void loadBundle() {
        bundle = ResourceBundle.getBundle("gui.resources.gui.messages", new Locale(language));
    }

    public String tr(String key) {
        return bundle.getString(key);
    }

    public void localizeInternalFrame() {
        UIManager.put("InternalFrameTitlePane.closeButtonText", tr("internal.close"));
        UIManager.put("InternalFrameTitlePane.iconifyButtonText", tr("internal.iconify"));
        UIManager.put("InternalFrameTitlePane.restoreButtonText", tr("internal.restore"));
        UIManager.put("InternalFrameTitlePane.maximizeButtonText", tr("internal.maximize"));
        UIManager.put("InternalFrameTitlePane.minimizeButtonText", tr("internal.minimize"));
        UIManager.put("InternalFrameTitlePane.moveButtonText", tr("internal.move"));
        UIManager.put("InternalFrameTitlePane.sizeButtonText", tr("internal.size"));
        UIManager.put("InternalFrame.closeButtonToolTip", tr("internal.close"));
        UIManager.put("InternalFrame.iconButtonToolTip", tr("internal.iconify"));
        UIManager.put("InternalFrame.maxButtonToolTip", tr("internal.maximize"));
        UIManager.put("InternalFrame.restoreButtonToolTip", tr("internal.minimize"));
    }
}
