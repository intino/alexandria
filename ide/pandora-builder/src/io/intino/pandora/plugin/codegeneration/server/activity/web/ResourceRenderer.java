package io.intino.pandora.plugin.codegeneration.server.activity.web;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Activity;
import io.intino.pandora.plugin.codegeneration.action.UIActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class ResourceRenderer {


	private static final String RESOURCES = "resources";
	private final Project project;
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Activity.AbstractPage> pages;

	public ResourceRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.pages = graph.find(Activity.AbstractPage.class);
	}

	public void execute() {
		pages.forEach(this::processPage);
	}

	private void processPage(Activity.AbstractPage page) {
		Frame frame = new Frame().addTypes("page");
		frame.addSlot("package", packageName);
		frame.addSlot("name", page.name());
		frame.addSlot("box", boxName);
		Commons.writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(page.name() + "Resource"), template().format(frame));
		createCorrespondingAction(page);
	}

	private void createCorrespondingAction(Activity.AbstractPage page) {
		new UIActionRenderer(project, page, src, packageName, boxName).execute();
	}

	private Template template() {
		Template template = ResourceTemplate.create();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
