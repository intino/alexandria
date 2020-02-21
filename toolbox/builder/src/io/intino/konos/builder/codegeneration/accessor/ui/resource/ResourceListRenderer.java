package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.resource.AccessibleDisplayRenderer;
import io.intino.konos.model.graph.Service;

public class ResourceListRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer {

	public ResourceListRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, service.graph(), Target.Accessor);
	}

	@Override
	public void render() {
		resourceList.forEach(r -> new ResourceRenderer(context, r).execute());
		accessibleDisplays.forEach(d -> new AccessibleDisplayRenderer(context, d, Target.Accessor).execute());
	}

}