package io.intino.konos.builder.codegeneration.ui;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.ChildComponents;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.PrivateComponents;
import io.intino.konos.model.graph.temporal.TemporalCatalog;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.magritte.Layer;
import io.intino.tara.magritte.Node;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.javascriptFile;
import static io.intino.konos.model.graph.Utils.isUUID;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;

public abstract class UIRenderer {
	protected Settings settings;
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
		this.target = target;
	}

	public abstract void execute();

	public Frame buildFrame() {
		return baseFrame();
	}

	public Frame baseFrame() {
		return new Frame().addSlot("box", boxName()).addSlot("package", settings.packageName());
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

	protected String nameOf(Layer element) {
		String result = element.name$();
		if (!isUUID(result)) return result;
		return generateName(element);
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

	protected String shortId(Layer element) {
		return shortId(element, "");
	}

	protected String ownerId(Layer element) {
		Node owner = element.core$().owner();
		List<String> result = new ArrayList<>();
		while (owner != null) {
			result.add(shortId(owner.as(Layer.class)));
			owner = owner.owner();
		}
		reverse(result);
		return String.join(".", result);
	}

	protected String shortId(Layer element, String suffix) {
		return settings.idGenerator().shortId(nameOf(element) + suffix);
	}

	private String generateName(Layer element) {
		return generateName(element.core$(), "");
	}

	private String generateName(Node element, String name) {
		Node owner = element.owner();
		if (isRoot(owner)) return owner.name() + position(element, owner);
		return generateName(owner, name) + position(element, owner);
	}

	private int position(Node element, Node owner) {
		List<Node> children = owner.componentList();
		for (int pos = 0; pos< children.size(); pos++)
			if (children.get(pos).id().equals(element.id())) return pos;
		return -1;
	}

	private boolean isRoot(Node node) {
		return node.owner() == null || node.owner() == node.model();
	}

}