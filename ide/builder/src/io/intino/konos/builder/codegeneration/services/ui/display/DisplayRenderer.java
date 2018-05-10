package io.intino.konos.builder.codegeneration.services.ui.display;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.display.catalog.CatalogRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.desktop.DesktopRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.mold.MoldRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.panel.PanelRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.desktop.DesktopPanel;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.Display.Request.ResponseType.Asset;
import static java.io.File.separator;

@SuppressWarnings("Duplicates")
public class DisplayRenderer {
	private static final String DISPLAYS = "displays";
	private static final String NOTIFIERS = "notifiers";
	private static final String REQUESTERS = "requesters";
	private final Project project;
	private final File gen;
	private final File src;
	private final String packageName;
	private final List<Display> displays;
	private final String boxName;
	private final String parent;

	public DisplayRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String parent, String boxName) {
		this.project = project;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.parent = parent;
		this.displays = graph.displayList();
		this.boxName = boxName;
	}

	public void execute() {
		displays.forEach(this::processDisplay);
	}

	private void processDisplay(Display display) {
		if (display == null) return;
		Frame frame = createFrame(display);
		writeNotifier(display, frame);
		writeRequester(display, frame);
		if (display.getClass().getSimpleName().equals(Display.class.getSimpleName())) writeDisplay(display, frame);
		else processPrototype(display);
	}

	private void processPrototype(Display display) {
		if (display.i$(Catalog.class)) new CatalogRenderer(project, display.a$(Catalog.class), packageName, boxName).write(src, gen);
		else if (display.i$(Panel.class)) {
			Panel panel = display.a$(Panel.class);
			if (panel.isDesktop()) {
				DesktopPanel desktop = display.a$(Panel.class).asDesktop();
				new DesktopRenderer(project, desktop, packageName, boxName).write(src, gen);
			} else new PanelRenderer(project, display.a$(Panel.class), packageName, boxName).write(src, gen);
		} else if (display.i$(Mold.class)) new MoldRenderer(project, display.a$(Mold.class), packageName, boxName).write(src, gen);
	}

	private void writeDisplay(Display display, Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		if (!javaFile(new File(src, DISPLAYS), newDisplay).exists())
			writeFrame(new File(src, DISPLAYS), newDisplay, displayTemplate().format(frame));
		else new DisplayUpdater(project, display, javaFile(new File(src, DISPLAYS), newDisplay), packageName).update();
	}

	private void writeRequester(Display display, Frame frame) {
		writeFrame(new File(gen, DISPLAYS + separator + REQUESTERS), snakeCaseToCamelCase(display.name$() + "Requester"), displayRequesterTemplate().format(frame));
	}

	private void writeNotifier(Display display, Frame frame) {
		writeFrame(new File(gen, DISPLAYS + separator + NOTIFIERS), snakeCaseToCamelCase(display.name$() + "Notifier"), displayNotifierTemplate().format(frame));
	}

	@NotNull
	private Frame createFrame(Display display) {
		Frame frame = new Frame().addTypes("display");
		frame.addSlot("package", packageName);
		frame.addSlot("name", display.name$());
		frame.addSlot("type", typeOf(display));
		frame.addSlot("innerDisplay", display.displays().stream().map(Layer::name$).toArray(String[]::new));
		if (display.parentDisplay() != null) addParent(display, frame);
		if (!display.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		frame.addSlot("notification", framesOfNotifications(display.notificationList()));
		frame.addSlot("request", framesOfRequests(display.requestList()));
		frame.addSlot("box", boxName);
		return frame;
	}

	private String typeOf(Display display) {
		String type = display.getClass().getSimpleName().toLowerCase();
		if (display.i$(DesktopPanel.class)) return "desktop";
		if (type.equalsIgnoreCase("temporalCatalog")) return "temporal" + display.a$(TemporalCatalog.class).type().name() + "Catalog";
		else return type;
	}

	private void addParent(Display display, Frame frame) {
		final Frame parent = new Frame().addSlot("value", display.parentDisplay()).addSlot("dsl", this.parent).addSlot("package", this.parent.substring(0, this.parent.lastIndexOf(".")));
		frame.addSlot("parent", parent);
	}

	private Frame[] framesOfNotifications(List<Display.Notification> notifications) {
		return notifications.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Display.Notification notification) {
		final Frame frame = new Frame().addTypes("notification");
		frame.addSlot("name", notification.name$());
		frame.addSlot("target", notification.to().name());
		if (notification.asType() != null) {
			final Frame parameterFrame = new Frame().addTypes("parameter", notification.asType().type(), notification.asType().getClass().getSimpleName().replace("Data", "")).addSlot("value", notification.asType().type());
			if (notification.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
		return frame;
	}

	private Frame[] framesOfRequests(List<Display.Request> requests) {
		return requests.stream().map(r -> frameOf(r, this.packageName)).toArray(Frame[]::new);
	}

	static Frame frameOf(Display.Request request, String packageName) {
		final Frame frame = new Frame().addTypes("request");
		if (request.responseType().equals(Asset)) frame.addTypes("asset");
		frame.addSlot("name", request.name$());
		if (request.asType() != null) {
			final Frame parameterFrame = new Frame().addTypes("parameter", request.asType().type(), request.asType().getClass().getSimpleName().replace("Data", "")).addSlot("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
		return frame;
	}

	private static String parameter(Display.Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}

	private Template displayNotifierTemplate() {
		Template template = DisplayNotifierTemplate.create();
		addFormats(template);
		return template;
	}

	private Template displayTemplate() {
		Template template = DisplayTemplate.create();
		addFormats(template);
		return template;
	}

	private Template displayRequesterTemplate() {
		Template template = DisplayRequesterTemplate.create();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
