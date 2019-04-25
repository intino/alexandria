package io.intino.konos.builder.codegeneration;


import cottons.utils.StringHelper;
import io.intino.konos.builder.helpers.Commons;
import org.siani.itrules.Formatter;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

public class Formatters {

	public static Formatter validMoldName() {
		return (value) -> {
			final String name = value.toString().replace("-", "");
			return Character.isDigit(name.charAt(0)) ? "m" + name : name;
		};
	}

	public static Formatter validName() {
		return (value) -> StringHelper.snakeCaseToCamelCase(value.toString().replace(".", "-"));
	}

	public static Formatter snakeCaseToCamelCase() {
		return value -> StringHelper.snakeCaseToCamelCase(value.toString());
	}

	public static Formatter camelCaseToSnakeCase() {
		return value -> StringHelper.camelCaseToSnakeCase(value.toString());
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

	public static Formatter shortType() {
		return value -> {
			String type = value.toString();
			final String[] s = type.split("\\.");
			return s[s.length - 1];
		};
	}

	public static Frame customize(String name,String value) {
		Frame frame = new Frame().addTypes(name);
		frame.addSlot("name", value);
		for (String parameter : Commons.extractParameters(value)) frame.addSlot("custom", parameter);
		return frame;
	}


	public static Template customize(Template template) {
		template.add("validname", validName());
		template.add("snakeCaseToCamelCase", snakeCaseToCamelCase());
		template.add("camelCaseToSnakeCase", camelCaseToSnakeCase());
		template.add("returnType", returnType());
		template.add("returnTypeFormatter", returnTypeFormatter());
		template.add("quoted", quoted());
		template.add("validPackage", validPackage());
		template.add("subpath", subPath());
		template.add("shortType", shortType());
		template.add("quoted", quoted());
		template.add("customParameter", customParameter());
		return template;
	}

	private static Formatter customParameter() {
		return value -> value.toString().substring(1, value.toString().length() - 1);
	}

}
