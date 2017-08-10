package io.intino.konos.builder.helpers;

import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.file.FileData;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.konos.model.graph.rest.RESTService.Resource.Operation;
import io.intino.konos.model.graph.rest.RESTService.Resource.Parameter.In;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Commons {

	public static void writeFrame(File packageFolder, String name, String format) {
		try {
			packageFolder.mkdirs();
			File file = javaFile(packageFolder, name);
			Files.write(file.toPath(), format.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File javaFile(File packageFolder, String name) {
		return new File(packageFolder, prepareName(name) + ".java");
	}

	private static String prepareName(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public static String[] pathParameters(Operation operation) {
		return operation.parameterList().stream().filter(p -> p.in() == In.path)
				.map(Resource.Parameter::name$).toArray(String[]::new);
	}

	public static long queryParameters(Operation resource) {
		return resource.parameterList().stream().filter(p -> p.in() == In.query).count();
	}

	public static long bodyParameters(Operation resource) {
		return resource.parameterList().stream().filter(p -> p.in() == In.body).count();
	}

	public static String format(String path) {
		return path.isEmpty() ? "" : path + "/";
	}

	public static String path(Resource resource) {
		String basePath = cleanPath(resource.core$().ownerAs(RESTService.class).basePath());
		String resourcePath = cleanPath(resource.path());
		return format(basePath) + resourcePath;
	}

	private static String cleanPath(String path) {
		path = path.endsWith("/") ? path.substring(path.length() - 1) : path;
		return path.startsWith("/") ? path.substring(1) : path;
	}

	public static Set<String> extractParameters(String text) {
		Set<String> list = new LinkedHashSet<>();
		Pattern pattern = Pattern.compile("\\{([^\\}]*)\\}");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) list.add(matcher.group(1));
		return list;
	}

	public static List<String> extractUrlPathParameters(String url) {
		return Arrays.stream(url.split("/")).filter(s -> s.contains(":")).map(s -> s.replace(":", "")).collect(toList());
	}

	public static String returnType(Response response) {
		if (response == null || response.asType() == null) return "void";
		return response.isList() ? "List<" + response.asType().type() + ">" : response.asType().type();
	}

	public static String returnType(Response response, String rootPackage) {
		if (response == null || response.asType() == null) return "void";
		String innerPackage = response.isObject() && response.asObject().isComponent() ? String.join(".", rootPackage, "schemas.") : "";
		String type = innerPackage + response.asType().type();
		return response.isList() ? "List<" + type + ">" : type;
	}

	public static int fileParameters(Operation operation) {
		return (int) operation.parameterList().stream().filter(p -> p.i$(FileData.class)).count();
	}

	public static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}
