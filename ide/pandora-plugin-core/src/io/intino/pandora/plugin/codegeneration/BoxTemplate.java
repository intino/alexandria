package io.intino.pandora.plugin.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BoxTemplate extends Template {

	protected BoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(literal("import tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\n\npublic class ")).add(mark("name")).add(literal("Box {\n\n\t")).add(mark("name")).add(literal("Configuration configuration;\n\tprivate Map<String, Object> map = new LinkedHashMap<>();\n\n\t")).add(expression().add(literal("\n")).add(literal("\tprivate String graphID;")).add(literal("\n")).add(literal("\n")).add(literal("\tpublic ")).add(mark("tara")).add(literal("Box(Graph graph) {")).add(literal("\n")).add(literal("\t\tmap.put(graphID = UUID.randomUUID().toString(), graph);")).add(literal("\n")).add(literal("\t}")).add(literal("\n")).add(literal("\n")).add(literal("\tpublic Graph graph() {")).add(literal("\n")).add(literal("\t\treturn (Graph) map.get(graphID);")).add(literal("\n")).add(literal("\t}")).add(literal("\n")).add(literal("\t"))).add(literal("\n\n\tpublic ")).add(mark("name")).add(literal("Box(")).add(mark("name")).add(literal("Configuration configuration) {\n\t\tthis.configuration = configuration;\n\t}\n\n\t")).add(mark("service").multiple("\n")).add(literal("\n\n\tpublic <T> T get(Class<T> tClass) {\n\t\treturn (T) map.values().stream().filter(tClass::isInstance).findFirst().orElse(null);\n\t}\n\n\n\tpublic <T> T get(String object, Class<T> tClass) {\n\t\treturn (T) map.get(object);\n\t}\n\n\tpublic ")).add(mark("name")).add(literal("BoxConfiguration put(Object object) {\n\t\tmap.put(\"\", object);\n\t\treturn this;\n\t}\n}")),
			rule().add((condition("trigger", "import")))
		);
		return this;
	}
}