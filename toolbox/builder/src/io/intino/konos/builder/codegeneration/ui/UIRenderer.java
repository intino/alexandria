package io.intino.konos.builder.codegeneration.ui;

import com.intellij.openapi.project.Project;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.temporal.TemporalCatalog;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.util.Map;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.javascriptFile;

public abstract class UIRenderer {
	protected Settings settings;
	private final ElementHelper elementHelper;
	protected final Target target;

	public static final String UI = "ui";
	public static final String Resources = "%sresources";
	public static final String Sources = "%ssources";
	public static final String Notifiers = "%sdisplays/notifiers";
	public static final String Requesters = "%sdisplays/requesters";

	public static final String Pages = "%spages";
	public static final String Displays = "%sdisplays";
	public static final String DisplaysType = "%sdisplays/%ss";

	public enum Target {Accessor, Service}

	protected UIRenderer(Settings settings, Target target) {
		this.settings = settings;
		this.elementHelper = new ElementHelper();
		this.target = target;
	}

	public abstract void execute();

	public FrameBuilder frameBuilder() {
		return baseFrameBuilder();
	}

	public FrameBuilder baseFrameBuilder() {
		return new FrameBuilder().add("box", boxName()).add("package", settings.packageName());
	}

	public Project project() {
		return settings.project();
	}

	public String boxName() {
		return settings.boxName();
	}

	protected File fileOf(File file, String name) {
		if (target == Target.Service) return javaFile(file, name);
		return javascriptFile(file, name);
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
		return path(typeOf(display));
	}

	protected String path(String displayType) {
		return displayType.equalsIgnoreCase("display") ? format(Displays) : String.format(DisplaysType, uiSubPath(), displayType).toLowerCase();
	}

	protected Template addFormats(Template template) {
		Formatters.customize(template);
		return template;
	}

	protected String clean(String name) {
		name = name.replaceAll("-", "");
		return Character.isDigit(name.charAt(0)) ? "_" + name : name;
	}

	protected String typeOf(PassiveView element) {
		if (element.i$(Display.class)) return typeOf(element.a$(Display.class));
		return element.getClass().getSimpleName();
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

	protected String nameOf(Layer element) {
		return elementHelper.nameOf(element);
	}

	protected String shortId(Layer element) {
		return elementHelper.shortId(element);
	}

	protected String shortId(Layer element, String suffix) {
		return elementHelper.shortId(element, suffix);
	}

	protected boolean isRendered(Layer element) {
		return settings.cache().containsKey(element.name$());
	}

	protected void saveRendered(Layer element) {
		settings.cache().add(element);
	}

}