package io.intino.alexandria.tabb.streamers;

import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.mapp.MappStream;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.ColumnStreamer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MappColumnStreamer implements ColumnStreamer {
	private final MappReader reader;
	private List<Selector> selectors = new ArrayList<>();

	private MappStream.Item current = nullItem();

	public MappColumnStreamer(MappReader reader) {
		this.reader = reader;
	}

	private static MappStream.Item nullItem() {
		return new MappStream.Item() {
			@Override
			public long key() {
				return -1;
			}

			@Override
			public String value() {
				return null;
			}
		};
	}

	public void add(Selector selector) {
		selectors.add(selector);
	}

	@Override
	public ColumnStream[] get() {
		return selectors.stream().map(s -> new ColumnStream() {
			private MappStream.Item current = nullItem();

			@Override
			public String name() {
				return s.name();
			}

			@Override
			public Type type() {
				return s.type();
			}

			@Override
			public boolean hasNext() {
				return getNext(current.key());
			}

			@Override
			public void next() {
				current = getCurrent(current.key() + 1);
			}

			@Override
			public Long key() {
				return current.key();
			}

			@Override
			public Object value() {
				return s.select(current);
			}
		}).toArray(ColumnStream[]::new);
	}

	private boolean getNext(long key) {
		return key < this.current.key() || reader.hasNext();
	}

	private MappStream.Item getCurrent(long key) {
		if (key > this.current.key()) this.current = reader.next();
		return this.current;
	}

	public interface Selector {

		String name();

		Type type();

		Object select(MappStream.Item item);

	}

	public static class SimpleSelector implements Selector {

		private String name;
		private Type type;
		private Function<MappStream.Item, ?> function;

		public SimpleSelector(String name, Type type, Function<MappStream.Item, ?> function) {
			this.name = name;
			this.type = type;
			this.function = function;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public Type type() {
			return type;
		}

		@Override
		public Object select(MappStream.Item item) {
			return function.apply(item);
		}

	}

	public static class BypassSelector extends SimpleSelector {

		public BypassSelector(String name, Type type) {
			super(name, type, MappStream.Item::value);
		}

	}
}


