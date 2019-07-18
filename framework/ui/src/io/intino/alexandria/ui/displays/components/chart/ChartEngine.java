package io.intino.alexandria.ui.displays.components.chart;

import io.intino.alexandria.Base64;
import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RFileInputStream;
import org.rosuda.REngine.Rserve.RFileOutputStream;

import java.io.*;
import java.net.URL;

import static io.intino.alexandria.ui.utils.IOUtils.toByteArray;

public class ChartEngine {

	public static final int DefaultRServePort = 6311;

	public String execute(URL serverUrl, Dataframe input, String query, Output mode) {
		RConnection connection = null;
		String host = serverUrl != null ? serverUrl.getHost() : "";
		int port = serverUrl != null ? serverUrl.getPort() : DefaultRServePort;

		if (query == null || query.isEmpty()) return null;

		try {
			connection = new RConnection(host, port);

			connection.voidEval("library(ggplot2)");
			connection.voidEval(query);

			String output = null;
			if (mode == Output.Image) {
				connection.parseAndEval("ggsave('data.png', output)");
				output = Base64.encode(toByteArray(get(connection, "data.png")));
			}
			else if (mode == Output.Html) {
				connection.voidEval("library(plotly)");
				connection.voidEval("output <- ggplotly(output)");
				connection.voidEval("output <- plotly_json(output, FALSE)");
				connection.parseAndEval("write(output, 'data.json')");
				output = new String(toByteArray(get(connection, "data.json")));
			}

			return output;

		} catch (REngineException | IOException | REXPMismatchException e) {
			Logger.error(e);
			throw new RuntimeException(e.getMessage());
		}
		finally {
			if (connection != null) connection.close();
		}
	}

	private void sendInput(RConnection connection, Dataframe input) {
	}

	public void put(RConnection connection, InputStream content, String name) {
		try {
			RFileOutputStream serverStream = connection.createFile(name);
			copy(content, serverStream);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public ByteArrayInputStream get(RConnection connection, String file) {
		try {
			RFileInputStream serverStream = connection.openFile(file);
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			copy(serverStream, result);
			return new ByteArrayInputStream(result.toByteArray());
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
	}

	private void copy(InputStream clientStream, OutputStream serverStream) throws IOException {
		byte [] buffer = new byte[8192];

		int c = clientStream.read(buffer);
		while(c >= 0) {
			serverStream.write(buffer,0, c);
			c = clientStream.read(buffer);
		}

		serverStream.close();
		clientStream.close();
	}

}
