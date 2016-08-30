package teseo.codegeneration.server;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JavaServerActionTemplate extends Template {

	protected JavaServerActionTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JavaServerActionTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "action")), not(condition("type", "impl"))).add(literal("package ")).add(mark("package")).add(literal(".actions;\n\nimport teseo.framework.actions.Action;\nimport teseo.Error;\nimport static teseo.Commons.error;\nimport static java.util.Collections.emptyMap;\nimport teseo.exceptions.*;\nimport java.time.LocalDateTime;\nimport ")).add(mark("package")).add(literal(".*;\nimport com.google.gson.Gson;\n")).add(mark("schemaImport")).add(literal("\n\npublic abstract class ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("AbstractAction implements Action {\n\n\t@Override\n\tpublic Task<Input, Output> task() {\n\t\treturn (input, output) -> {\n\t\t\ttry {\n\t\t\t\t")).add(mark("doTask")).add(literal("\n\t\t\t} catch(TeseoException e){\n\t\t\t\toutput.error(error(e.code(), e.message(), emptyMap()));\n\t\t\t}\n\t\t};\n\t}\n\n\tprotected abstract ")).add(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" doTask(Input input) throws ")).add(mark("throws").multiple(", ")).add(literal(";\n\n\tprotected interface Input extends Action.Input {\n\n\t\t")).add(mark("parameters").multiple("\n\n")).add(literal("\n\n\t}\n}")),
			rule().add((condition("type", "action & impl"))).add(literal("package ")).add(mark("package")).add(literal(".actions;\n\nimport teseo.framework.actions.Action;\nimport teseo.Error;\nimport teseo.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport com.google.gson.Gson;\nimport tara.magritte.Graph;\n")).add(mark("schemaImport")).add(literal("\n\nimport static teseo.Commons.error;\nimport static java.util.Collections.emptyMap;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Action extends ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("AbstractAction {\n\n\tprivate Graph graph;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Action(Graph graph) {\n\t\tthis.graph = graph;\n\t}\n\n\tprotected ")).add(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" doTask(Input input) throws ")).add(mark("throws").multiple(", ")).add(literal(" {\n\t\t//TODO Insert action code here\n\t\t")).add(mark("doTask", "content")).add(literal("\n\t}\n\n}")),
			rule().add((condition("type", "parameter"))).add(mark("parameterType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("();")),
			rule().add((condition("type", "void")), (condition("trigger", "content"))),
			rule().add((condition("trigger", "content"))).add(literal("return null;")),
			rule().add((condition("type", "void")), (condition("trigger", "doTask"))).add(literal("doTask(input);")),
			rule().add((condition("type", "object")), (condition("trigger", "doTask"))).add(literal("output.write(new Gson().toJson(doTask(input), ")).add(mark("returnType", "firstUpperCase")).add(literal(".class));")),
			rule().add((condition("trigger", "doTask"))).add(literal("output.write(doTask(input).toString());")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}