package io.intino.alexandria.ui.model.dynamictable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Section {
	private String label;
	private String color;
	private String backgroundColor;
	private int fontSize;
	private List<Column> columns = new ArrayList<>();
	private List<Row> rowList = new ArrayList<>();
	private List<Section> sectionList = new ArrayList<>();

	public Section(String label) {
		this(label, "black", "#efefef", 12);
	}

	public Section(String label, String color, String backgroundColor, int fontSizeInPt) {
		this.label = label;
		this.color = color;
		this.backgroundColor = backgroundColor;
		this.fontSize = fontSizeInPt;
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

	public String backgroundColor() {
		return backgroundColor;
	}

	public Section backgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public int fontSize() {
		return fontSize;
	}

	public Section fontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public List<Row> rows() {
		return rowList;
	}

	public List<Column> columns() {
		return columns;
	}

	public Section columns(String... columns) {
		columns(Arrays.stream(columns).map(c -> new Column(c, Column.Operator.Sum)).collect(Collectors.toList()));
		return this;
	}

	public Section columns(List<Column> columnList) {
		this.columns = columnList;
		return this;
	}

	public Column column(String label) {
		return columns.stream().filter(c -> c.label().equals(label)).findFirst().orElse(null);
	}

	public Section add(String rowLabel, List<Double> values) {
		return add(rowLabel, null, values);
	}

	public Section add(String rowLabel, double... values) {
		add(rowLabel, null, values);
		return this;
	}

	public Section add(String rowLabel, String rowDescription, List<Double> values) {
		return add(rowLabel, rowDescription, values.stream().mapToDouble(v -> v).toArray());
	}

	public Section add(String rowLabel, String rowDescription, double... values) {
		Row row = new Row(rowLabel, rowDescription);
		if (columns.size() != values.length) throw new NumberFormatException("Columns and value size must be equal");
		for (int i = 0; i < values.length; i++) row.add(new Cell(columns.get(i).label(), values[i]));
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

	public Section add(String label) {
		return add(new Section(label));
	}

	public Section add(String label, String color, String backgroundColor, int fontSize) {
		return add(new Section(label, color, backgroundColor, fontSize));
	}

	public Section add(Section section) {
		sectionList.add(section);
		return section;
	}

}
