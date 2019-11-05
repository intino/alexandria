package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.resource.AccessibleDisplayRenderer;
import io.intino.konos.model.graph.Service;

public class ResourceListRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer {

	public ResourceListRenderer(Settings settings, Service.UI service) {
		super(settings, service.graph(), Target.Accessor);
	}

	@Override
	public void render() {
		resourceList.forEach(r -> new ResourceRenderer(settings, r).execute());
		accessibleDisplays.forEach(d -> new AccessibleDisplayRenderer(settings, d, Target.Accessor).execute());
	}

}