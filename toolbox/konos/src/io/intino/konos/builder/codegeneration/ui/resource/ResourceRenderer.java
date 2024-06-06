package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Service;
import io.intino.magritte.framework.Layer;

import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class ResourceRenderer extends UIRenderer {
	protected final Service.UI.Resource resource;
	protected final Target target;

	public ResourceRenderer(CompilationContext compilationContext, Service.UI.Resource resource, Target target) {
		super(compilationContext);
		this.target = target;
		this.resource = resource;
	}

	@Override
	public void render() throws KonosException {
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);

		FrameBuilder builder = buildFrame().add("resource").add("name", resource.name$()).add("parameter", parameters(resource));
		if (resource.isStaticPage()) builder.add("static");
		if (resource.isAssetPage()) builder.add("asset");
		if (uiService.googleApiKey() != null)
			builder.add("googleApiKey", customize("googleApiKey", uiService.googleApiKey()));
		if (resource.isConfidential()) builder.add("confidential", "");
		builder.add("page", buildBaseFrame().add("page").add(isMobile(uiService) ? "mobile" : "no-mobile").add("name", resource.name$()));

		Commons.writeFrame(resourceFolder(gen(target), target), resourceFilename(resource.name$()), new ResourceTemplate().render(builder.toFrame(), Formatters.all));
		if (target.equals(Target.Service))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(resourceFolder(gen(target), target), resourceFilename(resource.name$())).getAbsolutePath()));

		new PageRenderer(context, resource, target).execute();
	}

	private FrameBuilder[] parameters(Service.UI.Resource resource) {
		Service.UI service = resource.core$().ownerAs(Service.UI.class);
		List<String> parameters = Commons.extractUrlPathParameters(resource.path());
		parameters.addAll(resource.parameterList().stream().map(Layer::name$).toList());
		return parameters.stream().map(parameter -> new FrameBuilder().add("parameter").add(isMobile(service) ? "mobile" : "no-mobile")
				.add("name", parameter).add("resource", resource.name$())).toArray(FrameBuilder[]::new);
	}

}