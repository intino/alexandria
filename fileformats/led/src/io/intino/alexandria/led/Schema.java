package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.memory.MemoryUtils;
import io.intino.alexandria.led.util.OffHeapObject;
import io.intino.alexandria.logger.Logger;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;
import java.util.Objects;

public abstract class Schema implements OffHeapObject, Comparable<Schema> {

	public static long idOf(Schema schema) {
		return MemoryUtils.getLong(schema.address(), schema.baseOffset());
	}

	public static <T extends Schema> int sizeOf(Class<T> type) {
		try {
			return (int) type.getField("SIZE").get(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			Logger.error(e);
		}
		return -1;
	}

	/**
	 * This uses reflection!!
	 * */
	public static <T extends Schema> SchemaFactory<T> factoryOf(Class<T> type) {
		try {
			final Constructor<T> constructor = type.getConstructor(ByteStore.class);
			return store -> {
				try {
					return constructor.newInstance(store);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		} catch (NoSuchMethodException e) {
			Logger.error(e);
			throw new RuntimeException(e);
		}
	}

	protected final BitBuffer bitBuffer;

	public Schema(ByteStore store) {
		bitBuffer = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ?
				new LittleEndianBitBuffer(store) :
				new BigEndianBitBuffer(store);
	}

	protected abstract long id();

	public abstract int size();

	@Override
	public int hashCode() {
		return Objects.hashCode(id());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() != getClass()) return false;
		Schema other = (Schema) obj;
		return notNull() && other.notNull() && id() == other.id();
	}

	public boolean isReadOnly() {
		return bitBuffer.isReadOnly();
	}

	@Override
	public boolean isNull() {
		return bitBuffer.isNull();
	}

	@Override
	public boolean notNull() {
		return bitBuffer.notNull();
	}

	@Override
	public int compareTo(Schema o) {
		return Long.compare(id(), o.id());
	}

	public void clear() {
		bitBuffer.clear();
	}

	@Override
	public long address() {
		return bitBuffer.address();
	}

	@Override
	public long byteSize() {
		return bitBuffer.byteSize();
	}

	@Override
	public long baseOffset() {
		return bitBuffer.baseOffset();
	}

	public void invalidate() {
		bitBuffer.invalidate();
	}

	public long bitCount() {
		return bitBuffer.bitCount();
	}

	public long endOffset() {
		return bitBuffer.endOffset();
	}

	public String toBinaryString() {
		return bitBuffer.toBinaryString();
	}

	public String toBinaryString(int splitSize) {
		return bitBuffer.toBinaryString(splitSize);
	}

	public String toHexString() {
		return bitBuffer.toHexString();
	}
}