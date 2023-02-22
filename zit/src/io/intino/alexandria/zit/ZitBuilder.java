package io.intino.alexandria.zit;

import com.github.luben.zstd.ZstdOutputStream;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.time.Instant;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class ZitBuilder implements AutoCloseable {
	private final BufferedWriter writer;

	public ZitBuilder(File file, String sensor) throws IOException {
		boolean exists = file.exists();
		file.getParentFile().mkdirs();
		this.writer = writer(file);
		if (!exists) writeLine("@id " + sensor);
	}

	public ZitBuilder put(String[] sensorModel) {
		writeLine("@measurements " + String.join("\t", sensorModel).stripTrailing());
		return this;
	}

	public ZitBuilder put(Instant timeModel) {
		writeLine("@instant " + timeModel.toString() + "\n");
		return this;
	}

	public ZitBuilder put(double[] data) {
		writeLine(toString(data));
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
		return new BufferedWriter(new OutputStreamWriter(new ZstdOutputStream(new FileOutputStream(file))));
	}

	@Override
	public void close() throws Exception {
		writer.close();
	}
}