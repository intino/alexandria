package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class TabbColumnStream implements ColumnStream {

	private final File file;
	private final TabbInfo.ColumnInfo column;
	private final byte[] value;
	private InputStream inputStream;

	public TabbColumnStream(File file, TabbInfo.ColumnInfo column) {
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