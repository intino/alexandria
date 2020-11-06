package io.intino.alexandria.ui.displays.components.collection.builders;

import io.intino.alexandria.schemas.DynamicTableCell;
import io.intino.alexandria.schemas.DynamicTableColumn;
import io.intino.alexandria.schemas.DynamicTableRow;
import io.intino.alexandria.schemas.DynamicTableSection;
import io.intino.alexandria.ui.model.dynamictable.Column;
import io.intino.alexandria.ui.model.dynamictable.Row;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

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
		result.backgroundColor(section.backgroundColor());
		result.fontSize(section.fontSize());
		result.columns(buildColumnList(section));
		result.rows(buildRowList(section.rows(), calculateTotal(section.rows()), language));
		result.sections(buildList(section.sections(), language));
		return result;
	}

	private static Map<String, Double> calculateTotal(List<Row> rows) {
		Map<String, Double> result = new HashMap<>();
		rows.forEach(r -> r.columns().forEach(c -> {
			if (!result.containsKey(c.name())) result.put(c.name(), 0.0);
			result.put(c.name(), result.get(c.name()) + c.value());
		}));
		return result;
	}

	private static List<DynamicTableRow> buildRowList(List<Row> rows, Map<String, Double> totalValues, String language) {
		List<DynamicTableRow> result = rows.stream().map(r -> build(r, totalValues, language)).collect(toList());
		if (result.size() <= 0) return result;
		result.add(totalRow(rows, totalValues, language));
		return result;
	}

	private static DynamicTableRow totalRow(List<Row> rows, Map<String, Double> totalValues, String language) {
		DynamicTableRow row = new DynamicTableRow();
		row.label("Total");
		row.cells(rows.get(0).columns().stream().map(c -> buildCell(c.name(), totalValues.get(c.name()), 100.0, language).highlighted(true)).collect(toList()));
		row.isTotalRow(true);
		return row;
	}

	private static DynamicTableRow build(Row row, Map<String, Double> totalValues, String language) {
		DynamicTableRow result = new DynamicTableRow();
		result.label(row.label());
		result.cells(buildCellList(row.columns(), totalValues, language));
		return result;
	}

	private static List<DynamicTableColumn> buildColumnList(Section section) {
		List<Row> rows = section.rows();
		if (rows.size() <= 0) return Collections.emptyList();
		return rows.get(0).columns().stream().map(DynamicTableBuilder::build).collect(toList());
	}

	private static DynamicTableColumn build(Column column) {
		return new DynamicTableColumn().label(column.name());
	}

	private static List<DynamicTableCell> buildCellList(List<Column> columns, Map<String, Double> totalValues, String language) {
		return columns.stream().map(c -> buildCell(c, totalValues.getOrDefault(c.name(), 0.0), language)).collect(toList());
	}

	private static DynamicTableCell buildCell(Column column, Double total, String language) {
		return buildCell(column.name(), column.value(), total != 0 ? round(column.value() / total * 100.0, 2) : 0.0, language);
	}

	private static DynamicTableCell buildCell(String name, Double absolute, Double relative, String language) {
		DynamicTableCell result = new DynamicTableCell();
		result.label(name);
		result.absolute(formatNumber(absolute, language));
		result.relative(formatNumber(relative, language));
		return result;
	}

	private static String formatNumber(double value, String language) {
		return NumberFormat.getNumberInstance(Locale.forLanguageTag(language)).format(round(value, 2));
	}

	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
