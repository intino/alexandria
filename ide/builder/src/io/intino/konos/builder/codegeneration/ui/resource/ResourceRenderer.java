package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;

public class ResourceRenderer extends UIRenderer {
	protected final UIService.Resource resource;

	public ResourceRenderer(Settings settings, UIService.Resource resource, Target target) {
		super(settings, target);
		this.resource = resource;
	}

	public void execute() {
		UIService uiService = resource.core$().ownerAs(UIService.class);

		Frame frame = buildFrame().addTypes("resource").addSlot("name", resource.name$()).addSlot("parameter", parameters(resource));
		if (uiService.googleApiKey() != null) frame.addSlot("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) frame.addSlot("confidential", "");
		Commons.writeFrame(new File(gen(), format(Resources)), snakeCaseToCamelCase(resource.name$() + "Resource"), setup(ResourceTemplate.create()).format(frame));

		new PageRenderer(settings, resource).execute();
	}

	private Frame[] parameters(UIService.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

}