package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.konos.model.graph.ui.UIService;

import java.util.List;

public class ResourceListRenderer extends UIRenderer {
	protected final List<UIService.Resource> resourceList;
	protected final List<AccessibleDisplay> accessibleDisplays;

	public ResourceListRenderer(Settings settings, KonosGraph graph, Target target) {
		super(settings, target);
		this.resourceList = graph.core$().find(UIService.Resource.class);
		this.accessibleDisplays = graph.accessibleDisplayList();
	}

	@Override
	public void render() {
		resourceList.forEach(r -> new ResourceRenderer(settings, r, target).execute());
		accessibleDisplays.forEach(d -> new AccessibleDisplayRenderer(settings, d, target).execute());
	}
}