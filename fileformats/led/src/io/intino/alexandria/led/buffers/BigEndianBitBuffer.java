package io.intino.alexandria.led.buffers;

import io.intino.alexandria.led.buffers.store.ByteStore;

import static io.intino.alexandria.led.util.BitUtils.offsetOf;

public class BigEndianBitBuffer extends AbstractBitBuffer {

	public BigEndianBitBuffer(ByteStore store) {
		super(store);
	}

	@Override
	protected int computeBitOffset(int bitIndex, int bitCount, int byteIndex, int numBytes, int additionalBytes) {
		return (numBytes * Byte.SIZE - bitCount - offsetOf(bitIndex)) - additionalBytes * Byte.SIZE;
	}

}