package teseo.codegeneration.server;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JavaServerTemplate extends Template {

	protected JavaServerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JavaServerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "server"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport ForrestServer;\nimport ")).add(mark("package", "validname")).add(literal(".actions.*;\nimport tara.magritte.Graph;\n\nimport static Router.Method.*;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Actions {\n\n\tpublic static void fillActions(ForrestServer server, Graph graph) {\n\t\t")).add(mark("resources").multiple("\n")).add(literal("\n\t}\n\n}")),
			rule().add((condition("type", "resource"))).add(literal("server.route(\"")).add(mark("path")).add(expression().add(literal("/:")).add(mark("parameters").multiple("/:"))).add(literal("\").as(")).add(mark("method", "firstUpperCase")).add(literal(").with(new ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Action(graph));"))
		);
		return this;
	}
}