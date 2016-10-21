package io.intino.pandora.plugin.codegeneration.server.jms.service;

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
				rule().add((condition("type", "jms"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport ")).add(mark("package")).add(literal(".requests.*;\nimport tara.magritte.Graph;\nimport io.intino.pandora.jms.QueueConsumer;\n\nimport javax.jms.Session;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("JMSService {\n\n\tpublic static void init(Session session, Graph graph) {\n\t\t")).add(mark("request").multiple("\n")).add(literal("\n\t}\n}")),
				rule().add((condition("type", "request"))).add(literal("new QueueConsumer(session, \"")).add(mark("queue")).add(literal("\").listen(new ")).add(mark("name", "firstUpperCase")).add(literal("Request(graph));"))
		);
		return this;
	}
}