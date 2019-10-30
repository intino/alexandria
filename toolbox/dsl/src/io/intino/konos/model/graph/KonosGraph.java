package io.intino.konos.model.graph;

import io.intino.tara.magritte.Graph;

import java.util.*;
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

	public static Template templateFor(Service.UI.Resource resource) {
		return resource.asPage().template();
	}

	public static boolean isParentComponent(Component component) {
		loadParentComponents(component.graph());
		return hierarchyDisplays.contains(component.name$());
	}

	public Set<String> findCustomParameters(Service.Messaging service) {
		Set<String> set = new LinkedHashSet<>();
		for (Service.Messaging.Request request : service.requestList())
			set.addAll(extractParameters(request.path()));
		return set;
	}

	public Set<String> findCustomParameters(Service.REST service) {
		Set<String> set = new LinkedHashSet<>();
		for (Service.REST.Resource resource : service.resourceList())
			set.addAll(extractParameters(resource.path()));
		return set;
	}

	public Set<String> findCustomParameters(Service.UI service) {
		Set<String> set = new LinkedHashSet<>();
		if (service.authentication() != null)
			set.addAll(extractParameters(service.authentication().by()));
		for (Service.UI.Resource resource : service.resourceList())
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
		hierarchyDisplays = graph.core$().find(PassiveView.ExtensionOf.class).stream().map(d -> d.core$().as(PassiveView.ExtensionOf.class).parentView().name$()).collect(toSet());
	}

	private void createPrivateComponents() {
		tablesDisplays(this).forEach(this::createUiTableRow);
	}

	private void createUiTableRow(CatalogComponents.Table element) {
		List<CatalogComponents.Collection.Mold.Item> itemList = element.moldList().stream().map(CatalogComponents.Collection.Mold::item).collect(toList());
		String name = firstUpperCase(element.name$()) + "Row";
		PrivateComponents privateComponents = privateComponentsList().size() <= 0 ? create().privateComponents() : privateComponents(0);
		PrivateComponents.Row row = privateComponents.rowList().stream().filter(c -> c.name$().equals(name)).findFirst().orElse(null);
		if (row == null) privateComponents.create(name).row(itemList);//TODO parameters antes no estaba, es empty list correcto? ask mario
	}

	private static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public List<Service.Messaging> messagingServiceList() {
		return serviceList(Service::isMessaging).map(Service::asMessaging).collect(toList());
	}

	public List<Service.REST> restServiceList() {
		return serviceList(Service::isREST).map(Service::asREST).collect(toList());
	}

	public List<Service.UI> uiServiceList() {
		return serviceList(Service::isUI).map(Service::asUI).collect(toList());
	}

	public List<Service.JMX> jmxServiceList() {
		return serviceList(Service::isJMX).map(Service::asJMX).collect(toList());
	}

	public List<Service.SlackBot> slackBotServiceList() {
		return serviceList(Service::isSlackBot).map(Service::asSlackBot).collect(toList());
	}


//	public Service.UI.Resource home() {
//		return homeList().stream().findFirst().orElse(null);
//	}
//
//	public Service.UI.Resource userHome() {
//		return homeList().stream().filter(Service.UI.Resource::isConfidential).findFirst().orElse(null);
//	}
//
//	public List<Template> templates() {
//		return resourceList.stream().map(this::template).collect(Collectors.toList());
//	}
//
//	public Template template(Service.UI.Resource resource) {
//		return resource.asPage().template();
//	}
//
//	private List<Service.UI.Resource> homeList() {
//		return resourceList.stream().filter(Service.UI.Resource::isMain).collect(toList());
//	}TODO
}