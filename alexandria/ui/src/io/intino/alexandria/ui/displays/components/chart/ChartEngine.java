package io.intino.alexandria.ui.displays.components.chart;

import io.intino.alexandria.logger.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import java.net.URL;

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


	public URL execute(ChartSheet input, String code) {
		try {
			RConnection connection = new RConnection();

			connection.eval("library(plotly)");
			connection.eval("library(ggplot2)");
			connection.eval("set.seed(100)");
			connection.eval("d <- diamonds[sample(nrow(diamonds), 1000), ]");
			connection.eval("png(filename='/tmp/example.png',width=400,height=350,res=72)");
			connection.eval("ggplot(data = d, aes(x = carat, y = price)) + geom_point(aes(text = paste(\"Clarity:\", clarity)), size = 4) + geom_smooth(aes(colour = cut, fill = cut)) + facet_wrap(~ cut)");
			connection.eval("dev.off()");

			REXP xp = connection.parseAndEval("r=readBin('/tmp/example.png','raw',1024*1024)");
			connection.parseAndEval("unlink('/tmp/example.png'); r");

			//			connection.eval("YashCustomers <- read.csv('YashCustomer.csv', header=TRUE)");
//			connection.eval("YashAccounts <- read.csv('YashAccount.csv', header=TRUE)");
//			connection.eval("YashCustomersAccounts <- merge(YashCustomers,YashAccounts, by='CUSTOMER_ID')");
//
//			connection.eval("library(ggplot2)");
//			connection.eval("require(ggplot2)");

//			connection.eval("png(file='Yash_GenderVsTotalAccountBalance.png',width=400,height=350,res=72)");
//			connection.parseAndEval("ggplot(data=YashCustomersAccounts, aes(x=GENDER_DESC,y=ACCOUNT_BALANCE)) + geom_bar(stat='identity');dev.off()");
//			System.out.println("5. plotting done ----------------------------------------------------------------------");

//			REXP xp = connection.parseAndEval("r=readBin('Yash_GenderVsTotalAccountBalance.png','raw',1024*1024)");
//			connection.parseAndEval("unlink('Yash_GenderVsTotalAccountBalance.jpg'); r");
			byte[] image = xp.asBytes();
			connection.close();

			return null;
		} catch (REngineException | REXPMismatchException e) {
			Logger.error(e);
		}

		return null;
	}

}
