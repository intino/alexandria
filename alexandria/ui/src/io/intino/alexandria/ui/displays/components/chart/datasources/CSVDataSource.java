package io.intino.alexandria.ui.displays.components.chart.datasources;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.components.chart.DataFrameColumn;
import io.intino.alexandria.ui.displays.components.chart.DataFrame;
import io.intino.alexandria.ui.displays.components.chart.DataSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class CSVDataSource implements DataSource {
	private final URL source;

	public CSVDataSource(URL csvFile) {
		this.source = csvFile;
	}

	public DataFrame load() {
		try {
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			CSVReader data = new CSVReaderBuilder(new InputStreamReader(source.openStream(), StandardCharsets.UTF_8)).withCSVParser(parser).build();
			List<String[]> lines = data.readAll();

			if (lines.size() <= 0) return null;

			DataFrame sheet = new DataFrame();
			addColumns(sheet, lines);
			addValues(sheet, lines);

			return sheet;
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private void addColumns(DataFrame sheet, List<String[]> lines) {
		Stream.of(lines.get(0)).forEach(c -> {
			String[] definition = c.split("\\#");
			String name = definition[0];
			DataFrameColumn.Type type = definition.length > 1 ? DataFrameColumn.Type.valueOf(definition[1]) : DataFrameColumn.Type.Double;
			sheet.add(new DataFrameColumn().name(name).type(type));
		});
	}

	private void addValues(DataFrame dataFrame, List<String[]> lines) {
		if (lines.size() <= 1) return;
		for (int i=1; i<lines.size(); i++) {
			String[] line = lines.get(i);
			for (int j=0; j<line.length; j++)
				dataFrame.add(j, line[j]);
		}
	}

}
