package io.intino.alexandria.message;

import java.time.Instant;

class DataValue implements Message.Value {
	private final String data;

	public DataValue(String data) {
		this.data = data;
	}

	@Override
	public String data() {
		return data;
	}

	@Override
	public <T> T as(Class<T> type) {
		if (data != null) return (T) Parser.of(type).parse(data);
		else {
			if (type.isArray()) return (T) new Object[0];
			return null;
		}
	}

	@Override
	public Instant asInstant() {
		return Instant.parse(data);
	}

	@Override
	public int asInteger() {
		return Integer.parseInt(data);
	}

	@Override
	public Long asLong() {
		return Long.parseLong(data);
	}

	@Override
	public String asString() {
		return data;
	}

	@Override
	public double asDouble() {
		return Double.parseDouble(data);
	}

	@Override
	public boolean asBoolean() {
		return Boolean.parseBoolean(data);
	}

	@Override
	public String toString() {
		return data;
	}
}
