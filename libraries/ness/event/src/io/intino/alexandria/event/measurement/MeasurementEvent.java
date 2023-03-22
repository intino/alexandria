package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent.Measurement.Attribute;
import io.intino.alexandria.message.Message;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class MeasurementEvent implements Event {
	private static final String MEASUREMENT_SEP = "\\|";
	private static final String ATTRIBUTE_SEP = ":";

	protected final String type;
	protected final String source;
	protected final Instant ts;
	protected final Measurement[] measurements;
	protected final double[] values;

	public MeasurementEvent(String type, String source, Instant ts, String[] measurements, double[] values) {
		this.type = requireNonNull(type, "type cannot be null");
		this.source = requireNonNull(source, "source cannot be null");
		this.ts = requireNonNull(ts, "ts cannot be null");
		this.measurements = loadMeasurements(measurements);
		this.values = values;
	}

	public MeasurementEvent(String type, String source, Instant ts, Measurement[] measurements, double[] values) {
		this.type = requireNonNull(type, "type cannot be null");
		this.source = requireNonNull(source, "source cannot be null");
		this.ts = requireNonNull(ts, "ts cannot be null");
		this.measurements = measurements;
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

	public Measurement[] measurements() {
		return measurements;
	}

	public double[] values() {
		return values;
	}

	@Override
	public Format format() {
		return Format.Measurement;
	}

	private Measurement[] loadMeasurements(String[] measurements) {
		return Arrays.stream(measurements)
				.map(m -> m.split(MEASUREMENT_SEP))
				.map(fs -> new Measurement(fs[0], fs.length > 1 ? attributesOf(fs) : new Attribute[0]))
				.toArray(Measurement[]::new);
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
		message.set("measurements", measurements);
		message.set("values", values);
		return message.toString();
	}

	public static class Measurement {
		private final String name;
		private final Attribute[] attributes;

		private Measurement(String name, Attribute[] attributes) {
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
			return name + ":{" + Arrays.stream(attributes).map(Object::toString).collect(Collectors.joining(",")) + "}";
		}

		public static class Attribute {
			public final String name;
			public final String value;

			private Attribute(String[] nameValue) {
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
				return name + "=" + value;
			}
		}
	}
}