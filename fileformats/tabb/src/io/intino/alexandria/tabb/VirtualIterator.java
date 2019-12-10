package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static io.intino.alexandria.tabb.TabbManifest.ColumnInfo;
import static java.util.stream.Collectors.joining;

class VirtualIterator implements Iterator<Row>, AutoCloseable {
	private final List<ColumnInfo> columns;
	private final List<Integer> indexColumns;
	private final List<String[]> appends;
	private final List<Map<String, Object>> updatesByColumn;
	private final List<String> removes;
	private final TabbReader reader;

	VirtualIterator(File file, List<ColumnInfo> columns, List<Integer> indexColumns, List<String[]> appends, List<Map<String, Object>> updatesByColumn, List<String> removes) {
		this.reader = newReader(file);
		this.columns = columns;
		this.indexColumns = indexColumns;
		this.appends = new ArrayList<>(appends);
		this.updatesByColumn = updatesByColumn;
		this.removes = removes;
	}

	@Override
	public boolean hasNext() {
		boolean hasNext = reader.hasNext() || !appends.isEmpty();
		if (!hasNext) reader.close();
		return hasNext;
	}

	@Override
	public Row next() {
		if (!reader.hasNext()) return nextAppend(appends);
		Row next = null;
		while (next == null) next = makeUp(reader.next());
		return next;
	}

	public void close() {
		reader.close();
	}

	private Row nextAppend(List<String[]> appends) {
		String[] values = appends.get(0);
		Row row = IntStream.range(0, values.length).mapToObj(i -> valueFrom(columns.get(i), values[i])).collect(Collectors.toCollection(Row::new));
		appends.remove(0);
		return row;
	}

	private Row makeUp(Row row) {
		String id = idOf(row);
		if (isRemoved(id)) return null;
		if (isUpdated(id)) return updateOf(id, row);
		return row;
	}

	private Row updateOf(String id, Row row) {
		for (int i = 0; i < updatesByColumn.size(); i++)
			if (updatesByColumn.get(i).containsKey(id)) {
				Object object = updatesByColumn.get(i).get(id);
				Value value = value(columns.get(i), object);
				row.remove(i);
				row.add(i, value);
			}
		return row;
	}

	private boolean isUpdated(String id) {
		return updatesByColumn.stream().anyMatch(u -> u.containsKey(id));
	}

	private boolean isRemoved(String id) {
		return removes.contains(id);
	}

	private Value valueFrom(ColumnInfo info, String value) {
		Object object = value == null || value.equals("null") ? null : info.type.parse(value);
		if (info.type == Nominal && object != null) info.features.add(value);
		return value(info, object);
	}

	private Value value(ColumnInfo column, Object object) {
		return new Value(column.type, new Mode(column.features.toArray(new String[0])), column.type.toByteArray(column.type == Nominal && object != null ? column.features.indexOf(object) : object));
	}

	private String idOf(Row row) {
		return indexColumns.stream().map(i -> row.get(i).asObject().toString()).collect(joining());
	}

	private TabbReader newReader(File file) {
		try {
			return new TabbReader(file);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}
}