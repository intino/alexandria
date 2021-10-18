package io.intino.konos.builder.codegeneration.futures;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class FuturesServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("server"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".futures.*;\nimport io.intino.alexandria.core.Box;\n\npublic class Futures {\n\n\tprivate static Map<String, >\n\n\tpublic static AlexandriaSpark setup(AlexandriaSpark server, ")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(expression().output(mark("future").multiple("\n"))).output(literal("\n\t\treturn server;\n\t}\n\n\tpublic static Future loadFuture(")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box, String basePath, String id) {\n\t\tif(\n\t}\n\n\n\tprivate\n}")),
			rule().condition((type("resource"))).output(literal("server.route(")).output(mark("path", "format")).output(literal(").")).output(mark("method", "firstlowerCase")).output(literal("(manager -> loadFuture(box, manager.basePath(), manager.fromPath(\"id\")).execute());")),
			rule().condition((type("path")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", box.configuration().get(\"")).output(mark("value")).output(literal("\"))"))
		);
	}
}