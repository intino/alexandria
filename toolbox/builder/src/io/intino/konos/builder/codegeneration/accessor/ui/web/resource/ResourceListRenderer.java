package io.intino.konos.builder.codegeneration.accessor.ui.web.resource;

import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.resource.AccessibleDisplayRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.Display;
import io.intino.konos.model.Service;

public class ResourceListRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer {

	public ResourceListRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, service.graph(), Target.Accessor);
	}

	@Override
	public void render() throws KonosException {
		for (Service.UI.Resource r : resourceList) new ResourceRenderer(context, r).execute();
		for (Display.Accessible d : accessibleDisplays) new AccessibleDisplayRenderer(context, d, Target.Accessor).execute();
	}
}