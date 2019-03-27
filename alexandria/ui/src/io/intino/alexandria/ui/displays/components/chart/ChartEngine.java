package io.intino.alexandria.ui.displays.components.chart;

import io.intino.alexandria.Base64;
import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RFileInputStream;
import org.rosuda.REngine.Rserve.RFileOutputStream;

import java.io.*;

import static io.intino.alexandria.ui.utils.IOUtils.toByteArray;

public class ChartEngine {
//	private final ScriptEngine engine;

//	public ChartEngine() {
//		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
//		engine = factory.getScriptEngine();
//	}

//	public URL execute(ChartSheet input, String code) {
//		RCaller caller = new RCaller();
//		caller.setRscriptExecutable("/Library/Frameworks/R.framework/Versions/3.5/Resources/Rscript");
//		RCode rCode = new RCode();
//		rCode.addRCode("library(plotly)");
//		rCode.addRCode("library(ggplot2)");
//		rCode.addRCode("set.seed(100)");
//		rCode.addRCode("d <- diamonds[sample(nrow(diamonds), 1000), ]");
//		rCode.addRCode("p <- ggplot(data = d, aes(x = carat, y = price)) + geom_point(aes(text = paste(\"Clarity:\", clarity)), size = 4) + geom_smooth(aes(colour = cut, fill = cut)) + facet_wrap(~ cut)");
//		rCode.addRCode("p <- ggplotly(p)");
//		rCode.addRCode("htmlwidgets::saveWidget(p, \"result.html\")");
//		caller.setRCode(rCode);
//		caller.runAndReturnResult("p");
//		String[] ps = caller.getParser().getAsStringArray("p");
//		Stream.of(ps).forEach(System.out::println);
//		return null;
//	}


//	public URL execute(ChartSheet input, String code) {
//		try {
//			StringWriter writer = new StringWriter();
//			engine.getContext().setWriter(writer);
//
//			engine.eval("library(plotly)");
//			engine.eval("set.seed(100)");
//			engine.eval("d <- diamonds[sample(nrow(diamonds), 1000), ]");
//			engine.eval("p <- ggplot(data = d, aes(x = carat, y = price)) + geom_point(aes(text = paste(\"Clarity:\", clarity)), size = 4) + geom_smooth(aes(colour = cut, fill = cut)) + facet_wrap(~ cut)");
//			engine.eval("p <- ggplotly(p)");
//
//			Object p = engine.eval("p");
//			System.out.println(p);
//
//			engine.getContext().setWriter(new PrintWriter(System.out));
//		} catch (ScriptException e) {
//			Logger.error("Could not execute R code %s for Chart");
//		}
//		return null;
//	}


	public String execute(ChartSheet input, String code, ChartMode mode) {
		RConnection connection = null;

		if (code == null || code.isEmpty()) return null;

		try {
			connection = new RConnection("");

//			connection.eval("library(ggplot2)");
//			connection.eval("set.seed(100)");
//			connection.eval("d <- diamonds[sample(nrow(diamonds), 1000), ]");
//			connection.eval("result = ggplot(data = d, aes(x = carat, y = price)) + geom_point(aes(text = paste(\"Clarity:\", clarity)), size = 4) + geom_smooth(aes(colour = cut, fill = cut)) + facet_wrap(~ cut)");

//			connection.eval("library(plotly)");
//			connection.eval("ggiris <- qplot(Petal.Width, Sepal.Length, data = iris, color = Species)");
//			connection.eval("result <- ggplotly(ggiris)");
//			connection.eval("result <- plotly_json(result, FALSE)");

			connection.eval(code);
			String result = null;

			if (mode == ChartMode.Image) {
				connection.parseAndEval("ggsave('data.png', result)");
				result = Base64.encode(toByteArray(get(connection, "data.png")));
			}
			else if (mode == ChartMode.Html) {
				connection.eval("library(plotly)");
				connection.eval("result <- ggplotly(result)");
				connection.eval("result <- plotly_json(result, FALSE)");
				connection.parseAndEval("write(result, 'data.json')");
				result = new String(toByteArray(get(connection, "data.json")));
			}

			return result;

		} catch (REngineException | IOException | REXPMismatchException e) {
			throw new RuntimeException(e.getMessage());
		}
		finally {
			if (connection != null) connection.close();
		}
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
