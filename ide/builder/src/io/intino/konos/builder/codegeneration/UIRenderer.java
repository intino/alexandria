package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.temporal.TemporalCatalog;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Map;

public abstract class UIRenderer {
	protected Settings settings;

	public static final String UI = "ui";
	public static final String Resources = UI + "/resources";
	public static final String Notifiers = UI + "/displays/notifiers";
	public static final String Requesters = UI + "/displays/requesters";

	public static final String Pages = UI + "/pages";
	public static final String Displays = UI + "/displays";
	public static final String DisplaysType = UI + "/displays/%ss";

	protected UIRenderer(Settings settings) {
		this.settings = settings;
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
		return settings.src();
	}

	protected File gen() {
		return settings.gen();
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

	protected String path(io.intino.konos.model.graph.Display display) {
		String type = typeOf(display);
		return type.equalsIgnoreCase("display") ? Displays : String.format(DisplaysType, type).toLowerCase();
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
}
