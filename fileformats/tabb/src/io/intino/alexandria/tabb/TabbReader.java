package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream.Type;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.toList;

public class TabbReader {
	private final TabbManifest info;
	private final List<ColumnStream> columns;

	public TabbReader(File file, String... columns) throws IOException {
		this.info = TabbManifest.of(file);
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
		return new Value(stream.type(), stream.mode(), (byte[]) stream.value());
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

		private static int get32(byte[] data) {
			return (data[0] & 0xFF) << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
		}

		private static long get64(byte[] data) {
			return (data[0] & 0xFFL) << 56 | (data[1] & 0xFFL) << 48 | (data[2] & 0xFFL) << 40 | (data[3] & 0xFFL) << 32 |
					(data[4] & 0xFF) << 24 | (data[5] & 0xFF) << 16 | (data[6] & 0xFF) << 8 | (data[7] & 0xFF);
		}

		public Type type() {
			return type;
		}

		public ColumnStream.Mode mode() {
			return mode;
		}

		public boolean isAvailable() {
			return !Arrays.equals(type.notAvailable(), value);
		}

		public int asInteger() {
			return get32(isAvailable() ? value : Type.Integer.notAvailable());
		}

		public double asDouble() {
			return get64(isAvailable() ? value : Type.Double.notAvailable());
		}

		public boolean asBoolean() {
			return get32(value) == 1;//FIXME Na??
		}

		public Long asLong() {
			return get64(isAvailable() ? value : Type.Long.notAvailable());
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

	}

	static class ZipEntryReader {

		static InputStream openEntry(File file, String entryName) throws IOException {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entryName.equals(entry.getName())) return zipFile.getInputStream(entry);
			}
			return null;
		}

	}

	static class TabbColumnStream implements ColumnStream {

		private final File file;
		private final TabbManifest.ColumnInfo column;
		private final byte[] value;
		private InputStream inputStream;

		TabbColumnStream(File file, TabbManifest.ColumnInfo column) {
			this.file = file;
			this.column = column;
			value = new byte[size()];
			try {
				inputStream = new BufferedInputStream(ZipEntryReader.openEntry(file, column.name + ColumnExtension));
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		private int size() {
			if (column.type.equals(Type.Double)) return Double.SIZE / 8;
			else if (column.type.equals(Type.Long)) return Long.SIZE / 8;
			else return Integer.SIZE / 8;
		}

		@Override
		public String name() {
			return file.getName().replace(ColumnExtension, "");
		}

		@Override
		public Type type() {
			return column.type;
		}

		@Override
		public Mode mode() {
			return new Mode(column.modes);
		}

		@Override
		public boolean hasNext() {
			try {
				return inputStream.available() > 0;
			} catch (IOException e) {
				Logger.error(e);
				return false;
			}
		}

		@Override
		public void next() {
			try {
				inputStream.read(value);
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		@Override
		public Long key() {
			return null;
		}

		@Override
		public byte[] value() {
			return value;
		}
	}
}
