package io.intino.test.alignment;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.memory.MemoryUtils;
import io.intino.alexandria.led.util.memory.ModifiableMemoryAddress;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.*;

public class AlignmentTest {

    private AlignSchema schema;

    @Test
    public void testTheLongBigEndian() {
        schema = new AlignSchema(BIG_ENDIAN);
        final byte id = (byte) 0b110;
        final byte theByte = (byte) 0b101;
        final long theLong = 0b1010101010101010101010101010101010101010101010101010101010101011L;
        schema.id(id);
        Assert.assertEquals(id, schema.id());
        schema.theByte(theByte);
        Assert.assertEquals(theByte, schema.theByte());
        schema.theLong(theLong);
        Assert.assertEquals(theLong, schema.theLong());
        Assert.assertEquals(id, schema.id());
        Assert.assertEquals(theByte, schema.theByte());
    }

    @Test
    public void testTheLongLittleEndian() {
        schema = new AlignSchema(LITTLE_ENDIAN);
        final byte id = (byte) 0b101;
        final byte theByte = (byte) 0b101;
        final long theLong = Long.MAX_VALUE;
        schema.id(id);
        Assert.assertEquals(id, schema.id());
        schema.theByte(theByte);
        Assert.assertEquals(theByte, schema.theByte());
        schema.theLong(theLong);
        Assert.assertEquals(theLong, schema.theLong());
        Assert.assertEquals(id, schema.id());
        Assert.assertEquals(theByte, schema.theByte());
    }


    static class AlignSchema extends Schema {

        public static final int SIZE = 9;

        private static final int ID_OFFSET = 0;
        private static final int ID_SIZE = 3;
        private static final int THE_LONG_OFFSET = ID_OFFSET + ID_SIZE;
        private static final int THE_LONG_SIZE = 40;
        private static final int THE_BYTE_OFFSET = THE_LONG_OFFSET + THE_LONG_SIZE;
        private static final int THE_BYTE_SIZE = SIZE * Byte.SIZE - THE_BYTE_OFFSET;

        public AlignSchema(ByteOrder order) {
            super(getDefaultByteStore(order));
        }

        @Override
        public long id() {
            return ((long)bitBuffer.getByteNBits(ID_OFFSET, ID_SIZE)) & 0xFF;
        }

        public void id(byte id) {
            bitBuffer.setByteNBits(ID_OFFSET, ID_SIZE, id);
        }

        public long theLong() {
            return bitBuffer.getLongNBits(THE_LONG_OFFSET, THE_LONG_SIZE);
        }

        public void theLong(long value) {
            bitBuffer.setLongNBits(THE_LONG_OFFSET, THE_LONG_SIZE, value);
        }

        public byte theByte() {
            return bitBuffer.getByteNBits(THE_BYTE_OFFSET, THE_BYTE_SIZE);
        }

        public void theByte(byte value) {
            bitBuffer.setByteNBits(THE_BYTE_OFFSET, THE_BYTE_SIZE, value);
        }

        private static ByteStore getDefaultByteStore(ByteOrder order) {
            final ByteBuffer buffer = MemoryUtils.allocBuffer(SIZE, order);
            return new ByteBufferStore(buffer, ModifiableMemoryAddress.of(buffer), 0, SIZE);
        }

        @Override
        public int size() {
            return SIZE;
        }

        @Override
        public String toString() {
            return "AlignSchema{" +
                    "\n" + bitBuffer.toBinaryString(8) +
                    ",\nid=" + id() +
                    ",\n theLong=" + theLong() +
                    ",\n theByte=" + theByte() +
                    '}';
        }
    }
}
