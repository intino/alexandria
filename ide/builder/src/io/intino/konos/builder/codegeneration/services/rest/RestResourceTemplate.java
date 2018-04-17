package io.intino.konos.builder.codegeneration.services.rest;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class RestResourceTemplate extends Template {

	protected RestResourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RestResourceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "resource"))).add(literal("package ")).add(mark("package")).add(literal(".resources;\n\nimport java.util.List;\nimport io.intino.konos.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport io.intino.konos.alexandria.Box;\nimport io.intino.konos.alexandria.rest.Resource;\nimport io.intino.konos.alexandria.rest.spark.SparkManager;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Resource implements Resource {\n\n\tprivate ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\tprivate SparkManager manager;\n\n\tpublic ")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Resource(")).add(mark("box", "FirstUpperCase")).add(literal("Box box, SparkManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t}\n\n\tpublic void execute() throws ")).add(mark("throws").multiple(", ")).add(literal(" {\n\t\t")).add(expression().add(mark("returnType", "writeCall"))).add(literal("fill(new ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Action()).execute()")).add(expression().add(mark("returnType", "ending"))).add(literal(";\n\t}\n\n\tprivate ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Action fill(")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parameter", "assign").multiple("\n"))).add(literal("\n\t\treturn action;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("returnType", "write"))).add(literal("\n\n\tprivate io.intino.konos.alexandria.schema.Context context() {\n\t\tio.intino.konos.alexandria.schema.Context context = new io.intino.konos.alexandria.schema.Context();\n\t\tcontext.put(\"requestUrl\", manager.request().url());\n\t\treturn context;\n\t}\n}")),
			rule().add((condition("attribute", "void")), (condition("trigger", "writeCall"))),
			rule().add((condition("trigger", "writeCall"))).add(literal("write(")),
			rule().add((condition("attribute", "void")), (condition("trigger", "ending"))),
			rule().add((condition("trigger", "ending"))).add(literal(")")),
			rule().add((condition("attribute", "void")), (condition("trigger", "write"))),
			rule().add((condition("trigger", "write"))).add(literal("private void write(")).add(mark("value", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" object) {\n\tmanager.write(object")).add(expression().add(literal(", \"")).add(mark("format")).add(literal("\""))).add(literal(");\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(mark("parameterType")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = manager.from")).add(mark("in", "firstUpperCase")).add(literal("(\"")).add(mark("name")).add(literal("\", ")).add(mark("parameterType")).add(literal(");")),
			rule().add((condition("type", "list")), (condition("trigger", "parameterType"))).add(literal("new java.util.ArrayList<")).add(mark("value")).add(literal(">(0).getClass()")),
			rule().add((condition("trigger", "parameterType"))).add(mark("value")).add(literal(".class")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}