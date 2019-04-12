package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class ColumnStreamReader {
	static ColumnStream of(File file, TabbInfo.ColumnInfo column) {

		try {
			return new ColumnStream() {
				InputStream inputStream = ZipEntryReader.openEntry(file, column.name + ColumnStream.ColumnExtension);
				byte[] value = new byte[size()];

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
			};
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}


}
