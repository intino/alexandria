package io.intino.konos.builder.codegeneration.services.ui.resource;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.action.UIActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;

public class ResourceRenderer extends UIRenderer {
	private final Project project;
	private final File src;
	private final File gen;
	private final List<UIService.Resource> resourceList;
	private final Map<String, String> classes;
	private final List<AccessibleDisplay> accessibleDisplays;

	public ResourceRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		super(boxName, packageName);
		this.project = project;
		this.src = src;
		this.gen = gen;
		this.resourceList = graph.core$().find(UIService.Resource.class);
		this.accessibleDisplays = graph.accessibleDisplayList();
		this.classes = classes;
	}

	public void execute() {
		resourceList.forEach(this::processResource);
		accessibleDisplays.forEach(this::processDisplay);
	}

	private void processDisplay(AccessibleDisplay display) {
		Frame frame = buildFrame().addSlot("name", display.name$()).addTypes("resource", display.getClass().getSimpleName());
		frame.addSlot("parameter", parameters(display));
		Commons.writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(display.name$() + "ProxyResource"), template().format(frame));
		createCorrespondingAction(display);
	}

	private void processResource(UIService.Resource resource) {
		UIService uiService = resource.core$().ownerAs(UIService.class);
		Frame frame = buildFrame().addTypes("resource").addSlot("name", resource.name$()).addSlot("parameter", parameters(resource));
		if (resource.isEditorPage()) frame.addSlot("editor", "Editor");
		if (uiService.googleApiKey() != null) frame.addSlot("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) frame.addSlot("confidential", "");
		Commons.writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(resource.name$() + "Resource"), template().format(frame));
		createCorrespondingAction(resource);
	}

	protected Frame buildFrame() {
		return new Frame().addSlot("box", box).addSlot("package", packageName);
	}

	private void createCorrespondingAction(UIService.Resource resource) {
		new UIActionRenderer(project, resource, src, gen, packageName, box, classes).execute();
	}

	private void createCorrespondingAction(AccessibleDisplay display) {
		new AccessibleDisplayActionRenderer(project, display, src, packageName, box, classes).execute();
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

	private Frame[] parameters(UIService.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

	private Frame[] parameters(AccessibleDisplay display) {
		return display.parameters().stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}
}