package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.ArrayList;
import java.util.List;

public class OpenCatalog extends Operation {
	private int width = 100;
	private int height = 100;
	private List<String> views = new ArrayList<>();
	private Catalog catalog;
	private CatalogDisplayBuilder catalogDisplayBuilder;
	private Filter filter;
	private Position position = Position.Standalone;

	public enum Position { Standalone, RelativeToOperation };

	public OpenCatalog() {
		alexandriaIcon("icons:list");
	}

	public int width() {
		return this.width;
	}

	public OpenCatalog width(int width) {
		this.width = width;
		return this;
	}

	public int height() {
		return this.height;
	}

	public OpenCatalog height(int height) {
		this.height = height;
		return this;
	}

	public List<String> views() {
		return views;
	}

	public OpenCatalog views(List<String> views) {
		this.views = views;
		return this;
	}

	public Catalog catalog() {
		return this.catalog;
	}

	public OpenCatalog catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public AlexandriaAbstractCatalog createCatalog(ActivitySession session) {
		AlexandriaAbstractCatalog catalog = catalogDisplayBuilder != null ? catalogDisplayBuilder.build(session) : null;
		if (catalog == null) return null;
		catalog.enabledViews(views);
		return catalog;
	}

	public OpenCatalog catalogDisplayBuilder(CatalogDisplayBuilder builder) {
		this.catalogDisplayBuilder = builder;
		return this;
	}

	public boolean filtered() {
		return filter != null;
	}

	public boolean filter(Element context, Item item, ActivitySession session) {
		if (filter == null) return true;
		return filter.filter(context, item != null ? item.object() : null, session);
	}

	public OpenCatalog filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public Position position() {
		return position;
	}

	public OpenCatalog position(String position) {
		return position(Position.valueOf(position));
	}

	public OpenCatalog position(Position position) {
		this.position = position;
		return this;
	}

	public interface Filter {
		boolean filter(Element context, Object object, ActivitySession session);
	}

	public interface CatalogDisplayBuilder {
		AlexandriaAbstractCatalog build(ActivitySession session);
	}

}
