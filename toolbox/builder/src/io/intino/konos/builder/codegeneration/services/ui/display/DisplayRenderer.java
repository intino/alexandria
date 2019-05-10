package io.intino.konos.builder.codegeneration.services.ui.display;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.catalog.CatalogRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.desktop.DesktopRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.editor.EditorRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.mold.MoldRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.panel.PanelRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.konos.model.graph.desktop.DesktopPanel;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.Display.Request.ResponseType.Asset;

@SuppressWarnings("Duplicates")
public class DisplayRenderer extends UIRenderer {
	private final Project project;
	private final File gen;
	private final File src;
	private final List<Display> displays;
	private final Map<String, String> classes;
	private final String parent;

	public DisplayRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String parent, String boxName, Map<String, String> classes) {
		super(boxName, packageName);
		this.project = project;
		this.gen = gen;
		this.src = src;
		this.parent = parent;
		this.displays = graph.displayList();
		this.classes = classes;
	}

	static FrameBuilder frameOf(Display.Request request, String packageName) {
		final FrameBuilder builder = new FrameBuilder("request");
		if (request.responseType().equals(Asset)) builder.add("asset");
		builder.add("name", request.name$());
		if (request.isType()) {
			final FrameBuilder parameterFrame = new FrameBuilder("parameter", request.asType().type(), request.asType().getClass().getSimpleName().replace("Data", "")).add("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.add("list");
			builder.add("parameter", parameterFrame.toFrame());
		}
		return builder;
	}

	private static String parameter(Display.Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}

	public void execute() {
		displays.forEach(this::processDisplay);
	}

	private void processDisplay(Display display) {
		if (display == null) return;
		FrameBuilder builder = createFrameBuilder(display);
		writeNotifier(display, builder.toFrame());
		writeRequester(display, builder.toFrame());
		if (display.getClass().getSimpleName().equals(Display.class.getSimpleName())) writeDisplay(display, builder.toFrame());
		else processPrototype(display);
		if (display.isAccessible()) writeDisplaysFor(display.asAccessible(), builder);
	}

	private void processPrototype(Display display) {
		classes.put(display.getClass().getSimpleName() + "#" + display.name$(), DISPLAYS + "." + snakeCaseToCamelCase(display.name$()));
		if (display.i$(Editor.class)) new EditorRenderer(project, display.a$(Editor.class), packageName, box).write(src, gen);
		else if (display.i$(Catalog.class))
			new CatalogRenderer(project, display.a$(Catalog.class), packageName, box).write(src, gen);
		else if (display.i$(Panel.class)) {
			Panel panel = display.a$(Panel.class);
			if (panel.isDesktop()) {
				DesktopPanel desktop = display.a$(Panel.class).asDesktop();
				new DesktopRenderer(project, desktop, packageName, box).write(src, gen);
			} else new PanelRenderer(project, display.a$(Panel.class), packageName, box).write(src, gen);
		} else if (display.i$(Mold.class)) new MoldRenderer(project, display.a$(Mold.class), packageName, box).write(src, gen);
	}

	private void writeDisplay(Display display, Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		classes.put("Display#" + display.name$(), DISPLAYS + "." + newDisplay);
		if (!javaFile(new File(src, DISPLAYS), newDisplay).exists())
			writeFrame(new File(src, DISPLAYS), newDisplay, displayTemplate().render(frame));
		else new DisplayUpdater(project, display, javaFile(new File(src, DISPLAYS), newDisplay), packageName).update();
	}

	private void writeRequester(Display display, Frame frame) {
		writeFrame(new File(gen, REQUESTERS), snakeCaseToCamelCase(display.name$() + (frame.is("accessible") ? "Proxy" : "") + "Requester"), displayRequesterTemplate().render(frame));
	}

	private void writeNotifier(Display display, Frame frame) {
		writeFrame(new File(gen, NOTIFIERS), snakeCaseToCamelCase(display.name$() + (frame.is("accessible") ? "Proxy" : "") + "Notifier"), displayNotifierTemplate().render(frame));
	}

	private void writeDisplaysFor(AccessibleDisplay display, FrameBuilder builder) {
		builder.add("accessible");
		final String name = snakeCaseToCamelCase(display.name$());
		writeFrame(new File(src, DISPLAYS), name + "Proxy", displayTemplate().render(builder.add("accessible")));
		writeNotifier(display.a$(Display.class), builder.toFrame());
		writeRequester(display.a$(Display.class), builder.toFrame());
	}

	@NotNull
	private FrameBuilder createFrameBuilder(Display display) {
		FrameBuilder builder = new FrameBuilder("display");
		builder.add("package", packageName);
		builder.add("name", display.name$());
		builder.add("type", typeOf(display));
		builder.add("innerDisplay", display.displays().stream().map(Layer::name$).toArray(String[]::new));
		if (display.parentDisplay() != null) addParent(display, builder);
		if (!display.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
		builder.add("notification", framesOfNotifications(display.notificationList()));
		builder.add("request", framesOfRequests(display.requestList()));
		builder.add("box", box);
		if (display.isAccessible())
			builder.add("parameter", display.asAccessible().parameters().stream().map(p -> new FrameBuilder("parameter", "accessible").add("value", p).toFrame()).toArray(Frame[]::new));
		return builder;
	}

	private String typeOf(Display display) {
		String type = display.getClass().getSimpleName().toLowerCase();
		if (display.i$(DesktopPanel.class)) return "desktop";
		if (type.equalsIgnoreCase("temporalCatalog")) return "temporal" + display.a$(TemporalCatalog.class).type().name() + "Catalog";
		else return type;
	}

	private void addParent(Display display, FrameBuilder builder) {
		builder.add("parent", new FrameBuilder().add("value", display.parentDisplay()).add("dsl", this.parent).add("package", this.parent.substring(0, this.parent.lastIndexOf("."))));
	}

	private Frame[] framesOfNotifications(List<Display.Notification> notifications) {
		return notifications.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Display.Notification notification) {
		final FrameBuilder builder = new FrameBuilder("notification");
		builder.add("name", notification.name$());
		builder.add("target", notification.to().name());
		if (notification.isType()) {
			final FrameBuilder parameterFrame = new FrameBuilder("parameter", notification.asType().type(), notification.asType().getClass().getSimpleName().replace("Data", "")).add("value", notification.asType().type());
			if (notification.isList()) parameterFrame.add("list");
			builder.add("parameter", parameterFrame);
		}
		return builder.toFrame();
	}

	private Frame[] framesOfRequests(List<Display.Request> requests) {
		return requests.stream().map(r -> frameOf(r, this.packageName).toFrame()).toArray(Frame[]::new);
	}

	private Template displayNotifierTemplate() {
		Template template = new DisplayNotifierTemplate();
		addFormats(template);
		return template;
	}

	private Template displayTemplate() {
		Template template = new DisplayTemplate();
		addFormats(template);
		return template;
	}

	private Template displayRequesterTemplate() {
		Template template = new DisplayRequesterTemplate();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
