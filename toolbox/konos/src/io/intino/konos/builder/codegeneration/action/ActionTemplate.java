package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class ActionTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(all(allTypes("action", "jms", "process"), not(allTypes("ui")))).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Action implements ")).output(placeholder("package")).output(literal(".")).output(placeholder("service", "firstUpperCase")).output(literal("Service.InboxDispatcher<")).output(expression().output(placeholder("returnType")).next(expression().output(literal("Void")))).output(literal("> {\n\tprivate ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Action(")).output(placeholder("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void onRequest(")).output(expression().output(placeholder("parameter", "onlyType")).output(literal(" input"))).output(literal(") {\n\t\t//TODO save input\n\t}\n\n\tpublic ")).output(expression().output(placeholder("returnType")).next(expression().output(literal("Void")))).output(literal(" onResponse() {\n\t\treturn null;\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("action", "notification"), not(allTypes("ui")))).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.http.pushservice.Client;\nimport io.intino.alexandria.http.server.AlexandriaHttpNotifier;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Action {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\t")).output(placeholder("contextProperty")).output(literal("\n\t")).output(expression().output(placeholder("parameter", "type").multiple("\n"))).output(literal("\n\n\tpublic void onOpen(Client client, AlexandriaHttpNotifier notifier) {\n\t\t//register listener\n\t}\n\n\tpublic void onClose(Client client) {\n\t\t//unregister listener\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("action", "rest"), not(allTypes("ui")))).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\nimport io.intino.alexandria.exceptions.*;\nimport java.time.*;\nimport java.util.*;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Action implements io.intino.alexandria.rest.RequestErrorHandler {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\t")).output(placeholder("contextProperty")).output(literal("\n\t")).output(expression().output(placeholder("parameter", "type").multiple("\n"))).output(literal("\n\n\tpublic ")).output(expression().output(placeholder("returnType")).next(expression().output(literal("void")))).output(literal(" execute() ")).output(expression().output(literal("throws ")).output(placeholder("throws", "FirstUpperCase").multiple(", "))).output(literal(" {\n\t\t")).output(placeholder("returnType", "return")).output(literal("\n\t}\n\n\tpublic void onMalformedRequest(Throwable e) throws AlexandriaException {\n\t\t//TODO\n\t\tthrow new BadRequest(\"Malformed request\");\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("action"), not(allTypes("ui")))).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Action {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\t")).output(placeholder("contextProperty")).output(literal("\n\t")).output(expression().output(placeholder("parameter", "type").multiple("\n"))).output(literal("\n\n\tpublic ")).output(expression().output(placeholder("returnType")).next(expression().output(literal("void")))).output(literal(" execute() ")).output(expression().output(literal("throws ")).output(placeholder("throws", "FirstUpperCase").multiple(", "))).output(literal(" {\n\t\t")).output(placeholder("returnType", "return")).output(literal("\n\t}\n}")));
		rules.add(rule().condition(allTypes("ui", "accessibleDisplay")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal(".")).output(placeholder("display", "PascalCase")).output(literal(";\nimport io.intino.alexandria.ui.Soul;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("ProxyPage extends io.intino.alexandria.ui.server.pages.ProxyPage {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\t")).output(expression().output(placeholder("parameter", "type").multiple("\n"))).output(literal("\n\tpublic Soul soul;\n\n\tpublic void execute() {\n\t\t")).output(placeholder("display", "PascalCase")).output(literal(" display = new ")).output(placeholder("display", "PascalCase")).output(literal("(box);\n\t\tdisplay.id(personifiedDisplay);\n\t\t")).output(placeholder("parameter", "methodCall").multiple("\n")).output(literal("\n\t\tsoul.register(display);\n\t\tdisplay.init();\n\t\tdisplay.refresh();\n\t}\n}")));
		rules.add(rule().condition(allTypes("ui", "mobile", "action", "gen")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\n\nimport java.util.Collections;\nimport java.util.List;\n\npublic abstract class Abstract")).output(placeholder("name", "PascalCase")).output(literal("MobilePage extends io.intino.alexandria.ui.server.pages.MobilePage {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\n    public String redirectUrl() { return null; }\n\n    public String execute() ")).output(expression().output(literal("throws ")).output(placeholder("throws", "FirstUpperCase").multiple(", "))).output(literal(" {\n        List<String> connections = pushConnections(Collections.emptyList(), session.id(), session.discoverLanguage(), session.browser());\n        return String.join(\",\", connections);\n    }\n\n}")));
		rules.add(rule().condition(allTypes("ui", "mobile", "action")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.pages;\n\nimport io.intino.alexandria.exceptions.*;\nimport java.time.*;\nimport java.util.*;\nimport ")).output(placeholder("importTemplates", "validPackage")).output(literal(".ui.displays.templates.*;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("MobilePage extends Abstract")).output(placeholder("name", "PascalCase")).output(literal("MobilePage {\n\t")).output(expression().output(placeholder("parameter", "type").multiple("\n"))).output(literal("\n\t")).output(expression().output(placeholder("editor", "parameter"))).output(literal("\n\n\t")).output(placeholder("component")).output(literal("\n}")));
		rules.add(rule().condition(allTypes("ui", "action", "gen", "asset")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\n\npublic abstract class Abstract")).output(placeholder("name", "PascalCase")).output(literal("Page extends io.intino.alexandria.ui.server.pages.Page {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n}")));
		rules.add(rule().condition(allTypes("ui", "action", "gen")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.pages;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\nimport io.intino.alexandria.exceptions.*;\nimport java.util.*;\n\npublic abstract class Abstract")).output(placeholder("name", "PascalCase")).output(literal("Page extends io.intino.alexandria.ui.server.pages.WebPage {\n\tpublic ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\n\tpublic Abstract")).output(placeholder("name", "PascalCase")).output(literal("Page() { super(\"")).output(placeholder("uiService", "camelCaseToKebabCase", "lowerCase")).output(literal("\"); }\n\n\tpublic ")).output(placeholder("returnType")).output(literal(" execute() ")).output(expression().output(literal("throws ")).output(placeholder("throws", "FirstUpperCase").multiple(", "))).output(literal(" {\n\t\t")).output(placeholder("executeBody")).output(literal("\n\t}\n\n\t@Override\n\tprotected String title() {\n\t\treturn ")).output(placeholder("title")).output(literal(";\n\t}\n\n\t@Override\n\tprotected java.net.URL favicon() {\n\t\treturn ")).output(expression().output(placeholder("favicon")).next(expression().output(literal("null;")))).output(literal("\n\t}\n}")));
		rules.add(rule().condition(allTypes("returnType", "asset")).output(literal("io.intino.alexandria.Resource")));
		rules.add(rule().condition(allTypes("returnType")).output(literal("String")));
		rules.add(rule().condition(allTypes("executeBody", "static")).output(literal("return \"")).output(placeholder("text")).output(literal("\";")));
		rules.add(rule().condition(allTypes("executeBody", "asset")).output(literal("return null;")));
		rules.add(rule().condition(allTypes("executeBody")).output(literal("return super.template(\"")).output(placeholder("templateName")).output(literal("\"")).output(expression().output(literal(", Arrays.asList(")).output(placeholder("usedUnit").multiple(",")).output(literal(")"))).output(literal(");")));
		rules.add(rule().condition(allTypes("title", "configuration")).output(literal("\"{")).output(placeholder("title")).output(literal("}\".replace(\"{")).output(placeholder("title")).output(literal("}\", box.configuration().get(\"")).output(placeholder("title")).output(literal("\"))")));
		rules.add(rule().condition(allTypes("title")).output(literal("\"")).output(placeholder("title")).output(literal("\"")));
		rules.add(rule().condition(trigger("favicon")).output(literal("this.getClass().getResource(\"")).output(placeholder("")).output(literal("\");")));
		rules.add(rule().condition(all(allTypes("ui", "action"), not(allTypes("gen")))).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.pages;\n\nimport io.intino.alexandria.exceptions.*;\nimport java.time.*;\nimport java.util.*;\nimport ")).output(placeholder("importTemplates", "validPackage")).output(literal(".ui.displays.templates.*;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Page extends Abstract")).output(placeholder("name", "PascalCase")).output(literal("Page {\n\t")).output(expression().output(placeholder("parameter", "type").multiple("\n"))).output(literal("\n\t")).output(expression().output(placeholder("editor", "parameter"))).output(literal("\n\n\t")).output(placeholder("component")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("parameter", "fileData"), trigger("type"))).output(literal("public io.intino.alexandria.Resource ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("parameter", "list"), trigger("type"))).output(literal("public java.util.List<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("parameter", "word"), trigger("type"))).output(literal("public String ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("type"))).output(literal("public ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("methodcall"))).output(literal("display.")).output(placeholder("name", "CamelCase")).output(literal("(")).output(placeholder("name", "CamelCase")).output(literal(");")));
		rules.add(rule().condition(all(allTypes("fileData"), trigger("onlytype"))).output(literal("io.intino.alexandria.Resource")));
		rules.add(rule().condition(all(allTypes("list"), trigger("onlytype"))).output(literal("java.util.List<")).output(placeholder("type")).output(literal(">")));
		rules.add(rule().condition(trigger("onlytype")).output(placeholder("type")));
		rules.add(rule().condition(all(allTypes("editor"), trigger("parameter"))).output(literal("public io.intino.alexandria.Resource document;\npublic io.intino.alexandria.ui.services.EditorService.Permission permission;")));
		rules.add(rule().condition(all(attribute("","void"), trigger("return"))));
		rules.add(rule().condition(trigger("return")).output(literal("return null;")));
		rules.add(rule().condition(all(allTypes("schemaImport"), trigger("schemaimport"))).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		rules.add(rule().condition(allTypes("component", "static")).output(literal("public String execute() {\n\treturn super.execute();\n}")));
		rules.add(rule().condition(allTypes("component", "asset")).output(literal("public io.intino.alexandria.Resource execute() {\n\treturn null;\n}")));
		rules.add(rule().condition(allTypes("component")).output(literal("public io.intino.alexandria.ui.Soul prepareSoul(io.intino.alexandria.ui.services.push.UIClient client) {\n\treturn new io.intino.alexandria.ui.Soul(session) {\n\t\t@Override\n\t\tpublic void personify() {\n\t\t\t")).output(placeholder("value", "PascalCase")).output(literal(" component = new ")).output(placeholder("value", "PascalCase")).output(literal("(box);\n\t\t\t")).output(expression().output(placeholder("editor", "component"))).output(literal("\n\t\t\tregister(component);\n\t\t\tcomponent.init();\n\t\t}\n\t};\n}")));
		rules.add(rule().condition(all(allTypes("editor"), trigger("component"))).output(literal("component.document(document);\ncomponent.permission(permission);\ncomponent.display(new ")).output(placeholder("display", "PascalCase")).output(literal("(box));")));
		rules.add(rule().condition(allTypes("usedUnit", "standard")).output(literal("new io.intino.alexandria.ui.server.pages.Unit(\"")).output(placeholder("name")).output(literal("\",\"")).output(placeholder("url")).output(literal("\"")).output(expression().output(literal(",\"")).output(placeholder("socketPath")).output(literal("\""))).output(literal(")")));
		rules.add(rule().condition(allTypes("usedUnit", "custom")).output(literal("new io.intino.alexandria.ui.server.pages.Unit(\"")).output(placeholder("name")).output(literal("\",box.configuration().get(\"")).output(placeholder("url")).output(literal("\")")).output(expression().output(literal(",\"")).output(placeholder("socketPath")).output(literal("\""))).output(literal(")")));
		rules.add(rule().condition(allTypes("contextProperty", "server")).output(literal("public io.intino.alexandria.http.server.AlexandriaHttpContext context;")));
		rules.add(rule().condition(allTypes("contextProperty")).output(literal("public io.intino.alexandria.Context context = new io.intino.alexandria.Context();")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}