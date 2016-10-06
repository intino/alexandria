package io.intino.pandora.plugin.codegeneration.server.rest;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class RESTServerTemplate extends Template {

	protected RESTServerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RESTServerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "server"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport org.siani.pandora.server.PandoraSpark;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".resources.*;\nimport tara.magritte.Graph;\nimport org.siani.pandora.server.security.DefaultSecurityManager;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Resources {\n\n\tpublic static void setup(PandoraSpark server, Graph graph) {\n\t\t")).add(mark("secure")).add(literal("\n\t\t")).add(mark("resources").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "secure"))).add(literal("server.secure(new DefaultSecurityManager(new java.io.File(\"")).add(mark("file")).add(literal("\"), \"")).add(mark("password")).add(literal("\"));")),
			rule().add((condition("type", "resource"))).add(literal("server.route(\"")).add(mark("path")).add(literal("\").")).add(mark("method", "firstlowerCase")).add(literal("((manager) -> new ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Resource(graph, manager).execute());"))
		);
		return this;
	}
}