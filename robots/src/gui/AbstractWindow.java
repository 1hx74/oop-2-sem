package gui;

import java.awt.Rectangle;
import javax.swing.JInternalFrame;

public abstract class AbstractWindow extends JInternalFrame {
    protected Localize localize;

    public AbstractWindow(Localize localize) {
        super("", true, true, true, true);
        this.localize = localize;
        setTitle(localize.tr(getPathToName()));
    }

    public abstract String getPathToName();

    /**
     * Возвращает информацию о текущем состоянии окна:
     * - bounds: положение и размеры (x, y, width, height)
     * - isIcon: свернуто ли окно
     * - isSelected: в фокусе ли окно
     */
    public WindowState getWindowState() {
        Rectangle bounds = getBounds();
        boolean isIcon = isIcon();
        boolean isSelected = isSelected();
        return new WindowState(bounds, isIcon, isSelected);
    }
}