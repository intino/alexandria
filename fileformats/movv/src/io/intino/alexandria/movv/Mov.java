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
		ChainReader.Record result = ChainReader.Record.Null;
		int cursor = head;
		while (cursor >= 0) {
			ChainReader.Record record= recordAt(cursor);
			if (record == ChainReader.Record.Null || record.isAfter(instant)) break;
			cursor = record.next();
			result = record;
		}
		return itemFrom(result);
	}

	private Item itemFrom(ChainReader.Record result) {
		return new Item(result.instant(), result.data());
	}

	public Item first() {
		return head >= 0 ? itemFrom(recordAt(head)) : Item.Null;
	}

	public Item last() {
		int cursor = head;
		while (cursor >= 0) {
			ChainReader.Record record = recordAt(cursor);
			if (record.next() < 0) return itemFrom(record);
			cursor = record.next();
		}
		return Item.Null;
	}

	public int length() {
		int length = 0;
		int cursor = head;
		while (cursor >= 0) {
			cursor = recordAt(cursor).next();
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
				ChainReader.Record record = recordAt(next);
				next = record.next();
				return itemFrom(record);
			}
		};
	}

	boolean reject(Item item) {
		Item last = last();
		if (last == Item.Null) return false;
		return item.instant.compareTo(last.instant) <= 0 || Arrays.equals(last.data, item.data);
	}

	private ChainReader.Record recordAt(int cursor) {
		try {
			return chainReader.recordAt(cursor);
		} catch (IOException e) {
			return ChainReader.Record.Null;
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
