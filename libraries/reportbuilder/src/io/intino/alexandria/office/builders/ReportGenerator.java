package io.intino.alexandria.office.builders;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.office.model.CellValue;
import io.intino.alexandria.office.model.Column;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.groupingBy;

public abstract class ReportGenerator {
	protected List<Column> columns = new ArrayList<>();
	protected List<String> lines = new ArrayList<>();

	public InputStream generate(String title, File tsvFile) {
		if (!tsvFile.exists()) return null;
		process(tsvFile);
		return generate(title);
	}

	private void process(File tsvFile) {
		List<String> lines = linesOf(tsvFile);
		if (lines.size() <= 1) return;
		this.columns = columnsOf(lines.get(0));
		this.lines = lines.subList(1, lines.size());
	}

	abstract InputStream generate(String title);

	File tempFile(String extension) {
		try {
			return File.createTempFile(UUID.randomUUID().toString(), "." + extension);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	List<Column> dimensions() {
		return columns.stream().filter(c -> c.type() == Column.Type.Dimension).collect(Collectors.toList());
	}

	List<Column> optionalColumns() {
		return columns.stream().filter(Column::optional).collect(Collectors.toList());
	}

	List<Column> columnsOf(String line) {
		return Arrays.stream(line.split("\t")).map(Column::from).collect(Collectors.toList());
	}

	List<CellValue> cellsValuesOf(String line) {
		return Arrays.stream(line.split("\t")).map(CellValue::from).collect(Collectors.toList());
	}

	Map<String, List<String>> linesGrouped(int column) {
		if (column == -1) return singletonMap("Hoja 1", lines);
		Map<String, List<String>> result = new LinkedHashMap<>();
		Map<String, List<String>> groupedLines = lines.stream().collect(groupingBy(l -> l.split("\t")[column]));
		Stream<String> entries = groupedLines.keySet().stream().sorted();
		entries.forEach(e -> result.put(e, groupedLines.get(e)));
		return result;
	}

	int firstDimensionIndex() {
		for (int i=0; i<columns.size(); i++) {
			if (columns.get(i).type() == Column.Type.Dimension) return i;
		}
		return 0;
	}

	List<Column> skipColumnDimension(List<Column> columns) {
		if (dimensions().size() <= 0) return columns;
		int index = firstDimensionIndex();
		List<Column> result = new ArrayList<>();
		for (int i=0; i<columns.size(); i++) {
			if (i == index) continue;
			result.add(columns.get(i));
		}
		return result;
	}

	List<CellValue> skipRowDimension(List<CellValue> values) {
		if (dimensions().size() <= 0) return values;
		int index = firstDimensionIndex();
		List<CellValue> result = new ArrayList<>();
		for (int i=0; i<values.size(); i++) {
			if (i == index) continue;
			result.add(values.get(i));
		}
		return result;
	}

	private List<String> linesOf(File tsvFile) {
		try {
			return Files.readAllLines(tsvFile.toPath());
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

}
