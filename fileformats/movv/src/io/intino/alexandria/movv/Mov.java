package io.intino.alexandria.movv;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;

public class Mov implements Iterable<Mov.Item> {
	private final ChainIndex chainIndex;
	private final ChainReader chainReader;
	private long id;
	private int head;

	Mov(ChainIndex chainIndex, ChainReader chainReader) {
		this.chainReader = chainReader;
		this.chainIndex = chainIndex;
	}

	public long id() {
		return id;
	}

	Mov of(long id) {
		this.id = id;
		this.head = chainIndex.headOf(id);
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

	public Item first() {
		return head >= 0 ? itemAt(head) : Item.Null;
	}

	public Item last() {
		int cursor = head;
		while (cursor >= 0) {
			int next = nextOf(cursor);
			if (next < 0) return itemAt(cursor);
			cursor = next;
		}
		return Item.Null;
	}

	public int length() {
		int length = 0;
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
			int next = head;

			@Override
			public boolean hasNext() {
				return next >= 0;
			}

			@Override
			public Item next() {
				Item item = itemAt(next);
				next = readNext();
				return item;
			}
		};
	}

	boolean reject(Item item) {
		Item last = last();
		if (last == Item.Null) return false;
		return item.instant.compareTo(last.instant) <= 0 || last.data.equals(item.data);
	}

	void append(long id, int next) {
		if (this.head < 0) create(id, next);
		else append(next);
	}

	private void create(long id, int next) {
		this.chainIndex.put(id, next);
		this.head = next;
	}

	private void append(int next) {
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
			chainReader.seekNextOf(cursor);
			return chainReader.readNext();
		} catch (IOException e) {
			return -1;
		}
	}

	private int readNext() {
		try {
			return chainReader.readNext();
		} catch (IOException e) {
			return -1;
		}
	}

	private Item itemAt(int cursor) {
		try {
			chainReader.seek(cursor);
			return new Item(chainReader.readInstant(), chainReader.readData());
		} catch (IOException e) {
			return null;
		}
	}

	private void updateNext(int cursor, int next) {
		try {
			chainReader.seekNextOf(cursor);
			chainReader.writeNext(next);
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
