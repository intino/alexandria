package io.intino.konos.builder.codegeneration.services.ui.resource;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.action.UIActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

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
		writeFrame(new File(gen, RESOURCES), name(display.name$(), "ProxyResource"), template()
				.render(frameBuilder().add("name", display.name$()).add("resource").add(display.getClass().getSimpleName()).add("parameter", parameters(display)).toFrame()));
		createCorrespondingAction(display);
	}

	private void processResource(UIService.Resource resource) {
		UIService uiService = resource.core$().ownerAs(UIService.class);
		FrameBuilder builder = frameBuilder().add("resource").add("name", resource.name$()).add("parameter", parameters(resource));
		if (resource.isEditorPage()) builder.add("editor", "Editor");
		if (uiService.googleApiKey() != null) builder.add("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) builder.add("confidential", "");
		writeFrame(new File(gen, RESOURCES), name(resource.name$(), "Resource"), template().render(builder.toFrame()));
		createCorrespondingAction(resource);
	}

	private String name(String s, String proxyResource) {
		return snakeCaseToCamelCase(s + proxyResource);
	}

	private void createCorrespondingAction(UIService.Resource resource) {
		new UIActionRenderer(project, resource, src, gen, packageName, box, classes).execute();
	}

	private void createCorrespondingAction(AccessibleDisplay display) {
		new AccessibleDisplayActionRenderer(project, display, src, packageName, box, classes).execute();
	}


	private Template template() {
		return customized(new ResourceTemplate());
	}

	private Template customized(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private Frame[] parameters(UIService.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		return parameters.stream()
				.map(parameter -> new FrameBuilder("parameter").add("name", parameter).toFrame())
				.toArray(Frame[]::new);
	}

	private Frame[] parameters(AccessibleDisplay display) {
		return display.parameters().stream().map(parameter -> new FrameBuilder("parameter")
				.add("name", parameter).toFrame()).toArray(Frame[]::new);
	}
}