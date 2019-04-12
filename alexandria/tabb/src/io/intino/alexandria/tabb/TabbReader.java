package io.intino.alexandria.tabb;

import io.intino.alexandria.tabb.ColumnStream.Type;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TabbReader {
	private final TabbInfo info;
	private final List<ColumnStream> columns;

	public TabbReader(File file, String... columns) throws IOException {
		this.info = TabbInfo.of(file);
		this.columns = Arrays.stream(info.columns(columns))
				.map(c -> ColumnStreamReader.of(file, c))
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

		public Value(Type type, ColumnStream.Mode mode, byte[] value) {
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
			return Arrays.equals(ColumnStream.NotAvailable.bytesOf(type), value);
		}

		public int asInteger() {
			return isAvailable() ? ByteBuffer.wrap(value).getInt() : ColumnStream.NotAvailable.NaInt;
		}

		public double asDouble() {
			return isAvailable() ? ByteBuffer.wrap(value).getDouble() : ColumnStream.NotAvailable.NaDouble;
		}

		public boolean asBoolean() {
			return ByteBuffer.wrap(value).getInt() == 1;//FIXME Na??
		}

		public Long asLong() {
			return isAvailable() ? ByteBuffer.wrap(value).getLong() : ColumnStream.NotAvailable.NaDouble;
		}

		public LocalDateTime asDatetime() {
			return null;
		}

		public Instant asInstant() {
			return null;
		}

		public String asString() {
			return mode.features[ByteBuffer.wrap(value).getInt()];
		}

	}

}
