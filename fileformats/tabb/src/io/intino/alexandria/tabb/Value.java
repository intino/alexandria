package io.intino.alexandria.tabb;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Value {
	private final ColumnStream.Type type;
	private final Mode mode;
	private final byte[] value;

	public Value(ColumnStream.Type type, Mode mode, byte[] value) {
		this.type = type;
		this.mode = mode;
		this.value = value;
	}

	public Value(ColumnStream.Type type, Object value) {
		this.type = type;
		this.mode = null;
		this.value = type.toByteArray(value);
	}


	byte[] bytes() {
		return value;
	}

	public ColumnStream.Type type() {
		return type;
	}

	public Mode mode() {
		return mode;
	}

	public boolean isAvailable() {
		return !Arrays.equals(type.notAvailable(), value);
	}

	public int asInteger() {
		return get32(isAvailable() ? value : ColumnStream.Type.Integer.notAvailable());
	}

	public double asDouble() {
		return get64(isAvailable() ? value : ColumnStream.Type.Double.notAvailable());
	}

	public boolean asBoolean() {
		return get32(value) == 1;//FIXME Na??
	}

	public Long asLong() {
		return get64(isAvailable() ? value : ColumnStream.Type.Long.notAvailable());
	}

	public LocalDateTime asDatetime() {
		return null;
	}

	public Instant asInstant() {
		return Instant.ofEpochSecond(asInteger());
	}

	public String asString() {
		return isAvailable() ? mode.features[get32(value)] : null;
	}

	public static Value of(ColumnStream.Type type, Mode mode, Object value) {
		return new Value(type, mode, type.toByteArray(mode != null ? Arrays.asList(mode.features).indexOf(value.toString()) : value));
	}

	private static int get32(byte[] data) {
		return (data[0] & 0xFF) << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
	}

	private static long get64(byte[] data) {
		return (data[0] & 0xFFL) << 56 | (data[1] & 0xFFL) << 48 | (data[2] & 0xFFL) << 40 | (data[3] & 0xFFL) << 32 |
				(data[4] & 0xFFL) << 24 | (data[5] & 0xFFL) << 16 | (data[6] & 0xFFL) << 8 | (data[7] & 0xFFL);
	}
}
