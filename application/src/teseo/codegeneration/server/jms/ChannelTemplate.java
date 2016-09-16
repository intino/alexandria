package teseo.codegeneration.server.jms;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ChannelTemplate extends Template {

	protected ChannelTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ChannelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "channel"))).add(literal("package ")).add(mark("package")).add(literal(".channels;\n\nimport ")).add(mark("package")).add(literal(".schemas.*;\nimport com.google.gson.Gson;\nimport tara.magritte.Graph;\nimport teseo.jms.Consumer;\n\nimport javax.jms.*;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Channel implements Consumer {\n\n\tprivate Graph graph;\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Channel(Graph graph) {\n\t\tthis.graph = graph;\n\t}\n\n\tpublic void consume(Message message) {\n\t\tactionFor(message).execute();\n\t}\n\n\tprivate ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action actionFor(Message message) {\n\t\tfinal ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\taction.graph = this.graph;")).add(expression().add(literal("\n")).add(literal("\t\ttry {")).add(literal("\n")).add(literal("\t\t\t")).add(mark("message", "assign")).add(literal("\n")).add(literal("\t\t} catch (JMSException e) {")).add(literal("\n")).add(literal("\t\t\te.printStackTrace();")).add(literal("\n")).add(literal("\t\t}"))).add(literal("\n\t\treturn action;\n\t}\n}")),
			rule().add((condition("type", "message & fileData")), (condition("trigger", "assign"))).add(literal("action.message = ((BytesMessage) message).getBytes();")),
			rule().add((condition("type", "message")), (condition("trigger", "assign"))).add(literal("action.message = new Gson().fromJson(((TextMessage) message).getText(), ")).add(mark("type")).add(literal(".class);"))
		);
		return this;
	}
}