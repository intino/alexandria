package io.intino.konos.builder.helpers;

import cottons.utils.StringHelper;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.Data;
import io.intino.konos.model.Redirect;
import io.intino.konos.model.Response;
import io.intino.konos.model.Service;
import io.intino.konos.model.Service.REST.Notification;
import io.intino.konos.model.Service.REST.Resource;
import io.intino.konos.model.Service.REST.Resource.Operation;
import io.intino.konos.model.Service.REST.Resource.Parameter.In;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Commons {
	private static final java.util.logging.Logger LOG = Logger.getGlobal();

	public static void writeFrame(File packageFolder, String name, String text) {
		try {
			packageFolder.mkdirs();
			File file = javaFile(packageFolder, name);
			Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}

	public static Frame fileFrame(String directory, String packageName, String archetypeQn) throws KonosException {
		if (archetypeQn == null) throw new KonosException("Archetype not found. Please define it in artifact");
		if (directory.startsWith(".archetype")) {
			String boxPackage = archetypeQn.replace(".Archetype", "");
			String archetypePath = Commons.archetypePath(directory);
			return new FrameBuilder("archetype").add("package", boxPackage).add("path", archetypePath).toFrame();
		}
		return new FrameBuilder("file").add(isCustom(directory) ? "custom" : "standard").add("path", directory).toFrame();
	}

	private static boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	public static void write(File file, String text) {
		write(file.toPath(), text);
	}

	public static void write(Path file, String text) {
		try {
			Files.write(file, text.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}

	public static File xmlFile(File packageFolder, String name) {
		return new File(packageFolder, StringHelper.camelCaseToSnakeCase(name).replace("-", "_") + ".xml");
	}

	public static File javaFile(File packageFolder, String name) {
		return preparedFile(packageFolder, firstUpperCase(name), "java");
	}

	public static File kotlinFile(File packageFolder, String name) {
		return preparedFile(packageFolder, firstUpperCase(name), "kt");
	}

	public static String javaFilename(String name) {
		return prepareName(name) + ".java";
	}

	public static File javascriptFile(File packageFolder, String name) {
		return preparedFile(packageFolder, name, "js");
	}

	public static long queryParameters(Operation operation) {
		return operation.parameterList().stream().filter(p -> p.in() == In.query).count();
	}

	public static long bodyParameters(Operation operation) {
		return operation.parameterList().stream().filter(p -> p.in() == In.body).count();
	}


	public static long queryParameters(Notification operation) {
		return operation.parameterList().stream().filter(p -> p.in() == Notification.Parameter.In.query).count();
	}

	public static long bodyParameters(Notification operation) {
		return operation.parameterList().stream().filter(p -> p.in() == Notification.Parameter.In.body).count();
	}

	public static String format(String path) {
		return path.isEmpty() ? "" : path + "/";
	}

	public static String path(Resource resource) {
		Service.REST service = resource.core$().ownerAs(Service.REST.class);
		String basePath = basePath(service.basePath());
		if (service.info() != null && service.info().version() != null)
			basePath = basePath + service.info().version() + "/";
		String resourcePath = resource.path();
		return (basePath + resourcePath).replace("//", "/");
	}

	public static String path(Service.Soap.Operation operation) {
		Service.Soap service = operation.core$().ownerAs(Service.Soap.class);
		return (basePath(service.basePath()) + operation.path()).replace("//", "/");
	}


	private static String basePath(String path) {
		path = !path.isEmpty() && !path.endsWith("/") ? path + "/" : path;
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

	public static String returnType(Response response, String packageName) {
		if (response == null) return "void";
		if (response.i$(Redirect.class)) return String.class.getName();
		if (response.asType() == null) return "void";
		String innerPackage = response.isObject() && response.asObject().isComponent() ? String.join(".", packageName, "schemas.") : "";
		String type = innerPackage + response.asType().type();
		if (response.isList()) return "List<" + type + ">";
		if (response.isSet()) return "Set<" + type + ">";
		return type;
	}

	public static String fullReturnType(Response response, String packageName) {
		if (response == null) return "void";
		if (response.i$(Redirect.class)) return String.class.getName();
		if (response.asType() == null) return "void";
		String innerPackage = response.isObject() ? String.join(".", packageName, "schemas.") : "";
		String type = innerPackage + response.asType().type();
		if (response.isList()) return "java.util.List<" + type + ">";
		if (response.isSet()) return "java.util.Set<" + type + ">";
		return type;
	}

	public static int fileParameters(Operation operation) {
		return (int) operation.parameterList().stream().filter(p -> p.i$(Data.File.class)).count();
	}

	public static String firstUpperCase(String value) {
		return value.isEmpty() ? "" : value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	private static File preparedFile(File packageFolder, String name, String extension) {
		return new File(packageFolder, prepareName(name) + "." + extension);
	}

	private static String prepareName(String name) {
		return name.isEmpty() ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public static String archetypePath(String path) {
		return path.replace(".archetype", "").substring(1).replace("+", "().").replace("-", "().") + "()";
	}
}
