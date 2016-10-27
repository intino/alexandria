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
			rule().add((condition("type", "boxConfiguration"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(literal("import tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\n\npublic class ")).add(mark("name")).add(literal("Configuration {\n\n\t")).add(mark("service", "field").multiple("\n")).add(literal("\n\n\t")).add(mark("service").multiple("\n\n")).add(literal("\n\t")).add(mark("service", "confClass").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "field"))).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration;")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\tString url;\n\tString user;\n\tString password;\n\t")).add(mark("customParameters").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\tint port;\n\tString webDirectory;\n\t")).add(mark("customParameters").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & jms"))).add(literal("public ")).add(mark("configuration", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(String url, String user, String password")).add(expression().add(literal(", ")).add(mark("customParameters").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.url = url;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.user = user;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.password = password;\n\treturn this;\n}\n\npublic ")).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & rest"))).add(literal("public ")).add(mark("configuration", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration(int port, String webDirectory")).add(expression().add(literal(", ")).add(mark("customParameters").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.port = port;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.webDirectory = webDirectory;\n\treturn this;\n}\n\npublic ")).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration() {\n\treturn ")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}"))
		);
		return this;
	}
}