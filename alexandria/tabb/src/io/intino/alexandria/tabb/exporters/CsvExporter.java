package io.intino.alexandria.tabb.exporters;

import com.opencsv.CSVWriter;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.NotAvailable;
import io.intino.alexandria.tabb.Exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.opencsv.CSVWriter.NO_QUOTE_CHARACTER;

public class CsvExporter implements Exporter {
	private static final char SEPARATOR = ';';
	private final List<ColumnStream> streams;
	private CSVWriter csvWriter;

	public CsvExporter(List<ColumnStream> streams) {
		this.streams = streams;
	}

	@Override
	public Exporter destination(File directory, String name) {
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
				.map(s -> s.type().toString(s.key().equals(key) ? s.value() : NotAvailable.bytesOf(s.type()))).toArray(String[]::new));
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
}
