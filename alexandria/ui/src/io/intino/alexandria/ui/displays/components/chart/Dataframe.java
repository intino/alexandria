package io.intino.alexandria.ui.displays.components.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Dataframe {
	private List<DataFrameColumn> columns = new ArrayList<>();
	private Map<DataFrameColumn, List<Object>> values = new HashMap<>();

	public DataFrame add(DataFrameColumn column) {
		columns.add(column);
		return this;
	}

	public DataFrame add(String column) {
		add(new DataFrameColumn().name(column));
		return this;
	}

	public DataFrame set(String columnName, List<Object> values) {
		DataFrameColumn column = column(columnName);
		this.values.put(column, values.stream().map(v -> parse(column, v)).collect(toList()));
		return this;
	}

	public DataFrame add(int columnPos, Object value) {
		return add(column(columnPos), value);
	}

	public DataFrame add(String columnName, Object value) {
		return add(column(columnName), value);
	}

	public DataFrame add(DataFrameColumn column, Object value) {
		if (!this.values.containsKey(column)) this.values.put(column, new ArrayList<>());
		values.get(column).add(parse(column, value));
		return this;
	}

	public DataFrameColumn column(String name) {
		return columns.stream().filter(c -> c.name.equals(name)).findFirst().orElse(null);
	}

	public DataFrameColumn column(int pos) {
		return columns.get(pos);
	}

	private Object parse(DataFrameColumn column, Object value) {
		if (!(value instanceof String)) return value;
		if (column.type == DataFrameColumn.Type.Double) return Double.parseDouble((String) value);
		else if (column.type == DataFrameColumn.Type.Integer) return Integer.parseInt((String) value);
		return value;
	}

}
