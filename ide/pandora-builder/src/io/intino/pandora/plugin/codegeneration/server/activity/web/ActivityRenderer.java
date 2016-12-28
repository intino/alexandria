package io.intino.pandora.plugin.codegeneration.server.activity.web;

import io.intino.pandora.model.Activity;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

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
		Frame frame = new Frame().addTypes("activity").
				addSlot("package", packageName).
				addSlot("name", activity.name()).
				addSlot("box", boxName).addSlot("resource", resourcesFrame(activity.abstractPageList())).
				addSlot("display", displaysFrame(activity.displayList()));
		if (activity.authenticated() != null) frame.addSlot("auth", activity.authenticated().by());
		Commons.writeFrame(gen, snakeCaseToCamelCase(activity.name() + "Activity"), template().format(frame));
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
