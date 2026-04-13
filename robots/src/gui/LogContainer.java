package gui;

import log.LogEntry;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LogContainer implements Iterable<LogEntry> {

    private final int capacity;
    private final LogEntry[] buffer;
    private long writeCount = 0;

    public LogContainer(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
        this.buffer = new LogEntry[capacity];
    }

    public synchronized void add(LogEntry entry) {
        buffer[(int)(writeCount % capacity)] = entry;
        writeCount++;
    }

    public synchronized LogEntry get(long index) {
        validateIndex(index);
        return buffer[(int)(index % capacity)];
    }

    public synchronized int size() {
        return (int) Math.min(writeCount, capacity);
    }

    public synchronized long firstIndex() {
        return Math.max(0L, writeCount - capacity);
    }

    public synchronized long lastIndex() {
        return writeCount;
    }

    public synchronized LogEntry[] getRange(long from, long to) {
        long safeFrom = Math.max(from, Math.max(0L, writeCount - capacity));
        long safeTo = Math.min(to, writeCount);
        if (safeFrom >= safeTo) return new LogEntry[0];

        LogEntry[] result = new LogEntry[(int)(safeTo - safeFrom)];
        for (int i = 0; i < result.length; i++)
            result[i] = buffer[(int)((safeFrom + i) % capacity)];
        return result;
    }

    @Override
    public synchronized Iterator<LogEntry> iterator() {
        long from = Math.max(0L, writeCount - capacity);
        long until = writeCount;
        LogEntry[] snapshot = new LogEntry[(int)(until - from)];
        for (int i = 0; i < snapshot.length; i++)
            snapshot[i] = buffer[(int)((from + i) % capacity)];
        return new ArrayIterator(snapshot);
    }

    private static class ArrayIterator implements Iterator<LogEntry> {
        private final LogEntry[] data;
        private int cursor = 0;
        ArrayIterator(LogEntry[] data) { this.data = data; }
        public boolean hasNext() { return cursor < data.length; }
        public LogEntry next() {
            if (!hasNext()) throw new NoSuchElementException();
            return data[cursor++];
        }
    }

    private void validateIndex(long index) {
        long oldest = Math.max(0L, writeCount - capacity);
        if (index < oldest || index >= writeCount)
            throw new IndexOutOfBoundsException(
                    "index " + index + " out of range [" + oldest + ", " + writeCount + ")");
    }

    public LogEntry[] getInnerArray() {
        return buffer;
    }
}