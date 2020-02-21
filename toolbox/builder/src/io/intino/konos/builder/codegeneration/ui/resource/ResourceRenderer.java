package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Service;

import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class ResourceRenderer extends UIRenderer {
	protected final Service.UI.Resource resource;

	public ResourceRenderer(CompilationContext compilationContext, Service.UI.Resource resource, Target target) {
		super(compilationContext, target);
		this.resource = resource;
	}

	@Override
	public void render() {
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);

		FrameBuilder builder = buildFrame().add("resource").add("name", resource.name$()).add("parameter", parameters(resource));
		if (uiService.googleApiKey() != null) builder.add("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) builder.add("confidential", "");
		Commons.writeFrame(resourceFolder(gen(), target), resourceFilename(resource.name$()), setup(new ResourceTemplate()).render(builder.toFrame()));
		if (target.equals(Target.Owner))
			context.compiledFiles().add(new OutputItem(javaFile(resourceFolder(gen(), target), resourceFilename(resource.name$())).getAbsolutePath()));
		new PageRenderer(context, resource).execute();
	}

	private FrameBuilder[] parameters(Service.UI.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}

}