package io.intino.alexandria.message;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataValue implements Message.Value {
	private final String data;

	public DataValue(String data) {
		this.data = data;
	}

	public DataValue(Object data) {
		this.data = data.toString();
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
	public List<Message.Value[]> asTable() {
		return Arrays.stream(data.split("\\u0001")).map(r -> Arrays.stream(r.split("\t")).map(DataValue::new).toArray(Message.Value[]::new)).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return data;
	}
}
