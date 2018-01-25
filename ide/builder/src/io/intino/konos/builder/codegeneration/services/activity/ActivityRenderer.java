package io.intino.konos.builder.codegeneration.services.activity;

import io.intino.konos.builder.codegeneration.services.activity.resource.AssetResourceLoaderTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.Display.Request.ResponseType.Asset;
import static io.intino.konos.model.graph.KonosGraph.dialogsOf;
import static io.intino.konos.model.graph.KonosGraph.displaysOf;

public class ActivityRenderer {
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Activity> activities;

	public ActivityRenderer(KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.activities = graph.activityList();
	}

	public void execute() {
		activities.forEach(this::processActivity);
	}

	private void processActivity(Activity activity) {
		final List<Dialog> dialogs = dialogsOf(activity);
		final List<Display> displays = displaysOf(activity);
		Frame frame = new Frame().addTypes("activity").
				addSlot("package", packageName).
				addSlot("name", activity.name$()).
				addSlot("box", boxName).addSlot("resource", resourcesFrame(activity.abstractPageList()));
		if (activity.userHome() != null) frame.addSlot("userHome", activity.userHome().name$());
		if (!dialogs.isEmpty())
			frame.addSlot("dialog", dialogsFrame(dialogs)).addSlot("dialogsImport", packageName);
		if (!displays.isEmpty())
			frame.addSlot("display", displaysFrame(displays)).addSlot("displaysImport", packageName);
		if (activity.authenticated() != null) frame.addSlot("auth", activity.authenticated().by());
		if (!Commons.javaFile(src, "AssetResourceLoader").exists())
			writeFrame(src, "AssetResourceLoader", AssetResourceLoaderTemplate.create().format(resourceLoaderFrame()));
		writeFrame(gen, snakeCaseToCamelCase(activity.name$() + "Activity"), template().format(frame));
	}

	private Frame resourceLoaderFrame() {
		return new Frame().addTypes("resourceloader").addSlot("package", packageName).addSlot("box", boxName);
	}

	private Frame[] resourcesFrame(List<Activity.AbstractPage> pages) {
		return pages.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] displaysFrame(List<Display> displays) {
		return displays.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] dialogsFrame(List<Dialog> dialogs) {
		return dialogs.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Activity.AbstractPage resource) {
		final Frame frame = new Frame().addTypes("resource", "abstractPage");
		frame.addSlot("name", resource.name$());
		final Activity activity = resource.core$().ownerAs(Activity.class);
		for (String path : resource.paths()) {
			Set<String> custom = Commons.extractParameters(path);
			Frame pathFrame = new Frame().addSlot("value", path).addSlot("name", resource.name$());
			if (activity.userHome() != null) pathFrame.addSlot("userHome", activity.userHome().name$());
			if (!custom.isEmpty()) pathFrame.addSlot("custom", custom.toArray(new String[custom.size()]));
			frame.addSlot("path", pathFrame);
		}
		return frame;
	}

	private Frame frameOf(Display display) {
		final Frame frame = new Frame().addTypes("display");
		frame.addSlot("name", display.name$()).addSlot("package", packageName);
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Asset)))
			frame.addSlot("asset", display.name$());
		return frame;
	}

	private Frame frameOf(Dialog dialog) {
		return new Frame().addTypes("dialog").addSlot("name", dialog.name$()).addSlot("package", packageName);
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
