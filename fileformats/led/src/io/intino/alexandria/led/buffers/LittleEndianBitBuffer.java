package io.intino.alexandria.led.buffers;

import io.intino.alexandria.led.buffers.store.ByteStore;

import static io.intino.alexandria.led.util.BitUtils.*;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class LittleEndianBitBuffer extends AbstractBitBuffer {

	public LittleEndianBitBuffer(ByteStore store) {
		super(store);
	}

	@Override
	protected BitInfo computeBitInfo(int bitIndex, int bitCount) {
		int byteIndex = byteIndex(bitIndex);
		final int numBytes = getMinimumBytesForBits(bitIndex, bitCount);
		final int additionalBytes = getAdditionalBytes(byteSize(), byteIndex, numBytes);
		byteIndex -= additionalBytes;
		final int bitOffset = offsetOf(bitIndex) + additionalBytes * Byte.SIZE;
		return new BitInfo(bitIndex, byteIndex, numBytes, bitOffset, bitCount);
	}
}