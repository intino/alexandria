package io.intino.konos.builder.codegeneration.services.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;

public class ResourceRenderer extends UIRenderer {
	private final UIService.Resource resource;

	public ResourceRenderer(Settings settings, UIService.Resource resource) {
		super(settings);
		this.resource = resource;
	}

	public void execute() {
		UIService uiService = resource.core$().ownerAs(UIService.class);

		Frame frame = baseFrame().addTypes("resource").addSlot("name", resource.name$()).addSlot("parameter", parameters(resource));
		if (resource.isEditorPage()) frame.addSlot("editor", "Editor");
		if (uiService.googleApiKey() != null) frame.addSlot("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) frame.addSlot("confidential", "");
		Commons.writeFrame(new File(gen(), Resources), snakeCaseToCamelCase(resource.name$() + "Resource"), setup(ResourceTemplate.create()).format(frame));

		new UIPageRenderer(settings, resource).execute();
	}

	private Frame[] parameters(UIService.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

}