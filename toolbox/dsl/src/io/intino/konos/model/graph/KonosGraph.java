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
	private static List<CatalogComponents.Collection.Mold.Item> items = null;
	private static List<PrivateComponents.Row> rows = null;
	private static List<CatalogComponents.Table> tables = null;

	public KonosGraph(Graph graph) {
		super(graph);
	}

	public KonosGraph(io.intino.tara.magritte.Graph graph, KonosGraph wrapper) {
		super(graph, wrapper);
	}

	public KonosGraph init() {
		resetCache();
		createPrivateComponents();
		return this;
	}

	private void resetCache() {
		tables = null;
		items = null;
		rows = null;
	}

	public List<Display> rootDisplays() {
		KonosGraph graph = this;
		List<Display> rootDisplays = graph.displayList().stream().filter(d -> d.core$().ownerAs(PassiveView.class) == null).collect(toList());
		rootDisplays.addAll(itemsDisplays());
		rootDisplays.addAll(rowsDisplays());
		return rootDisplays;
	}

	public List<CatalogComponents.Collection.Mold.Item> itemsDisplays() {
		if (items == null) items = core$().find(CatalogComponents.Collection.Mold.Item.class);
		return items;
	}

	public List<PrivateComponents.Row> rowsDisplays() {
		if (rows == null) rows = core$().find(PrivateComponents.Row.class);
		return rows;
	}

	public static List<CatalogComponents.Table> tablesDisplays(KonosGraph graph) {
		if (tables == null) tables = graph.core$().find(CatalogComponents.Table.class);
		return tables;
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

	private void createPrivateComponents() {
		tablesDisplays(this).forEach(this::createUiTableRow);
	}

	private void createUiTableRow(CatalogComponents.Table element) {
		List<CatalogComponents.Collection.Mold.Item> itemList = element.moldList().stream().map(CatalogComponents.Collection.Mold::item).collect(toList());
		String name = firstUpperCase(element.name$()) + "Row";
		PrivateComponents privateComponents = privateComponentsList().size() <= 0 ? create().privateComponents() : privateComponents(0);
		PrivateComponents.Row row = privateComponents.rowList().stream().filter(c -> c.name$().equals(name)).findFirst().orElse(null);
		if (row == null) privateComponents.create(name).row(itemList);
	}

	private static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

}