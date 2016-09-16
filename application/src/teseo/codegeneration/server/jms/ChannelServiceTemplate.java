package teseo.codegeneration.server.jms;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ChannelServiceTemplate extends Template {

	protected ChannelServiceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ChannelServiceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "channels"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport ")).add(mark("package")).add(literal(".channels.*;\nimport ")).add(mark("package")).add(literal(".schemas.*;\nimport tara.magritte.Graph;\nimport teseo.jms.*;\n\nimport javax.jms.Session;\nimport javax.jms.JMSException;\n\npublic class Channels {\n\n\tpublic static void init(Session session, Graph graph) {\n\t\t")).add(mark("channel").multiple("\n")).add(literal("\n\t}\n\n\t")).add(mark("channel", "runner").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "channel & timeout & input")), (condition("trigger", "runner"))).add(literal("public static void consume")).add(mark("name", "firstUpperCase")).add(literal("(Session session, Graph graph) {\n\tnew ")).add(mark("type")).add(literal("Consumer(session, \"")).add(mark("path")).add(literal("\").read(")).add(mark("timeout")).add(literal(", new ")).add(mark("name", "firstUpperCase")).add(literal("Channel(graph));\n}")),
			rule().add((condition("type", "channel & output")), (condition("trigger", "runner"))).add(literal("public static void publish")).add(mark("name", "firstUpperCase")).add(literal("(Session session, ")).add(mark("message")).add(literal(" object) throws JMSException {\n\tnew ")).add(mark("type")).add(literal("Producer(session, \"")).add(mark("path")).add(literal("\").produce(MessageFactory.createMessageFor(object));\n}")),
			rule().add((condition("trigger", "runner"))),
			rule().add((condition("type", "channel & input")), not(condition("type", "timeout"))).add(literal("new ")).add(mark("type")).add(literal("Consumer(session, \"")).add(mark("path")).add(literal("\").listen(new ")).add(mark("name", "firstUpperCase")).add(literal("Channel(graph));")),
			rule().add((condition("type", "channel")))
		);
		return this;
	}
}