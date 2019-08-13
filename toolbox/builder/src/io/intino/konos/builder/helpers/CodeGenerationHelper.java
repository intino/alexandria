package io.intino.konos.builder.helpers;

import io.intino.konos.builder.codegeneration.Target;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.javascriptFile;

public class CodeGenerationHelper {
	public static final String UI = "ui";
	public static final String Displays = "%sdisplays";
	public static final String DisplaysType = "%sdisplays/%ss";
	public static final String Notifiers = "%sdisplays/notifiers";
	public static final String Requesters = "%sdisplays/requesters";
	public static final String Resources = "%sresources";
	public static final String Pages = "%spages";

	public static File folder(File root, String path, Target target) {
		return new File(root.getAbsolutePath() + format(path, target));
	}

	public static File displayFolder(File folder, String type, Target target) {
		return new File(folder, displayPath(type, target));
	}

	public static File displayFile(File folder, String name, String type, Target target) {
		return fileOf(new File(folder, displayPath(type, target)), name, target);
	}

	public static String displayFilename(String name) {
		return displayFilename(name, "");
	}

	public static String displayFilename(String name, String suffix) {
		return snakeCaseToCamelCase(name + suffix);
	}

	public static String displayPath(String type, Target target) {
		return type.equalsIgnoreCase("display") || type.equalsIgnoreCase("AccessibleDisplay") ? format(Displays, target) : String.format(DisplaysType, uiSubPath(target), type).toLowerCase();
	}

	public static File displayRequesterFolder(File folder, Target target) {
		return new File(folder, format(Requesters, target));
	}

	public static File displayNotifierFolder(File folder, Target target) {
		return new File(folder, format(Notifiers, target));
	}

	public static File resourceFolder(File folder, Target target) {
		return new File(folder, format(Resources, target));
	}

	public static String resourceFilename(String name) {
		return resourceFilename(name, "Resource");
	}

	public static String resourceFilename(String name, String suffix) {
		return snakeCaseToCamelCase(name + suffix);
	}

	public static File serviceFolder(File folder) {
		return new File(folder, UI);
	}

	public static String serviceFilename(String name) {
		return snakeCaseToCamelCase(name + "Service");
	}

	public static String format(String path, Target target) {
		return String.format(path, uiSubPath(target));
	}

	public static String uiSubPath(Target target) {
		return target == Target.Owner ? UI + "/" : "";
	}

	public static File fileOf(File file, String name, Target target) {
		if (target == Target.Owner) return javaFile(file, name);
		return javascriptFile(file, name);
	}

	public static File createIfNotExists(File file) {
		if (!file.exists()) file.mkdirs();
		return file;
	}

	public static String toSnakeCase(String name) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1-$2";
		return name.replaceAll(regex, replacement).toLowerCase();
	}
}
