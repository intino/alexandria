package io.intino.alexandria.zit;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.resourcecleaner.DisposableResource;
import io.intino.alexandria.zit.model.Data;
import io.intino.alexandria.zit.model.Period;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import static io.intino.alexandria.zit.Zit.MAGNITUDE_DELIMITER;
import static java.util.stream.Collectors.joining;

public class ZitWriter implements AutoCloseable {
	private final Writer writer;
	private final DisposableResource resource;
	private Period period;
	private String[] sensorModel;
	private Instant nextTs;

	public ZitWriter(File file) throws IOException {
		file.getParentFile().mkdirs();
		if (file.exists() && file.length() > 0) loadHeader(file);
		this.writer = new OutputStreamWriter(Zit.compressing(new BufferedOutputStream(new FileOutputStream(file, true))));
		this.resource = DisposableResource.whenDestroyed(this).thenClose(writer);
	}

	public ZitWriter(OutputStream stream) throws IOException {
		this.writer = new BufferedWriter(new OutputStreamWriter(Zit.compressing(stream)));
		this.resource = DisposableResource.whenDestroyed(this).thenClose(writer);
	}

	public ZitWriter(File file, String id, String sensor, Period period, String[] sensorModel) throws IOException {
		this.period = period;
		this.sensorModel = sensorModel;
		boolean exists = file.exists();
		file.getParentFile().mkdirs();
		this.writer = new OutputStreamWriter(Zit.compressing(new BufferedOutputStream(new FileOutputStream(file))));
		if (!exists || file.length() == 0) {
			writeLine("@id " + id);
			writeLine("@sensor " + sensor);
			writeLine("@period " + period.toString());
			put(sensorModel);
		}
		this.resource = DisposableResource.whenDestroyed(this).thenClose(writer);
	}

	public void put(String[] sensorModel) {
		writeLine("@measurements " + String.join(MAGNITUDE_DELIMITER, sensorModel).stripTrailing());
		this.sensorModel = sensorModel;
	}

	public String[] sensorModel() {
		return sensorModel;
	}

	private void loadHeader(File file) throws IOException {
		try (ZitStream stream = ZitStream.of(file)) {
			Data data = stream.reduce((first, second) -> second).orElse(null);
			if (data != null) {
				period = stream.period();
				nextTs = period.next(data.ts());
			}
		}
	}

	public void put(Instant instant) {
		writeLine("@instant " + instant.toString());
		this.nextTs = instant;
	}

	public void put(Instant instant, double[] data) {
		if (!areClose(instant, this.nextTs)) put(instant);
		put(data);
	}

	private boolean areClose(Instant instant, Instant nextTs) {
		if (nextTs == null) return false;
		Duration between = Duration.between(instant, nextTs);
		return Math.abs(between.getSeconds()) < (period.duration() / 2);
	}

	public void put(double[] data) {
		writeLine(toString(data));
		this.nextTs = period.next(nextTs);
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

	@Override
	public void close() {
		resource.close();
	}
}