package io.intino.konos.alexandria.ui.model;

import io.intino.konos.alexandria.ui.model.catalog.Events;
import io.intino.konos.alexandria.ui.model.catalog.Scope;
import io.intino.konos.alexandria.ui.model.catalog.arrangement.*;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Catalog extends Element {
	private ObjectsLoader objectsLoader;
	private RootObjectLoader rootObjectLoader;
	private DefaultObjectLoader defaultObjectLoader;
	private ScopeChangeEvent scopeChangeEvent;
	private ArrangementHistogramsMode arrangementHistogramsMode;
	private List<Arrangement> arrangementList = new ArrayList<>();
	private ArrangementFiltererLoader arrangementFiltererLoader;
	private ItemsArrivalMessageLoader itemsArrivalMessageLoader;
	private Mode mode;
	private Events events;

	public Catalog() {
		arrangementHistogramsMode = ArrangementHistogramsMode.Disabled;
		mode = Mode.Normal;
	}

	public enum Mode { Normal, Preview }

	public enum ArrangementHistogramsMode {
		EnabledAndVisible, EnabledButHidden, Disabled
	}

	public Item rootItem(List<Item> itemList, UISession session) {
		return rootObjectLoader != null ? item(rootObjectLoader.load(objects(itemList), session)) : null;
	}

	public Catalog rootObjectLoader(RootObjectLoader loader) {
		this.rootObjectLoader = loader;
		return this;
	}

	public Item defaultItem(String id, UISession session) {
		return defaultObjectLoader != null ? item(defaultObjectLoader.load(id, session)) : null;
	}

	public Catalog defaultObjectLoader(DefaultObjectLoader loader) {
		this.defaultObjectLoader = loader;
		return this;
	}

	public ItemList items(Scope scope, String condition, UISession session) {
		if (objectsLoader == null) return new ItemList();
		return new ItemList(objectsLoader.load(scope, condition, session).stream().map(this::item).collect(toList()));
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

	public ArrangementFilterer arrangementFilterer(UISession session) {
		return this.arrangementFiltererLoader != null ? arrangementFiltererLoader.load(session) : null;
	}

	public Catalog arrangementFiltererLoader(ArrangementFiltererLoader loader) {
		this.arrangementFiltererLoader = loader;
		return this;
	}

	public String itemsArrivalMessage(int count, UISession session) {
		return this.itemsArrivalMessageLoader != null ? itemsArrivalMessageLoader.load(count, session) : null;
	}

	public Catalog itemsArrivalMessageLoader(ItemsArrivalMessageLoader loader) {
		this.itemsArrivalMessageLoader = loader;
		return this;
	}

	public Mode mode() {
		return mode;
	}

	public Catalog mode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public Events events() {
		return events;
	}

	public Catalog events(Events events) {
		this.events = events;
		return this;
	}

	public ArrangementHistogramsMode arrangementHistogramsMode() {
		return arrangementHistogramsMode;
	}

	public Catalog arrangementHistogramsMode(String mode) {
		return arrangementHistogramsMode(ArrangementHistogramsMode.valueOf(mode));
	}

	public Catalog arrangementHistogramsMode(ArrangementHistogramsMode mode) {
		this.arrangementHistogramsMode = mode;
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

	public interface ObjectsLoader {
		List<Object> load(Scope scope, String condition, UISession session);
	}

	public interface RootObjectLoader {
		Object load(List<Object> objectList, UISession session);
	}

	public interface DefaultObjectLoader {
		Object load(String id, UISession session);
	}

	public interface ScopeChangeEvent {
		void onChange(Scope scope, UISession session);
	}

	public interface ArrangementFiltererLoader {
		ArrangementFilterer load(UISession session);
	}

	public interface ItemsArrivalMessageLoader {
		String load(int count, UISession session);
	}

	public interface ArrangementFilterer {
		UISession session();
		void add(String grouping, Group... groups);
		boolean contains(Item item);
		void clear();
		boolean isEmpty();
	}
}
