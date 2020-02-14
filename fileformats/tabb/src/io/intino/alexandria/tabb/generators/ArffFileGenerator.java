package io.intino.alexandria.tabb.generators;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.ExplicitColumnStream;
import io.intino.alexandria.tabb.FileGenerator;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.joining;

public class ArffFileGenerator implements FileGenerator {
	private static final String NULL_VALUE = "?";

	private final List<ColumnStream> streams;
	private final Map<ColumnStream, Set<String>> modes = new HashMap<>();
	private BufferedWriter writer;
	private File file;
	private File temp;

	public ArffFileGenerator(List<ColumnStream> streams) {
		this.streams = streams;
		streams.stream().filter(f -> f instanceof ExplicitColumnStream).forEach(s -> modes.put(s, new HashSet<>(((ExplicitColumnStream) s).modes())));
	}

	public FileGenerator destination(File directory, String name) {
		try {
			file = new File(directory, name + ".arff");
			temp = new File(directory, name + ".temp");
			writer = new BufferedWriter(new FileWriter(temp));
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			writer.close();
			append(new ArffTemplate().render(new FrameBuilder("arff").add("attribute", attributes())) + "\n");
			temp.delete();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void append(String preface) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		BufferedReader reader = new BufferedReader(new FileReader(temp));
		writer.write(preface);
		String line;
		while ((line = reader.readLine()) != null) writer.write(line + "\n");
		writer.close();
		reader.close();
	}

	@Override
	public void put(long key) {
		String line = streams.stream()
				.map(s -> s.key().equals(key) ? formatterOf(s).format(s.value()) : NULL_VALUE)
				.collect(joining(","));
		try {
			writer.write(line + "\n");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private Formatter formatterOf(ColumnStream stream) {
		return new Formatter() {
			@Override
			public String pattern() {
				return null;
			}

			@Override
			public String format(Object value) {
				if (value == null) return NULL_VALUE;
				else {
					if (stream.type().equals(Type.Nominal))
						return "\'" + register(stream, value) + "\'";
					if (stream.type().equals(Type.Boolean) || stream.type().equals(Type.String))
						return "\'" + value + "\'";
					return value.toString();
				}
			}

		};
	}

	private String register(ColumnStream stream, Object value) {
		if (!modes.containsKey(stream)) modes.put(stream, new LinkedHashSet<>());
		String nominal = value.toString().replace('\n', '|');
		modes.get(stream).add(nominal);
		return nominal;
	}

	private Frame[] attributes() {
		List<Frame> headers = new ArrayList<>();
		for (ColumnStream stream : streams)
			headers.add(new FrameBuilder("attribute").add("name", stream.name()).add("type", columnType(stream)).toFrame());
		return headers.toArray(new Frame[0]);
	}

	private Frame columnType(ColumnStream stream) {
		Type type = stream.type();
		if (type.equals(Type.Integer) || type.equals(Type.Double) || type.equals(Type.Long))
			return new FrameBuilder("Numeric").toFrame();
		if (type.equals(Type.Datetime) || type.equals(Type.Instant))
			return new FrameBuilder("Date").add("format", "yyyy-MM-dd'T'HH:mm:ss").toFrame();
		if (type.equals(Type.Nominal))
			return new FrameBuilder("Nominal").add("value", modes.get(stream).toArray(new String[0])).toFrame();
		if (type.equals(Type.Boolean)) {
			return new FrameBuilder("Nominal").add("value", new String[]{"true", "false"}).toFrame();
		}
		return new FrameBuilder("String").toFrame();
	}


	private interface Formatter {
		String pattern();

		String format(Object value);
	}

}
