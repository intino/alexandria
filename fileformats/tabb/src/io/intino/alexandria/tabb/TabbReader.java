package io.intino.alexandria.tabb;

import io.intino.alexandria.tabb.ColumnStream.NotAvailable;
import io.intino.alexandria.tabb.ColumnStream.Type;

import java.io.File;
import java.io.IOException;
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

	public static class Value {
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
			return isAvailable() ? get32(value) : NotAvailable.NaInt;
		}

		public double asDouble() {
			return isAvailable() ? Double.longBitsToDouble(get64(value)) : NotAvailable.NaDouble;
		}

		public boolean asBoolean() {
			return get32(value) == 1;//FIXME Na??
		}

		public Long asLong() {
			return isAvailable() ? get64(value) : NotAvailable.NaDouble;
		}

		public LocalDateTime asDatetime() {
			return null;
		}

		public Instant asInstant() {
			return null;
		}

		public String asString() {
			return mode.features[get32(value)];
		}

		private static int get32(byte[] data) {
			return (data[0] & 0xFF) << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
		}

		private static long get64(byte[] data) {
			return (data[0] & 0xFFL) << 56 | (data[1] & 0xFFL) << 48 | (data[2] & 0xFFL) << 40 | (data[3] & 0xFFL) << 32 |
					(data[4] & 0xFF) << 24 | (data[5] & 0xFF) << 16 | (data[6] & 0xFF) << 8 | (data[7] & 0xFF);
		}

	}

}
