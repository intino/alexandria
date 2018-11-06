package io.intino.konos.builder.codegeneration;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class BoxConfigurationTemplate extends Template {

	protected BoxConfigurationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxConfigurationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "boxConfiguration"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.Map;\nimport java.util.HashMap;\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration extends ")).add(expression().add(mark("parent")).add(literal("Configuration")).or(expression().add(literal("io.intino.alexandria.core.BoxConfiguration")))).add(literal(" {\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration(String[] args) {\n\t\tsuper(args);\n\t\t")).add(expression().add(literal("if (store == null) {")).add(literal("\n")).add(literal("\t\t\tif (this.args.get(\"graph_store\") != null)")).add(literal("\n")).add(literal("\t\t\t\t")).add(mark("tara", "empty")).add(literal("store = new java.io.File(this.args.get(\"graph_store\"));")).add(literal("\n")).add(literal("\t\t\telse ")).add(mark("tara", "empty")).add(literal("store = new java.io.File(\"./store\");")).add(literal("\n")).add(literal("\t\t}"))).add(literal("\n\t}\n\n\tpublic String get(String key) {\n\t\treturn args.get(key);\n\t}\n\n\t")).add(expression().add(mark("tara", "empty")).add(literal("public java.io.File store() {")).add(literal("\n")).add(literal("\t\treturn this.store;")).add(literal("\n")).add(literal("\t}"))).add(literal("\n\n\tpublic static java.net.URL url(String url) {\n\t\ttry {\n\t\treturn new java.net.URL(url);\n\t\t} catch (java.net.MalformedURLException e) {\n\t\t\treturn null;\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("name")).add(literal("}\", ")).add(mark("name", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("type", "custom")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "validname", "firstLowerCase")),
			rule().add((condition("type", "custom")), (condition("trigger", "name"))).add(mark("name", "validname", "firstLowerCase")),
			rule().add((condition("type", "custom")), (condition("trigger", "field"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name", "validname", "firstLowerCase")).add(literal(" = \"\";")),
			rule().add((condition("type", "custom")), (condition("trigger", "assign"))).add(literal("this.")).add(mark("conf", "validname", "firstLowerCase")).add(literal("Configuration.")).add(mark("name", "validname", "firstLowerCase")).add(literal(" = ")).add(mark("name", "validname", "firstLowerCase")).add(literal(";")),
			rule().add((condition("type", "custom")), (condition("trigger", "parameter"))).add(literal("args.get(\"")).add(mark("conf", "firstLowerCase")).add(literal("_")).add(mark("name", "validname", "firstLowerCase")).add(literal("\")")),
			rule().add((condition("trigger", "empty")))
		);
		return this;
	}
}