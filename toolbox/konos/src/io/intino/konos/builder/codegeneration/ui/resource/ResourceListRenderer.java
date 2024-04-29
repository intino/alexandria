package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceListRenderer extends UIRenderer {
	protected final List<Service.UI.Resource> resourceList;
	protected final List<Display.Accessible> accessibleDisplays;
	private final Target target;

	public ResourceListRenderer(CompilationContext compilationContext, KonosGraph graph, Target target) {
		super(compilationContext);
		this.target = target;
		this.resourceList = graph.core$().find(Service.UI.Resource.class);
		this.accessibleDisplays = graph.displayList(Display::isAccessible).map(Display::asAccessible).collect(Collectors.toList());
	}

	@Override
	public void render() throws KonosException {
		for (Service.UI.Resource r : resourceList) new ResourceRenderer(context, r, target).execute();
		for (Display.Accessible d : accessibleDisplays) new AccessibleDisplayRenderer(context, d, target).execute();
	}
}