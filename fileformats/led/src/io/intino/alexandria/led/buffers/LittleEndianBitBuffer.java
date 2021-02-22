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
	protected int computeBitOffset(int bitIndex, int bitCount, int byteIndex, int numBytes, int additionalBytes) {
		return offsetOf(bitIndex) + additionalBytes * Byte.SIZE;
	}
}