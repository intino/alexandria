package io.intino.alexandria.zit;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zit.model.Period;

import java.io.*;
import java.time.Instant;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class ZitWriter implements AutoCloseable {
	private final BufferedWriter writer;
	private Period period;
	private Instant nextTs;

	public ZitWriter(File file) throws IOException {
		file.getParentFile().mkdirs();
		this.writer = writer(file);
	}

	public ZitWriter(File file, String sensor, Period period, String[] sensorModel) throws IOException {
		this.period = period;
		boolean exists = file.exists();
		file.getParentFile().mkdirs();
		this.writer = writer(file);
		if (!exists || file.length() == 0) {
			writeLine("@id " + sensor);
			writeLine("@period " + period.toString());
			put(sensorModel);
		}
	}

	public ZitWriter put(String[] sensorModel) {
		writeLine("@measurements " + String.join(",", sensorModel).stripTrailing());
		return this;
	}

	public ZitWriter put(Instant instant) {
		writeLine("@instant " + instant.toString());
		this.nextTs = instant;
		return this;
	}

	public ZitWriter put(Instant instant, double[] data) {
		if (!instant.equals(this.nextTs)) put(instant);
		return put(data);
	}

	public ZitWriter put(double[] data) {
		writeLine(toString(data));
		this.nextTs = period.next(nextTs);
		return this;
	}

	private void writeLine(String line) {
		try {
			writer.write(line + "\n");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String toString(double[] values) {
		return Arrays.stream(values).mapToObj(this::toString).collect(joining("\t")).stripTrailing();
	}

	private String toString(double value) {
		return Double.isNaN(value) ? "" : format(value);
	}

	private String format(double value) {
		long v = (long) value;
		return value == v ? String.valueOf(v) : String.valueOf(value);
	}

	private BufferedWriter writer(File file) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(Zit.compressing(new FileOutputStream(file))));
	}

	@Override
	public void close() throws Exception {
		writer.close();
	}
}