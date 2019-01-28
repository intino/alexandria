package io.intino.konos.builder.codegeneration.ui;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.temporal.TemporalCatalog;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Map;

public abstract class UIRenderer {
	protected Settings settings;
	protected final Target target;

	public static final String UI = "ui";
	public static final String Resources = "%sresources";
	public static final String Notifiers = "%sdisplays/notifiers";
	public static final String Requesters = "%sdisplays/requesters";

	public static final String Pages = "%spages";
	public static final String Displays = "%sdisplays";
	public static final String DisplaysType = "%sdisplays/%ss";

	public enum Target { Accessor, Service }

	protected UIRenderer(Settings settings, Target target) {
		this.settings = settings;
		this.target = target;
	}

	public abstract void execute();

	public Frame buildFrame() {
		return new Frame().addSlot("box", boxName()).addSlot("package", settings.packageName());
	}

	public Project project() {
		return settings.project();
	}

	public String boxName() {
		return settings.boxName();
	}

	protected String packageName() {
		return settings.packageName();
	}

	protected File src() {
		return createIfNotExists((target == Target.Service) ? settings.src() : accessorSrc());
	}

	protected File accessorSrc() {
		return createIfNotExists(new File(accessorRoot() + File.separator + "src"));
	}

	protected File gen() {
		return createIfNotExists((target == Target.Service) ? settings.gen() : accessorGen());
	}

	protected File accessorGen() {
		return createIfNotExists(new File(accessorRoot() + File.separator + "gen"));
	}

	protected File accessorRes() {
		return createIfNotExists(new File(accessorRoot() + File.separator + "res"));
	}

	protected File accessorRoot() {
		return createIfNotExists(new File(settings.webModule().getModuleFilePath()).getParentFile());
	}

	protected String parent() {
		return settings.parent();
	}

	protected Map<String, String> classes() {
		return settings.classes();
	}

	protected Template setup(Template template) {
		return addFormats(template);
	}

	protected String format(String path) {
		return String.format(path, uiSubPath());
	}

	public static String format(String path, Target target) {
		return String.format(path, uiSubPath(target));
	}

	protected String uiSubPath() {
		return uiSubPath(target);
	}

	public static String uiSubPath(Target target) {
		return target == Target.Service ? UI + "/" : "";
	}

	protected String path(io.intino.konos.model.graph.Display display) {
		String type = typeOf(display);
		return type.equalsIgnoreCase("display") ? format(Displays) : String.format(DisplaysType, uiSubPath(), type).toLowerCase();
	}

	protected Template addFormats(Template template) {
		Formatters.customize(template);
		return template;
	}

	protected String clean(String name) {
		name = name.replaceAll("-", "");
		return Character.isDigit(name.charAt(0)) ? "_" + name : name;
	}

	protected String typeOf(Display element) {
		String type = element.getClass().getSimpleName();
		boolean temporalCatalog = type.equalsIgnoreCase("temporalCatalog");
		return temporalCatalog ? "temporal" + element.a$(TemporalCatalog.class).type().name() + "Catalog" : type;
	}

	protected File createIfNotExists(File file) {
		if (!file.exists()) file.mkdirs();
		return file;
	}

}
