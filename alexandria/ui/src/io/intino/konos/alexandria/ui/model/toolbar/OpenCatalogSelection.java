package io.intino.konos.alexandria.ui.model.toolbar;

import io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.ArrayList;
import java.util.List;

public class OpenCatalogSelection extends Operation {
	private int width = 100;
	private int height = 100;
	private List<String> views = new ArrayList<>();
	private Catalog catalog;
	private CatalogDisplayBuilder catalogDisplayBuilder;
	private Filter filter;
	private Position position = Position.Standalone;
	private Selection selection = Selection.None;
	private Execution execution;

	public enum Position { Standalone, RelativeToOperation };
	public enum Selection { None, Single, Multiple };

	public OpenCatalogSelection() {
		alexandriaIcon("icons:list");
	}

	public int width() {
		return this.width;
	}

	public OpenCatalogSelection width(int width) {
		this.width = width;
		return this;
	}

	public int height() {
		return this.height;
	}

	public OpenCatalogSelection height(int height) {
		this.height = height;
		return this;
	}

	public List<String> views() {
		return views;
	}

	public OpenCatalogSelection views(List<String> views) {
		this.views = views;
		return this;
	}

	public Catalog catalog() {
		return this.catalog;
	}

	public OpenCatalogSelection catalog(Catalog catalog) {
		this.catalog = catalog;
		return this;
	}

	public AlexandriaAbstractCatalog createCatalog(UISession session) {
		AlexandriaAbstractCatalog catalog = catalogDisplayBuilder != null ? catalogDisplayBuilder.build(session) : null;
		if (catalog == null) return null;
		catalog.enabledViews(views);
		return catalog;
	}

	public OpenCatalogSelection catalogDisplayBuilder(CatalogDisplayBuilder builder) {
		this.catalogDisplayBuilder = builder;
		return this;
	}

	public boolean filtered() {
		return filter != null;
	}

	public boolean filter(Element context, List<Item> selection, Item item, UISession session) {
		if (filter == null) return true;
		return filter.filter(context, objects(selection), item != null ? item.object() : null, session);
	}

	public OpenCatalogSelection filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public Position position() {
		return position;
	}

	public OpenCatalogSelection position(String position) {
		return position(Position.valueOf(position));
	}

	public OpenCatalogSelection position(Position position) {
		this.position = position;
		return this;
	}

	public Selection selection() {
		return selection;
	}

	public OpenCatalogSelection selection(String selection) {
		return selection(Selection.valueOf(selection));
	}

	public OpenCatalogSelection selection(Selection selection) {
		this.selection = selection;
		return this;
	}

	public ToolbarSelectionResult execute(Element element, List<Item> selection, List<Item> openCatalogSelection, UISession session) {
		if (execution == null) return ToolbarSelectionResult.none();
		return execution.execute(element, objects(selection), objects(openCatalogSelection), session);
	}

	public OpenCatalogSelection execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface CatalogDisplayBuilder {
		AlexandriaAbstractCatalog build(UISession session);
	}

	public interface Filter {
		boolean filter(Element context, List<Object> selection, Object object, UISession session);
	}

	public interface Execution {
		ToolbarSelectionResult execute(Element element, List<Object> selection, List<Object> openCatalogSelection, UISession session);
	}

}
