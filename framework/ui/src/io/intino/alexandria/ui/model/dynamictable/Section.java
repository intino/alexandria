package io.intino.alexandria.ui.model.dynamictable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Section {
	private String label;
	private String color;
	private List<Row> rowList = new ArrayList<>();
	private List<Section> sectionList = new ArrayList<>();
	private List<String> columnNames = new ArrayList<>();

	public Section(String label) {
		this(label, "#efefef");
	}

	public Section(String label, String color) {
		this.label = label;
		this.color = color;
	}

	public String label() {
		return label;
	}

	public Section label(String label) {
		this.label = label;
		return this;
	}

	public String color() {
		return color;
	}

	public Section color(String color) {
		this.color = color;
		return this;
	}

	public List<Row> rows() {
		return rowList;
	}

	public List<String> columns() {
		return columnNames;
	}

	public Section columns(String... columnNames) {
		columns(Arrays.asList(columnNames));
		return this;
	}

	public Section columns(List<String> columnNameList) {
		this.columnNames = columnNameList;
		return this;
	}

	public Section add(String rowLabel, List<Double> values) {
		return add(rowLabel, values.stream().mapToDouble(v -> v).toArray());
	}

	public Section add(String rowLabel, double... values) {
		Row row = new Row(rowLabel);
		if (columnNames.size() != values.length) throw new NumberFormatException("Columns and value size must be equal");
		for (int i = 0; i < values.length; i++) row.add(new Column(columnNames.get(i), values[i]));
		rowList.add(row);
		return this;
	}

	public Section add(Row row) {
		rowList.add(row);
		return this;
	}

	public List<Section> sections() {
		return sectionList;
	}

	public Section add(String label, String color) {
		return add(new Section(label, color));
	}

	public Section add(Section section) {
		sectionList.add(section);
		return section;
	}
}