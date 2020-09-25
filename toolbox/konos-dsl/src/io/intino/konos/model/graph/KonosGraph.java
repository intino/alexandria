package io.intino.konos.model.graph;

import io.intino.magritte.framework.Graph;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class KonosGraph extends io.intino.konos.model.graph.AbstractGraph {
	private static Map<String, Set<String>> hierarchyDisplays = new HashMap<>();
	private static Map<String, List<CatalogComponents.Collection.Mold.Item>> items = new HashMap<>();
	private static Map<String, List<HelperComponents.Row>> rows = new HashMap<>();
	private static Map<String, List<CatalogComponents.Table>> tables = new HashMap<>();
	private List<CatalogComponents.Collection> collectionDisplays;
	private List<CatalogComponents.Table> tableDisplays;
	private List<CatalogComponents.List> listDisplays;
	private List<CatalogComponents.Magazine> magazines;
	private List<CatalogComponents.Map> mapDisplays;

	public KonosGraph(Graph graph) {
		super(graph);
	}

	public KonosGraph(io.intino.magritte.framework.Graph graph, KonosGraph wrapper) {
		super(graph, wrapper);
	}

	public KonosGraph init(String group) {
		resetCache();
		createPrivateComponents(group);
		return this;
	}

	private void resetCache() {
		tables = new HashMap<>();
		items = new HashMap<>();
		rows = new HashMap<>();
	}

	public List<Display> rootDisplays(String group) {
		KonosGraph graph = this;
		List<Display> rootDisplays = graph.displayList().stream().filter(d -> d.core$().ownerAs(PassiveView.class) == null).collect(toList());
		rootDisplays.addAll(itemsDisplays(group));
		rootDisplays.addAll(rowsDisplays(group));
		rootDisplays.addAll(collectionsDisplays(group));
		return rootDisplays;
	}

	public List<CatalogComponents.Collection.Mold.Item> itemsDisplays(String group) {
		if (!items.containsKey(group))
			items.put(group, core$().find(CatalogComponents.Collection.Mold.Item.class));
		return items.get(group);
	}

	public List<HelperComponents.Row> rowsDisplays(String group) {
		if (!rows.containsKey(group)) rows.put(group, core$().find(HelperComponents.Row.class));
		return rows.get(group);
	}

	public List<CatalogComponents.Collection> collectionsDisplays(String group) {
		KonosGraph graph = this;
		if (collectionDisplays == null)
			collectionDisplays = graph.displayList().stream().filter(d -> d.core$().ownerAs(PassiveView.class) == null && d.i$(CatalogComponents.Collection.class)).map(d -> d.a$(CatalogComponents.Collection.class)).collect(Collectors.toList());
		return collectionDisplays;
	}

	public List<CatalogComponents.Table> tablesDisplays(String group) {
		if (tableDisplays == null)
			tableDisplays = collectionsDisplays(group).stream().filter(c -> c.i$(CatalogComponents.Table.class)).map(c -> c.a$(CatalogComponents.Table.class)).collect(toList());
		return this.tableDisplays;
	}

	public List<CatalogComponents.List> listsDisplays(String group) {
		if (listDisplays == null)
			listDisplays = collectionsDisplays(group).stream().filter(c -> c.i$(CatalogComponents.List.class)).map(c -> c.a$(CatalogComponents.List.class)).collect(toList());
		return listDisplays;
	}

	public List<CatalogComponents.Magazine> magazinesDisplays(String group) {
		if (magazines == null)
			magazines = collectionsDisplays(group).stream().filter(c -> c.i$(CatalogComponents.Magazine.class)).map(c -> c.a$(CatalogComponents.Magazine.class)).collect(toList());
		return magazines;
	}

	public List<CatalogComponents.Map> mapsDisplays(String group) {
		if (mapDisplays == null)
			mapDisplays = collectionsDisplays(group).stream().filter(c -> c.i$(CatalogComponents.Map.class)).map(c -> c.a$(CatalogComponents.Map.class)).collect(toList());
		return mapDisplays;
	}

	public static List<CatalogComponents.Table> tablesDisplays(KonosGraph graph, String group) {
		if (!tables.containsKey(group))
			tables.put(group, graph.core$().find(CatalogComponents.Table.class));
		return tables.get(group);
	}

	public static Template templateFor(Service.UI.Resource resource) {
		return resource.asPage().template();
	}

	public static boolean isParent(String group, PassiveView passiveView) {
		loadParents(group, passiveView.graph());
		return hierarchyDisplays.get(group).contains(passiveView.name$());
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

	private static void loadParents(String group, KonosGraph graph) {
		if (hierarchyDisplays.containsKey(group)) return;
		hierarchyDisplays.put(group, graph.core$().find(PassiveView.ExtensionOf.class).stream().map(d -> d.core$().as(PassiveView.ExtensionOf.class).parentView().name$()).collect(toSet()));
	}

	private void createPrivateComponents(String group) {
		tablesDisplays(this, group).forEach(this::createUiTableRow);
	}

	private void createUiTableRow(CatalogComponents.Table element) {
		List<CatalogComponents.Collection.Mold.Item> itemList = element.moldList().stream().map(CatalogComponents.Collection.Mold::item).collect(toList());
		String name = firstUpperCase(element.name$()) + "Row";
		HelperComponents privateComponents = helperComponentsList().size() <= 0 ? create(element.core$().stash()).helperComponents() : helperComponents(0);
		HelperComponents.Row row = privateComponents.rowList().stream().filter(c -> c.name$().equals(name)).findFirst().orElse(null);
		if (row == null)
			privateComponents.create(name).row(itemList);//TODO parameters antes no estaba, es empty list correcto? ask mario
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


	public List<Service.Soap> soapServiceList() {
		return serviceList(Service::isSoap).map(Service::asSoap).collect(toList());
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