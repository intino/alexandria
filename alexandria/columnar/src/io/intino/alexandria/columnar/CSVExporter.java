package io.intino.alexandria.columnar;

import com.opencsv.CSVWriter;
import io.intino.alexandria.assa.AssaReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CSVExporter {
	private static final String CSV_NULL_VALUE = "NULL";
	private final List<AssaReader<String>> readers;
	private final List<Columnar.Select.ColumnFilter> filters;

	public CSVExporter(List<AssaReader<String>> readers, List<Columnar.Select.ColumnFilter> filters) {
		this.readers = readers;
		this.filters = filters;
	}

	public void export(File file) throws IOException {
		CSVWriter csvWriter = new CSVWriter(new FileWriter(file), ';');
		csvWriter.writeNext(headers(readers));
		AssaMerger merger = new AssaMerger(readers, filters);
		while (merger.hasNext()) {
			String[] next = merger.next();
			if (next == null) break;
			csvWriter.writeNext(replaceNulls(next, CSV_NULL_VALUE));
		}
		csvWriter.close();
	}


	private String[] replaceNulls(String[] next, @SuppressWarnings("SameParameterValue") String nullValue) {
		for (int i = 0; i < next.length; i++) if (next[i] == null) next[i] = nullValue;
		return next;
	}

	private String[] headers(List<AssaReader<String>> assas) {
		List<String> header = new ArrayList<>();
		header.add("id");
		header.addAll(assas.stream().map(AssaReader::name).collect(Collectors.toList()));
		return header.toArray(new String[0]);
	}
}
