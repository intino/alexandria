package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.resource.AccessibleDisplayRenderer;
import io.intino.konos.model.graph.KonosGraph;

public class ResourceListRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer {

	public ResourceListRenderer(Settings settings, KonosGraph graph) {
		super(settings, graph, Target.Accessor);
	}

	public void execute() {
		resourceList.forEach(r -> new ResourceRenderer(settings, r).execute());
		accessibleDisplays.forEach(d -> new AccessibleDisplayRenderer(settings, d, Target.Accessor).execute());
	}

}