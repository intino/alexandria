package teseo.codegeneration.accessor;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JavaAccessorTemplate extends Template {

	protected JavaAccessorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JavaAccessorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "accessor"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport java.net.URL;\nimport teseo.restful.core.Resource;\nimport teseo.restful.core.RestfulAccessor;\nimport teseo.restful.exceptions.RestfulFailure;\nimport com.google.gson.Gson;\nimport teseo.exceptions.*;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Accessor {\n\n\tprivate URL url;\n\tprivate RestfulAccessor accessor = new RestfulAccessor();")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("certificate")).add(literal("private URL certificate;"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("user")).add(literal("private String user;"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("auth")).add(literal("private String password;"))).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Accessor(URL url")).add(expression().add(literal(", ")).add(mark("certificate")).add(literal("URL certificate"))).add(expression().add(mark("user")).add(literal(", String user"))).add(expression().add(mark("auth")).add(literal(", String password"))).add(literal(") {\n\t\tthis.url = url;")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("certificate")).add(literal("this.certificate = certificate;"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("user")).add(literal("this.user = user;"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("auth")).add(literal("this.password = password;"))).add(literal("\n\t}\n\n\t")).add(mark("resource").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "resource"))).add(literal("public ")).add(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" ")).add(mark("name")).add(literal("(")).add(mark("parameter", "signature").multiple(", ")).add(literal(") ")).add(mark("exceptionResponses", "declaration")).add(literal(" {\n\ttry {\n\t\t")).add(expression().add(literal("java.util.Map<String, String> parameters = new java.util.HashMap<String, String>() {{")).add(literal("\n")).add(literal("\t\t\t")).add(mark("parameter", "declaration").multiple("\n")).add(literal("\n")).add(literal("\t\t}};"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parameter", "fileDeclaration").multiple("\n"))).add(literal("\n\t\t")).add(mark("invokeSentence")).add(literal("\n\t} catch (RestfulFailure e) {\n\t\t")).add(mark("exceptionResponses", "throws")).add(literal("\n\t} ")).add(mark("parameter", "exception")).add(literal("\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("parameterType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "invoke"))).add(literal("parameters")),
			rule().add((condition("type", "parameter & required & fileData")), (condition("trigger", "fileDeclaration"))).add(literal("Resource resource = new Resource(new java.io.FileInputStream(")).add(mark("name")).add(literal("), Resource.resolveContentType(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "dateTimeData | dateData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toInstant(java.time.ZoneOffset.UTC).toEpochMilli()));")),
			rule().add((condition("type", "parameter & query & required & textData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "boolData | integerData | realData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("));")),
			rule().add((condition("type", "parameter & query & required & objectData")), (condition("trigger", "declaration"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(new Gson().toJson(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(")));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "dateTimeData | dateData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toInstant(java.time.ZoneOffset.UTC).toEpochMilli()));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "textData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "parameter & query")), (condition("type", "boolData | integerData | realData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("));")),
			rule().add((condition("type", "parameter & query & objectData")), (condition("trigger", "declaration"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(new Gson().toJson(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(")));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "declaration"))),
			rule().add((condition("type", "parameter")), (condition("trigger", "fileDeclaration"))),
			rule().add((condition("type", "fileData")), (condition("trigger", "exception"))).add(literal("catch (java.io.IOException e) {\n\te.printStackTrace();\n}")),
			rule().add((condition("trigger", "exception"))),
			rule().add((condition("type", "invokeSentence & void"))).add(mark("doInvoke")).add(literal(";")),
			rule().add((condition("type", "invokeSentence & object"))).add(literal("return new Gson().fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & file"))).add(literal("return new Gson().fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & html"))).add(literal("return new Gson().fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & date"))).add(literal("return new Gson().fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & datetime"))).add(literal("return new Gson().fromJson(")).add(mark("doInvoke")).add(literal(".content(), ")).add(mark("returnType", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("type", "invokeSentence & primitive & int"))).add(literal("return Integer.valueOf(")).add(mark("doInvoke")).add(literal(".content());")),
			rule().add((condition("type", "invokeSentence & primitive"))).add(literal("return ")).add(mark("returnType", "firstUpperCase")).add(literal(".valueOf(")).add(mark("doInvoke")).add(literal(".content());")),
			rule().add((condition("type", "exceptionResponses & none")), (condition("trigger", "throws"))).add(literal("throw new ErrorUnknown(e.label());")),
			rule().add((condition("type", "exceptionResponses")), (condition("trigger", "throws"))).add(mark("exceptionResponse", "throws").multiple("\nelse ")).add(literal("\nthrow new ErrorUnknown(e.label());")),
			rule().add((condition("type", "exceptionResponse")), (condition("trigger", "throws"))).add(literal("if (e.code().equals(\"")).add(mark("code")).add(literal("\")) throw new ")).add(mark("exceptionName")).add(literal("(e.label());")),
			rule().add((condition("type", "exceptionResponses")), (condition("trigger", "declaration"))).add(literal("throws ")).add(expression().add(mark("exceptionResponse", "declaration").multiple(", ")).add(literal(", "))).add(literal("ErrorUnknown")),
			rule().add((condition("type", "exceptionResponse")), (condition("trigger", "declaration"))).add(mark("exceptionName")),
			rule().add((condition("type", "auth & cert & doInvoke"))).add(literal("accessor.secure(this.url, ")).add(mark("certificate")).add(literal("this.certificate, this.password).")).add(mark("type", "firstLowerCase")).add(literal("(\"")).add(mark("relativePath")).add(literal("\"")).add(expression().add(literal(" + \"/\" + ")).add(mark("pathParameters", "SnakeCaseToCamelCase", "firstLowerCase").multiple(" + \"/\" + "))).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "auth & doInvoke"))).add(literal("accessor.secure(this.url, this.user, this.password).")).add(mark("type", "firstLowerCase")).add(literal("(\"")).add(mark("relativePath")).add(literal("\"")).add(expression().add(literal(" + \"/\" + ")).add(mark("pathParameters", "SnakeCaseToCamelCase", "firstLowerCase").multiple(" + \"/\" + "))).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "doInvoke"))).add(literal("accessor.")).add(mark("type", "firstLowerCase")).add(literal("(this.url, \"")).add(mark("relativePath")).add(literal("\"")).add(expression().add(literal(" + \"/\" + ")).add(mark("pathParameters", "SnakeCaseToCamelCase", "firstLowerCase").multiple(" + \"/\" + "))).add(expression().add(literal(", ")).add(mark("parameters"))).add(literal(")")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}