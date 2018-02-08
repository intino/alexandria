package io.intino.konos.alexandria.activity.model;

import io.intino.konos.alexandria.activity.model.catalog.Events;
import io.intino.konos.alexandria.activity.model.catalog.Scope;
import io.intino.konos.alexandria.activity.model.catalog.arrangement.Arrangement;
import io.intino.konos.alexandria.activity.model.catalog.arrangement.Group;
import io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping;
import io.intino.konos.alexandria.activity.model.catalog.arrangement.Sorting;
import io.intino.konos.alexandria.activity.services.push.User;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Catalog extends Element {
	private ObjectsLoader objectsLoader;
	private RootObjectLoader rootObjectLoader;
	private DefaultObjectLoader defaultObjectLoader;
	private ScopeChangeEvent scopeChangeEvent;
	private List<Arrangement> arrangementList = new ArrayList<>();
	private ArrangementFiltererLoader arrangementFiltererLoader;
	private ClusterManager clusterManager;
	private Events events;

	public Item rootItem(List<Item> itemList, User user) {
		return rootObjectLoader != null ? item(rootObjectLoader.load(objects(itemList), user)) : null;
	}

	public Catalog rootObjectLoader(RootObjectLoader loader) {
		this.rootObjectLoader = loader;
		return this;
	}

	public Item defaultItem(String id, User user) {
		return defaultObjectLoader != null ? item(defaultObjectLoader.load(id, user)) : null;
	}

	public Catalog defaultObjectLoader(DefaultObjectLoader loader) {
		this.defaultObjectLoader = loader;
		return this;
	}

	public ItemList items(Scope scope, String condition, User user) {
		if (objectsLoader == null) return new ItemList();
		return new ItemList(objectsLoader.load(scope, condition, user).stream().map(this::item).collect(toList()));
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

	public ArrangementFilterer arrangementFilterer(User user) {
		return this.arrangementFiltererLoader != null ? arrangementFiltererLoader.load(user) : null;
	}

	public Catalog arrangementFiltererLoader(ArrangementFiltererLoader loader) {
		this.arrangementFiltererLoader = loader;
		return this;
	}

	public Events events() {
		return events;
	}

	public Catalog events(Events events) {
		this.events = events;
		return this;
	}

//	public Catalog onScopeChange(ScopeChangeEvent event) {
//		this.scopeChangeEvent = event;
//		return this;
//	}
//
//	public void scope(Scope scope, String username) {
//		this.scopeChangeEvent.onChange(scope, username);
//	}

	public void addGroupingGroup(String grouping, String label, List<Item> itemList, User user) {
		// TODO MC
//		if (clusterManager == null) return;
//		Group group = new Group().label(label).objects(objects(itemList));
//		clusterManager.createClusterGroup(this, grouping, group, username);
	}

	public Catalog clusterManager(ClusterManager manager) {
		this.clusterManager = manager;
		return this;
	}

	public interface ObjectsLoader {
		List<Object> load(Scope scope, String condition, User user);
	}

	public interface RootObjectLoader {
		Object load(List<Object> objectList, User user);
	}

	public interface DefaultObjectLoader {
		Object load(String id, User user);
	}

	public interface ScopeChangeEvent {
		void onChange(Scope scope, User user);
	}

	public interface ArrangementFiltererLoader {
		ArrangementFilterer load(User user);
	}

	public interface ArrangementFilterer {
		User user();
		void add(Group... groups);
		boolean contains(String id);
		void clear();
		boolean isEmpty();
	}

	public interface ClusterManager {
		void createClusterGroup(Catalog element, String grouping, Group group, User user);
	}
}
