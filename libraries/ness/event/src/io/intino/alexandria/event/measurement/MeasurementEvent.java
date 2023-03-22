package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent.Measurement.Attribute;

import java.time.Instant;
import java.util.Arrays;

public class MeasurementEvent implements Event {
	protected final String type;
	protected final Instant ts;
	protected final String source;
	protected final Measurement[] measurements;
	protected final double[] values;

	public MeasurementEvent(String type, String source, Instant ts, String[] measurements, double[] values) {
		this.type = type;
		this.ts = ts;
		this.source = source;
		this.measurements = loadMeasurements(measurements);
		this.values = values;
	}

	public MeasurementEvent(String type, Instant ts, String source, Measurement[] measurements, double[] values) {
		this.type = type;
		this.ts = ts;
		this.source = source;
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
				.map(m -> m.split("\\|"))
				.map(fs -> new Measurement(fs[0], fs.length > 1 ? attributesOf(fs) : new Attribute[0]))
				.toArray(Measurement[]::new);
	}

	private Attribute[] attributesOf(String[] fs) {
		return Arrays.stream(fs)
				.skip(1)
				.map(f -> new Attribute(f.split(":")))
				.toArray(Attribute[]::new);
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
		}
	}
}