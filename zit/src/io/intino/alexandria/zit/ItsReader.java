package io.intino.alexandria.zit;

import io.intino.alexandria.zit.model.Data;
import io.intino.alexandria.zit.model.Period;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Double.NaN;

public class ItsReader implements AutoCloseable {
	private final Stream<String> lines;
	private String sensor;
	private Instant instant = null;
	private Period period = null;
	private String[] measurements;

	public ItsReader(InputStream is) {
		this.lines = new BufferedReader(new InputStreamReader(is)).lines();
	}

	public String sensor() {
		return sensor;
	}

	public Stream<Data> data() {
		return lines
				.map(this::parse)
				.filter(Objects::nonNull);
	}

	private Data parse(String l) {
		if (isAnnotation(l)) {
			loadAnnotation(l);
			return null;
		} else {
			final Data data = new Data(instant, loadMeasurement(l.split(",")), measurements);
			instant = period.next(instant);
			return data;
		}
	}

	private double[] loadMeasurement(String[] fields) {
		checkModel();
		double[] values = new double[fields.length];
		for (int i = 0; i < fields.length; i++)
			values[i] = parseValue(fields[i]);
		return values;
	}


	private static boolean isAnnotation(String line) {
		return line.startsWith("@");
	}

	private double parseValue(String s) {
		return s.isEmpty() ? NaN : Double.parseDouble(s);
	}

	private void checkModel() {
		if (period == null) throw new IllegalArgumentException("No period defined");
		if (instant == null) throw new IllegalArgumentException("No instant defined");
		if (measurements == null) throw new IllegalArgumentException("No measurements defined");
	}

	private void loadAnnotation(String line) {
		int index = line.indexOf(' ');
		if (index < 0) return;
		String metadata = line.substring(1, index).toLowerCase();
		String data = line.substring(index + 1);
		switch (metadata) {
			case "id":
				sensor = data;
			case "instant":
				instant = Instant.parse(data);
				break;
			case "period":
				period = Period.of(data);
				break;
			case "measurements":
				measurements = data.split(",");
				break;
		}
	}

	@Override
	public void close() {
		lines.close();
	}
}
