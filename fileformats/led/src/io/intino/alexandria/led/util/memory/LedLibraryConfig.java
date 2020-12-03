package io.intino.alexandria.led.util.memory;

import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public final class LedLibraryConfig {

	private LedLibraryConfig() {}

	public static final LedLibraryConfig.Variable<ByteOrder> DEFAULT_BYTE_ORDER = new LedLibraryConfig.Variable<>(ByteOrder.nativeOrder());
	public static final LedLibraryConfig.Variable<Boolean> USE_MEMORY_TRACKER = new LedLibraryConfig.Variable<>(false);
	public static final LedLibraryConfig.Variable<Consumer<Long>> BEFORE_ALLOCATION_CALLBACK = new LedLibraryConfig.Variable<>();
	public static final LedLibraryConfig.Variable<Consumer<AllocationInfo>> ALLOCATION_CALLBACK = new LedLibraryConfig.Variable<>();
	public static final LedLibraryConfig.Variable<Consumer<AllocationInfo>> FREE_CALLBACK = new LedLibraryConfig.Variable<>();
	public static final LedLibraryConfig.Variable<Integer> DEFAULT_BUFFER_SIZE = new LedLibraryConfig.Variable<>(1024);
	public static final LedLibraryConfig.Variable<Boolean> INPUTLEDSTREAM_CONCURRENCY_ENABLED = new LedLibraryConfig.Variable<>(false);


	public static final class Variable<T> {

		private final AtomicReference<T> value;

		Variable() {
			value = new AtomicReference<>();
		}

		Variable(T defaultValue) {
			this();
			set(defaultValue);
		}

		public boolean isEmpty() {
			return value.get() == null;
		}

		public T get() {
			return value.get();
		}

		public T getOrDefault(T newValue) {
			if (isEmpty()) {
				set(newValue);
			}
			return value.get();
		}

		public Variable<T> set(T value) {
			this.value.set(requireNonNull(value));
			return this;
		}
	}
}
