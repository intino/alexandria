package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ReflowAssistantTemplate extends Template {

	protected ReflowAssistantTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ReflowAssistantTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "operations"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".datalake;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.tara.magritte.Graph;\nimport io.intino.konos.datalake.EventDatalake;\nimport io.intino.tara.magritte.Store;\n\nimport java.util.List;\n\nclass ReflowAssistant {\n\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\n\tReflowAssistant(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tvoid before() {\n\n\t}\n\n\tint defaultBlockSize() {\n\t\treturn Integer.MAX_VALUE;\n\t}\n\n\tList<EventDatalake.Tank> defaultTanks() {\n\t \treturn java.util.Collections.emptyList(); //TODO\n\t}\n\n\tGraph graph() {\n\t\treturn null;\n\t\t//return box.graph().core$();\n\t}\n\n\tString[] coreStashes() {\n\t\treturn new String[]{\"Model\"};\n\t}\n\n\tvoid saveGraph(Graph graph) {\n\t\tgraph.saveAll(\"Model\");\n\t}\n\n\tvoid after() {\n\n\t}\n}"))
		);
		return this;
	}
}