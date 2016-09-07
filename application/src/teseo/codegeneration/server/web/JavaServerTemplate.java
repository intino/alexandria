package teseo.codegeneration.server.web;

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
			rule().add((condition("type", "server"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport teseo.framework.web.TeseoServer;\nimport ")).add(mark("package", "validname")).add(literal(".resources.*;\nimport tara.magritte.Graph;\n\nimport static teseo.framework.actions.Router.Method.*;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Resources {\n\n\tpublic static void setup(TeseoServer server, Graph graph) {\n\t\t")).add(mark("resources").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "resource"))).add(literal("server.route(\"")).add(mark("path")).add(expression().add(literal("/:")).add(mark("parameters").multiple("/:"))).add(literal("\").as(")).add(mark("method", "firstUpperCase")).add(literal(").with(new ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Resource(graph));"))
		);
		return this;
	}
}