package io.intino.alexandria.ui.displays.components.collection.builders;

import io.intino.alexandria.schemas.DynamicTableCell;
import io.intino.alexandria.schemas.DynamicTableColumn;
import io.intino.alexandria.schemas.DynamicTableRow;
import io.intino.alexandria.schemas.DynamicTableSection;
import io.intino.alexandria.ui.model.dynamictable.Cell;
import io.intino.alexandria.ui.model.dynamictable.Column;
import io.intino.alexandria.ui.model.dynamictable.Row;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

public class DynamicTableBuilder {

	private static DecimalFormat df = new DecimalFormat("0.00");

	public static List<DynamicTableSection> buildList(List<Section> sections, String language) {
		return sections.stream().map(s -> build(s, language)).collect(toList());
	}

	public static DynamicTableSection build(Section section, String language) {
		DynamicTableSection result = new DynamicTableSection();
		result.label(section.label());
		result.color(section.color());
		result.isOrdinal(section.isOrdinal());
		result.backgroundColor(section.backgroundColor());
		result.fontSize(section.fontSize());
		result.columns(buildColumnList(section));
		result.rows(buildRowList(section.rows(), section.showTotals() ? calculateTotal(section, section.rows()) : emptyMap(), language));
		result.sections(buildList(section.sections(), language));
		return result;
	}

	private static Map<String, Double> calculateTotal(Section section, List<Row> rows) {
		Map<String, Double> result = new HashMap<>();
		rows.forEach(r -> r.cells().forEach(cell -> {
			if (!result.containsKey(cell.name())) result.put(cell.name(), 0.0);
			result.put(cell.name(), result.get(cell.name()) + cell.value());
		}));
		result.forEach((key, value) -> result.put(key, calculateTotal(section.column(key), value, rows.size())));
		return result;
	}

	private static Double calculateTotal(Column column, Double value, int rowsCount) {
		if (column.totalCalculator() != null) return column.totalCalculator().apply(value, rowsCount);
		if (column.operator() == Column.Operator.Average) return rowsCount > 0 ? value / rowsCount : 0;
		return value;
	}

	private static List<DynamicTableRow> buildRowList(List<Row> rows, Map<String, Double> totalValues, String language) {
		List<DynamicTableRow> result = rows.stream().map(r -> build(r, totalValues, language)).collect(toList());
		if (result.size() <= 0) return result;
		if (!totalValues.isEmpty()) result.add(totalRow(rows, totalValues, language));
		return result;
	}

	private static DynamicTableRow totalRow(List<Row> rows, Map<String, Double> totalValues, String language) {
		DynamicTableRow row = new DynamicTableRow();
		row.label("Total");
		row.cells(rows.get(0).cells().stream().map(c -> buildCell(c.name(), totalValues.get(c.name()), 100.0, language).highlighted(true)).collect(toList()));
		row.isTotalRow(true);
		return row;
	}

	private static DynamicTableRow build(Row row, Map<String, Double> totalValues, String language) {
		DynamicTableRow result = new DynamicTableRow();
		result.label(row.label());
		result.description(row.description());
		result.cells(buildCellList(row.cells(), totalValues, language));
		return result;
	}

	private static List<DynamicTableColumn> buildColumnList(Section section) {
		List<Row> rows = section.rows();
		if (rows.size() <= 0) return emptyList();
		return rows.get(0).cells().stream().map(c -> build(c, section.column(c.name()))).collect(toList());
	}

	private static DynamicTableColumn build(Cell cell, Column column) {
		DynamicTableColumn result = new DynamicTableColumn();
		result.label(column.label());
		result.color(column.color());
		result.operator(operatorOf(column.operator()));
		result.metric(column.metric());
		result.countDecimals(column.countDecimals());
		return result;
	}

	private static DynamicTableColumn.Operator operatorOf(Column.Operator operator) {
		if (operator == Column.Operator.Average) return DynamicTableColumn.Operator.Average;
		return DynamicTableColumn.Operator.Sum;
	}

	private static List<DynamicTableCell> buildCellList(List<Cell> columns, Map<String, Double> totalValues, String language) {
		return columns.stream().map(c -> buildCell(c, totalValues.getOrDefault(c.name(), 0.0), language)).collect(toList());
	}

	private static DynamicTableCell buildCell(Cell column, Double total, String language) {
		return buildCell(column.name(), column.value(), total != 0 ? round(column.value() / total * 100.0, 2) : 0.0, language);
	}

	private static DynamicTableCell buildCell(String name, Double absolute, Double relative, String language) {
		DynamicTableCell result = new DynamicTableCell();
		result.label(name);
		result.absolute(absolute);
		result.relative(relative);
		return result;
	}

	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
