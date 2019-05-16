package io.intino.konos.model.graph;

import io.intino.konos.model.graph.extensionof.ExtensionOfPassiveView;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.magritte.Graph;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class KonosGraph extends io.intino.konos.model.graph.AbstractGraph {
	private static Set<String> hierarchyDisplays = null;

	public KonosGraph(Graph graph) {
		super(graph);
	}

	public KonosGraph(io.intino.tara.magritte.Graph graph, KonosGraph wrapper) {
		super(graph, wrapper);
	}

	public static List<Display> rootDisplays(UIService service) {
		KonosGraph graph = service.graph();
		List<Display> result = graph.displayList().stream().filter(d -> d.core$().ownerAs(PassiveView.class) == null).collect(toList());
		result.addAll(graph.core$().find(CatalogComponents.Collection.Mold.Item.class));
		result.addAll(graph.core$().find(PrivateComponents.Row.class));
		return result;
	}

	public static Template templateFor(UIService.Resource resource) {
		UIService uiService = resource.core$().ownerAs(UIService.class);
		return uiService.template(resource);
	}

	public static boolean isParentComponent(Component component) {
		loadParentComponents(component.graph());
		return hierarchyDisplays.contains(component.name$());
	}

	public Set<String> findCustomParameters(JMSService service) {
		Set<String> set = new LinkedHashSet<>();
		for (JMSService.Request request : service.requestList())
			set.addAll(extractParameters(request.path()));
		return set;
	}

	public Set<String> findCustomParameters(RESTService service) {
		Set<String> set = new LinkedHashSet<>();
		for (RESTService.Resource resource : service.resourceList())
			set.addAll(extractParameters(resource.path()));
		return set;
	}

	public Set<String> findCustomParameters(UIService service) {
		Set<String> set = new LinkedHashSet<>();
		if (service.authentication() != null)
			set.addAll(extractParameters(service.authentication().by()));
		for (UIService.Resource resource : service.resourceList())
			set.addAll(extractParameters(resource.path()));
		return set;
	}

	@SuppressWarnings("Duplicates")
	private static Set<String> extractParameters(String text) {
		Set<String> list = new LinkedHashSet<>();
		Pattern pattern = Pattern.compile("\\{([^\\}]*)\\}");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) list.add(matcher.group(1));
		return list;
	}

	private static void loadParentComponents(KonosGraph graph) {
		if (hierarchyDisplays != null) return;
		hierarchyDisplays = graph.core$().find(ExtensionOfPassiveView.class).stream().map(d -> d.core$().as(ExtensionOfPassiveView.class).parentView().name$()).collect(toSet());
	}

}