package io.intino.konos.server.activity.displays.catalogs.model;

import io.intino.konos.server.activity.displays.catalogs.model.arrangement.Arrangement;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.Group;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.Grouping;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.Sorting;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.ItemList;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Catalog extends Element {
	private ObjectsLoader objectsLoader;
	private RootObjectLoader rootObjectLoader;
	private DefaultObjectLoader defaultObjectLoader;
	private ScopeChangeEvent scopeChangeEvent;
	private List<Arrangement> arrangementList = new ArrayList<>();
	private ArrangementFilter arrangementsFilter;
	private ClusterManager clusterManager;
	private Events events;

	public Item rootItem(List<Item> itemList, String username) {
		return rootObjectLoader != null ? item(rootObjectLoader.load(objects(itemList), username)) : null;
	}

	public Catalog rootObjectLoader(RootObjectLoader loader) {
		this.rootObjectLoader = loader;
		return this;
	}

	public Item defaultItem(String id, String username) {
		return defaultObjectLoader != null ? item(defaultObjectLoader.load(id, username)) : null;
	}

	public Catalog defaultObjectLoader(DefaultObjectLoader loader) {
		this.defaultObjectLoader = loader;
		return this;
	}

	public ItemList items(String condition, String username) {
		if (objectsLoader == null) return new ItemList();
		return new ItemList(objectsLoader.load(condition, username).stream().map(this::item).collect(toList()));
	}

	public Catalog objectsLoader(ObjectsLoader loader) {
		this.objectsLoader = loader;
		return this;
	}

	public List<Grouping> groupings() {
		return arrangementList.stream().filter(a -> a instanceof Grouping).map(a -> (Grouping)a).collect(toList());
	}

	public List<Sorting> sortings() {
		return arrangementList.stream().filter(a -> a instanceof Sorting).map(a -> (Sorting)a).collect(toList());
	}

	public Catalog add(Arrangement arrangement) {
		this.arrangementList.add(arrangement);
		return this;
	}

	public ArrangementFilter arrangementFilter() {
		return this.arrangementsFilter;
	}

	public Catalog arrangementFilter(ArrangementFilter filter) {
		this.arrangementsFilter = filter;
		return this;
	}

	public Events events() {
		return events;
	}

	public Catalog events(Events events) {
		this.events = events;
		return this;
	}

	public Catalog onScopeChange(ScopeChangeEvent event) {
		this.scopeChangeEvent = event;
		return this;
	}

	public void scope(Scope scope) {
		this.scopeChangeEvent.onChange(scope);
	}

	public void addGroupingGroup(String grouping, String groupKey, List<Item> itemList, String username) {
		if (clusterManager == null) return;
		Group group = new Group().name(groupKey).label(groupKey).objects(objects(itemList));
		clusterManager.createClusterGroup(this, grouping, group, username);
	}

	public Catalog clusterManager(ClusterManager manager) {
		this.clusterManager = manager;
		return this;
	}

	public interface ObjectsLoader {
		List<Object> load(String condition, String username);
	}

	public interface RootObjectLoader {
		Object load(List<Object> objectList, String username);
	}

	public interface DefaultObjectLoader {
		Object load(String id, String username);
	}

	public interface ScopeChangeEvent {
		void onChange(Scope scope);
	}

	public interface ArrangementFilter {
		void add(Group... groups);
		boolean contains(String id);
	}

	public interface ClusterManager {
		void createClusterGroup(Catalog element, String grouping, Group group, String username);
	}
}
