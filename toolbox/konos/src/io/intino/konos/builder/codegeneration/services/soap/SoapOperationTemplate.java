package io.intino.konos.builder.codegeneration.services.soap;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class SoapOperationTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("operation")).output(literal("package ")).output(placeholder("package")).output(literal(".soap.operations;\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package")).output(literal(".*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.http.server.AlexandriaHttpManager;\nimport io.intino.alexandria.http.pushservice.PushService;\n\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Operation {\n\n\tprivate ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\tprivate AlexandriaHttpManager<PushService<?,?>> manager;\n\t")).output(expression().output(placeholder("authenticationValidator", "field"))).output(literal("\n\n\tpublic ")).output(placeholder("name", "PascalCase")).output(literal("Operation(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box, AlexandriaHttpManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t\t")).output(placeholder("authenticationValidator", "assign")).output(literal("\n\t}\n\n\tpublic void execute() ")).output(expression().output(literal("throws ")).output(placeholder("throws").multiple(", "))).output(literal(" {\n\t\t")).output(placeholder("returnType", "methodCall")).output(literal("fill(new ")).output(placeholder("package", "validPackage")).output(literal(".actions.")).output(placeholder("operation", "firstUpperCase")).output(placeholder("name", "firstUpperCase")).output(literal("Action()).execute()")).output(expression().output(placeholder("returnType", "ending"))).output(literal(";\n\t}\n\n\tprivate ")).output(placeholder("package", "validPackage")).output(literal(".actions.")).output(placeholder("operation", "firstUpperCase")).output(placeholder("name", "firstUpperCase")).output(literal("Action fill(")).output(placeholder("package", "validPackage")).output(literal(".actions.")).output(placeholder("operation", "firstUpperCase")).output(placeholder("name", "firstUpperCase")).output(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();\n\t\t")).output(expression().output(placeholder("input", "assign"))).output(literal("\n\t\treturn action;\n\t}\n\n\t")).output(expression().output(placeholder("returnType", "method"))).output(literal("\n\n\tprivate io.intino.alexandria.http.server.AlexandriaHttpContext context() {\n\t\tio.intino.alexandria.http.server.AlexandriaHttpContext context = new io.intino.alexandria.http.server.AlexandriaHttpContext(manager);\n\t\t")).output(placeholder("authenticationValidator", "put")).output(literal("\n\t\tcontext.put(\"ip\", manager.ip());\n\t\treturn context;\n\t}\n}\n")));
		rules.add(rule().condition(trigger("methodcall")).output(literal("write(")));
		rules.add(rule().condition(trigger("ending")).output(literal(")")));
		rules.add(rule().condition(all(attribute("void"), trigger("write"))));
		rules.add(rule().condition(trigger("method")).output(literal("private void write(")).output(placeholder("value", "firstUpperCase", "ReturnTypeFormatter")).output(literal(" object) {\n\tmanager.write(new io.intino.alexandria.Soap().writeEnvelope(object, \"")).output(placeholder("xmlns")).output(literal("\"), \"text/xml\");\n}")));
		rules.add(rule().condition(all(allTypes("input"), trigger("assign"))).output(literal("action.")).output(placeholder("name", "CamelCase")).output(literal(" = new io.intino.alexandria.Soap().readEnvelope(manager.body()).body().schema(")).output(placeholder("type")).output(literal(".class);")));
		rules.add(rule().condition(all(allTypes("authenticationValidator"), trigger("field"))).output(literal("io.intino.alexandria.http.security.")).output(placeholder("type", "FirstUpperCase")).output(literal("AuthenticationValidator validator;")));
		rules.add(rule().condition(all(allTypes("authenticationValidator"), trigger("assign"))).output(literal("this.validator = box.authenticationValidator();")));
		rules.add(rule().condition(all(allTypes("authenticationValidator"), trigger("validate"))).output(literal("String auth = manager.fromHeader(\"Authorization\", String.class);\nif (auth == null || !validator.validate(auth.replace(\"Basic \", \"\"))) throw new Unauthorized(\"Credential not found\");")));
		rules.add(rule().condition(all(allTypes("authenticationValidator"), trigger("put"))).output(literal("context.put(\"auth\", manager.fromHeader(\"Authorization\", String.class).replace(\"Basic \", \"\"));")));
		rules.add(rule().condition(trigger("parametertype")).output(placeholder("value")).output(literal(".class")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}