package io.intino.pandora.plugin.codegeneration.server.activity.display;

import io.intino.pandora.model.Activity;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.date.DateData;
import io.intino.pandora.model.type.TypeData;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.model.Activity.Display.Request.ResponseType.Asset;

public class DisplayRenderer {

	private static final String DISPLAYS = "displays";
	private static final String NOTIFIERS = "notifiers";
	private static final String REQUESTERS = "requesters";
	private final File gen;
	private final File src;
	private final String packageName;
	private final List<Activity.Display> displays;
	private final String boxName;

	public DisplayRenderer(Graph graph, File src, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.displays = graph.find(Activity.Display.class);
		this.boxName = boxName;
	}

	public void execute() {
		displays.forEach(this::processDisplay);
	}

	private void processDisplay(Activity.Display display) {
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
		if (!Commons.javaFile(new File(src, DISPLAYS), snakeCaseToCamelCase(display.name() + "Display")).exists())
			Commons.writeFrame(new File(src, DISPLAYS), snakeCaseToCamelCase(display.name() + "Display"), displayTemplate().format(frame));
	}

	private Frame[] framesOfNotifications(List<Activity.Display.Notification> notifications) {
		List<Frame> frames = notifications.stream().map(this::frameOf).collect(Collectors.toList());
		return frames.toArray(new Frame[frames.size()]);
	}

	private Frame frameOf(Activity.Display.Notification notification) {
		final Frame frame = new Frame().addTypes("notification");
		frame.addSlot("name", notification.name());
		frame.addSlot("target", notification.to().name());
		if (notification.asType() != null) frame.addSlot("parameter", notification.asType().type());
		return frame;
	}

	private Frame[] framesOfRequests(List<Activity.Display.Request> requests) {
		List<Frame> frames = requests.stream().map(this::frameOf).collect(Collectors.toList());
		return frames.toArray(new Frame[frames.size()]);
	}

	private Frame frameOf(Activity.Display.Request request) {
		final Frame frame = new Frame().addTypes("request");
		if (request.responseType().equals(Asset)) frame.addTypes("asset");
		frame.addSlot("name", request.name());
		if (request.asType() != null) frame.addSlot("parameter", type(request));
		return frame;
	}

	private String type(Activity.Display.Request request) {
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
