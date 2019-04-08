package io.intino.alexandria.ui.displays.components.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Dataframe {
	private List<DataframeColumn> columns = new ArrayList<>();
	private Map<DataframeColumn, List<Object>> values = new HashMap<>();

	public Dataframe add(DataframeColumn column) {
		columns.add(column);
		return this;
	}

	public Dataframe add(String column) {
		add(new DataframeColumn().name(column));
		return this;
	}

	public Dataframe set(String columnName, List<Object> values) {
		DataframeColumn column = column(columnName);
		this.values.put(column, values.stream().map(v -> parse(column, v)).collect(toList()));
		return this;
	}

	public Dataframe add(int columnPos, Object value) {
		return add(column(columnPos), value);
	}

	public Dataframe add(String columnName, Object value) {
		return add(column(columnName), value);
	}

	public Dataframe add(DataframeColumn column, Object value) {
		if (!this.values.containsKey(column)) this.values.put(column, new ArrayList<>());
		values.get(column).add(parse(column, value));
		return this;
	}

	public DataframeColumn column(String name) {
		return columns.stream().filter(c -> c.name.equals(name)).findFirst().orElse(null);
	}

	public DataframeColumn column(int pos) {
		return columns.get(pos);
	}

	private Object parse(DataframeColumn column, Object value) {
		if (!(value instanceof String)) return value;
		if (column.type == DataframeColumn.Type.Double) return Double.parseDouble((String) value);
		else if (column.type == DataframeColumn.Type.Integer) return Integer.parseInt((String) value);
		return value;
	}

}
