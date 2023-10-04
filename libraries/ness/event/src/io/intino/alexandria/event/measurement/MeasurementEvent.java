package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent.Magnitude.Attribute;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.zit.Zit;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class MeasurementEvent implements Event {
	public static final String MAGNITUDE_SEP = Zit.MAGNITUDE_DELIMITER;
	public static final String ATTRIBUTE_SEP = Zit.ATTRIBUTE_DELIMITER;
	public static final String NAME_VALUE_SEP = Zit.NAME_VALUE_SEP;

	protected final String type;
	protected final String source;
	protected final Instant ts;
	protected final Magnitude[] magnitudes;
	protected final double[] values;

	public MeasurementEvent(String type, String source, Instant ts, String[] measurements, double[] values) {
		this.type = requireNonNull(type, "type cannot be null");
		this.source = requireNonNull(source, "source cannot be null");
		this.ts = requireNonNull(ts, "ts cannot be null");
		this.magnitudes = loadMeasurements(measurements);
		this.values = values;
	}

	public MeasurementEvent(String type, String source, Instant ts, Magnitude[] magnitudes, double[] values) {
		this.type = requireNonNull(type, "type cannot be null");
		this.source = requireNonNull(source, "source cannot be null");
		this.ts = requireNonNull(ts, "ts cannot be null");
		this.magnitudes = magnitudes;
		this.values = values;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public Instant ts() {
		return ts;
	}

	@Override
	public String ss() {
		return source;
	}

	public Magnitude[] measurements() {
		return magnitudes;
	}

	public double[] values() {
		return values;
	}

	@Override
	public Format format() {
		return Format.Measurement;
	}

	private Magnitude[] loadMeasurements(String[] measurements) {
		return Arrays.stream(measurements)
				.map(m -> m.split(MAGNITUDE_SEP))
				.map(fs -> new Magnitude(fs[0], fs.length > 1 ? attributesOf(fs) : new Attribute[0]))
				.toArray(Magnitude[]::new);
	}

	private Attribute[] attributesOf(String[] fs) {
		return Arrays.stream(fs)
				.skip(1)
				.map(f -> new Attribute(f.split(ATTRIBUTE_SEP)))
				.toArray(Attribute[]::new);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MeasurementEvent that = (MeasurementEvent) o;
		return Objects.equals(type, that.type) && Objects.equals(source, that.source) && Objects.equals(ts, that.ts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, source, ts);
	}

	@Override
	public String toString() {
		Message message = new Message(type());
		message.set("ss", ss());
		message.set("ts", ts());
		message.set("measurements", Arrays.stream(magnitudes).map(Magnitude::toString).collect(toList()));
		message.set("values", values);
		return message.toString();
	}

	public static class Magnitude {
		private final String name;
		private final Attribute[] attributes;

		public Magnitude(String name, Attribute[] attributes) {
			this.name = name;
			this.attributes = attributes;
		}

		public String name() {
			return name;
		}

		public Attribute[] attributes() {
			return attributes;
		}

		@Override
		public String toString() {
			String attributes = Arrays.stream(this.attributes).map(Attribute::toString).collect(joining(ATTRIBUTE_SEP));
			return name + (this.attributes.length > 0 ? ATTRIBUTE_SEP + attributes : "");
		}

		public static class Attribute {
			public final String name;
			public final String value;

			public Attribute(String[] nameValue) {
				this.name = nameValue[0];
				this.value = nameValue.length > 1 ? nameValue[1] : null;
			}

			public String name() {
				return name;
			}

			public String value() {
				return value;
			}

			@Override
			public String toString() {
				return name + NAME_VALUE_SEP + value;
			}
		}
	}
}