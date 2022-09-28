package io.intino.konos.builder.codegeneration.accessor.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RESTAccessorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("accessor"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport java.util.List;\nimport java.util.Map;\nimport java.util.HashMap;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.net.URL;\nimport java.lang.reflect.Type;\nimport com.google.gson.JsonSerializer;\nimport com.google.gson.JsonDeserializer;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.restaccessor.RequestBuilder;\nimport io.intino.alexandria.restaccessor.core.RestAccessorNotifier;\nimport io.intino.alexandria.restaccessor.exceptions.RestfulFailure;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(mark("<missing ID>")).output(literal("Accessor {\n\tprivate final URL url;\n\tprivate final RestAccessorNotifier notifier = new RestAccessorNotifier();\n\tprivate int timeoutMillis = 120 * 1_000;\n\tprivate io.intino.alexandria.restaccessor.OutBox outBox = null;\n\tprivate Map<String, String> additionalHeaders = new HashMap<>();\n\t")).output(mark("authentication", "field")).output(literal("\n\t")).output(mark("enumParameter").multiple("\n\n")).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("Accessor(URL url")).output(expression().output(literal(", ")).output(mark("authentication", "parameter"))).output(literal(") {\n\t\tthis.url = url;\n\t\t")).output(mark("authentication", "assign")).output(literal("\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("Accessor(URL url, int timeoutMillis")).output(expression().output(literal(", ")).output(mark("authentication", "parameter"))).output(literal(") {\n\t\tthis.url = url;\n\t\tthis.timeoutMillis = timeoutMillis;\n\t\t")).output(mark("authentication", "assign")).output(literal("\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("Accessor(URL url, int timeoutMillis, java.io.File outBoxDirectory, int intervalRetrySeconds")).output(expression().output(literal(", ")).output(mark("authentication", "parameter"))).output(literal(") {\n\t\tthis.url = url;\n\t\tthis.timeoutMillis = timeoutMillis;\n\t\tthis.outBox = new io.intino.alexandria.restaccessor.OutBox(outBoxDirectory, intervalRetrySeconds);\n\t\t")).output(mark("authentication", "assign")).output(literal("\n\t}\n\n\tpublic void addCommonHeader(String name, String value) {\n\t\tadditionalHeaders.put(name, value);\n\t}\n\n\tpublic void addRequestSerializer(Type type, JsonSerializer<?> adapter) {\n\t\tio.intino.alexandria.restaccessor.adapters.RequestAdapter.addCustomAdapter(type, adapter);\n\t}\n\n\tpublic void addResponseDeserializer(Type type, JsonDeserializer<?> adapter) {\n\t\tio.intino.alexandria.restaccessor.adapters.ResponseAdapter.addCustomAdapter(type, adapter);\n\t}\n\n\t")).output(mark("resource").multiple("\n\n")).output(literal("\n\n\t")).output(mark("notification").multiple("\n\n")).output(literal("\n}")),
			rule().condition((type("resource"))).output(literal("public ")).output(mark("response")).output(literal(" ")).output(mark("method", "firstLowerCase")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") ")).output(mark("exceptionResponses", "declaration")).output(literal(" {\n\tRequestBuilder builder = new RequestBuilder(this.url).timeOut(this.timeoutMillis);\n\tadditionalHeaders.forEach((k,v) -> builder.headerParameter(k,v));\n\tRequestBuilder.Request request = builder\n\t\t")).output(expression().output(literal(".")).output(mark("auth"))).output(literal("\n\t\t")).output(expression().output(literal(".")).output(mark("parameter").multiple("\n."))).output(literal("\n\t\t.build(RequestBuilder.Method.")).output(mark("method", "upperCase")).output(literal(", ")).output(mark("path")).output(literal(");\n\ttry {\n\t\tio.intino.alexandria.restaccessor.Response response = request.execute();\n\t\t")).output(expression().output(mark("response", "return"))).output(literal("\n\t} catch (AlexandriaException e) {\n\t\t")).output(mark("exceptionResponses", "throws")).output(literal("\n\t\tif (outBox != null) outBox.push(request);\n\t\tthrow new InternalServerError(e.message());\n\t}\n}")),
			rule().condition((allTypes("parameter","query","word")), (trigger("parameter"))).output(literal("queryParameter(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".name())")),
			rule().condition((allTypes("parameter","query")), (trigger("parameter"))).output(literal("queryParameter(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(")")),
			rule().condition((allTypes("parameter","header","word")), (trigger("parameter"))).output(literal("headerParameter(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".name())")),
			rule().condition((allTypes("parameter","header")), (trigger("parameter"))).output(literal("headerParameter(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(")")),
			rule().condition(not(type("file")), (allTypes("parameter","body")), (trigger("parameter"))).output(literal("entityPart(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(")")),
			rule().condition(not(type("file")), (allTypes("parameter","form")), (trigger("parameter"))).output(literal("entityPart(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(")")),
			rule().condition((type("file")), (type("parameter")), (trigger("parameter"))).output(literal("entityPart(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(")")),
			rule().condition((trigger("enumparameter"))).output(literal("public enum ")).output(mark("class", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("value").multiple(", ")).output(literal("\n}")),
			rule().condition((type("list")), (trigger("return"))).output(literal("return io.intino.alexandria.restaccessor.adapters.ResponseAdapter.adapt(response.content(), new com.google.gson.reflect.TypeToken<Array")).output(mark("value")).output(literal(">(){}.getType());")),
			rule().condition((type("file")), (trigger("return"))).output(literal("String filename = !response.headers().containsKey(\"Content-Disposition\") ? \"filename=content\":\n\tArrays.stream(response.headers().get(\"Content-Disposition\").split(\";\")).filter(c-> c.startsWith(\"filename\")).findFirst().orElse(null);\nreturn new io.intino.alexandria.Resource(filename.split(\"=\")[1], response.contentType(), response.contentAsStream());")),
			rule().condition(not(type("list")), not(attribute("value", "void")), (trigger("return"))).output(literal("return io.intino.alexandria.restaccessor.adapters.ResponseAdapter.adapt(response.content(), ")).output(mark("value")).output(literal(".class);")),
			rule().condition((trigger("return"))),
			rule().condition((trigger("response"))).output(mark("value")),
			rule().condition((type("notification"))).output(literal("public void listen")).output(mark("name", "snakeCaseToCamelCase", "firstUppercase")).output(literal("(")).output(expression().output(mark("parameter", "signature").multiple(", ")).output(literal(", "))).output(literal("java.util.function.Consumer<String> listener) throws InternalServerError {\n\tRequestBuilder.Request request = new RequestBuilder(this.url).timeOut(this.timeoutMillis)\n\t\t")).output(expression().output(literal(".")).output(mark("auth"))).output(literal("\n\t\t")).output(expression().output(literal(".")).output(mark("parameter").multiple("."))).output(literal("\n\t\t.build(RequestBuilder.Method.POST, \"")).output(mark("path")).output(literal("\");\n\ttry {\n\t\tthis.notifier.listen(listener, request.execute().content().trim());\n\t} catch (AlexandriaException e) {\n\t\tthrow new InternalServerError(e.message());\n\t}\n}\n\npublic void stopListen")).output(mark("name", "snakeCaseToCamelCase", "firstUppercase")).output(literal("() {\n\tthis.notifier.close();\n}")),
			rule().condition((type("basic")), (trigger("auth"))).output(literal("basicAuth(this.user, this.pasword)")),
			rule().condition((type("bearer")), (trigger("auth"))).output(literal("bearerAuth(this.token)")),
			rule().condition((allTypes("authentication","basic")), (trigger("parameter"))).output(literal("String user, String password")),
			rule().condition((allTypes("authentication","bearer")), (trigger("parameter"))).output(literal("String token")),
			rule().condition((allTypes("authentication","withCertificate")), (trigger("parameter"))).output(literal("URL certificate")),
			rule().condition((allTypes("authentication","withCertificate")), (trigger("assign"))).output(literal("this.certificate = certificate;")),
			rule().condition((allTypes("authentication","basic")), (trigger("assign"))).output(literal("this.user = user;\nthis.password = password;")),
			rule().condition((allTypes("authentication","bearer")), (trigger("assign"))).output(literal("this.token = token;")),
			rule().condition((allTypes("authentication","withCertificate")), (trigger("assign"))).output(literal("this.certificate = certificate;")),
			rule().condition((type("authentication")), (type("basic")), (trigger("field"))).output(literal("private final String user;\nprivate final String password;")),
			rule().condition((type("authentication")), (type("bearer")), (trigger("field"))).output(literal("private final String token;")),
			rule().condition((allTypes("authentication","withCertificate")), (trigger("field"))).output(literal("private final URL certificate;")),
			rule().condition((type("parameter")), (trigger("signature"))).output(mark("parameterType")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((type("exceptionResponses")), (trigger("throws"))).output(mark("exceptionResponse", "throws").multiple("\nelse ")),
			rule().condition((type("exceptionResponse")), (trigger("throws"))).output(literal("if (e instanceof ")).output(mark("exceptionName")).output(literal(") throw ((")).output(mark("exceptionName")).output(literal(") e);")),
			rule().condition((type("exceptionResponses")), (trigger("declaration"))).output(literal("throws ")).output(expression().output(mark("exceptionResponse", "declaration").multiple(", ")).output(literal(","))).output(literal(" InternalServerError")),
			rule().condition((type("exceptionResponse")), (trigger("declaration"))).output(mark("exceptionName")),
			rule().condition((type("schemaImport")), (trigger("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}