package io.intino.test.datatypes;

import io.intino.alexandria.led.buffers.AbstractBitBuffer;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.memory.MemoryUtils;
import io.intino.alexandria.led.util.memory.ModifiableMemoryAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.List;

import static io.intino.alexandria.led.util.BitUtils.maxPossibleNumber;
import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestDataTypes {

    private final ByteOrder byteOrder;
    private AbstractBitBuffer buffer;

    public TestDataTypes(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Before
    public void setUp() throws Exception {
        ByteBuffer b = MemoryUtils.allocBuffer(1024);
        ByteStore store = new ByteBufferStore(b, ModifiableMemoryAddress.of(b), 0, 1024);
        buffer = byteOrder == LITTLE_ENDIAN ? new LittleEndianBitBuffer(store) : new BigEndianBitBuffer(store);
    }

    @Test
    public void test() {
        int x = 99;
        buffer.setIntegerNBits(0, 7, x);
        assertEquals(x, buffer.getIntegerNBits(0, 7));
    }

    @Test
    public void testSignedByte() {
        byte value = 2; // todo: UNO DE LOS BITS DEBE SER RESERVADO PARA EL SIGNO!!!!!!!!!!!!!!!!! nbits = nbits - 1
        buffer.setByteNBits(0, 3, value);
        assertEquals(value, buffer.getByteNBits(0, 3));
    }

    @Test
    public void testUnsignedByte() {
        short value = Byte.MAX_VALUE * 2;
        buffer.setAlignedUByte(0, value);
        assertEquals(value, buffer.getAlignedUByte(0));
    }

    @Test
    public void testSignedShort() {
        short value = -64;
        buffer.setShortNBits(0, 16, value);
        assertEquals(value, buffer.getShortNBits(0, 16));
    }

    @Test
    public void testUnsignedShort() {
        int value = Short.MAX_VALUE * 2;
        buffer.setUShortNBits(0, 16, value);
        assertEquals(value, buffer.getUShortNBits(0, 16));
    }

    @Test
    public void testSignedAlignedInteger() {
        int value = Integer.MIN_VALUE;
        buffer.setAlignedInteger(0, value);
        assertEquals(value, buffer.getAlignedInteger(0));
    }

    @Test
    public void testSignedInteger() {
        int value = -2;
        buffer.setIntegerNBits(0, 4, value);
        assertEquals(value, buffer.getIntegerNBits(0, 4));
    }

    @Test
    public void testUnsignedInteger() {
        long value = Integer.MAX_VALUE * 2L;
        buffer.setAlignedUInteger(0, value);
        assertEquals(value, buffer.getAlignedUInteger(0));
    }

    @Test
    public void testSignedLong() {
        long value = -100;
        buffer.setLongNBits(0, 40, value);
        assertEquals(value, buffer.getLongNBits(0, 40));
    }

    @Test
    public void testUnsignedLong() {
        long value = maxPossibleNumber(Long.SIZE - 1);
        buffer.setAlignedULong(0, value);
        assertEquals(value, buffer.getAlignedULong(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsignedLong64Bits() {
        long value = Long.MIN_VALUE;
        buffer.setULongNBits(0, 64, value);
        assertEquals(value, buffer.getULongNBits(0, 64));
    }

    @Parameterized.Parameters
    public static Collection<?> getParameters() {
        return List.of(LITTLE_ENDIAN, BIG_ENDIAN);
    }

}
