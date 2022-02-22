package io.intino.alexandria.office.builders;

import io.intino.alexandria.office.model.CellValue;
import io.intino.alexandria.office.model.Column;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HtmlReportGenerator extends ReportGenerator {
	protected final Template template;

	public HtmlReportGenerator() {
		this(null);
	}

	public HtmlReportGenerator(Template template) {
		this.template = template != null ? template : new HtmlReportTemplate();
	}

	@Override
	InputStream generate(String title) {
		return new ByteArrayInputStream(template.render(buildFrame(title)).getBytes(StandardCharsets.UTF_8));
	}

	private Frame buildFrame(String title) {
		FrameBuilder result = new FrameBuilder("html");
		Map<String, List<String>> groupedLines = linesGrouped(dimensions().size() > 0 ? firstDimensionIndex() : -1);
		addDimensions(result, groupedLines);
		return result.toFrame();
	}

	private void addDimensions(FrameBuilder result, Map<String, List<String>> groupedLines) {
		List<String> dimensionOptions = new ArrayList<>(groupedLines.keySet());
		result.add("dimensionSelectorHidden", groupedLines.size() > 1 ? "" : "hidden");
		result.add("dimensionSelectorAlone", optionalColumns().size() > 0 ? "" : "alone");
		for (int i = 0; i < dimensionOptions.size(); i++)
			result.add("dimension", dimensionFrame(dimensionOptions.get(i), groupedLines.get(dimensionOptions.get(i)), i));
	}

	private FrameBuilder dimensionFrame(String key, List<String> values, int index) {
		FrameBuilder result = new FrameBuilder("dimension");
		result.add("name", key);
		result.add("style", new FrameBuilder("style", index == 0 ? "first" : ""));
		result.add("toolbarHidden", optionalColumns().size() > 0 ? "" : "hidden");
		result.add("contentFull", optionalColumns().size() > 0 ? "" : "full");
		result.add("display", new FrameBuilder("display", index == 0 ? "first" : ""));
		addColumns(key, result);
		addRows(key, values, result);
		return result;
	}

	private void addColumns(String dimension, FrameBuilder result) {
		List<Column> columns = skipColumnDimension(this.columns);
		for (int i = 0; i < columns.size(); i++) result.add("column", columnFrame(dimension, columns.get(i), i));
	}

	private FrameBuilder columnFrame(String dimension, Column column, int index) {
		FrameBuilder result = new FrameBuilder("column");
		result.add("name", clean(column.label()));
		result.add("label", column.label());
		result.add("checked", column.optional() ? "" : "checked");
		result.add("hidden", column.optional() ? "hidden" : "");
		result.add("dimension", clean(dimension));
		result.add("alignment", column.alignment().name());
		if (column.color() != null && !column.color().equals(Column.DefaultColor)) result.add("borderColor", column.color());
		return result;
	}

	private void addRows(String dimension, List<String> values, FrameBuilder result) {
		for (int i = 0; i < values.size(); i++)
			addRow(dimension, skipRowDimension(cellsValuesOf(values.get(i))), i, result);
	}

	private void addRow(String dimension, List<CellValue> values, int index, FrameBuilder result) {
		result.add("row", rowFrame(dimension, values, index));
	}

	private FrameBuilder rowFrame(String dimension, List<CellValue> cells, int index) {
		List<Column> columns = skipColumnDimension(this.columns);
		FrameBuilder row = new FrameBuilder("row");
		for (int i=0; i<cells.size(); i++)
			addCell(dimension, columns.get(i), cells.get(i), row);
		return row;
	}

	private void addCell(String dimension, Column column, CellValue cellValue, FrameBuilder row) {
		FrameBuilder cell = new FrameBuilder("cell");
		cell.add("name", clean(column.label()));
		cell.add("dimension", clean(dimension));
		cell.add("value", column.valueWithUnitOf(cellValue.data()));
		cell.add("hidden", column.optional() ? "hidden" : "");
		cell.add("alignment", column.alignment().name());
		if (cellValue.color() != null) cell.add("color", cellValue.color());
		if (cellValue.style() == CellValue.Style.Bold) cell.add("bold", "bold");
		row.add("cell", cell);
	}

	private String clean(String label) {
		return label.replace(" ", "").replace("$", "");
	}

}
