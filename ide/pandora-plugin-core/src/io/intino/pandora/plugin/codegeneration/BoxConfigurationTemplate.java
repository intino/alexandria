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
			rule().add((condition("type", "boxConfiguration"))).add(literal("public class ")).add(mark("name")).add(literal("Configuration {\n\n")).add(mark("service", "field")).add(literal("\n\nservice...")).add(expression().add(literal("\n"))).add(literal("\n\n")).add(mark("service", "confClass")).add(literal("\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "field"))).add(mark("name", "firstUpperCase")).add(literal("Configuration ")).add(mark("name", "firstLowerCase")).add(literal("Configuration;")),
			rule().add((condition("type", "service & jms"))).add(literal("public ")).add(mark("configuration")).add(literal("Configuration ")).add(mark("name")).add(literal("Configuration(String url, String user, String password")).add(expression().add(literal(", ")).add(mark("customParameters").multiple(", "))).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration = new this.")).add(mark("name", "firstLowerCase")).add(literal("Configuration();\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.url = url;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.user = user;\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.password = password;\n\treturn this;\n}\n\npublic ")).add(mark("configuration")).add(literal("Configuration ")).add(mark("name")).add(literal("Configuration() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & rest"))).add(literal("public ")).add(mark("configuration")).add(literal("Configuration ")).add(mark("name")).add(literal("Configuration() {\n\treturn this;\n}\n\npublic ")).add(mark("configuration")).add(literal("Configuration ")).add(mark("name")).add(literal("Configuration() {\n\treturn ")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n}")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "confClass"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal("Configuration {\n\tString url;\n\tString user;\n\tString password;\n\t")).add(mark("customParameters").multiple("\n")).add(literal("\n}"))
		);
		return this;
	}
}