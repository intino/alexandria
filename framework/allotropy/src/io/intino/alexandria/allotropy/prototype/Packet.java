package io.intino.alexandria.allotropy.prototype;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public interface Packet extends Iterable<Packet.Item> {
	Packet BOF = control("BOF");
	Packet EOF = control("EOF");

	String name();
	Item[] items();

	default int line() { return -1; }
	default Iterator<Item> iterator() {
		return Arrays.stream(items()).iterator();
	}
	default Map<String, String> map() { return stream(items()).collect(Collectors.toMap(Item::name, Item::value, (i1, i2) -> i1));}
	default boolean is(String name) { return name().equalsIgnoreCase(name); }

	class Item {
		private final String name;
		private final String value;

		public Item(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String name() {
			return name;
		}

		public String value() {
			return value;
		}
	}

	static Packet control(String name) {
		return new Packet() {
			@Override
			public String name() {
				return name;
			}

			@Override
			public Item[] items() {
				return new Item[0];
			}
		};
	}
}
