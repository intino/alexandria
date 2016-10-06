package io.intino.pandora.plugin.codegeneration.server.rest;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.CRLF;

public class RestResourceTemplate extends Template {

	protected RestResourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RestResourceTemplate(Locale.ENGLISH, CRLF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "resource"))).add(literal("package ")).add(mark("package")).add(literal(".resources;\n\nimport java.util.List;\nimport org.siani.pandora.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport tara.magritte.Graph;\nimport org.siani.pandora.server.web.Resource;\nimport org.siani.pandora.server.web.spark.SparkManager;\n")).add(mark("formatImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Resource implements Resource {\n\n\tprivate Graph graph;\n\tprivate SparkManager manager;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Resource(Graph graph, SparkManager manager) {\n\t\tthis.graph = graph;\n\t\tthis.manager = manager;\n\t}\n\n\tpublic void execute() throws ")).add(mark("throws").multiple(", ")).add(literal(" {\n\t\t")).add(expression().add(mark("returnType", "return"))).add(literal("fill(new ")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action()).execute()")).add(expression().add(mark("returnType", "ending"))).add(literal(";\n\t}\n\n\tprivate ")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action fill(")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action) {\n\t\taction.graph = this.graph;")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parameter", "assign").multiple("\n"))).add(literal("\n\t\treturn action;\n\t}\n    ")).add(expression().add(literal("\n")).add(literal("    ")).add(mark("returnType", "write"))).add(literal("\n}")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("write(")),
			rule().add((condition("attribute", "void")), (condition("trigger", "ending"))),
			rule().add((condition("trigger", "ending"))).add(literal(")")),
			rule().add((condition("attribute", "void")), (condition("trigger", "write"))),
			rule().add((condition("trigger", "write"))).add(literal("private void write(")).add(mark("value", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" object) {\n\tmanager.write(object);\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(mark("parameterType")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = manager.from")).add(mark("in", "firstUpperCase")).add(literal("(\"")).add(mark("name")).add(literal("\", ")).add(mark("parameterType")).add(literal(".class);")),
			rule().add((condition("type", "formatImport"))).add(literal("import ")).add(mark("package")).add(literal(".formats.*;"))
		);
		return this;
	}
}