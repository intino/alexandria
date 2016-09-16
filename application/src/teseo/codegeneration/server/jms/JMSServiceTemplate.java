package teseo.codegeneration.server.jms;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMSServiceTemplate extends Template {

	protected JMSServiceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMSServiceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "jms"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport ")).add(mark("package")).add(literal(".resources.*;\nimport tara.magritte.Graph;\nimport teseo.jms.QueueConsumer;\n\nimport javax.jms.Session;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("JMSService {\n\n\tpublic static void init(Session session, Graph graph) {\n\t\t")).add(mark("resource").multiple("\n")).add(literal("\n\t}\n}")),
				rule().add((condition("type", "resource"))).add(literal("new QueueConsumer(session, \"")).add(mark("queue")).add(literal("\").listen(new ")).add(mark("name", "firstUpperCase")).add(literal("Resource(graph));"))
		);
		return this;
	}
}