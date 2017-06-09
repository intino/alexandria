package io.intino.konos.model;

import io.intino.konos.model.jms.JMSService;
import io.intino.konos.model.rest.RESTService;
import io.intino.tara.magritte.Graph;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Konos extends io.intino.konos.model.GraphWrapper {

	public Konos(Graph graph) {
		super(graph);
	}


	public static List<Display> displaysOf(Activity activity) {
		return activity.abstractPageList().stream()
				.filter(page -> page.uses().is(Display.class))
				.map(page -> page.uses().as(Display.class))
				.map(Konos::allDisplays).flatMap(Collection::stream).distinct()
				.collect(toList());
	}

	public static List<Dialog> dialogsOf(Activity activity) {
		return activity.abstractPageList().stream().filter(page -> page.uses().is(Dialog.class)).map(page -> page.uses().as(Dialog.class)).collect(toList());
	}

	public static List<Component> componentsOf(Activity activity) {
		List<Component> components = new ArrayList<>();
		components.addAll(dialogsOf(activity));
		components.addAll(displaysOf(activity));
		return components;
	}

	private static HashSet<Display> allDisplays(Display d) {
		final HashSet<Display> displays = new HashSet<>();
		displaysOf(d, displays);
		return displays;
	}

	private static void displaysOf(Display display, Set<Display> collection) {
		if (collection.add(display))
			display.displays().forEach(child -> displaysOf(child, collection));
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