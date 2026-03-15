package gui;

import java.awt.*;

public class WindowState {
    public final Rectangle bounds;
    public final boolean isIcon;
    public final boolean isSelected;

    public WindowState(Rectangle bounds, boolean isIcon, boolean isSelected) {
        this.bounds = bounds;
        this.isIcon = isIcon;
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return String.format("bounds=%s, isIcon=%b, isSelected=%b",
                bounds, isIcon, isSelected);
    }
}