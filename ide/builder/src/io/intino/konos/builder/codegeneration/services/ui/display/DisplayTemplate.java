package io.intino.konos.builder.codegeneration.services.ui.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayTemplate extends Template {

	protected DisplayTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display & accessible"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package")).add(literal(".displays.notifiers.")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("ProxyNotifier;\nimport io.intino.konos.alexandria.ui.displays.AlexandriaProxyDisplay;\nimport io.intino.konos.alexandria.ui.services.push.UISession;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Proxy extends AlexandriaProxyDisplay<")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("ProxyNotifier> {\n    ")).add(mark("parameter", "field").multiple("\n")).add(literal("\n\n    public ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Proxy(UISession session, String appUrl) {\n        super(session, appUrl, \"/")).add(mark("name", "SnakeCaseToCamelCase", "lowercase")).add(literal("proxy\");\n    }\n\n\t")).add(mark("request", "accessible").multiple("\n\n")).add(literal("\n\t")).add(mark("parameter", "method").multiple("\n\n")).add(literal("\n\n    @Override\n    protected Map<String, String> parameters() {\n        Map<String, String> result = new HashMap<>();\n        ")).add(mark("parameter", "map").multiple("\n")).add(literal("\n        return result;\n    }\n\n    @Override\n    protected void refreshBaseUrl(String url) {\n        notifier.refreshBaseUrl(url);\n    }\n\n    @Override\n    protected void refreshError(String error) {\n        notifier.refreshError(error);\n    }\n\n}")),
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\nimport io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;\n")).add(mark("parent", "import")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" extends AlexandriaDisplay<")).add(mark("name", "firstUpperCase")).add(literal("Notifier> {\n    private ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n        super();\n        this.box = box;\n    }\n\t")).add(expression().add(literal("\n")).add(literal("    @Override")).add(literal("\n")).add(literal("\tprotected void init() {")).add(literal("\n")).add(literal("\t\tsuper.init();")).add(literal("\n")).add(literal("\t\t")).add(mark("parent")).add(literal("\n")).add(literal("\t\t")).add(mark("innerDisplay").multiple("\n")).add(literal("\n")).add(literal("\t}")).add(literal("\n"))).add(literal("\n\t")).add(mark("request").multiple("\n\n")).add(literal("\n\t")).add(mark("parameter", "setter").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "request & asset"))).add(literal("public io.intino.konos.alexandria.ui.spark.UIFile ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n    return null;\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "accessible"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n\trequest(\"")).add(mark("name")).add(literal("\"")).add(expression().add(literal(", ")).add(mark("parameter", "parameterValue"))).add(literal(");\n}")),
			rule().add((condition("trigger", "parameterValue"))).add(literal("value")),
			rule().add((condition("type", "request"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n\n}")),
			rule().add((condition("trigger", "setter"))).add(literal("public void ")).add(mark("value", "firstLowerCase")).add(literal("(String value) {\n\n}")),
			rule().add((condition("trigger", "field"))).add(literal("private String ")).add(mark("value", "firstLowerCase")).add(literal(";")),
			rule().add((condition("trigger", "map"))).add(literal("result.put(\"")).add(mark("value")).add(literal("\", ")).add(mark("value", "firstLowerCase")).add(literal(");")),
			rule().add((condition("trigger", "method"))).add(literal("public void ")).add(mark("value", "firstLowerCase")).add(literal("(String value) {\n    this.")).add(mark("value", "firstLowerCase")).add(literal(" = value;\n}")),
			rule().add((condition("type", "dateTime | date")), (condition("type", "list")), (condition("trigger", "parameter"))).add(mark("value")),
			rule().add((condition("type", "dateTime | date")), (condition("trigger", "parameter"))).add(mark("value")),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(mark("value")).add(literal("[]")),
			rule().add((condition("trigger", "parameter"))).add(mark("value")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("trigger", "import"))).add(literal("import ")).add(mark("package")).add(literal(".displays.*;")),
			rule().add((condition("trigger", "parent"))).add(literal("addAndPersonify(new ")).add(mark("value", "firstUpperCase")).add(literal("((")).add(mark("dsl")).add(literal("Box) box.owner()));")),
			rule().add((condition("trigger", "innerDisplay"))).add(literal("addAndPersonify(new ")).add(mark("value", "firstUpperCase")).add(literal("((box)));"))
		);
		return this;
	}
}