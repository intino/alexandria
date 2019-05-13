package io.intino.alexandria.tabb.streamers;

import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.mapp.MappStream;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.ColumnStreamer;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class MappColumnStreamer implements ColumnStreamer {
	private final MappReader reader;
	private final Type type;
	private final String name;

	public MappColumnStreamer(MappReader reader) {
		this(reader, Type.Nominal);
	}

	public MappColumnStreamer(MappReader reader, String name) {
		this(reader, name, Type.Nominal);
	}

	public MappColumnStreamer(MappReader reader, Type type) {
		this(reader, reader.name(), type);
	}

	public MappColumnStreamer(MappReader reader, String name, Type type) {
		this.reader = reader;
		this.type = type;
		this.name = name;
	}

	@Override
	public ColumnStream[] get() {
		return new ColumnStream[]{create()};
	}

	private ColumnStream create() {
		return new MappColumnStream(mapOf(reader.labels()));
	}

	private Map<String, Integer> mapOf(List<String> labels) {
		return range(0, labels.size()).boxed()
				.collect(toMap(labels::get, i -> i));
	}


	public class MappColumnStream implements ColumnStream {
		private final Map<String, Integer> labels;
		private MappStream.Item current;
		private Parser parser;

		MappColumnStream(Map<String, Integer> labels) {
			this.labels = labels;
			this.parser = parserOf(type);
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
			return new Mode(reader.labels().stream()
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
			return parser.parse(current.value());
		}

		private Parser parserOf(Type type) {
			if (type == Type.Nominal) return labels::get;
			if (type == Type.Double) return Double::parseDouble;
			if (type == Type.Integer || type == Type.Datetime) return Integer::parseInt;
			if (type == Type.Instant) return Instant::parse;
			if (type == Type.Boolean) return Boolean::parseBoolean;
			if (type == Type.Long) return Long::parseLong;
			return value -> value;
		}
	}
	private interface Parser {
		Object parse(String value);
	}

}


