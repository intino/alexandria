package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.OffHeapObject;
import io.intino.alexandria.logger.Logger;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public abstract class Schema implements OffHeapObject, Comparable<Schema>, Serializable {

	public static long idOf(Schema schema) {
		return schema.id();
	}

	public static <T extends Schema> int sizeOf(Class<T> type) {
		try {
			final Field size = type.getField("SIZE");
			size.setAccessible(true);
			return (int) size.get(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException("SIZE is not defined for this schema class: " + type.getSimpleName(), e);
		}
	}

	public static <T extends Schema> UUID getSerialUUID(Class<T> type) {
		try {
			final Field serialUUID = type.getField("SERIAL_UUID");
			serialUUID.setAccessible(true);
			return (UUID) serialUUID.get(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException("SERIAL_UUID is not defined for this schema class: " + type.getSimpleName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Schema> SchemaFactory<T> factoryOf(Class<T> type) {
		try {
			final Field factory = type.getField("FACTORY");
			factory.setAccessible(true);
			return (SchemaFactory<T>) factory.get(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException("FACTORY is not defined for schema class: " + type.getSimpleName(), e);
		}
	}

	protected final BitBuffer bitBuffer;

	public Schema(ByteStore store) {
		bitBuffer = store.order() == LITTLE_ENDIAN
				? new LittleEndianBitBuffer(store)
				: new BigEndianBitBuffer(store);
	}

	public abstract long id();
	public abstract int size();
	public abstract UUID serialUUID();

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

	public BitBuffer bitBuffer() {
		return bitBuffer;
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"id="+ id() +
				", memoryAddress=" + address() + baseOffset() +
				", byteSize=" + byteSize() +
				", binaryString='" + toBinaryString(Byte.SIZE) + '\'' +
				'}';
	}
}