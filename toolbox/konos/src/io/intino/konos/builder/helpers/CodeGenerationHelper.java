package io.intino.konos.builder.helpers;

import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.dsl.CatalogComponents;
import io.intino.konos.dsl.HelperComponents;
import io.intino.konos.dsl.Template;
import io.intino.magritte.framework.Layer;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class CodeGenerationHelper {
	public static final String UI = "ui";
	public static final String Displays = "%sdisplays";
	public static final String DisplaysType = "%sdisplays/%ss";
	public static final String Notifiers = "%sdisplays/notifiers";
	public static final String Requesters = "%sdisplays/requesters";
	public static final String Resources = "%sresources";
	public static final String Pages = "%spages";

	public static boolean hasAbstractClass(Layer element, Target target) {
		if (target == Target.Server) return true;
		return !element.i$(conceptOf(io.intino.konos.dsl.Template.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class));
	}

	public static boolean isScrollable(Layer element, Target target) {
		return element.i$(conceptOf(io.intino.konos.dsl.Template.class)) && element.a$(Template.class).scrollable();
	}

	public static File folder(File root, String path, Target target) {
		return new File(root.getAbsolutePath() + format(path, target));
	}

	public static File displaysFolder(File folder, Target target) {
		return new File(folder, displaysPath(target));
	}

	public static File displayFolder(File folder, String type, Target target) {
		return new File(folder, displayPath(folder, type, target));
	}

	public static File displayFile(File folder, String name, String type, Target target) {
		return fileOf(new File(folder, displayPath(folder, type, target)), name, target);
	}

	public static String displayFilename(String name) {
		return displayFilename(name, "");
	}

	public static String displayFilename(String name, String suffix) {
		return snakeCaseToCamelCase(name + suffix);
	}

	private static String displaysPath(Target target) {
		return format(Displays, target);
	}

	public static String displayPath(File folder, String type, Target target) {
		if (target == Target.AndroidResource) return File.separator + "layout";
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
		return target == Target.Server ? UI + "/" : "";
	}

	public static File fileOf(File file, String name, Target target) {
		if (target == Target.Server) return javaFile(file, name);
		if (target == Target.Android || target == Target.MobileShared) return kotlinFile(file, name);
		if (target == Target.AndroidResource) return xmlFile(file, name);
		return javascriptFile(file, name);
	}

	public static File createIfNotExists(File file) {
		if (!file.exists()) file.mkdirs();
		return file;
	}
}
