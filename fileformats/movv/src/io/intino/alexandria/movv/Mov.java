package io.intino.alexandria.movv;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
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
		return item.instant.compareTo(last.instant) <= 0 || Arrays.equals(last.data, item.data);
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

	int head() {
		return head;
	}

	void head(int head) {
		this.head = head;
	}

	public static class Item {
		static final Item Null = new Item(null, new byte[0]);
		public final Instant instant;
		public final byte[] data;

		Item(Instant instant, byte[] data) {
			this.instant = instant;
			this.data = data != null ? data : new byte[0];
		}

		boolean isAfter(Instant instant) {
			return this.instant.compareTo(instant) > 0;
		}

	}
}
