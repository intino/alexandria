package io.intino.alexandria.tabb.generators;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.FileGenerator;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class ArffFileGenerator implements FileGenerator {
	private static final String NULL_VALUE = "?";

	private final List<ColumnStream> streams;
	private BufferedWriter writer;

	public ArffFileGenerator(List<ColumnStream> streams) {
		this.streams = streams;

	}

	public FileGenerator destination(File directory, String name) {
		try {
			writer = new BufferedWriter(new FileWriter(new File(directory, name + ".arff")));
			writer.write(new ArffTemplate().render(new FrameBuilder("arff").add("attribute", attributes())));
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	@Override
	public void put(long key) {
		String line = streams.stream()
				.map(s -> s.key().equals(key) ? formatterOf().format(s.value()) : NULL_VALUE)
				.collect(joining(","));
		try {
			writer.write(line + "\n");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private Formatter formatterOf() {
		return new Formatter() {
			@Override
			public String pattern() {
				return null;
			}

			@Override
			public String format(Object value) {
				return value == null ? NULL_VALUE : value.toString();
			}
		};
	}

	private Frame[] attributes() {
		List<Frame> headers = new ArrayList<>();
		headers.add(new FrameBuilder("attribute").add("name", "id").add("type", new FrameBuilder("Numeric").toFrame()).toFrame());
		headers.add(new FrameBuilder("attribute").add("name", "timetag").add("type", new FrameBuilder("Date").add("format", "yyyyMM").toFrame()).toFrame());
		for (ColumnStream stream : streams)
			headers.add(new FrameBuilder("attribute").add("name", stream).add("type", columnType(stream)).toFrame());
		return headers.toArray(new Frame[0]);
	}

	private Frame columnType(ColumnStream stream) {
		Type type = stream.type();
		if (type.equals(Type.Integer) || type.equals(Type.Double) || type.equals(Type.Long)) return new FrameBuilder("Numeric").toFrame();
		if (type.equals(Type.Datetime) || type.equals(Type.Instant))
			return new FrameBuilder("Date").add("format", "yyyy-MM-dd'T'HH:mm:ss").toFrame();
		if (type.equals(Type.Nominal)) return new FrameBuilder("Nominal").add("value", stream.mode().features).toFrame();
		return new FrameBuilder("String").toFrame();
	}


	private interface Formatter {
		String pattern();

		String format(Object value);
	}

}
