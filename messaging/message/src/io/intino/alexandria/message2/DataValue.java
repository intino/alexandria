package io.intino.alexandria.message2;

import io.intino.alexandria.Resource;

import java.time.Instant;
import java.util.Map;

import static java.util.Arrays.stream;

class DataValue implements Message.Value {
	private final String data;
	private final Map<String, byte[]> attachments;

	public DataValue(String data, Map<String, byte[]> attachments) {
		this.data = data;
		this.attachments = attachments;
	}

	@Override
	public String data() {
		return data;
	}

	@Override
	public <T> T as(Class<T> type) {
		if (data != null) return (T) fill(Parser.of(type).parse(data));
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

	private Object fill(Object object) {
		if (object == null) return null;
		if (object instanceof Resource) return fill((Resource) object);
		if (object instanceof Resource[]) return fill((Resource[]) object);
		return object;
	}

	private Resource[] fill(Resource[] resources) {
		return stream(resources).map(this::fill).toArray(Resource[]::new);
	}

	private Resource fill(Resource resource) {
		String key = new String(resource.bytes());
		return new Resource(resource.name(), attachments.getOrDefault(key, new byte[0]));
	}

	@Override
	public String toString() {
		return data;
	}
}
