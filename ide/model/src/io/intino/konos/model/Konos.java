package io.intino.konos.model;

import io.intino.tara.magritte.Graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private static HashSet<Display> allDisplays(Display d) {
		final HashSet<Display> displays = new HashSet<>();
		displaysOf(d, displays);
		return displays;
	}

	private static void displaysOf(Display display, Set<Display> collection) {
		if (collection.add(display))
			display.displays().forEach(child -> displaysOf(child, collection));
	}
}