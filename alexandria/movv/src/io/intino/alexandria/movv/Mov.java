package io.intino.alexandria.movv;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;

public class Mov implements Iterable<Mov.Entry> {
    private final Index index;
    private final Access access;
    private int head;

    Mov(Index index, Access access) {
        this.access = access;
        this.index = index;
    }

    Mov of(long id) {
        this.head = index.headOf(id);
        return this;
    }

    public String at(Instant instant) throws IOException {
        String result = null;
        int cursor = head;
        while (cursor >= 0) {
            access.seekInstantOf(cursor);
            if (isAfter(instant)) break;
            result = access.readData();
            cursor = access.readNext();
        }
        return result;
    }

    public String last() throws IOException {
        int cursor = head;
        while (cursor >= 0) {
            int next = nextOf(cursor);
            if (next < 0) return dataOf(cursor);
            cursor = next;
        }
        return null;
    }

    public int length() throws IOException {
        int length =0;
        int cursor = head;
        while (cursor >= 0) {
            cursor = nextOf(cursor);
            length++;
        }
        return length;
    }

    @Override
    public Iterator<Entry> iterator() {
        return new Iterator<Entry>() {
            int cursor = head;
            @Override
            public boolean hasNext() {
                return cursor >= 0;
            }

            @Override
            public Entry next() {
                try {
                    Entry entry = entryAt(cursor);
                    cursor = nextOf(cursor);
                    return entry;
                } catch (IOException e) {
                    return null;
                }
            }
        };
    }

    private boolean isAfter(Instant instant) throws IOException {
        return access.readInstant().compareTo(instant) > 0;
    }

    void link(long id, int cursor) throws IOException {
        if (this.head < 0) create(id, cursor); else append(cursor);
    }

    private void create(long id, int value) {
        this.index.put(id, value);
        this.head = value;
    }

    private void append(int value) throws IOException {
        int cursor = head;
        while (true) {
            int next = nextOf(cursor);
            if (next == -1) break;
            cursor = next;
        }
        access.seekNextOf(cursor);
        write(value);
    }

    private Entry entryAt(int cursor) throws IOException {
        access.seekInstantOf(cursor);
        return new Entry(access.readInstant(), access.readData());
    }

    private int nextOf(int cursor) throws IOException {
        access.seekNextOf(cursor);
        return access.readNext();
    }

    private String dataOf(int cursor) throws IOException {
        access.seekDataOf(cursor);
        return access.readData();
    }

    private void write(int cursor) throws IOException {
        access.writeNext(cursor);
    }

    public static class Entry {
        public final Instant instant;
        public final String data;

        Entry(Instant instant, String data) {
            this.instant = instant;
            this.data = data;
        }
    }
}
