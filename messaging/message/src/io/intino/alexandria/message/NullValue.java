package io.intino.alexandria.message;

import java.time.Instant;

class NullValue implements Message.Value {
	@Override
	public String data() {
		return null;
	}

	@Override
	public <T> T as(Class<T> type) {
		if (type.isArray()) return (T) new Object[0];
		return null;
	}

	@Override
	public Instant asInstant() {
		return null;
	}

	@Override
	public int asInteger() {
		return 0;
	}

	@Override
	public Long asLong() {
		return null;
	}

	@Override
	public String asString() {
		return null;
	}

	@Override
	public double asDouble() {
		return 0;
	}

	@Override
	public boolean asBoolean() {
		return false;
	}
}
