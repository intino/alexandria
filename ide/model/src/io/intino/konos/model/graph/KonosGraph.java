package io.intino.konos.model.graph;

import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.magritte.Graph;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KonosGraph extends io.intino.konos.model.graph.AbstractGraph {

	public KonosGraph(Graph graph) {
		super(graph);
	}

	public KonosGraph(io.intino.tara.magritte.Graph graph, KonosGraph wrapper) {
		super(graph, wrapper);
	}

	public static List<Display> displaysOf(UIService service) {
		return service.graph().core$().find(Display.class);
	}

	public static List<Dialog> dialogsOf(UIService service) {
		return service.graph().core$().find(Dialog.class);
	}

	public static Component componentFor(UIService.Resource resource) {
		if (resource.isDisplayPage()) return resource.asDisplayPage().display();
		if (resource.isEditorPage()) return resource.asEditorPage().editor();
		return resource.asDialogPage().dialog();
	}

	public static List<Component> componentsOf(UIService service) {
		List<Component> components = new ArrayList<>();
		components.addAll(displaysOf(service));
		components.addAll(dialogsOf(service));
		return components;
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
}