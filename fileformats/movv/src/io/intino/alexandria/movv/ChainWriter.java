package io.intino.alexandria.movv;

import java.io.*;

import static java.lang.Integer.min;
import static java.lang.System.arraycopy;

interface ChainWriter {
    int write(Mov.Item item, boolean isTheLast) throws IOException;
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
        public int write(Mov.Item item, boolean isTheLast) throws IOException {
            os.writeLong(item.instant.toEpochMilli());
            os.write(RandomChainWriter.toByteArray(item.data, dataSize));
            os.writeInt(isTheLast ? -1 : cursor + 1);
            return cursor++;
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
        public int write(Mov.Item item, boolean isTheLast) throws IOException {
            raf.seek(raf.length());
            raf.writeLong(item.instant.toEpochMilli());
            raf.write(toByteArray(item.data, dataSize));
            raf.writeInt(isTheLast ? -1 : cursor + 1);
            return cursor++;
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }

        static byte[] toByteArray(String data, int dataSize) {
            byte[] bytes = new byte[dataSize];
            arraycopy(data.getBytes(),0,bytes,0,min(dataSize, data.length()));
            return bytes;
        }


    }
}
