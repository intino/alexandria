package teseo.codegeneration.server.rest;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class RestResourceTemplate extends Template {

	protected RestResourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RestResourceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "resource"))).add(literal("package ")).add(mark("package")).add(literal(".resources;\n\nimport org.apache.http.HttpRequest;\nimport org.apache.http.HttpResponse;\nimport teseo.Error;\nimport teseo.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport com.google.gson.Gson;\nimport tara.magritte.Graph;\n")).add(mark("schemaImport")).add(literal("\n\nimport static teseo.Commons.error;\nimport static java.util.Collections.emptyMap;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Resource {\n\n\tprivate Graph graph;\n\tprivate final HttpRequest request;\n\tprivate final HttpResponse response;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Resource(Graph graph, HttpRequest request, HttpResponse response) {\n\t\tthis.graph = graph;\n\t\tthis.request = request;\n\t\tthis.response = response;\n\t}\n\n\tpublic void execute() throws ")).add(mark("throws").multiple(", ")).add(literal(" {\n        ")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action();\n        fill(action);\n        ")).add(expression().add(mark("returnType", "return"))).add(literal("new ")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action().execute()")).add(expression().add(mark("returnType", "ending"))).add(literal(";\n\t}")).add(expression().add(literal("\n")).add(literal("    ")).add(mark("returnType", "write"))).add(literal("\n\n\tprivate void fill(")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action) {\n\t    action.graph = graph;\n\t    ")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
				rule().add((condition("trigger", "return"))).add(literal("write(")),
				rule().add((condition("attribute", "void")), (condition("trigger", "ending"))),
				rule().add((condition("trigger", "ending"))).add(literal(")")),
				rule().add((condition("attribute", "void")), (condition("trigger", "write"))),
				rule().add((condition("attribute", "object")), (condition("trigger", "write"))).add(literal("private void write(")).add(mark("value", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" object) {\n    response.write(new Gson().toJson(object, ")).add(mark("value", "firstUpperCase")).add(literal(".class));\n}")),
				rule().add((condition("trigger", "write"))).add(literal("private void write(")).add(mark("value", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" object) {\n    response.write(object);\n}")),
				rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(mark("parameterType")),
				rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = (")).add(mark("parameterType")).add(literal(") request.parameter(\"")).add(mark("name")).add(literal("\");")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}