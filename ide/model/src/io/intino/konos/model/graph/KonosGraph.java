package io.intino.konos.model.graph;

import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.rest.RESTService;
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

	public static List<Display> displaysOf(Activity activity) {
		return activity.graph().core$().find(Display.class);
	}

	public static List<Dialog> dialogsOf(Activity activity) {
		return activity.graph().core$().find(Dialog.class);
	}

	public static List<Component> componentsOf(Activity activity) {
		List<Component> components = new ArrayList<>();
		components.addAll(dialogsOf(activity));
		components.addAll(displaysOf(activity));
		return components;
	}

	public Set<String> findCustomParameters(DataLake.Tank channel) {
		Set<String> set = new LinkedHashSet<>();
		set.addAll(extractParameters(channel.topic()));
		return set;
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

	public Set<String> findCustomParameters(Activity activity) {
		Set<String> set = new LinkedHashSet<>();
		if (activity.authenticated() != null)
			set.addAll(extractParameters(activity.authenticated().by()));
		for (Activity.AbstractPage page : activity.abstractPageList())
			for (String path : page.paths()) set.addAll(extractParameters(path));
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