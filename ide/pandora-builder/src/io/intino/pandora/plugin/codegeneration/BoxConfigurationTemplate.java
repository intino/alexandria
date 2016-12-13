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
			rule().add((condition("type", "boxConfiguration"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(literal("import tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(expression().add(literal("extends ")).add(mark("parentPackage")).add(literal(".pandora.")).add(mark("parent", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration"))).add(literal(" {\n\n\t")).add(mark("service", "field").multiple("\n")).add(literal("\n\n\t")).add(mark("service").multiple("\n\n")).add(literal("\n\n\t")).add(mark("service", "confClass").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "field"))).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration;")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration {\n\tpublic String url;\n\tpublic String user;\n\tpublic String password;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & channel")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration {\n\tpublic String url;\n\tpublic String user;\n\tpublic String password;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n\t")).add(expression().add(literal("public String clientID = \"")).add(mark("clientID")).add(literal("\""))).add(expression().add(mark("custom", "replace").multiple(""))).add(literal(";\n}")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration {\n\tpublic int port;\n}")),
			rule().add((condition("type", "service & activity")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration {\n\tpublic int port;\n\tpublic String webDirectory;\n\tpublic io.intino.pandora.server.activity.services.AuthService authService;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n\n\t")).add(expression().add(literal("public java.net.URL authServiceUrl() {")).add(literal("\n")).add(literal("\t\ttry {")).add(literal("\n")).add(literal("\t\t\treturn new java.net.URL(\"")).add(mark("auth")).add(literal("\"")).add(mark("custom", "replace").multiple("")).add(literal(");")).add(literal("\n")).add(literal("\t\t} catch (java.net.MalformedURLException e) {")).add(literal("\n")).add(literal("\t\t\treturn null;")).add(literal("\n")).add(literal("\t\t}")).add(literal("\n")).add(literal("\t}"))).add(literal("\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("name")).add(literal("}\", ")).add(mark("name", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration {\n\tpublic int port;\n\tpublic String webDirectory;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "service & jms"))).add(literal("public ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(String url, String user, String password")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.url = url;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.user = user;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.password = password;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & jmx"))).add(literal("public ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(int port) {\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port = port;\n\treturn this;\n}\n\npublic ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & channel"))).add(literal("public ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(String url, String user, String password")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.url = url;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.user = user;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.password = password;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "durable")), (condition("trigger", "assign"))).add(expression().add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.clientID "))),
			rule().add((condition("type", "service & rest"))).add(literal("public ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(int port, String webDirectory")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port = port;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory = webDirectory;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(int port")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\treturn ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(port, \"www/\"")).add(expression().add(literal(", ")).add(mark("custom", "name").multiple(", "))).add(literal(");\n}\n\npublic ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration() {\n\treturn ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & activity"))).add(literal("public ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(int port, String webDirectory, io.intino.pandora.server.activity.services.AuthService authService")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration = new ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port = port;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory = webDirectory;\n\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.authService = authService;\n\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\treturn this;\n}\n\npublic ")).add(mark("configuration", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(int port, io.intino.pandora.server.activity.services.AuthService authService")).add(expression().add(literal(", ")).add(mark("custom", "signature").multiple(", "))).add(literal(") {\n\treturn ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration(port, \"www/\", authService")).add(expression().add(literal(", ")).add(mark("custom", "name").multiple(", "))).add(literal(");\n}\n\npublic ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration() {\n\treturn ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "validname", "firstLowerCase")),
			rule().add((condition("type", "custom")), (condition("trigger", "name"))).add(mark("name", "validname", "firstLowerCase")),
			rule().add((condition("type", "custom")), (condition("trigger", "field"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name", "validname", "firstLowerCase")).add(literal(";")),
			rule().add((condition("type", "custom")), (condition("trigger", "assign"))).add(literal("this.")).add(mark("conf", "validname", "firstLowerCase")).add(literal("Configuration.")).add(mark("name", "validname", "firstLowerCase")).add(literal(" = ")).add(mark("name", "validname", "firstLowerCase")).add(literal(";"))
		);
		return this;
	}
}