package io.intino.konos.builder.codegeneration.services.activity.resource;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.action.UIActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static java.util.stream.Collectors.toList;

public class ResourceRenderer {


	private static final String RESOURCES = "resources";
	private final Project project;
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Activity.AbstractPage> pages;

	public ResourceRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.pages = graph.core$().find(Activity.AbstractPage.class);
	}

	public void execute() {
		pages.forEach(this::processPage);
	}

	private void processPage(Activity.AbstractPage page) {
		Frame frame = new Frame().addTypes("page");
		frame.addSlot("package", packageName);
		frame.addSlot("name", page.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("parameter", parameters(page));
		if (page.core$().ownerAs(Activity.class).googleApiKey() != null)
			frame.addSlot("googleApiKey", page.core$().ownerAs(Activity.class).googleApiKey());
		if (page.isRestricted()) frame.addSlot("restrict", "");
		Commons.writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(page.name$() + "Resource"), template().format(frame));
		createCorrespondingAction(page);
	}

	private void createCorrespondingAction(Activity.AbstractPage page) {
		new UIActionRenderer(project, page, src, gen, packageName, boxName).execute();
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

	private Frame[] parameters(Activity.AbstractPage page) {
		List<String> parameters = page.paths().stream().filter(path -> path.contains(":"))
				.map(Commons::extractUrlPathParameters).flatMap(Collection::stream).collect(toList());

		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("name", parameter)).collect(toList()).toArray(new Frame[0]);
	}

}
