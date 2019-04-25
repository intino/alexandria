package io.intino.alexandria.tabb.exporters;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.Exporter;
import org.siani.itrules.model.Frame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class ArffExporter implements Exporter {
	private static final String NULL_VALUE = "?";

	private final List<ColumnStream> streams;
	private BufferedWriter writer;

	public ArffExporter(List<ColumnStream> streams) {
		this.streams = streams;
	}

	@Override
	public Exporter destination(File directory, String name) {
		try {
			writer = new BufferedWriter(new FileWriter(new File(directory, name + ".arff")));
			writer.write(ArffTemplate.create().format(new Frame("arff").addSlot("attribute", attributes())));
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
				.map(s -> s.key().equals(key) ? s.type().toString(s.value()) : NULL_VALUE)
				.collect(joining(","));
		try {
			writer.write(line + "\n");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private Frame[] attributes() {
		List<Frame> headers = new ArrayList<>();
		headers.add(new Frame("attribute").addSlot("name", "id").addSlot("type", new Frame("Numeric")));
		headers.add(new Frame("attribute").addSlot("name", "timetag").addSlot("type", new Frame("Date").addSlot("format", "yyyyMM")));
		for (ColumnStream stream : streams)
			headers.add(new Frame("attribute").addSlot("name", stream).addSlot("type", columnType(stream)));
		return headers.toArray(new Frame[0]);
	}

	private Frame columnType(ColumnStream stream) {
		Type type = stream.type();
		if (type.equals(Type.Integer) || type.equals(Type.Double) || type.equals(Type.Long)) return new Frame("Numeric");
		if (type.equals(Type.Datetime) || type.equals(Type.Instant)) return new Frame("Date").addSlot("format", "yyyy-MM-dd'T'HH:mm:ss");
		if (type.equals(Type.Nominal)) return new Frame("Nominal").addSlot("value", stream.mode().features);
		return new Frame("String");
	}
}
