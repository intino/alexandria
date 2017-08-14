package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class GraphProviderTemplate extends Template {

	protected GraphProviderTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new GraphProviderTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "operations"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.tara.magritte.Graph;\nimport io.intino.tara.magritte.Store;\n\nclass GraphProvider {\n\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\n\tGraphProvider(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tGraph graph() {\n\t\treturn null;\n\t\t//return box.graph();\n\t}\n\n\tStore store() {\n\t\treturn null;\n\t\t//return new SumusStore(new File(\"store\"));\n\t}\n\n\tvoid saveGraph(Graph graph) {\n\t\tgraph.saveAll(\"Model\");\n\t}\n}"))
		);
		return this;
	}
}