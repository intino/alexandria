package io.intino.konos.builder.codegeneration.accessor.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RESTAccessorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("accessor"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.net.URL;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.restaccessor.core.RestAccessorNotifier;\nimport io.intino.alexandria.restaccessor.exceptions.RestfulFailure;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(mark("<missing ID>")).output(literal("Accessor {\n\tprivate final URL url;\n\tprivate int timeoutMillis = 120 * 1000;\n\tprivate final RestAccessorNotifier notifier = new RestAccessorNotifier();\n\t")).output(mark("authentication", "field")).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Accessor(URL url")).output(expression().output(literal(", ")).output(mark("authentication", "parameter"))).output(literal(") {\n\t\tthis.url = url;\n\t\t")).output(mark("authentication", "assign")).output(literal("\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Accessor(URL url, int timeoutMillis")).output(expression().output(literal(", ")).output(mark("authentication", "parameter"))).output(literal(") {\n\t\tthis.url = url;\n\t\tthis.timeoutMillis = timeoutMillis;\n\t\t")).output(mark("authentication", "assign")).output(literal("\n\t}\n\n\t")).output(mark("resource").multiple("\n\n")).output(literal("\n\n\t")).output(mark("notification").multiple("\n\n")).output(literal("\n}")),
				rule().condition((type("resource"))).output(literal("public ")).output(mark("response")).output(literal(" ")).output(mark("method", "firstLowerCase")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") ")).output(mark("exceptionResponses", "declaration")).output(literal(" {\n\ttry {\n\t\tio.intino.alexandria.restaccessor.Response response = new io.intino.alexandria.restaccessor.RestQueryBuilder(this.url).timeOut(this.timeoutMillis)")).output(expression().output(literal(".")).output(mark("auth"))).output(expression().output(literal(".")).output(mark("parameter").multiple("."))).output(expression().output(literal(".")).output(mark("body").multiple("."))).output(expression().output(literal(".")).output(mark("resource").multiple("."))).output(literal(".")).output(mark("method", "lowercase")).output(literal("(")).output(mark("path")).output(literal(");\n\t\t")).output(mark("response", "return")).output(literal("\n\t} catch (AlexandriaException e) {\n\t\t")).output(mark("exceptionResponses", "throws")).output(literal("\n\t}\n}")),
				rule().condition((allTypes("parameter", "query")), (trigger("parameter"))).output(literal("queryParameter(\"")).output(mark("name")).output(literal("\", ")).output(mark("name")).output(literal(")")),
				rule().condition((allTypes("parameter", "header")), (trigger("parameter"))).output(literal("headerParameter(\"")).output(mark("name")).output(literal("\", ")).output(mark("name")).output(literal(")")),
				rule().condition((type("list")), (trigger("return"))).output(literal("return io.intino.alexandria.restaccessor.adapters.ResponseAdapter.adapt(response.content(), new com.google.gson.reflect.TypeToken<Array")).output(mark("value")).output(literal(">(){}.getType());")),
				rule().condition(not(type("list")), not(attribute("value", "void")), (trigger("return"))).output(literal("return io.intino.alexandria.restaccessor.adapters.ResponseAdapter.adapt(response.content(), ")).output(mark("value")).output(literal(".class);")),
				rule().condition((type("file")), (trigger("return"))).output(literal("return io.intino.alexandria.Resource(\"content\", response.contentAsStream());")),
				rule().condition((trigger("return"))),
				rule().condition((trigger("response"))).output(mark("value")),
				rule().condition((type("notification"))).output(literal("public void listen")).output(mark("name", "SnakeCaseToCamelCase", "firstUppercase")).output(literal("(")).output(expression().output(mark("parameter", "signature").multiple(", ")).output(literal(", "))).output(literal("java.util.function.Consumer<String> listener) throws InternalServerError {\n\ttry {\n\t\tthis.notifier.listen(listener, new io.intino.alexandria.restaccessor.RestQueryBuilder(this.url).timeOut(this.timeoutMillis)")).output(expression().output(literal(".")).output(mark("auth"))).output(expression().output(literal(".")).output(mark("parameter").multiple("."))).output(literal(".post(\"")).output(mark("path")).output(literal("\").content().trim());\n\t} catch (AlexandriaException e) {\n\t\tthrow new InternalServerError(e.message());\n\t}\n}\n\npublic void stopListen")).output(mark("name", "SnakeCaseToCamelCase", "firstUppercase")).output(literal("() {\n\tthis.notifier.close();\n}")),
				rule().condition((type("basic")), (trigger("auth"))).output(literal("basicAuth(this.user, this.pasword)")),
				rule().condition((type("bearer")), (trigger("auth"))).output(literal("bearerAuth(this.token)")),
				rule().condition((allTypes("authentication", "basic")), (trigger("parameter"))).output(literal("String user, String password")),
				rule().condition((allTypes("authentication", "bearer")), (trigger("parameter"))).output(literal("String token")),
				rule().condition((allTypes("authentication", "withCertificate")), (trigger("parameter"))).output(literal("URL certificate")),
				rule().condition((allTypes("authentication", "withCertificate")), (trigger("assign"))).output(literal("this.certificate = certificate;")),
				rule().condition((allTypes("authentication", "basic")), (trigger("assign"))).output(literal("this.user = user;\nthis.password = password;")),
				rule().condition((allTypes("authentication", "bearer")), (trigger("assign"))).output(literal("this.token = token;")),
				rule().condition((allTypes("authentication", "withCertificate")), (trigger("assign"))).output(literal("this.certificate = certificate;")),
				rule().condition((type("authentication")), (type("basic")), (trigger("field"))).output(literal("private final String user;\nprivate final String password;")),
				rule().condition((type("authentication")), (type("bearer")), (trigger("field"))).output(literal("private final String token;")),
				rule().condition((allTypes("authentication", "withCertificate")), (trigger("field"))).output(literal("private final URL certificate;")),
				rule().condition((type("parameter")), (trigger("signature"))).output(mark("parameterType")).output(literal(" ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
				rule().condition((allTypes("exceptionResponses", "none")), (trigger("throws"))).output(literal("throw new InternalServerError(e.message());")),
				rule().condition((type("exceptionResponses")), (trigger("throws"))).output(mark("exceptionResponse", "throws").multiple("\nelse ")).output(literal("\nthrow new InternalServerError(e.message());")),
				rule().condition((type("exceptionResponse")), (trigger("throws"))).output(literal("if (e instanceof ")).output(mark("exceptionName")).output(literal(") throw ((")).output(mark("exceptionName")).output(literal(") e);")),
				rule().condition((type("exceptionResponses")), (trigger("declaration"))).output(literal("throws ")).output(expression().output(mark("exceptionResponse", "declaration").multiple(", ")).output(literal(","))).output(literal(" InternalServerError")),
				rule().condition((type("exceptionResponse")), (trigger("declaration"))).output(mark("exceptionName")),
				rule().condition((type("schemaImport")), (trigger("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}