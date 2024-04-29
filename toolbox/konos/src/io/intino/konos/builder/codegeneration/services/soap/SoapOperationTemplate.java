package io.intino.konos.builder.codegeneration.services.soap;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SoapOperationTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("operation"))).output(literal("package ")).output(mark("package")).output(literal(".soap.operations;\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package")).output(literal(".*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.http.spark.SparkManager;\nimport io.intino.alexandria.http.spark.SparkPushService;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("Operation {\n\n\tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\tprivate SparkManager<SparkPushService> manager;\n\t")).output(expression().output(mark("authenticationValidator", "field"))).output(literal("\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("Operation(")).output(mark("box", "FirstUpperCase")).output(literal("Box box, SparkManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t\t")).output(mark("authenticationValidator", "assign")).output(literal("\n\t}\n\n\tpublic void execute() ")).output(expression().output(literal("throws ")).output(mark("throws").multiple(", "))).output(literal(" {\n\t\t")).output(mark("returnType", "methodCall")).output(literal("fill(new ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Action()).execute()")).output(expression().output(mark("returnType", "ending"))).output(literal(";\n\t}\n\n\tprivate ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Action fill(")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();\n\t\t")).output(expression().output(mark("input", "assign"))).output(literal("\n\t\treturn action;\n\t}\n\n\t")).output(expression().output(mark("returnType", "method"))).output(literal("\n\n\tprivate io.intino.alexandria.http.spark.SparkContext context() {\n\t\tio.intino.alexandria.http.spark.SparkContext context = new io.intino.alexandria.http.spark.SparkContext(manager);\n\t\t")).output(mark("authenticationValidator", "put")).output(literal("\n\t\tcontext.put(\"ip\", manager.ip());\n\t\treturn context;\n\t}\n}")),
				rule().condition((trigger("methodcall"))).output(literal("write(")),
				rule().condition((trigger("ending"))).output(literal(")")),
				rule().condition((attribute("void")), (trigger("write"))),
				rule().condition((trigger("method"))).output(literal("private void write(")).output(mark("value", "firstUpperCase", "ReturnTypeFormatter")).output(literal(" object) {\n\tmanager.write(new io.intino.alexandria.Soap().writeEnvelope(object, \"")).output(mark("xmlns")).output(literal("\"), \"text/xml\");\n}")),
				rule().condition((type("input")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new io.intino.alexandria.Soap().readEnvelope(manager.body()).body().schema(")).output(mark("type")).output(literal(".class);")),
				rule().condition((type("authenticationValidator")), (trigger("field"))).output(literal("io.intino.alexandria.http.security.")).output(mark("type", "FirstUpperCase")).output(literal("AuthenticationValidator validator;")),
				rule().condition((type("authenticationValidator")), (trigger("assign"))).output(literal("this.validator = box.authenticationValidator();")),
				rule().condition((type("authenticationValidator")), (trigger("validate"))).output(literal("String auth = manager.fromHeader(\"Authorization\", String.class);\nif (auth == null || !validator.validate(auth.replace(\"Basic \", \"\"))) throw new Unauthorized(\"Credential not found\");")),
				rule().condition((type("authenticationValidator")), (trigger("put"))).output(literal("context.put(\"auth\", manager.fromHeader(\"Authorization\", String.class).replace(\"Basic \", \"\"));")),
				rule().condition((trigger("parametertype"))).output(mark("value")).output(literal(".class")),
				rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}