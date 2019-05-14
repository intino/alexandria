package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.ui.UIService;

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

	@Override
	public void render() {
		UIService uiService = resource.core$().ownerAs(UIService.class);

		FrameBuilder builder = frameBuilder().add("resource").add("name", resource.name$()).add("parameter", parameters(resource));
		if (uiService.googleApiKey() != null) builder.add("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) builder.add("confidential", "");
		Commons.writeFrame(new File(gen(), format(Resources)), snakeCaseToCamelCase(resource.name$() + "Resource"), setup(new ResourceTemplate()).render(builder.toFrame()));

		new PageRenderer(settings, resource).execute();
	}

	private FrameBuilder[] parameters(UIService.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}

}