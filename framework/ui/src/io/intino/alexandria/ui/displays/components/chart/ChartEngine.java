package io.intino.alexandria.ui.displays.components.chart;

import io.intino.alexandria.Base64;
import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.r.Driver;
import io.intino.alexandria.drivers.r.Result;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.net.URL;

import static io.intino.alexandria.ui.utils.IOUtils.toByteArray;

public class ChartEngine {

	public String execute(URL serverUrl, Dataframe input, String query, Output mode) {
		Result result = null;

		if (query == null || query.isEmpty()) return null;

		try {
			StringBuilder lines = new StringBuilder();
			lines.append("library(ggplot2);\n");
			lines.append(query);
			if (!query.endsWith(";")) lines.append(";\n");

			String output = null;
			if (mode == Output.Image) {
				lines.append("ggsave('data.png', output);\n");
				result = run(serverUrl, lines.toString());
				output = Base64.encode(toByteArray(result.get("data.png")));
			}
			else if (mode == Output.Html) {
				lines.append("library(plotly);\n");
				lines.append("output <- ggplotly(output);\n");
				lines.append("output <- plotly_json(output, FALSE);\n");
				lines.append("write(output, 'data.json');\n");
				result = run(serverUrl, lines.toString());
				output = new String(toByteArray(result.get("data.json")));
			}

			return output;

		} catch (IOException e) {
			Logger.error(e);
			throw new RuntimeException(e.getMessage());
		}
		finally {
			if (result != null) result.close();
		}
	}

	private Result run(URL serverUrl, String script) {
		Driver driver = new Driver(serverUrl.toString());
		Program program = new Program().add("script", script);
		return driver.run(program);
	}

}
