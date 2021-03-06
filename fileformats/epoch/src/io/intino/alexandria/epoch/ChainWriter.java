package io.intino.alexandria.epoch;

import java.io.*;
import java.util.Arrays;

interface ChainWriter {
	int write(Timeline.Item item, boolean isTheLast) throws IOException;
	void writeNext(int cursor, int next) throws IOException;
    void close() throws IOException;

    class BulkChainWriter implements ChainWriter {
        private final DataOutputStream os;
        private int dataSize;
        private int cursor;

        static ChainWriter create(File file, int dataSize) throws IOException {
            return new BulkChainWriter(file, dataSize);
        }

        BulkChainWriter(File file, int dataSize) throws FileNotFoundException {
            this.os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            this.dataSize = dataSize;
            this.cursor = 0;
        }

        @Override
        public int write(Timeline.Item item, boolean isTheLast) throws IOException {
            os.writeLong(item.instant.toEpochMilli());
            os.write(RandomChainWriter.adjust(item.data, dataSize));
            os.writeInt(isTheLast ? -1 : cursor + 1);
            return cursor++;
        }

		@Override
		public void writeNext(int cursor, int next) throws IOException {

		}

		@Override
        public void close() throws IOException {
            os.close();
        }
    }

    class RandomChainWriter implements ChainWriter {
        private final RandomAccessFile raf;
        private final int dataSize;
        private int cursor;

        static ChainWriter create(RandomAccessFile raf, int dataSize) throws IOException {
            return new RandomChainWriter(raf, dataSize);
        }

        RandomChainWriter(RandomAccessFile raf, int dataSize) throws IOException {
            this.raf = raf;
            this.dataSize = dataSize;
            this.cursor = (int) (raf.length() / recordSize());
        }

	    private int recordSize() {
		    return Long.BYTES + dataSize + Integer.BYTES;
	    }

        @Override
        public int write(Timeline.Item item, boolean isTheLast) throws IOException {
            raf.seek(raf.length());
            raf.writeLong(item.instant.toEpochMilli());
            raf.write(adjust(item.data, dataSize));
            raf.writeInt(isTheLast ? -1 : cursor + 1);
            return cursor++;
        }

		@Override
		public void writeNext(int cursor, int next) throws IOException {
			seekNextOf(cursor);
			raf.writeInt(next);
		}

		private void seekNextOf(int cursor) throws IOException {
			raf.seek(positionOf(cursor) + Long.BYTES + dataSize);
		}

		private long positionOf(int cursor) {
			return cursor * recordSize();
		}


		@Override
        public void close() throws IOException {
            raf.close();
        }

        static byte[] adjust(byte[] data, int dataSize) {
        	if (data.length == dataSize) return data;
			return Arrays.copyOf(data, dataSize);
        }


    }
}
