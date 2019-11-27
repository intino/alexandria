package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TabbReader {
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

	public void next() {
		columns.forEach(ColumnStream::next);
	}

	public Value get(int index) {
		ColumnStream stream = columns.get(index);
		return new Value(stream.type(), new Mode(info.columns(stream.name())[0].features), (byte[]) stream.value());
	}

	static class TabbColumnStream implements ColumnStream {
		private final TabbManifest.ColumnInfo column;
		private final byte[] value;
		private InputStream inputStream;

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
			if (column.type.equals(Type.Double)) return Double.SIZE / 8;
			else if (column.type.equals(Type.Long)) return Long.SIZE / 8;
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

		public void close() {
			try {
				inputStream.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}
	}
}
