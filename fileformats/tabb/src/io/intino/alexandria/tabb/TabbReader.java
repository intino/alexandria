package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public class TabbReader implements Iterator<Row>, AutoCloseable {
	private final TabbManifest info;
	private final List<TabbColumnStream> columns;

	public TabbReader(File file, String... columns) throws IOException {
		this.info = TabbManifest.of(file);
		this.columns = Arrays.stream(info.columns(columns))
				.map(c -> new TabbColumnStream(file, c))
				.collect(toList());
	}

	public void close() {
		columns.forEach(TabbColumnStream::close);
	}

	public long size() {
		return info.size();
	}

	public boolean hasNext() {
		return columns.get(0).hasNext();
	}

	public Row next() {
		columns.forEach(ColumnStream::next);
		return columns.stream().map(this::valueOf).collect(toCollection(Row::new));
	}

	public Value get(int index) {
		return valueOf(columns.get(index));
	}

	private Value valueOf(ColumnStream stream) {
		return new Value(stream.type(), new Mode(info.columns(stream.name())[0].features), (byte[]) stream.value());
	}

	static class TabbColumnStream implements ColumnStream {
		private final TabbManifest.ColumnInfo column;
		private byte[] value;
		private InputStream inputStream;
		private Scanner scanner;

		TabbColumnStream(File file, TabbManifest.ColumnInfo column) {
			this.column = column;
			value = new byte[size()];
			try {
				inputStream = new BufferedInputStream(ZipHandler.openEntry(file, column.name + ColumnExtension));
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		private int size() {
			if (Type.Double.equals(column.type)) return Double.SIZE / 8;
			else if (Type.Long.equals(column.type)) return Long.SIZE / 8;
			else return Integer.SIZE / 8;
		}

		@Override
		public String name() {
			return column.name;
		}

		@Override
		public boolean isIndex() {
			return column.isIndex;
		}

		@Override
		public Type type() {
			return column.type;
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
				if (type().equals(Type.String)) value = readStringValue();
				else inputStream.read(value);
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		private byte[] readStringValue() {
			scanner = scanner == null ? new Scanner(inputStream).useDelimiter("\0") : scanner;
			String result = scanner.hasNext() ? scanner.next() : "";
			return result.getBytes();
		}

		@Override
		public Long key() {
			return null;
		}

		@Override
		public byte[] value() {
			return value;
		}

		public void close() {
			try {
				inputStream.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}
	}
}
