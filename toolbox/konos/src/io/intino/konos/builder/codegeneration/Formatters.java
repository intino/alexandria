package io.intino.konos.builder.codegeneration;


import cottons.utils.StringHelper;
import io.intino.itrules.Formatter;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class Formatters {
	public static Map<String, Formatter> all = new HashMap<>();

	static {
		all.put("validname", validName());
		all.put("camelCaseToSnakeCase", camelCaseToKebabCase());
		all.put("escapeHtml", escapeHtml());
		all.put("returnType", returnType());
		all.put("returnTypeFormatter", returnTypeFormatter());
		all.put("validPackage", validPackage());
		all.put("subpath", subPath());
		all.put("shortType", shortType());
		all.put("quoted", quoted());
		all.put("customParameter", customParameter());
		all.put("dotsWithUnderscore", dotsWithUnderscore());
		all.put("slashToCamelCase", slashToCamel());
		all.put("typeFormat", typeFormatter());
	}

	public static Formatter validName() {
		return (value) -> StringHelper.snakeCaseToCamelCase(value.toString().replace(".", "-"));
	}

	public static Formatter camelCaseToKebabCase() {
		return value -> StringHelper.camelCaseToSnakeCase(value.toString());
	}

	public static Formatter escapeHtml() {
		return value -> StringEscapeUtils.escapeHtml((String) value);
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

	public static Formatter typeFormatter() {
		return value -> {
			if (value.toString().contains(".")) return Formatters.firstLowerCase(value.toString());
			else return value;
		};
	}

	public static Formatter slashToCamel() {
		return o -> snakeCaseToCamelCase(o.toString().replace("|", "_"));
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

	public static Frame customize(String name, String value) {
		FrameBuilder builder = new FrameBuilder(name);
		builder.add("name", value);
		builder.add("custom", Commons.extractParameters(value).toArray(String[]::new));
		return builder.toFrame();
	}


	private static Formatter customParameter() {
		return value -> value.toString().substring(1, value.toString().length() - 1);
	}

	private static Formatter dotsWithUnderscore() {
		return value -> value.toString().replaceAll("\\.", "_");
	}

}
