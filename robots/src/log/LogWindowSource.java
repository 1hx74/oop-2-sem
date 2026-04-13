package log;

import gui.LogContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LogWindowSource {

    private final LogContainer m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        if (iQueueLength <= 0) throw new IllegalArgumentException("Queue length must be > 0");
        m_messages = new LogContainer(iQueueLength);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);

        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }

        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public int size() {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(long startFrom, long count) {
        long to = Math.min(startFrom + count, m_messages.lastIndex());
        if (startFrom >= to) return Collections.emptyList();
        return Arrays.asList(m_messages.getRange(startFrom, to));
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}