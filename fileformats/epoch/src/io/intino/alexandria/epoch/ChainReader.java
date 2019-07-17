package io.intino.alexandria.epoch;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;

import static java.lang.System.arraycopy;

interface ChainReader {
	ChainReader Null = cursor -> Record.Null;

	static ChainReader load(byte[] content, int dataSize) {
		return new ChainReader() {

			@Override
			public Record recordAt(int cursor) {
				int position = positionOf(cursor);
				return new Record(readInstant(position), readData(position + Long.BYTES), readNext(position + Long.BYTES + dataSize));
			}

			Instant readInstant(int position) {
				return Instant.ofEpochMilli(readLong(position));
			}

			byte[] readData(int position) {
				byte[] bytes = new byte[dataSize];
				arraycopy(content, position, bytes, 0, dataSize);
				return bytes;
			}

			int readNext(int position) {
				return readInt(position);
			}

			private int positionOf(int cursor) {
				return cursor * recordSize();
			}

			private int readInt(int position) {
				int value = 0;
				for (int i = 0; i < 4; i++)
					value = (value << 8) + ((int) content[position++] & 0xFF);
				return value;
			}

			private long readLong(int position) {
				long value = 0;
				for (int i = 0; i < 8; i++)
					value = (value << 8) + ((long) content[position++] & 0xFFL);
				return value;
			}

			private int recordSize() {
				return Long.BYTES + dataSize + Integer.BYTES;
			}

		};
	}

	static ChainReader load(RandomAccessFile raf, int dataSize) {
		return new ChainReader() {
			@Override
			synchronized public Record recordAt(int cursor) throws IOException {
				raf.seek(positionOf(cursor));
				return new Record(readInstant(), readData(), readNext());
			}

			Instant readInstant() throws IOException {
				return Instant.ofEpochMilli(raf.readLong());
			}

			byte[] readData() throws IOException {
				byte[] bytes = new byte[dataSize];
				raf.read(bytes);
				return bytes;
			}

			int readNext() throws IOException {
				return raf.readInt();
			}

			private long positionOf(int cursor) {
				return cursor * recordSize();
			}

			private int recordSize() {
				return Long.BYTES + dataSize + Integer.BYTES;
			}

		};
	}

	Record recordAt(int cursor) throws IOException;

	class Record {
		static Record Null = new Record(null, new byte[0], 0);
		private final Instant instant;
		private final byte[] data;
		private final int next;

		public Record(Instant instant, byte[] data, int next) {
			this.instant = instant;
			this.data = data;
			this.next = next;
		}

		public Instant instant() {
			return instant;
		}

		public byte[] data() {
			return data;
		}

		public int next() {
			return next;
		}

		boolean isAfter(Instant instant) {
			return this.instant.isAfter(instant);
		}
	}
}
