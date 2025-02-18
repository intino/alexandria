package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class RESTServiceTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("server")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.http.AlexandriaHttpServer;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".rest.resources.*;\nimport io.intino.alexandria.core.Box;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("Service {\n\n\t")).output(expression().output(placeholder("authenticator", "field"))).output(literal("\n\n\tpublic static AlexandriaHttpServer setup(AlexandriaHttpServer server, ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(expression().output(placeholder("authenticator", "assign"))).output(literal("\n\t\t")).output(expression().output(placeholder("authenticationWithCertificate"))).output(literal("\n\t\t")).output(expression().output(placeholder("hasNotifications"))).output(literal("\n\t\t")).output(expression().output(placeholder("notification").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("resource").multiple("\n"))).output(literal("\n\n\t\treturn server;\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("authenticator"), trigger("assign"))).output(literal("authenticator = new ")).output(placeholder("service", "pascalCase")).output(literal("ServiceAuthenticator(box);")));
		rules.add(rule().condition(all(allTypes("authenticator"), trigger("field"))).output(literal("private static ")).output(placeholder("service", "pascalCase")).output(literal("ServiceAuthenticator authenticator;")));
		rules.add(rule().condition(allTypes("authenticationWithCertificate")).output(literal("server.secure(new io.intino.alexandria.http.security.DefaultSecurityManager(new java.io.File(\"")).output(placeholder("file")).output(literal("\"), \"")).output(placeholder("password")).output(literal("\"));")));
		rules.add(rule().condition(trigger("notification")).output(literal("server.route(\"")).output(placeholder("path", "format")).output(literal("\").post(manager -> new ")).output(placeholder("package")).output(literal(".rest.notifications.")).output(placeholder("name", "pascalCase")).output(literal("Notification(box, manager).execute());")));
		rules.add(rule().condition(allTypes("resource")).output(literal("server.route(")).output(placeholder("path", "format")).output(literal(")")).output(expression().output(placeholder("authenticate"))).output(literal(".")).output(placeholder("method", "firstlowerCase")).output(literal("(manager -> new ")).output(placeholder("operation", "firstUpperCase")).output(placeholder("name", "pascalCase")).output(literal("Resource(box, manager).execute());")));
		rules.add(rule().condition(all(allTypes("bearer"), trigger("authenticate"))).output(literal(".before(manager -> { if (manager.fromHeader(\"Authorization\") == null || !authenticator.isAuthenticated(manager.fromHeader(\"Authorization\").replace(\"Bearer \", \"\"))) throw new io.intino.alexandria.exceptions.Unauthorized(\"Credential not found\");})")));
		rules.add(rule().condition(all(allTypes("basic"), trigger("authenticate"))).output(literal(".before(manager -> { if (manager.fromHeader(\"Authorization\") == null || !authenticator.isAuthenticated(manager.fromHeader(\"Authorization\").replace(\"Basic \", \"\"))) throw new io.intino.alexandria.exceptions.Unauthorized(\"Credential not found\");})")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("authenticate"))).output(literal(".before(manager -> { if (!authenticator.isAuthenticated(manager.request().queryParams().stream().collect(java.util.stream.Collectors.toMap(p -> p, p -> manager.request().queryParams(p))))) throw new io.intino.alexandria.exceptions.Unauthorized(\"Credential not found\");})")));
		rules.add(rule().condition(all(allTypes("path"), trigger("format"))).output(literal("\"")).output(placeholder("name")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))));
		rules.add(rule().condition(trigger("custom")).output(literal(".replace(\"{")).output(placeholder("value")).output(literal("}\", box.configuration().get(\"")).output(placeholder("value")).output(literal("\"))")));
		rules.add(rule().condition(trigger("hasnotifications")).output(literal("if (!io.intino.alexandria.http.AlexandriaHttpServerBuilder.isUI()) server.route(\"/_alexandria/push\").push(new io.intino.alexandria.http.pushservice.PushService());")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}