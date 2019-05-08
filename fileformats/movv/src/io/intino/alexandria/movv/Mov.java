package io.intino.alexandria.movv;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;

public class Mov implements Iterable<Mov.Item> {
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

    public Item at(Instant instant) {
        Item result = Item.Null;
        int cursor = head;
        while (cursor >= 0) {
            Item item = itemAt(cursor);
            if (item == null || item.isAfter(instant)) break;
            cursor = readNext();
            result = item;
        }
        return result;
    }

    public Item last()  {
        int cursor = head;
        while (cursor >= 0) {
            int next = nextOf(cursor);
            if (next < 0) return itemAt(cursor);
            cursor = next;
        }
        return Item.Null;
    }

    public int length() {
        int length =0;
        int cursor = head;
        while (cursor >= 0) {
            cursor = nextOf(cursor);
            length++;
        }
        return length;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            int cursor = head;
            @Override
            public boolean hasNext() {
                return cursor >= 0;
            }

            @Override
            public Item next() {
                Item item = itemAt(cursor);
                cursor = readNext();
                return item;
            }
        };
    }

    boolean reject(Item item) {
        Item last = last();
        if (last == Item.Null) return false;
        return last.isAfter(item.instant) || last.data.equals(item.data);
    }

    void append(long id, int next) {
        if (this.head < 0) create(id, next); else append(next);
    }

    private void create(long id, int next) {
        this.index.put(id, next);
        this.head = next;
    }

    private void append(int next)  {
        int cursor = head;
        while (true) {
            int last = nextOf(cursor);
            if (last == -1) break;
            cursor = last;
        }
        updateNext(cursor, next);
    }

    private int nextOf(int cursor) {
        try {
            access.seekNextOf(cursor);
            return access.readNext();
        } catch (IOException e) {
            return -1;
        }
    }

    private int readNext() {
        try {
            return access.readNext();
        } catch (IOException e) {
            return -1;
        }
    }

    private Item itemAt(int cursor) {
        try {
            access.seek(cursor);
            return new Item(access.readInstant(), access.readData());
        } catch (IOException e) {
            return null;
        }
    }

    private void updateNext(int cursor, int next)  {
        try {
            access.seekNextOf(cursor);
            access.writeNext(next);
        } catch (IOException ignored) {
        }
    }

    public static class Item {
        static final Item Null = new Item(null, null);
        public final Instant instant;
        public final String data;

        Item(Instant instant, String data) {
            this.instant = instant;
            this.data = data;
        }

        boolean isAfter(Instant instant) {
            return this.instant.compareTo(instant) > 0;
        }

    }
}
