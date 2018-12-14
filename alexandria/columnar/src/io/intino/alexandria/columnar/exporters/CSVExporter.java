package io.intino.alexandria.columnar.exporters;

import com.opencsv.CSVWriter;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.columnar.ColumnTypes;
import io.intino.alexandria.columnar.Columnar;
import io.intino.alexandria.columnar.Exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVExporter implements Exporter {
	private static final String NULL_VALUE = "";
	private final Map<Timetag, List<AssaReader>> readers;
	private final List<Columnar.Select.ColumnFilter> filters;
	private final ColumnTypes columnTypes;

	public CSVExporter(Map<Timetag, List<AssaReader>> readers, List<Columnar.Select.ColumnFilter> filters, ColumnTypes columnTypes) {
		this.readers = readers;
		this.filters = filters;
		this.columnTypes = columnTypes;
	}

	public void export(File file) throws IOException {
		CSVWriter csvWriter = new CSVWriter(new FileWriter(file), ';');
		csvWriter.writeNext(headers(readers.values().iterator().next()));
		for (Timetag timetag : readers.keySet()) {
			ColumnJoiner merger = new ColumnJoiner(timetag, readers.get(timetag), filters);
			while (merger.hasNext()) {
				String[] next = merger.next();
				if (next == null) break;
				csvWriter.writeNext(replaceNulls(next));
			}
		}
		csvWriter.close();
	}

	private String[] replaceNulls(String[] next) {
		for (int i = 0; i < next.length; i++) if (next[i] == null) next[i] = NULL_VALUE;
		return next;
	}

	private String[] headers(List<AssaReader> assas) {
		List<String> header = new ArrayList<>();
		header.add("id");
		header.add("timetag");
		header.addAll(assas.stream().map(AssaReader::name).collect(Collectors.toList()));
		return header.toArray(new String[0]);
	}
}
