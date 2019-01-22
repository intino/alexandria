package io.intino.konos.builder.codegeneration.services.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.konos.model.graph.ui.UIService;

import java.util.List;

public class ResourceListRenderer extends UIRenderer {
	private final List<UIService.Resource> resourceList;
	private final List<AccessibleDisplay> accessibleDisplays;

	public ResourceListRenderer(Settings settings, KonosGraph graph) {
		super(settings);
		this.resourceList = graph.core$().find(UIService.Resource.class);
		this.accessibleDisplays = graph.accessibleDisplayList();
	}

	public void execute() {
		resourceList.forEach(r -> new ResourceRenderer(settings, r).execute());
		accessibleDisplays.forEach(d -> new AccessibleDisplayRenderer(settings, d).execute());
	}

}