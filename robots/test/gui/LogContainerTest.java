package gui;

import log.LogEntry;
import log.LogLevel;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LogContainerTest {

    private LogEntry entry(int id) {
        return new LogEntry(LogLevel.Error, "msg-" + id);
    }

    @Test
    void testAddAndSize() {
        // проверяет что элементы добавляются и size() отражает их количество
        LogContainer container = new LogContainer(3);

        container.add(entry(1));
        container.add(entry(2));

        assertEquals(2, container.size());
    }

    @Test
    void testCapacityLimit() {
        // проверяет что контейнер не превышает capacity (старые элементы перезаписываются)
        LogContainer container = new LogContainer(3);

        container.add(entry(1));
        container.add(entry(2));
        container.add(entry(3));
        container.add(entry(4)); // перезапись самого старого

        assertEquals(3, container.size());
    }

    @Test
    void testOverwriteOldData() {
        // проверка корректной работу ring buffer (старые данные затираются)
        LogContainer container = new LogContainer(3);

        container.add(entry(1)); // msg-1
        container.add(entry(2));
        container.add(entry(3));
        container.add(entry(4)); // вытесняет msg-1

        // проверка диапазона индексов
        assertEquals(1, container.firstIndex());
        assertEquals(4, container.lastIndex());

        // проверка фактических данных
        assertEquals("msg-2", container.get(1).getMessage());
        assertEquals("msg-3", container.get(2).getMessage());
        assertEquals("msg-4", container.get(3).getMessage());
    }

    @Test
    void testGetInvalidIndex() {
        // проверяем что get() выбрасывает исключение при неверном индексе
        LogContainer container = new LogContainer(2);

        container.add(entry(1));

        assertThrows(IndexOutOfBoundsException.class, () -> container.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> container.get(5));
    }

    @Test
    void testGetRange() {
        // проверяет что getRange() возвращает правильный подмассив
        LogContainer container = new LogContainer(5);

        for (int i = 0; i < 5; i++) {
            container.add(entry(i));
        }

        LogEntry[] range = container.getRange(1, 4);

        assertEquals(3, range.length);
        assertEquals("msg-1", range[0].getMessage());
        assertEquals("msg-2", range[1].getMessage());
        assertEquals("msg-3", range[2].getMessage());
    }

    @Test
    void testGetRangeWithOverflow() {
        // проверяем что getRange() корректно обрезает диапазон при переполнении буфера
        LogContainer container = new LogContainer(3);

        for (int i = 0; i < 5; i++) {
            container.add(entry(i));
        }

        // доступны только последние 3 элемента (msg-2, msg-3, msg-4)
        LogEntry[] range = container.getRange(0, 5);

        assertEquals(3, range.length);
        assertEquals("msg-2", range[0].getMessage());
        assertEquals("msg-3", range[1].getMessage());
        assertEquals("msg-4", range[2].getMessage());
    }

    @Test
    void testIterator() {
        // проверка что iterator() проходит по элементам в правильном порядке
        LogContainer container = new LogContainer(3);

        container.add(entry(1));
        container.add(entry(2));
        container.add(entry(3));

        Iterator<LogEntry> it = container.iterator();

        assertTrue(it.hasNext());
        assertEquals("msg-1", it.next().getMessage());
        assertEquals("msg-2", it.next().getMessage());
        assertEquals("msg-3", it.next().getMessage());
        assertFalse(it.hasNext());
    }

    @Test
    void testIteratorAfterOverflow() {
        // проверяет что iterator() возвращает только актуальные (не перезаписанные) элементы
        LogContainer container = new LogContainer(3);

        for (int i = 0; i < 5; i++) {
            container.add(entry(i));
        }

        Iterator<LogEntry> it = container.iterator();

        assertEquals("msg-2", it.next().getMessage());
        assertEquals("msg-3", it.next().getMessage());
        assertEquals("msg-4", it.next().getMessage());
    }

    @Test
    void testIteratorNoSuchElement() {
        // проверка что iterator().next() кидает исключение при выходе за пределы
        LogContainer container = new LogContainer(1);
        container.add(entry(1));

        Iterator<LogEntry> it = container.iterator();
        it.next();

        assertThrows(java.util.NoSuchElementException.class, it::next);
    }

    @Test
    void testInvalidCapacity() {
        // нельзя создавать контейнер на <= 0 ёмкостью
        assertThrows(IllegalArgumentException.class, () -> new LogContainer(0));
        assertThrows(IllegalArgumentException.class, () -> new LogContainer(-1));
    }
}