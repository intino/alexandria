package io.intino.alexandria.led.util.memory;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

public class Configuration<T> {

	private final AtomicReference<T> value;

	public Configuration() {
		value = new AtomicReference<>();
	}

	public boolean isEmpty() {
		return value.get() == null;
	}

	public T get() {
		return value.get();
	}

	public T get(T newValue) {
		if (isEmpty()) {
			set(newValue);
		}
		return value.get();
	}

	public Configuration<T> set(T value) {
		if (!isEmpty()) {
			throw new RuntimeException("Value already set.");
		}
		this.value.set(requireNonNull(value));
		return this;
	}
}
