package io.intino.konos.builder.codegeneration.accessor.rest;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class RESTAccessorTemplate extends Template {

	protected RESTAccessorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RESTAccessorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "accessor"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport java.net.URL;\nimport io.intino.konos.alexandria.exceptions.*;\nimport io.intino.konos.alexandria.schema.Resource;\nimport io.intino.konos.restful.core.RestfulAccessor;\nimport io.intino.konos.restful.exceptions.RestfulFailure;\nimport com.google.gson.Gson;\nimport com.google.gson.GsonBuilder;\nimport com.google.gson.reflect.TypeToken;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\n\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Accessor {\n\tprivate static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);\n\n\tprivate URL url;\n\tprivate static Gson gsonReader = gsonReader();\n\tprivate static Gson gsonWriter = gsonWriter();\n\tprivate RestfulAccessor accessor = new RestfulAccessor();")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("certificate")).add(literal("private URL certificate;"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("user")).add(literal("private String user;"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("auth")).add(literal("private String password;"))).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Accessor(URL url")).add(expression().add(literal(", ")).add(mark("certificate")).add(literal("URL certificate"))).add(expression().add(mark("user")).add(literal(", String user"))).add(expression().add(mark("auth")).add(literal(", String password"))).add(literal(") {\n\t\tthis.url = url;")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("certificate")).add(literal("this.certificate = certificate;"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("user")).add(literal("this.user = user;"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("auth")).add(literal("this.password = password;"))).add(literal("\n\t}\n\n\t")).add(mark("resource").multiple("\n\n")).add(literal("\n\n\tprivate String encode(String value) {\n\t\ttry {\n\t\t\treturn java.net.URLEncoder.encode(value, \"UTF-8\");\n\t\t} catch (java.io.UnsupportedEncodingException e) {\n\t\t\tlogger.error(e.getMessage(), e);\n\t\t\treturn \"\";\n\t\t}\n\t}\n\n\tprivate static Gson gsonReader() {\n        return new GsonBuilder().\n            registerTypeAdapter(java.time.Instant.class, (com.google.gson.JsonDeserializer<java.time.Instant>) (json, type1, jsonDeserializationContext) -> java.time.Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).\n            registerTypeAdapter(java.util.Date.class, (com.google.gson.JsonDeserializer<java.util.Date>) (json, type1, jsonDeserializationContext) -> new java.util.Date(json.getAsJsonPrimitive().getAsLong())).\n            create();\n\t}\n\n\tprivate static Gson gsonWriter() {\n        return new GsonBuilder().\n            registerTypeAdapter(java.time.Instant.class, (com.google.gson.JsonSerializer<java.time.Instant>) (instant, type, context) -> new com.google.gson.JsonPrimitive(instant.toEpochMilli())).\n            registerTypeAdapter(java.util.Date.class, (com.google.gson.JsonSerializer<java.util.Date>) (date, type, context) -> new com.google.gson.JsonPrimitive(date.getTime())).\n            create();\n    }\n}")),
			rule().add((condition("type", "resource"))).add(literal("public ")).add(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" ")).add(mark("operation", "firstLowerCase")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("(")).add(mark("parameter", "signature").multiple(", ")).add(literal(") ")).add(mark("exceptionResponses", "declaration")).add(literal(" {\n\ttry {\n\t\t")).add(expression().add(literal("java.util.Map<String, String> parameters = new java.util.HashMap<String, String>() {{")).add(literal("\n")).add(literal("\t\t\t")).add(mark("parameter", "declaration").multiple("\n")).add(literal("\n")).add(literal("\t\t}};"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parameter", "fileDeclaration").multiple("\n"))).add(literal("\n\t\t")).add(mark("invokeSentence")).add(literal("\n\t} catch (RestfulFailure e) {\n\t\t")).add(mark("exceptionResponses", "throws")).add(literal("\n\t} ")).add(mark("finally")).add(literal("\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("parameterType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "invoke"))).add(literal("parameters")),
			rule().add((condition("type", "parameter & required & fileData")), (condition("trigger", "fileDeclaration"))).add(literal("Resource resource = new Resource(new java.io.FileInputStream(")).add(mark("name")).add(literal("), Resource.resolveContentType(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "dateData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toInstant().toEpochMilli()));")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "dateTimeData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toEpochMilli()));")),
			rule().add((condition("type", "parameter & query & required & textData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "boolData | integerData | realData | longIntegerData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("));")),
			rule().add((condition("type", "parameter & query & required & objectData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(gsonWriter.toJson(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(")));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "dateData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toInstant().toEpochMilli()));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "dateTimeData | dateData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toEpochMilli()));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "textData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "parameter & query")), (condition("type", "boolData | integerData | realData | longIntegerData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("));")),
			rule().add((condition("type", "parameter & objectData")), (condition("type", "query | body")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", encode(String.valueOf(gsonWriter.toJson(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("))));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "declaration"))),
			rule().add((condition("type", "parameter")), (condition("trigger", "fileDeclaration"))),
			rule().add((condition("type", "io")), (condition("trigger", "finally"))).add(literal("catch (java.io.IOException e) {\n\tlogger.error(e.getMessage(), e);")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("return")).add(literal("return null;"))).add(literal("\n}")),
			rule().add((condition("trigger", "finally"))),
			rule().add((condition("type", "invokeSentence & void"))).add(mark("doInvoke")).add(literal(";")),
			rule().add((condition("type", "invokeSentence & object & list"))).add(literal("return gsonReader.fromJson(")).add(mark("doInvoke")).add(literal(".content(), new TypeToken<")).add(mark("returnType")).add(literal(">(){}.getType());")),
			rule().add((condition("type", "invokeSentence & object"))).add(literal("return gsonReader.fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & file & list"))).add(literal("return null; //TODO")),
			rule().add((condition("type", "invokeSentence & file"))).add(literal("return ")).add(mark("doInvoke")).add(literal(";")),
			rule().add((condition("type", "invokeSentence & date & list"))).add(literal("return null; //TODO")),
			rule().add((condition("type", "invokeSentence")), (condition("type", "date | instant"))).add(literal("return gsonReader.fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & datetime & list"))).add(literal("return gsonReader.fromJson(")).add(mark("doInvoke")).add(literal(".content(), new TypeToken<ArrayList<Instant>>(){}.getType());")),
			rule().add((condition("type", "invokeSentence & datetime"))).add(literal("return java.time.Instant.ofEpochMilli(Long.valueOf(")).add(mark("doInvoke")).add(literal(".content()));")),
			rule().add((condition("type", "invokeSentence & primitive & int & list"))).add(literal("return gsonReader.fromJson(")).add(mark("doInvoke")).add(literal(".content(), new TypeToken<ArrayList<Integer>>(){}.getType());")),
			rule().add((condition("type", "invokeSentence & primitive & int"))).add(literal("return Integer.valueOf(")).add(mark("doInvoke")).add(literal(".content());")),
			rule().add((condition("type", "invokeSentence & primitive & list"))).add(literal("return gsonReader.fromJson(")).add(mark("doInvoke")).add(literal(".content(), new TypeToken<ArrayList<")).add(mark("returnType")).add(literal(">>(){}.getType());")),
			rule().add((condition("type", "invokeSentence & primitive"))).add(literal("return ")).add(mark("returnType", "firstUpperCase")).add(literal(".valueOf(")).add(mark("doInvoke")).add(literal(".content());")),
			rule().add((condition("type", "exceptionResponses & none")), (condition("trigger", "throws"))).add(literal("throw new Unknown(e.label());")),
			rule().add((condition("type", "exceptionResponses")), (condition("trigger", "throws"))).add(mark("exceptionResponse", "throws").multiple("\nelse ")).add(literal("\nthrow new Unknown(e.label());")),
			rule().add((condition("type", "exceptionResponse")), (condition("trigger", "throws"))).add(literal("if (e.code().equals(\"")).add(mark("code")).add(literal("\")) throw new ")).add(mark("exceptionName")).add(literal("(e.label());")),
			rule().add((condition("type", "exceptionResponses")), (condition("trigger", "declaration"))).add(literal("throws ")).add(expression().add(mark("exceptionResponse", "declaration").multiple(", ")).add(literal(", "))).add(literal("Unknown")),
			rule().add((condition("type", "exceptionResponse")), (condition("trigger", "declaration"))).add(mark("exceptionName")),
			rule().add((condition("type", "auth & cert & doInvoke"))).add(literal("accessor.secure(this.url, ")).add(mark("certificate")).add(literal("this.certificate, this.password).")).add(mark("type", "firstLowerCase")).add(literal("(")).add(mark("relativePath")).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "auth & doInvoke"))).add(literal("accessor.secure(this.url, this.user, this.password).")).add(mark("type", "firstLowerCase")).add(literal("(")).add(mark("relativePath")).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "doInvoke"))).add(literal("accessor.")).add(mark("type", "firstLowerCase")).add(literal("(this.url, ")).add(mark("relativePath")).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "doInvoke"))).add(literal("accessor.")).add(mark("type", "firstLowerCase")).add(literal("(this.url, ")).add(mark("relativePath")).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "schemaImport")), (condition("trigger", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}