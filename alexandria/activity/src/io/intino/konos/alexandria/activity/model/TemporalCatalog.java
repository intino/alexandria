package io.intino.konos.alexandria.activity.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TemporalCatalog extends Catalog {
	private ObjectsLoader objectsLoader;
	private RootObjectLoader rootObjectLoader;
	private DefaultObjectLoader defaultObjectLoader;
	private RangeLoader rangeLoader;
	private ObjectCreatedCalculator objectCreatedCalculator;
	private List<TimeScale> scales = new ArrayList<>();
	private List<TimeScale> localizedScales = new ArrayList<>();
	private int maxZoom = 0;
	private Type type = Type.Time;

	public enum Type {
		Time, Range
	}

	public Type type() {
		return this.type;
	}

	public TemporalCatalog type(Type type) {
		this.type = type;
		return this;
	}

	public Item rootItem(List<Item> itemList, TimeRange range, String username) {
		return rootObjectLoader != null ? item(rootObjectLoader.load(objects(itemList), range, username)) : null;
	}

	public TemporalCatalog rootObjectLoader(RootObjectLoader loader) {
		this.rootObjectLoader = loader;
		return this;
	}

	public Item defaultItem(String id, TimeRange range, String username) {
		return defaultObjectLoader != null ? item(defaultObjectLoader.load(id, range, username)) : null;
	}

	public TemporalCatalog defaultObjectLoader(DefaultObjectLoader loader) {
		this.defaultObjectLoader = loader;
		return this;
	}

	public ItemList items(String condition, TimeRange range, String username) {
		if (objectsLoader == null) return new ItemList();
		return new ItemList(objectsLoader.load(condition, range, username).stream().map(this::item).collect(toList()));
	}

	public TemporalCatalog objectsLoader(ObjectsLoader loader) {
		this.objectsLoader = loader;
		return this;
	}

	public TimeRange range(String username) {
		return rangeLoader != null ? rangeLoader.load(username) : null;
	}

	public TemporalCatalog rangeLoader(RangeLoader loader) {
		this.rangeLoader = loader;
		return this;
	}

	public Instant created(Object item) {
		return objectCreatedCalculator != null ? objectCreatedCalculator.created(item) : null;
	}

	public TemporalCatalog created(ObjectCreatedCalculator calculator) {
		this.objectCreatedCalculator = calculator;
		return this;
	}

	public List<TimeScale> scales() {
		return scales;
	}

	public TemporalCatalog scales(List<TimeScale> scales) {
		this.scales = scales;
		return this;
	}

	public List<TimeScale> localizedScales() {
		return localizedScales;
	}

	public TemporalCatalog localizedScales(List<TimeScale> localizedScales) {
		this.localizedScales = localizedScales;
		return this;
	}

	public int maxZoom() {
		return maxZoom;
	}

	public TemporalCatalog maxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
		return this;
	}

	public interface ObjectsLoader {
		List<Object> load(String condition, TimeRange range, String username);
	}

	public interface RootObjectLoader {
		Object load(List<Object> objectList, TimeRange range, String username);
	}

	public interface DefaultObjectLoader {
		Object load(String id, TimeRange range, String username);
	}

	public interface ObjectCreatedCalculator {
		Instant created(Object item);
	}

	public interface RangeLoader {
		TimeRange load(String username);
	}
}
