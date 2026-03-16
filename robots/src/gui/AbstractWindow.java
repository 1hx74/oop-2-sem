package gui;

import javax.swing.JInternalFrame;

public abstract class AbstractWindow extends JInternalFrame {
    protected Localize localize;

    public AbstractWindow(Localize localize) {
        super("", true, true, true, true);
        this.localize = localize;
        setTitle(localize.tr(getPathToName()));

        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
                try {
                    setIcon(true);
                } catch (Exception ignored) {}
            }
        });
    }

    public abstract String getPathToName();

    public abstract String getPrefix();

}