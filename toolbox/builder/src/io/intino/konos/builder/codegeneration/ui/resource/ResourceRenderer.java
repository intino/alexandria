package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Service;
import io.intino.magritte.framework.Layer;

import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class ResourceRenderer extends UIRenderer {
	protected final Service.UI.Resource resource;

	public ResourceRenderer(CompilationContext compilationContext, Service.UI.Resource resource, Target target) {
		super(compilationContext, target);
		this.resource = resource;
	}

	@Override
	public void render() throws KonosException {
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);

		FrameBuilder builder = buildFrame().add("resource").add("name", resource.name$()).add("parameter", parameters(resource));
		if (resource.isStaticPage()) builder.add("static");
		if (uiService.googleApiKey() != null) builder.add("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) builder.add("confidential", "");

		Commons.writeFrame(resourceFolder(gen(), target), resourceFilename(resource.name$()), setup(new ResourceTemplate()).render(builder.toFrame()));
		if (target.equals(Target.Owner))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(resourceFolder(gen(), target), resourceFilename(resource.name$())).getAbsolutePath()));

		new PageRenderer(context, resource).execute();
	}

	private FrameBuilder[] parameters(Service.UI.Resource resource) {
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		parameters.addAll(resource.parameterList().stream().map(Layer::name$).collect(Collectors.toList()));
		return parameters.stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}

}