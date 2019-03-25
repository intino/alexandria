package io.intino.alexandria.ui.displays.components.chart;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ChartSheet {
	private List<ChartColumn> columns = new ArrayList<>();

	public ChartSheet add(ChartColumn column) {
		columns.add(column);
		return this;
	}

	public ChartSheet add(String column) {
		add(new ChartColumn().name(column));
		return this;
	}

	public ChartSheet add(String column, List<Object> values) {
		this.columns.add(new ChartColumn().name(column).values(values));
		return this;
	}

	public ChartColumn column(int pos) {
		return columns.get(pos);
	}

	public ChartColumn column(String name) {
		return columns.stream().filter(c -> c.name.equals(name)).findFirst().orElse(null);
	}

	public static ChartSheet fromSource(URL source) {
		try {
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			CSVReader data = new CSVReaderBuilder(new InputStreamReader(source.openStream(), StandardCharsets.UTF_8)).withCSVParser(parser).build();
			List<String[]> lines = data.readAll();

			if (lines.size() <= 0) return null;

			ChartSheet sheet = new ChartSheet();
			addColumns(sheet, lines);
			addRows(sheet, lines);

			return sheet;
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private static void addColumns(ChartSheet sheet, List<String[]> lines) {
		Stream.of(lines.get(0)).forEach(c -> {
			String[] definition = c.split("\\#");
			String name = definition[0];
			ChartColumn.Type type = definition.length > 1 ? ChartColumn.Type.valueOf(definition[1]) : ChartColumn.Type.Double;
			sheet.add(new ChartColumn().name(name).type(type));
		});
	}

	private static void addRows(ChartSheet sheet, List<String[]> lines) {
		if (lines.size() <= 1) return;
		for (int i=1; i<lines.size(); i++) {
			String[] line = lines.get(i);
			for (int j=0; j<line.length; j++)
				sheet.column(j).add(line[j]);
		}
	}

}
