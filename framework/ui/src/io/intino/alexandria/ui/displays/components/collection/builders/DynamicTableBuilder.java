package io.intino.alexandria.ui.displays.components.collection.builders;

import io.intino.alexandria.schemas.DynamicTableCell;
import io.intino.alexandria.schemas.DynamicTableColumn;
import io.intino.alexandria.schemas.DynamicTableRow;
import io.intino.alexandria.schemas.DynamicTableSection;
import io.intino.alexandria.ui.model.dynamictable.Column;
import io.intino.alexandria.ui.model.dynamictable.Row;
import io.intino.alexandria.ui.model.dynamictable.Section;
import org.eclipse.jetty.util.component.StopLifeCycle;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class DynamicTableBuilder {

	public static List<DynamicTableSection> buildList(List<Section> sections, URL baseAssetUrl) {
		return sections.stream().map(s -> build(s, baseAssetUrl)).collect(toList());
	}

	public static DynamicTableSection build(Section section, URL baseAssetUrl) {
		DynamicTableSection result = new DynamicTableSection();
		result.label(section.label());
		result.color(section.color());
		result.columns(buildColumnList(section, baseAssetUrl));
		result.rows(buildRowList(section.rows(), baseAssetUrl));
		result.sections(buildList(section.sections(), baseAssetUrl));
		return result;
	}

	private static List<DynamicTableRow> buildRowList(List<Row> rows, URL baseAssetUrl) {
		return rows.stream().map(r -> build(r, baseAssetUrl)).collect(toList());
	}

	private static DynamicTableRow build(Row row, URL baseAssetUrl) {
		DynamicTableRow result = new DynamicTableRow();
		result.label(row.label());
		result.cells(buildCellList(row.columns(), baseAssetUrl));
		return result;
	}

	private static List<DynamicTableColumn> buildColumnList(Section section, URL baseAssetUrl) {
		List<Row> rows = section.rows();
		if (rows.size() <= 0) return Collections.emptyList();
		return rows.get(0).columns().stream().map(c -> build(c, baseAssetUrl)).collect(toList());
	}

	private static DynamicTableColumn build(Column column, URL baseAssetUrl) {
		return new DynamicTableColumn().label(column.name());
	}

	private static List<DynamicTableCell> buildCellList(List<Column> columns, URL baseAssetUrl) {
		return columns.stream().map(c -> buildCell(c, baseAssetUrl)).collect(toList());
	}

	private static DynamicTableCell buildCell(Column column, URL baseAssetUrl) {
		DynamicTableCell result = new DynamicTableCell();
		result.label(column.name());
		result.value(column.value());
		return result;
	}
}
