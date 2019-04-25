package io.intino.alexandria.tabb.generators;

import com.opencsv.CSVWriter;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.FileGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.opencsv.CSVWriter.NO_QUOTE_CHARACTER;

public class CsvFileGenerator implements FileGenerator {
	private static final char SEPARATOR = ';';
	private final List<ColumnStream> streams;
	private CSVWriter csvWriter;

	public CsvFileGenerator(List<ColumnStream> streams) {
		this.streams = streams;
	}

	public FileGenerator destination(File directory, String name) {
		try {
			csvWriter = new CSVWriter(new FileWriter(new File(directory, name + ".csv")), SEPARATOR, NO_QUOTE_CHARACTER);
			csvWriter.writeNext(headers());
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	@Override
	public void put(long key) {
		csvWriter.writeNext(streams.stream()
				.map(s -> value(key, s)).toArray(String[]::new));
	}

	private String value(long key, ColumnStream stream) {
		Object o = stream.key().equals(key) ? stream.value() : stream.type().notAvailable();
		return formatterOf(stream.type()).format(o);
	}

	@Override
	public void close() {
		try {
			csvWriter.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String[] headers() {
		return streams.stream().map(ColumnStream::name).toArray(String[]::new);
	}

	private CsvFileGenerator.Formatter formatterOf(ColumnStream.Type type) {
		return new CsvFileGenerator.Formatter() {
			@Override
			public String pattern() {
				return null;
			}

			@Override
			public String format(Object value) {
				return value.toString();
			}
		};
	}

	private interface Formatter {
		String pattern();

		String format(Object value);
	}
}
