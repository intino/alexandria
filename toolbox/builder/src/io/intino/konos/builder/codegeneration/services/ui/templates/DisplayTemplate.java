package io.intino.konos.builder.codegeneration.services.ui.templates;

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
			rule().add((condition("type", "display & accessible"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(";\n\n")).add(mark("templatesImport")).add(literal("\n")).add(mark("blocksImport")).add(literal("\n")).add(mark("itemsImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("ProxyNotifier;\nimport io.intino.alexandria.ui.displays.ProxyDisplay;\nimport io.intino.alexandria.ui.services.push.UISession;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Proxy extends ProxyDisplay<")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("ProxyNotifier> {\n    ")).add(mark("parameter", "field").multiple("\n")).add(literal("\n\n    public ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Proxy(UISession session, String appUrl) {\n        super(session, appUrl, \"/")).add(mark("name", "SnakeCaseToCamelCase", "lowercase")).add(literal("proxy\");\n    }\n\n\t")).add(mark("request", "accessible").multiple("\n\n")).add(literal("\n\t")).add(mark("parameter", "method").multiple("\n\n")).add(literal("\n\n    @Override\n    protected Map<String, String> parameters() {\n        Map<String, String> result = new HashMap<>();\n        ")).add(mark("parameter", "map").multiple("\n")).add(literal("\n        return result;\n    }\n\n    @Override\n    protected void refreshBaseUrl(String url) {\n        notifier.refreshBaseUrl(url);\n    }\n\n    @Override\n    protected void refreshError(String error) {\n        notifier.refreshError(error);\n    }\n\n}")),
			rule().add((condition("type", "templatesImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.templates.*;")),
			rule().add((condition("type", "blocksImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.blocks.*;")),
			rule().add((condition("type", "itemsImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.items.*;")),
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(";\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(".Abstract")).add(mark("name", "firstUpperCase")).add(literal(";\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "firstUpperCase")).add(literal("<")).add(mark("box", "firstUpperCase")).add(literal("Box> {\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n        super(box);\n    }")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t"))).add(mark("request").multiple("\n\n")).add(literal("\n\t")).add(mark("parameter", "setter").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "request & asset"))).add(literal("public io.intino.alexandria.ui.spark.UIFile ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n    return null;\n}")),
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
			rule().add((condition("trigger", "import"))).add(literal("import ")).add(mark("package")).add(literal(".ui.displays.*;"))
		);
		return this;
	}
}