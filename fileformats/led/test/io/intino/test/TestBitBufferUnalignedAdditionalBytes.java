package io.intino.test;

import io.intino.alexandria.led.buffers.AbstractBitBuffer;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.util.BitUtils;
import io.intino.alexandria.led.util.memory.MemoryAddress;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;
import static org.junit.Assert.assertEquals;

public class TestBitBufferUnalignedAdditionalBytes {

    private static final int SIZE = 22;
    private static final int BIT_INDEX = 158;
    private static final int BIT_COUNT = 12;

    @Test
    public void testLittleEndian() {
        ByteBuffer byteBuffer = allocBuffer(SIZE, ByteOrder.LITTLE_ENDIAN);
        ByteBufferStore store = new ByteBufferStore(byteBuffer, MemoryAddress.of(byteBuffer), 0, byteBuffer.capacity());
        AbstractBitBuffer bitBuffer = new LittleEndianBitBuffer(store);
        final int value = (int) BitUtils.maxPossibleNumber(BIT_COUNT);
        bitBuffer.setIntegerNBits(BIT_INDEX, BIT_COUNT, value);
        assertEquals(value, bitBuffer.getIntegerNBits(BIT_INDEX, BIT_COUNT));
    }

    @Test
    public void testBigEndian() {
        ByteBuffer byteBuffer = allocBuffer(SIZE, ByteOrder.BIG_ENDIAN);
        ByteBufferStore store = new ByteBufferStore(byteBuffer, MemoryAddress.of(byteBuffer), 0, byteBuffer.capacity());
        AbstractBitBuffer bitBuffer = new BigEndianBitBuffer(store);
        final int value = (int) BitUtils.maxPossibleNumber(BIT_COUNT);
        bitBuffer.setIntegerNBits(BIT_INDEX, BIT_COUNT, value);
        assertEquals(value, bitBuffer.getIntegerNBits(BIT_INDEX, BIT_COUNT));
    }
}
