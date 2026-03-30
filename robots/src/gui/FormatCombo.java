package gui;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;

public class FormatCombo {
    private final Localize localize;
    private final RobotState m_robotState;
    private final Mode mode;

    private final Object[] args = new Object[7];

    // кэш
    private static final ConcurrentHashMap<String, MessageFormat> MESSAGE_FORMAT_CACHE =
            new ConcurrentHashMap<>();

    private static final String PATTERN1 =
            "%s: %.2f  %s: %.2f  %s: %.1f%s";

    private static final String PATTERN2 =
            "{0}: {1,number,0.00}  {2}: {3,number,0.00}  {4}: {5,number,0.0}{6}";

    enum Mode { FORMATTER, MESSAGE_FORMAT_NO_CACHE, MESSAGE_FORMAT_CACHE, STRING_BUILDER }

    MessageFormat mf = MESSAGE_FORMAT_CACHE.computeIfAbsent(    //  вот эта штука и делает что бы мы один раз считали
            PATTERN2, MessageFormat::new);

    private final StringBuilder sb = new StringBuilder(64);

    FormatCombo(Localize localize, RobotState mRobotState, Mode mode) {
        this.localize = localize;
        this.m_robotState = mRobotState;
        this.mode = mode;
    }

    public String get() {
        return switch (mode) {
            case FORMATTER               -> formatWithFormatter();
            case MESSAGE_FORMAT_NO_CACHE -> formatWithMessageFormat();
            case MESSAGE_FORMAT_CACHE    -> formatWithMessageFormatCached();
            case STRING_BUILDER          -> formatWithStringBuilder();
        };
    }

    private String formatWithFormatter() {
        return String.format(PATTERN1,
                localize.tr("window.robotstate.x"),
                m_robotState.getPositionX(),
                localize.tr("window.robotstate.y"),
                m_robotState.getPositionY(),
                localize.tr("window.robotstate.dir"),
                Math.toDegrees(m_robotState.getDirection()),
                localize.tr("window.robotstate.degrees"));
    }

    private String formatWithMessageFormat() {
        return MessageFormat.format(PATTERN2,
                localize.tr("window.robotstate.x"),
                m_robotState.getPositionX(),
                localize.tr("window.robotstate.y"),
                m_robotState.getPositionY(),
                localize.tr("window.robotstate.dir"),
                Math.toDegrees(m_robotState.getDirection()),
                localize.tr("window.robotstate.degrees"));
    }

    private String formatWithMessageFormatCached() {
        args[0] = localize.tr("window.robotstate.x");
        args[1] = m_robotState.getPositionX();
        args[2] = localize.tr("window.robotstate.y");
        args[3] = m_robotState.getPositionY();
        args[4] = localize.tr("window.robotstate.dir");
        args[5] = Math.toDegrees(m_robotState.getDirection());
        args[6] = localize.tr("window.robotstate.degrees");
        return mf.format(args);
    }

    // string builder

    private String formatWithStringBuilder() {
        sb.setLength(0);
        sb.append(localize.tr("window.robotstate.x")).append(": ");
        appendDouble2(m_robotState.getPositionX());
        sb.append("  ");
        sb.append(localize.tr("window.robotstate.y")).append(": ");
        appendDouble2(m_robotState.getPositionY());
        sb.append("  ");
        sb.append(localize.tr("window.robotstate.dir")).append(": ");
        appendDouble1(Math.toDegrees(m_robotState.getDirection()));
        sb.append(localize.tr("window.robotstate.degrees"));
        return sb.toString();
    }

    private void appendDouble2(double v) {
        long scaled = Math.round(v * 100);
        long intPart = scaled / 100;
        long fracPart = Math.abs(scaled % 100);
        sb.append(intPart).append('.');
        if (fracPart < 10) sb.append('0');
        sb.append(fracPart);
    }

    private void appendDouble1(double v) {
        long scaled = Math.round(v * 10);
        long intPart = scaled / 10;
        long fracPart = Math.abs(scaled % 10);
        sb.append(intPart).append('.').append(fracPart);
    }
}