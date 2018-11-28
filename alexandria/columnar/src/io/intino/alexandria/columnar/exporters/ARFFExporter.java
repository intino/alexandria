package io.intino.alexandria.columnar.exporters;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.columnar.ColumnTypes;
import io.intino.alexandria.columnar.ColumnTypes.ColumnType;
import io.intino.alexandria.columnar.Columnar;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ARFFExporter {
	private static final String NULL_VALUE = "?";
	private final Map<Timetag, List<AssaReader<String>>> readers;
	private final List<Columnar.Select.ColumnFilter> filters;
	private final ColumnType[] columnTypes;

	public ARFFExporter(Map<Timetag, List<AssaReader<String>>> readers, List<Columnar.Select.ColumnFilter> filters, ColumnTypes columnTypes) {
		this.readers = readers;
		this.filters = filters;
		this.columnTypes = extractColumnTypes(columnTypes, readers.values().iterator().next());
	}

	public void export(File file) throws IOException {
		StringBuilder builder = new StringBuilder(ArffTemplate.create().format(new Frame("arff").addSlot("attribute", attributes(readers.values().iterator().next()))));
		for (Timetag timetag : readers.keySet()) {
			ColumnJoiner merger = new ColumnJoiner(timetag, readers.get(timetag), filters);
			while (merger.hasNext()) {
				String[] next = merger.next();
				if (next == null) break;
				String[] values = format(next);
				builder.append(String.join(",", values)).append("\n");
			}
		}
		Files.write(file.toPath(), builder.toString().getBytes());
	}

	private String[] format(String[] next) {
		next[1] = next[1] = "\"" + next[1] + "\"";
		for (int i = 2; i < next.length; i++)
			if (next[i] == null) next[i] = NULL_VALUE;
			else {
				if (columnTypes[i - 1] instanceof ColumnType.String) next[i] = "'" + next[i] + "'";
				else if (columnTypes[i - 1] instanceof ColumnType.Date) next[i] = "\"" + next[i] + "\"";
			}
		return next;
	}

	private Frame[] attributes(List<AssaReader<String>> readers) {
		List<Frame> headers = new ArrayList<>();
		headers.add(new Frame("attribute").addSlot("name", "id").addSlot("type", new Frame("Numeric")));
		headers.add(new Frame("attribute").addSlot("name", "timetag").addSlot("type", new Frame("Date").addSlot("format", "yyyyMMddhhmmss")));
		List<String> columns = readers.stream().map(AssaReader::name).collect(Collectors.toList());
		for (int i = 0; i < columns.size(); i++)
			headers.add(new Frame("attribute").addSlot("name", columns.get(i)).addSlot("type", columnType(i)));
		return headers.toArray(new Frame[0]);
	}

	private ColumnType[] extractColumnTypes(ColumnTypes columnTypes, List<AssaReader<String>> readers) {
		ColumnType[] types = new ColumnType[readers.size()];
		for (int i = 0; i < readers.size(); i++) types[i] = columnTypes.getOrDefault(readers.get(i).name(), new ColumnType.String());
		return types;
	}

	private Frame columnType(int i) {
		ColumnType type = columnTypes[i];
		if (type instanceof ColumnType.Numeric) return new Frame("Numeric");
		if (type instanceof ColumnType.Date) return new Frame("Date").addSlot("format", ((ColumnType.Date) type).format());
		if (type instanceof ColumnType.Nominal) return new Frame("Nominal").addSlot("value", ((ColumnType.Nominal) type).values());
		return new Frame("String");
	}
}
