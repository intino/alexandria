package io.intino.alexandria.zet;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

class ZInputStream extends InputStream {
	private final DataInputStream input;
	private long base = 0;
	private byte[] data = new byte[256];
	private int count = 0;
	private int index = 0;
	private int size = 0;

	public ZInputStream(InputStream inputStream) {
		this.input = new DataInputStream(inputStream);
		this.init();
	}

	private void init() {
		try {
			readData();
		} catch (IOException ignored) {
		}
	}

	public int size() {
		return size;
	}

	@Override
	public int read() {
		return 0;
	}

	public long readLong() throws IOException {
		if (index == count) readBlock();
		return (this.base << 8) | (data[index++] & 0xFF);
	}

	private void readBlock() throws IOException {
		readLevel();
		readData();
	}

	private void readLevel() throws IOException {
		int level = input.read();
		if (level < 0) throw new EOFException();
		this.base = this.base >> (level << 3);
		for (int i = 1; i <= level; i++)
			this.base = (this.base << 8) | (input.readByte() & 0xFF);
	}

	private void readData() throws IOException {
		count = input.readByte();
		size += count;
		index = 0;
		input.read(data, 0, this.count);
	}

	@Override
	public void close() throws IOException {
		input.close();
	}
}
