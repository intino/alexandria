package io.intino.konos.alexandria.activity.model;

import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class Element {
	private String name;
	private String label;
	private Object type;
	private Toolbar toolbar;
	private List<AbstractView> viewList = new ArrayList<>();
	private ObjectLoader objectLoader;
	private ObjectIdLoader objectIdLoader;
	private ObjectNameLoader objectNameLoader;

	public String name() {
		return name;
	}

	public Element name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public Element label(String label) {
		this.label = label;
		return this;
	}

	public Toolbar toolbar() {
		return this.toolbar;
	}

	public Element toolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
		return this;
	}

	public List<AbstractView> views() {
		return viewList;
	}

	public Element add(AbstractView view) {
		this.viewList.add(view);
		return this;
	}

	public Item item(String id, ActivitySession session) {
		return objectLoader != null ? item(objectLoader.load(id, session)) : null;
	}

	public Item item(Object object) {
		if (object == null) return null;
		return new Item().id(id(object)).name(name(object)).object(object);
	}

	public Element objectLoader(ObjectLoader loader) {
		this.objectLoader = loader;
		return this;
	}

	public Element objectIdLoader(ObjectIdLoader loader) {
		this.objectIdLoader = loader;
		return this;
	}

	public Element objectNameLoader(ObjectNameLoader loader) {
		this.objectNameLoader = loader;
		return this;
	}

	protected List<Object> objects(List<Item> itemList) {
		return itemList.stream().map(Item::object).collect(toList());
	}

	private String id(Object object) {
		return objectLoader != null ? objectIdLoader.load(object) : null;
	}

	private String name(Object object) {
		return objectLoader != null ? objectNameLoader.load(object) : null;
	}

	public interface ObjectLoader {
		Object load(String id, ActivitySession session);
	}

	public interface ObjectIdLoader {
		String load(Object object);
	}

	public interface ObjectNameLoader {
		String load(Object object);
	}
}
