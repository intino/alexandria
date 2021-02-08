package io.intino.other;

import io.intino.alexandria.led.buffers.AbstractBitBuffer;
import io.intino.alexandria.led.buffers.AbstractBitBuffer.BitInfo;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.BitUtils;
import io.intino.alexandria.led.util.memory.ModifiableMemoryAddress;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import static io.intino.alexandria.led.util.BitUtils.*;
import static io.intino.alexandria.led.util.memory.MemoryUtils.memset;
import static java.nio.ByteOrder.*;

public class OtherTests {

    public static BitInfo bitInfo;
    private static int bitOffset;
    private static int numBytes;
    private static int byteIndex;

    public static void main(String[] args) throws InterruptedException {
        //testLongAlignment(LITTLE_ENDIAN);
        //testLongAlignment(BIG_ENDIAN);
    }

    private static void testLongAlignment(ByteOrder order) {

        AbstractBitBuffer bitBuffer = createBitBuffer(128, order);

        final int maxBitIndex = (int) (bitBuffer.bitCount() - Long.SIZE - Byte.SIZE);
        final int maxBitCount = 64;

        Random random = new Random();
        for (int i = 0; i < bitBuffer.byteSize(); i++)
            memset(bitBuffer.address(), 1, random.nextInt() - random.nextInt());

        for(int bitCount = 64;bitCount <= maxBitCount;bitCount++) {
            for(int bitIndex = 0;bitIndex < maxBitIndex;bitIndex++) {
                long x = random.nextLong();
                bitBuffer.clear();
                bitBuffer.setLongNBits(bitIndex, bitCount, x);
                final long result = bitBuffer.getLongNBits(bitIndex, bitCount);
                if(x != result) {
                    System.err.println("[" + order + "] \n(bitIndex="+ bitIndex + ",bitCount=" + bitCount + ") ==> Expected "+ x + " but was " + result +
                            "\nORIGIN = " + BitUtils.toBinaryString(x, 64, 8)
                            + "\nRESULT = " + BitUtils.toBinaryString(result, 64, 8));
                }
            }
        }
    }


    private static AbstractBitBuffer createBitBuffer(int numBytes, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(numBytes);
        buffer.order(order);
        ByteBufferStore store = new ByteBufferStore(buffer, ModifiableMemoryAddress.of(buffer), 0, buffer.capacity());
        return buffer.order() == LITTLE_ENDIAN ? new LittleEndianBitBuffer(store) : new BigEndianBitBuffer(store);
    }

    public static BitInfo computeBitInfo(int bitIndex, int bitCount, int bufferSize) {
        int byteIndex = byteIndex(bitIndex);
        final int numBytes = getMinimumBytesFor(bitIndex, bitCount);
        final int additionalBytes = getAdditionalBytes(bufferSize, byteIndex, numBytes);
        byteIndex -= additionalBytes;
        final int bitOffset = (numBytes * Byte.SIZE - bitCount - offsetOf(bitIndex)) - additionalBytes * Byte.SIZE;
        return new BitInfo(bitIndex, byteIndex, numBytes, bitOffset, bitCount);
    }

    private static void testSigned() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(32);
        buffer.order(BIG_ENDIAN);
        ByteBufferStore store = new ByteBufferStore(buffer, ModifiableMemoryAddress.of(buffer), 0, buffer.capacity());
        BitBuffer bitBuffer = buffer.order() == LITTLE_ENDIAN ? new LittleEndianBitBuffer(store) : new BigEndianBitBuffer(store);

        int nBits = 40;

        long input = -12345;
        bitBuffer.setLongNBits(0, nBits, input);
        long output = bitBuffer.getLongNBits(0, nBits);
        System.out.println("mask = " + BitUtils.toBinaryString(input, 64, 8));
        System.out.println("outp = " + BitUtils.toBinaryString(output, 64, 8));
        System.out.println("outs = " + BitUtils.toBinaryString(extendSign(output, nBits), 64, 8));

        System.out.println(input + ": " + BitUtils.toBinaryString(input, 64, 8)
                + "\nvs\n" + extendSign(output, nBits) + ": " + BitUtils.toBinaryString(extendSign(output, nBits), 64, 8));
    }

    private static long extendSign(long n, int nBits) {
        long shift = Long.SIZE - nBits;
        return n << shift >> shift;
        //final long m = 1L << (nBits - 1);
        //return (n ^ m) - m;
    }

    private static void someTests() {
        int bitIndex = 1;
        int bits = 16;
        long buffer = 0;

        short input = 0b10000000_0000001;

        short high = (short) (input >> bitIndex);
        short low = (short) (input & 0xF);
        short output = (short) ((high << bitIndex) | low);

        int minBytes = BitUtils.getMinimumBytesFor(1, 64);
        int additionalBytes = BitUtils.getAdditionalBytes(128, 1, minBytes);
        System.out.println(minBytes);
        System.out.println(additionalBytes);

        System.out.println(BitUtils.toBinaryString(input, 16));
        System.out.println(BitUtils.toBinaryString(high, 16));
        System.out.println(BitUtils.toBinaryString(low, 16));
        System.out.println(BitUtils.toBinaryString(output, 16));
    }

}
