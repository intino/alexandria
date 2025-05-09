package io.intino.konos.builder.codegeneration.accessor.ui.android.resource;

import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.resource.ExposedDisplayRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.Service;

public class ResourceListRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer {

	public ResourceListRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, service.graph(), Target.Android);
	}

	@Override
	public void render() throws KonosException {
		for (Service.UI.Resource r : resourceList) new ResourceRenderer(context, r).execute();
		for (Display.Exposed d : exposedDisplays)
			new ExposedDisplayRenderer(context, d, Target.Android).execute();
	}
}