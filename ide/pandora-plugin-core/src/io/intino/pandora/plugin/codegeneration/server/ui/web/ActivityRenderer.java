package io.intino.pandora.plugin.codegeneration.server.ui.web;

import io.intino.pandora.plugin.Activity;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class ActivityRenderer {

	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Activity> activities;

	public ActivityRenderer(Graph graph, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.activities = graph.find(Activity.class);
	}


	public void execute() {
		activities.forEach(this::processActivity);
	}

	private void processActivity(Activity activity) {
		Frame frame = new Frame().addTypes("activity");
		frame.addSlot("package", packageName);
		frame.addSlot("name", activity.name());
		frame.addSlot("box", boxName);
		if (activity.authenticated() != null) frame.addSlot("auth", activity.authenticated().by());
		frame.addSlot("resource", resourcesFrame(activity.abstractPageList()));
		frame.addSlot("display", displaysFrame(activity.displayList()));
		frame.addSlot("auth", displaysFrame(activity.displayList()));
		writeFrame(gen, snakeCaseToCamelCase(activity.name() + "Activity"), template().format(frame));
	}

	private Frame[] resourcesFrame(List<Activity.AbstractPage> pages) {
		List<Frame> frames = pages.stream().map(this::frameOf).collect(Collectors.toList());
		return frames.toArray(new Frame[frames.size()]);
	}

	private Frame[] displaysFrame(List<Activity.Display> displays) {
		List<Frame> frames = displays.stream().map(this::frameOf).collect(Collectors.toList());
		return frames.toArray(new Frame[frames.size()]);
	}

	private Frame frameOf(Activity.AbstractPage resource) {
		final Frame frame = new Frame().addTypes("resource", "abstractPage");
		frame.addSlot("name", resource.name());
		for (String path : resource.paths())
			frame.addSlot("path", new Frame().addSlot("value", path).addSlot("name", resource.name()));
		return frame;
	}

	private Frame frameOf(Activity.Display display) {
		final Frame frame = new Frame().addTypes("display");
		frame.addSlot("name", display.name());
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Activity.Display.Request.ResponseType.Asset)))
			frame.addSlot("asset", display.name());
		return frame;
	}

	private Template template() {
		Template template = ActivityTemplate.create();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
