package io.intino.konos.builder.codegeneration.services.activity.display;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Display;
import io.intino.konos.model.Schema;
import io.intino.konos.model.date.DateData;
import io.intino.konos.model.type.TypeData;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.model.Display.Request.ResponseType.Asset;

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

	public DisplayRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.displays = graph.find(Display.class);
		this.boxName = boxName;
	}

	public void execute() {
		displays.forEach(this::processDisplay);
	}

	private void processDisplay(Display display) {
		Frame frame = new Frame().addTypes("display");
		frame.addSlot("package", packageName);
		frame.addSlot("name", display.name());
		if (!display.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		frame.addSlot("notification", framesOfNotifications(display.notificationList()));
		frame.addSlot("request", framesOfRequests(display.requestList()));
		frame.addSlot("box", boxName);
		Commons.writeFrame(new File(gen, DISPLAYS + File.separator + NOTIFIERS), snakeCaseToCamelCase(display.name() + "DisplayNotifier"), displayNotifierTemplate().format(frame));
		Commons.writeFrame(new File(gen, DISPLAYS + File.separator + REQUESTERS), snakeCaseToCamelCase(display.name() + "DisplayRequester"), displayRequesterTemplate().format(frame));
		final String newDisplay = snakeCaseToCamelCase(display.name() + "Display");
		if (!Commons.javaFile(new File(src, DISPLAYS), newDisplay).exists())
			Commons.writeFrame(new File(src, DISPLAYS), newDisplay, displayTemplate().format(frame));
		else new DisplayUpdater(project, display, Commons.javaFile(new File(src, DISPLAYS), newDisplay)).update();
	}


	private Frame[] framesOfNotifications(List<Display.Notification> notifications) {
		List<Frame> frames = notifications.stream().map(this::frameOf).collect(Collectors.toList());
		return frames.toArray(new Frame[frames.size()]);
	}

	private Frame frameOf(Display.Notification notification) {
		final Frame frame = new Frame().addTypes("notification");
		frame.addSlot("name", notification.name());
		frame.addSlot("target", notification.to().name());
		if (notification.asType() != null) {
			final Frame parameterFrame = new Frame().addTypes("parameter").addSlot("value", notification.asType().type());
			if (notification.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
		return frame;
	}

	private Frame[] framesOfRequests(List<Display.Request> requests) {
		List<Frame> frames = requests.stream().map(DisplayRenderer::frameOf).collect(Collectors.toList());
		return frames.toArray(new Frame[frames.size()]);
	}

	static Frame frameOf(Display.Request request) {
		final Frame frame = new Frame().addTypes("request");
		if (request.responseType().equals(Asset)) frame.addTypes("asset");
		frame.addSlot("name", request.name());
		if (request.asType() != null) {
			final Frame parameterFrame = new Frame().addTypes("parameter").addSlot("value", request.asType().type());
			if (request.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
		return frame;
	}

	private String type(Display.Request request) {
		final TypeData typeData = request.asType();
		if (typeData.is(DateData.class)) return "Long";
		else return typeData.type();
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
