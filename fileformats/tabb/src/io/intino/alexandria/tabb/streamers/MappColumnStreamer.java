package io.intino.alexandria.tabb.streamers;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.mapp.MappStream;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.ColumnStreamer;
import io.intino.alexandria.tabb.Function;

import java.util.HashMap;
import java.util.Map;

public class MappColumnStreamer implements ColumnStreamer {
	private final MappReader reader;
	private final Type type;
	private final Timetag timetag;
	private final Function<String, ?> function;
	private final String name;

	public MappColumnStreamer(MappReader reader, String name, Type type, Timetag timetag, Function<String, ?> function) {
		this.reader = reader;
		this.name = name;
		this.type = type;
		this.timetag = timetag;
		this.function = function;
	}

	@Override
	public ColumnStream[] get() {
		return new ColumnStream[]{create()};
	}

	private ColumnStream create() {
		return new MappColumnStream(reader, name, type, timetag, function);
	}

	public class MappColumnStream implements ColumnStream {
		private final MappReader reader;
		private final String name;
		private final Type type;
		private final Timetag timetag;
		private final Function<String, ?> function;
		private final Map<String, Integer> labels;
		private MappStream.Item current;

		public MappColumnStream(MappReader reader, String name, Type type, Timetag timetag, Function<String, ?> function) {
			this.reader = reader;
			this.type = type;
			this.name = name;
			this.timetag = timetag;
			this.function = function;
			this.labels = new HashMap<>();
		}

		@Override
		public String name() {
			return name == null ? reader.name() : name;
		}

		@Override
		public Type type() {
			return type;
		}

		@Override
		public Mode mode() {
			return new Mode(labels.keySet().stream()
					.map(l -> l.replace("\n", "|"))
					.toArray(String[]::new));
		}

		@Override
		public boolean hasNext() {
			return reader.hasNext();
		}

		@Override
		public void next() {
			current = reader.next();
		}

		@Override
		public Long key() {
			return current != null ? current.key() : null;
		}

		@Override
		public Object value() {
			if (type == Type.Nominal) return factorize(function.apply(current.key(), current.value(), timetag));
			return current.value();
		}

		private int factorize(Object object) {
			String value = object.toString();
			if (!labels.containsKey(value)) labels.put(value, labels.size());
			return labels.get(value);
		}

	}
}
