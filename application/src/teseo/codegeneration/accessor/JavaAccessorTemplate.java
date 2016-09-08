package teseo.codegeneration.accessor;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class JavaAccessorTemplate extends Template {

	protected JavaAccessorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JavaAccessorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "accessor"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport java.net.URL;\nimport java.time.LocalDateTime;\nimport teseo.restful.core.Resource;\nimport teseo.restful.core.RestfulAccessor;\nimport teseo.restful.exceptions.RestfulFailure;\nimport com.google.gson.Gson;\nimport teseo.exceptions.*;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Accessor {\n\n    private URL url;\n    private RestfulAccessor accessor = new RestfulAccessor();\n\n    public ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Accessor(URL url) {\n        this.url = url;\n    }\n\n    ")).add(mark("resources").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "resource"))).add(literal("public ")).add(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" ")).add(mark("name")).add(literal("(")).add(mark("parameters", "declaration").multiple(", ")).add(literal(") ")).add(mark("exceptionResponses", "declaration")).add(literal(" {\n    try {\n        java.util.Map<String, String> parameters = new java.util.HashMap<String,String>(){{\n            ")).add(mark("parameters", "toMap").multiple("\n")).add(literal("\n        }};\n        ")).add(mark("invokeSentence")).add(literal("\n    } catch (RestfulFailure e) {\n        ")).add(mark("exceptionResponses", "throws")).add(literal("\n    }\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "declaration"))).add(mark("parameterType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "dateTimeData | dateData")), (condition("trigger", "toMap"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toInstant(java.time.ZoneOffset.UTC).toEpochMilli());")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "htmlData | textData")), (condition("trigger", "toMap"))).add(literal("put(\"")).add(mark("name")).add(literal("\", ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "parameter & query & required")), (condition("type", "boolData | integerData | realData")), (condition("trigger", "toMap"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("));")),
			rule().add((condition("type", "parameter & query & required & objectData")), (condition("trigger", "toMap"))).add(literal("put(\"")).add(mark("name")).add(literal("\", String.valueOf(new Gson().toJson(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(")));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "dateTimeData | dateData")), (condition("trigger", "toMap"))).add(literal("if(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(".toInstant(java.time.ZoneOffset.UTC).toEpochMilli()));")),
			rule().add((condition("type", "parameter & query")), (condition("type", "htmlData | textData")), (condition("trigger", "toMap"))).add(literal("if(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "parameter & query")), (condition("type", "boolData | integerData | realData")), (condition("trigger", "toMap"))).add(literal("if(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("));")),
			rule().add((condition("type", "parameter & query & objectData")), (condition("trigger", "toMap"))).add(literal("if(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" != null) put(\"")).add(mark("name")).add(literal("\", String.valueOf(new Gson().toJson(")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(")));")),
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
			rule().add((condition("type", "exceptionResponse")), (condition("trigger", "throws"))).add(literal("if(e.code().equals(\"")).add(mark("code")).add(literal("\"))\n    throw new ")).add(mark("exceptionName")).add(literal("(e.label());")),
			rule().add((condition("type", "exceptionResponses")), (condition("trigger", "declaration"))).add(literal("throws ")).add(expression().add(mark("exceptionResponse", "declaration").multiple(", ")).add(literal(", "))).add(literal("ErrorUnknown")),
			rule().add((condition("type", "exceptionResponse")), (condition("trigger", "declaration"))).add(mark("exceptionName")),
				rule().add((condition("type", "doInvoke"))).add(literal("accessor.")).add(mark("type", "firstLowerCase")).add(literal("(this.url, \"")).add(mark("relativePath")).add(literal("\"")).add(expression().add(literal(" + \"/\" + ")).add(mark("parameters", "SnakeCaseToCamelCase", "firstLowerCase").multiple(" + \"/\" + "))).add(literal(", parameters)")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}