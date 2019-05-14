package io.intino.konos.builder.helpers;

import io.intino.konos.builder.codegeneration.Target;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.javascriptFile;

public class DisplayHelper {
	public static final String UI = "ui";
	public static final String Displays = "%sdisplays";
	public static final String DisplaysType = "%sdisplays/%ss";

	public static File displayFile(File folder, String name, String type, Target target) {
		return fileOf(new File(folder, displayPath(type, target)), snakeCaseToCamelCase(name));
	}

	public static String displayPath(String type, Target target) {
		return type.equalsIgnoreCase("display") ? format(Displays, target) : String.format(DisplaysType, uiSubPath(target), type).toLowerCase();
	}

	public static String notifierPath(String displayType, Target target) {
		return displayType.equalsIgnoreCase("display") ? format(Displays, target) : String.format(DisplaysType, uiSubPath(target), displayType).toLowerCase();
	}

	public static String format(String path, Target target) {
		return String.format(path, uiSubPath(target));
	}

	public static String uiSubPath(Target target) {
		return target == Target.Service ? UI + "/" : "";
	}

	private static File fileOf(File file, String name, Target target) {
		if (target == Target.Service) return javaFile(file, name);
		return javascriptFile(file, name);
	}
}
