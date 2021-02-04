package io.intino.other;

import io.intino.alexandria.led.buffers.AbstractBitBuffer;
import io.intino.alexandria.led.buffers.AbstractBitBuffer.BitInfo;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.util.BitUtils;
import io.intino.alexandria.led.util.memory.ModifiableMemoryAddress;
import org.junit.Assert;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static io.intino.alexandria.led.util.BitUtils.*;

public class Test {

    public static BitInfo bitInfo;
    private static int bitOffset;
    private static int numBytes;
    private static int byteIndex;

    public static void main(String[] args) throws InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(128);
        ByteBufferStore store = new ByteBufferStore(buffer, ModifiableMemoryAddress.of(buffer), 0, buffer.capacity());
        AbstractBitBuffer bitBuffer = buffer.order() == ByteOrder.LITTLE_ENDIAN ? new LittleEndianBitBuffer(store) : new BigEndianBitBuffer(store);


        long x = 0b1010101010101010101010101010101010101010101010101010101010101011L;
        int bitIndex = 1;
        int bitCount = 64;

        BitInfo bitInfo = computeBitInfo(bitIndex, bitCount, buffer.capacity());
        System.out.println(bitInfo);

        for(int i = 0;i <= 64;i++) {
            bitBuffer.clear();
            final long n = x;
            bitBuffer.setLongNBits(i, bitCount, n);
            final long result = bitBuffer.getLongNBits(i, bitCount);
            Assert.assertEquals(n, result);
            //System.out.println("=== " + i + " ===");
            //System.out.println("origin = " + BitUtils.toBinaryString(n, 64, 8));
            //System.out.println("result = " + BitUtils.toBinaryString(result, 64, 8));
            //System.out.println();
        }

        //System.out.println("origin = " + ": " + BitUtils.toBinaryString(x, 64, 8));
        //bitBuffer.setLongNBits(bitIndex, bitCount, x);
//
        //long result = bitBuffer.getLongNBits(bitIndex, bitCount);
        //System.out.println("result = " + ": " + BitUtils.toBinaryString(result, 64, 8));
//
        //System.out.println(bitBuffer.toBinaryString(8));
    }

    public static BitInfo computeBitInfo(int bitIndex, int bitCount, int bufferSize) {
        int byteIndex = byteIndex(bitIndex);
        final int numBytes = getMinimumBytesForBits(bitIndex, bitCount);
        final int additionalBytes = getAdditionalBytes(bufferSize, byteIndex, numBytes);
        byteIndex -= additionalBytes;
        final int bitOffset = (numBytes * Byte.SIZE - bitCount - offsetOf(bitIndex)) - additionalBytes * Byte.SIZE;
        return new BitInfo(bitIndex, byteIndex, numBytes, bitOffset, bitCount);
    }

    private static void testSigned() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(32);
        ByteBufferStore store = new ByteBufferStore(buffer, ModifiableMemoryAddress.of(buffer), 0, buffer.capacity());
        BitBuffer bitBuffer = buffer.order() == ByteOrder.LITTLE_ENDIAN ? new LittleEndianBitBuffer(store) : new BigEndianBitBuffer(store);

        int nBits = 40;

        long input = -1;
        bitBuffer.setLongNBits(0, nBits, input);
        long output = bitBuffer.getLongNBits(0, nBits);
        System.out.println("mask = " + BitUtils.toBinaryString(~(1L << (nBits)), 64));
        System.out.println("outp = " + BitUtils.toBinaryString(output, 64));
        System.out.println("outs = " + BitUtils.toBinaryString(extendSign(output, nBits+1), 64));

        System.out.println(input + ": " + BitUtils.toBinaryString(input, 64)
                + "\nvs\n" + output + ": " + BitUtils.toBinaryString(output, 64));
    }

    private static long extendSign(long n, int nBits) {
        final long m = 1L << (nBits - 1);
        return (n ^ m) - m;
    }

    private static void someTests() {
        int bitIndex = 1;
        int bits = 16;
        long buffer = 0;

        short input = 0b10000000_0000001;

        short high = (short) (input >> bitIndex);
        short low = (short) (input & 0xF);
        short output = (short) ((high << bitIndex) | low);

        int minBytes = BitUtils.getMinimumBytesForBits(1, 64);
        int additionalBytes = BitUtils.getAdditionalBytes(128, 1, minBytes);
        System.out.println(minBytes);
        System.out.println(additionalBytes);

        System.out.println(BitUtils.toBinaryString(input, 16));
        System.out.println(BitUtils.toBinaryString(high, 16));
        System.out.println(BitUtils.toBinaryString(low, 16));
        System.out.println(BitUtils.toBinaryString(output, 16));
    }

}
