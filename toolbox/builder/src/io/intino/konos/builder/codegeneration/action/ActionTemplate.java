package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ActionTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("notification","action")), not(type("ui"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.rest.pushservice.Client;\nimport io.intino.alexandria.rest.spark.SparkNotifier;\n\npublic class ")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Action {\n\tpublic ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\tpublic io.intino.alexandria.core.Context context = new io.intino.alexandria.core.Context();\n\t")).output(expression().output(mark("parameter", "type").multiple("\n"))).output(literal("\n\n\tpublic void onOpen(Client client, SparkNotifier notifier) {\n\t\t//register listener\n\t}\n\n\tpublic void onClose(Client client) {\n\t\t//unregister listener\n\t}\n}")),
			rule().condition((type("action")), not(type("ui"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.exceptions.*;\nimport java.time.*;\nimport java.util.*;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Page {\n\n\tpublic ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\tpublic io.intino.alexandria.core.Context context = new io.intino.alexandria.core.Context();\n\t")).output(expression().output(mark("parameter", "type").multiple("\n"))).output(literal("\n\n\tpublic ")).output(expression().output(mark("returnType")).next(expression().output(literal("void")))).output(literal(" execute() ")).output(expression().output(literal("throws ")).output(mark("throws", "FirstUpperCase").multiple(", "))).output(literal(" {\n\t\t")).output(mark("returnType", "return")).output(literal("\n\t}\n}")),
			rule().condition((allTypes("accessibledisplay","ui"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".displays.")).output(mark("display", "snakecaseToCamelCase", "firstUpperCase")).output(literal(";\nimport io.intino.alexandria.ui.Soul;\n\npublic class ")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("ProxyPage extends io.intino.alexandria.ui.spark.pages.ProxyPage {\n\tpublic ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\t")).output(expression().output(mark("parameter", "type").multiple("\n"))).output(literal("\n\tpublic Soul soul;\n\n\tpublic void execute() {\n\t\t")).output(mark("display", "snakecaseToCamelCase", "firstUpperCase")).output(literal(" display = new ")).output(mark("display", "snakecaseToCamelCase", "firstUpperCase")).output(literal("(box);\n\t\t")).output(mark("parameter", "methodCall").multiple("\n")).output(literal("\n\t\tsoul.register(display);\n\t\tdisplay.init();\n\t\tdisplay.refresh();\n\t}\n}")),
			rule().condition((allTypes("gen","ui","action"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.exceptions.*;\nimport java.util.*;\n\npublic abstract class Abstract")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Page extends io.intino.alexandria.ui.spark.pages.Page {\n\tpublic ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\n\tpublic Abstract")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Page() { super(\"")).output(mark("uiService", "camelCaseToSnakeCase", "lowerCase")).output(literal("\"); }\n\n\tpublic String execute() ")).output(expression().output(literal("throws ")).output(mark("throws", "FirstUpperCase").multiple(", "))).output(literal(" {\n\t\treturn super.template(\"")).output(mark("name")).output(literal("\"")).output(expression().output(literal(", Arrays.asList(")).output(mark("usedAppUrl").multiple(",")).output(literal(")"))).output(literal(");\n\t}\n\n\t@Override\n\tprotected String title() {\n\t\treturn \"")).output(mark("title")).output(literal("\";\n\t}\n\n\t@Override\n\tprotected java.net.URL favicon() {\n\t\treturn ")).output(expression().output(mark("favicon")).next(expression().output(literal("null;")))).output(literal("\n\t}\n}")),
			rule().condition((trigger("favicon"))).output(literal("this.getClass().getResource(\"")).output(mark("")).output(literal("\");")),
			rule().condition((allTypes("ui","action")), not(type("gen"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.pages;\n\nimport io.intino.alexandria.exceptions.*;\nimport java.time.*;\nimport java.util.*;\nimport ")).output(mark("importTemplates", "validPackage")).output(literal(".ui.displays.templates.*;\n\npublic class ")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Page extends Abstract")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Page {\n\t")).output(expression().output(mark("parameter", "type").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("editor", "parameter"))).output(literal("\n\n\t")).output(mark("component")).output(literal("\n}")),
			rule().condition((allTypes("parameter","filedata")), (trigger("type"))).output(literal("public io.intino.alexandria.Resource ")).output(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).output(literal(";")),
			rule().condition((allTypes("parameter","list")), (trigger("type"))).output(literal("public java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).output(literal(";")),
			rule().condition((type("parameter")), (trigger("type"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).output(literal(";")),
			rule().condition((type("parameter")), (trigger("methodcall"))).output(literal("display.")).output(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).output(literal("(")).output(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).output(literal(");")),
			rule().condition((type("editor")), (trigger("parameter"))).output(literal("public io.intino.alexandria.Resource document;\npublic io.intino.alexandria.ui.services.EditorService.Permission permission;")),
			rule().condition((attribute("", "void")), (trigger("return"))),
			rule().condition((trigger("return"))).output(literal("return null;")),
			rule().condition((type("schemaimport")), (trigger("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((type("component"))).output(literal("public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {\n    return new io.intino.alexandria.ui.Soul(session) {\n\t\t@Override\n\t\tpublic void personify() {\n\t\t\t")).output(mark("value", "snakecaseToCamelCase", "firstUpperCase")).output(literal(" component = new ")).output(mark("value", "snakecaseToCamelCase", "firstUpperCase")).output(literal("(box);\n\t\t\t")).output(expression().output(mark("editor", "component"))).output(literal("\n\t\t\tregister(component);\n\t\t\tcomponent.init();\n\t\t}\n\t};\n}")),
			rule().condition((type("editor")), (trigger("component"))).output(literal("component.document(document);\ncomponent.permission(permission);\ncomponent.display(new ")).output(mark("display", "snakecaseToCamelCase", "firstUpperCase")).output(literal("(box));")),
			rule().condition((allTypes("standard","usedappurl"))).output(literal("\"")).output(mark("")).output(literal("\"")),
			rule().condition((allTypes("usedappurl","custom"))).output(literal("box.configuration().get(\"")).output(mark("")).output(literal("\")"))
		);
	}
}