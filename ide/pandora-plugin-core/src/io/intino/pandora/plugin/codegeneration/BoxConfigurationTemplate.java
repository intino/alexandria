package io.intino.pandora.plugin.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BoxConfigurationTemplate extends Template {

	protected BoxConfigurationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxConfigurationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "boxConfiguration"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(literal("import tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\n\t")).add(mark("service", "field").multiple("\n")).add(literal("\n\n\t")).add(mark("service").multiple("\n\n")).add(literal("\n\n\t")).add(mark("service", "confClass").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "field"))).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration;")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\tpublic String url;\n\tpublic String user;\n\tpublic String password;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & channel")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\tpublic String url;\n\tpublic String user;\n\tpublic String password;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\tpublic int port;\n\tpublic String webDirectory;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & jms"))).add(literal("public ")).add(mark("configuration", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(String url, String user, String password")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.url = url;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.user = user;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.password = password;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & channel"))).add(literal("public ")).add(mark("configuration", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(String url, String user, String password")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.url = url;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.user = user;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.password = password;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & rest"))).add(literal("public ")).add(mark("configuration", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(int port, String webDirectory")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.port = port;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.webDirectory = webDirectory;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("configuration", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(int port")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\treturn ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(port, \"www/\"")).add(expression().add(literal(", ")).add(mark("custom", "name").multiple(", "))).add(literal(");\n}\n\npublic ")).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration() {\n\treturn ")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name")),
			rule().add((condition("type", "custom")), (condition("trigger", "name"))).add(mark("name")),
			rule().add((condition("type", "custom")), (condition("trigger", "field"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "custom")), (condition("trigger", "assign"))).add(literal("this.")).add(mark("conf", "firstLowerCase")).add(literal("Configuration.")).add(mark("name")).add(literal(" = ")).add(mark("name")).add(literal(";"))
		);
		return this;
	}
}