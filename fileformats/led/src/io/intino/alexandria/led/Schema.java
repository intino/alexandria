package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.OffHeapObject;
import io.intino.alexandria.led.util.memory.MemoryUtils;
import io.intino.alexandria.logger.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public abstract class Schema implements OffHeapObject, Comparable<Schema> {

	public static long idOf(Schema schema) {
		return schema.id();
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
		bitBuffer = store.order() == LITTLE_ENDIAN
				? new LittleEndianBitBuffer(store)
				: new BigEndianBitBuffer(store);
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

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(boolean verbose) {
		Class<? extends Schema> clazz = getClass();
		Definition definition = clazz.getAnnotation(Definition.class);
		if(definition == null) {
			return super.toString();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(definition.name());
		if(verbose) {
			sb.append("(").append(definition.size()).append(" bytes)");
		}
		sb.append(" {");

		final Method[] methods = Arrays.stream(clazz.getMethods())
				.filter(m -> m.isAnnotationPresent(Attribute.class))
				.sorted(Comparator.comparingInt(m -> m.getAnnotation(Attribute.class).index()))
				.toArray(Method[]::new);

		for(int i = 0;i < methods.length;i++) {
			Method method = methods[i];
			Attribute attribute = method.getAnnotation(Attribute.class);
			Object value;
			try {
				value = method.invoke(this);
			} catch (Throwable ignored) {
				value = null;
			}
			if(verbose) {
				sb.append('[').append(attribute.type().typename())
						.append(',').append(attribute.index())
						.append(',').append(attribute.size()).append("] ");
			}
			sb.append(attribute.name()).append(" = ").append(value);
			if(i < methods.length - 1) sb.append(", ");
		}
		return sb.append('}').toString();
	}


	public enum DataType {

		BYTE(Byte.SIZE),
		UNSIGNED_BYTE(Byte.SIZE),
		SHORT(Short.SIZE),
		UNSIGNED_SHORT(Short.SIZE),
		INT(Integer.SIZE),
		UNSIGNED_INT(Integer.SIZE),
		LONG(Long.SIZE),
		UNSIGNED_LONG(Long.SIZE - 1),
		FLOAT(Float.SIZE),
		DOUBLE(Double.SIZE),
		CATEGORY(Long.SIZE);

		public final int maxSize;

		DataType(int maxSize) {
			this.maxSize = maxSize;
		}

		public String typename() {
			return name().toLowerCase();
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Attribute {
		String name();
		DataType type();
		int index();
		int size();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Id {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Definition {
		String name();
		int size();
	}
}