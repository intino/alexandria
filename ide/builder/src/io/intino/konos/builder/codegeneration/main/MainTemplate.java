package io.intino.konos.builder.codegeneration.main;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MainTemplate extends Template {

	protected MainTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MainTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\n")).add(expression().add(mark("tara", "empty")).add(literal("import io.intino.tara.magritte.Graph;"))).add(literal("\n\npublic class Main {\n\n\tpublic static void main(String[] args) {\n\t\t")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration = createConfigurationFromArgs(args);\n\t\t")).add(expression().add(mark("tara", "empty")).add(literal("Graph graph = Setup.initGraph(configuration);"))).add(literal("\n\t\t")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box = run(")).add(expression().add(mark("tara", "empty")).add(literal("graph, "))).add(literal("configuration);\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(() -> box.quit()));\n\t}\n\n\tprivate static ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration createConfigurationFromArgs(String[] args) {\n\t\treturn new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration(args)")).add(mark("conf", "set")).add(literal(";\n\t}\n\n\tprivate static ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box run(")).add(expression().add(mark("tara", "empty")).add(literal("Graph graph, "))).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(")).add(expression().add(mark("tara", "empty")).add(literal("graph, "))).add(literal("configuration);\n\t\tSetup.configureBox(box);\n\t\tbox.init();\n\t\tSetup.execute(box);\n\t\treturn box;\n\t}\n}")),
			rule().add((condition("type", "conf")), (condition("trigger", "set"))).add(mark("name", "firstlowercase")).add(literal("Configuration(")).add(mark("parameter", "name").multiple(", ")).add(literal(")")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(literal("info.")).add(mark("conf", "firstlowercase")).add(mark("name", "FirtsUpperCase")).add(literal(";")),
			rule().add((condition("type", "parameter")), (condition("trigger", "field"))).add(mark("type")).add(literal(" ")).add(mark("conf", "firstlowercase")).add(mark("name", "FirtsUpperCase")).add(literal(";")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("if (keyValue[0].equalsIgnoreCase(\"")).add(mark("conf", "firstlowercase")).add(literal(".")).add(mark("name", "FirtsUpperCase")).add(literal("\"))\n    ")).add(mark("conf", "firstlowercase")).add(mark("name", "FirtsUpperCase")).add(literal(" = ")).add(mark("type", "convert")).add(literal("(keyValue[1]);")),
			rule().add((condition("attribute", "Integer")), (condition("trigger", "convert"))).add(literal("Integer.parseInt")),
			rule().add((condition("attribute", "Boolean")), (condition("trigger", "convert"))).add(literal("Boolean.parseBoolean")),
			rule().add((condition("trigger", "convert"))),
			rule().add((condition("trigger", "empty")))
		);
		return this;
	}
}