package io.intino.alexandria.tabb;

import io.intino.alexandria.tabb.ColumnStream.NotAvailable;
import io.intino.alexandria.tabb.ColumnStream.Type;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.nio.ByteBuffer.wrap;
import static java.util.stream.Collectors.toList;

public class TabbReader {
	private final TabbInfo info;
	private final List<ColumnStream> columns;

	public TabbReader(File file, String... columns) throws IOException {
		this.info = TabbInfo.of(file);
		this.columns = Arrays.stream(info.columns(columns))
				.map(c -> new TabbColumnStream(file, c))
				.collect(toList());
	}

	public long size() {
		return info.size();
	}

	public boolean hasNext() {
		return columns.get(0).hasNext();
	}

	public void next() {
		columns.forEach(ColumnStream::next);
	}

	public Value get(int index) {
		ColumnStream stream = columns.get(index);
		return new Value(stream.type(), stream.mode(), stream.value());
	}

	public class Value {
		private final Type type;
		private final ColumnStream.Mode mode;
		private final byte[] value;

		Value(Type type, ColumnStream.Mode mode, byte[] value) {
			this.type = type;
			this.mode = mode;
			this.value = value;
		}

		public Type type() {
			return type;
		}

		public ColumnStream.Mode mode() {
			return mode;
		}

		public boolean isAvailable() {
			return !Arrays.equals(NotAvailable.bytesOf(type), value);
		}

		public int asInteger() {
			return isAvailable() ? wrap(value).getInt() : NotAvailable.NaInt;
		}

		public double asDouble() {
			return isAvailable() ? wrap(value).getDouble() : NotAvailable.NaDouble;
		}

		public boolean asBoolean() {
			return wrap(value).getInt() == 1;//FIXME Na??
		}

		public Long asLong() {
			return isAvailable() ? wrap(value).getLong() : NotAvailable.NaDouble;
		}

		public LocalDateTime asDatetime() {
			return null;
		}

		public Instant asInstant() {
			return null;
		}

		public String asString() {
			return mode.features[wrap(value).getInt()];
		}

	}

}
