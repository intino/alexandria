package io.intino.konos.builder.codegeneration;


import cottons.utils.StringHelper;
import org.siani.itrules.Formatter;
import org.siani.itrules.Template;

public class Formatters {


	public static Formatter validName() {
		return (value) -> StringHelper.snakeCaseToCamelCase(value.toString().replace(".", "-"));
	}

	public static Formatter snakeCaseToCamelCase() {
		return value -> StringHelper.snakeCaseToCamelCase(value.toString());
	}

	public static Formatter returnType() {
		return value -> value.equals("Void") ? "void" : value;
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	public static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public static Formatter returnTypeFormatter() {
		return value -> {
			if (value.equals("Void")) return "void";
			else if (value.toString().contains(".")) return firstLowerCase(value.toString());
			else return value;
		};
	}

	public static Formatter camelCaseToSnakeCase() {
		return value -> StringHelper.camelCaseToSnakeCase(value.toString());
	}

	public static Formatter quoted() {
		return value -> '"' + value.toString() + '"';
	}

	public static Formatter validPackage() {
		return value -> value.toString().replace("-", "").toLowerCase();
	}

	private static Formatter subPath() {
		return value -> {
			final String path = value.toString();
			return path.contains(":") ? path.substring(0, path.indexOf(":")) : path;
		};
	}

	public static Template customize(Template template) {
		template.add("validname", validName());
		template.add("snakecaseToCamelCase", snakeCaseToCamelCase());
		template.add("returnType", returnType());
		template.add("returnTypeFormatter", returnTypeFormatter());
		template.add("quoted", quoted());
		template.add("validPackage", validPackage());
		template.add("camelCaseToSnakeCase", camelCaseToSnakeCase());
		template.add("subpath", subPath());
		return template;
	}


}
