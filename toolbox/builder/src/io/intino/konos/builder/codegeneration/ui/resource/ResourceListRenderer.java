package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceListRenderer extends UIRenderer {
	protected final List<Service.UI.Resource> resourceList;
	protected final List<Display.Accessible> accessibleDisplays;

	public ResourceListRenderer(CompilationContext compilationContext, KonosGraph graph, Target target) {
		super(compilationContext, target);
		this.resourceList = graph.core$().find(Service.UI.Resource.class);
		this.accessibleDisplays = graph.displayList(Display::isAccessible).map(Display::asAccessible).collect(Collectors.toList());
	}

	@Override
	public void render() {
		resourceList.forEach(r -> new ResourceRenderer(compilationContext, r, target).execute());
		accessibleDisplays.forEach(d -> new AccessibleDisplayRenderer(compilationContext, d, target).execute());
	}
}