package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.StampResult;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OpenCatalogOperation extends Operation<String> {
	private int width = 100;
	private List<String> views = new ArrayList<>();
	private Catalog catalog;
	private CatalogDisplayBuilder catalogDisplayBuilder;
	private Filter filter;
	private Position position = Position.Standalone;
	private Selection selection = Selection.None;
	private Execution execution;

	public enum Position { Standalone, RelativeToOperation };
	public enum Selection { None, Single, Multiple };

	public OpenCatalogOperation() {
		alexandriaIcon("icons:dns");
		height(100);
	}

	public int width() {
		return this.width;
	}

	public OpenCatalogOperation width(int width) {
		this.width = width;
		return this;
	}

	public List<String> views() {
		return views;
	}

	public OpenCatalogOperation views(List<String> views) {
		this.views = views;
		return this;
	}

	public Catalog catalog() {
		return this.catalog;
	}

	public OpenCatalogOperation catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public AlexandriaAbstractCatalog createCatalog(ActivitySession session) {
		AlexandriaAbstractCatalog catalog = catalogDisplayBuilder != null ? catalogDisplayBuilder.build(session) : null;
		if (catalog == null) return null;
		catalog.enabledViews(views);
		return catalog;
	}

	public OpenCatalogOperation catalogDisplayBuilder(CatalogDisplayBuilder builder) {
		this.catalogDisplayBuilder = builder;
		return this;
	}

	public boolean filtered() {
		return filter != null;
	}

	public boolean filter(Element context, Item target, Item item, ActivitySession session) {
		if (filter == null) return true;
		if (target == null && item == null) return true;
		if (target == null || item == null) return false;
		return filter.filter(context, target.object(), item.object(), session);
	}

	public OpenCatalogOperation filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public Position position() {
		return position;
	}

	public OpenCatalogOperation position(String position) {
		return position(Position.valueOf(position));
	}

	public OpenCatalogOperation position(Position position) {
		this.position = position;
		return this;
	}

	public Selection selection() {
		return selection;
	}

	public OpenCatalogOperation selection(String selection) {
		return selection(Selection.valueOf(selection));
	}

	public OpenCatalogOperation selection(Selection selection) {
		this.selection = selection;
		return this;
	}

	public StampResult execute(Item target, List<Item> items, ActivitySession session) {
		if (execution == null) return StampResult.none();
		return execution.execute(target != null ? target.object() : null, items.stream().map(Item::object).collect(toList()), session);
	}

	public OpenCatalogOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface CatalogDisplayBuilder {
		AlexandriaAbstractCatalog build(ActivitySession session);
	}

	public interface Filter {
		boolean filter(Element context, Object target, Object object, ActivitySession session);
	}

	public interface Execution {
		StampResult execute(Object target, List<Object> items, ActivitySession session);
	}

}
