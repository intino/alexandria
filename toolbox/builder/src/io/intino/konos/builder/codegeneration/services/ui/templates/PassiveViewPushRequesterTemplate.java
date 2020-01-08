package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewPushRequesterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.displays.requesters;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(mark("packageType")).output(literal("s."))).output(mark("name", "FirstUpperCase")).output(literal(";\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".*;\n")).output(mark("schemaImport")).output(literal("\n\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.rest.spark.SparkReader;\nimport io.intino.alexandria.ui.services.push.UIClient;\nimport io.intino.alexandria.ui.services.push.UIMessage;\nimport io.intino.alexandria.ui.spark.UISparkManager;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("PushRequester extends io.intino.alexandria.ui.displays.requesters.")).output(expression().output(mark("type", "class", "FirstUpperCase"))).output(literal("PushRequester {\n\n\tpublic void execute(UIClient client, UIMessage message) {\n\t\t")).output(mark("name", "firstUpperCase")).output(literal(" display = display(client, message);\n\t\tif (display == null) return;\n\t\tString operation = operation(message);\n\t\tString data = data(message);\n\n\t\t")).output(expression().output(mark("request").multiple("\nelse "))).output(literal("\n\n\t\tsuper.execute(client, message);\n\t}\n\n}")),
			rule().condition((attribute("", "Display")), (trigger("class"))).output(literal("Display")),
			rule().condition((allTypes("request","file")), (trigger("request"))),
			rule().condition((allTypes("request","asset")), (trigger("request"))),
			rule().condition((type("request")), (trigger("request"))).output(literal("if (operation.equals(\"")).output(mark("name")).output(literal("\")) {\n\tdisplay.")).output(mark("name")).output(literal("(")).output(mark("parameter")).output(literal(");\n\treturn;\n}")),
			rule().condition((type("list")), (trigger("parameter"))).output(literal("SparkReader.read(java.net.URLEncoder.encode(data, java.nio.charset.StandardCharsets.UTF_8), ")).output(mark("value")).output(literal("[].class)")),
			rule().condition((allTypes("parameter","file")), (trigger("parameter"))),
			rule().condition((type("parameter")), (trigger("parameter"))).output(literal("SparkReader.read(java.net.URLEncoder.encode(data, java.nio.charset.StandardCharsets.UTF_8), ")).output(mark("value")).output(literal(".class)")),
			rule().condition((trigger("parameter"))),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}