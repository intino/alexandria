package io.intino.konos.builder.codegeneration.services.ui.display.view;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.itrules.formatters.StringFormatters;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIPrototypeRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Catalog;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.View;
import io.intino.konos.model.graph.catalogcontainer.CatalogContainerView;
import io.intino.konos.model.graph.collectioncontainer.CollectionContainerView;
import io.intino.konos.model.graph.displaycontainer.DisplayContainerView;
import io.intino.konos.model.graph.gridcontainer.GridContainerView;
import io.intino.konos.model.graph.listcontainer.ListContainerView;
import io.intino.konos.model.graph.magazinecontainer.MagazineContainerView;
import io.intino.konos.model.graph.mapcontainer.MapContainerView;
import io.intino.konos.model.graph.panelcontainer.PanelContainerView;
import io.intino.konos.model.graph.setcontainer.SetContainerView;
import io.intino.konos.model.graph.setcontainer.SetContainerView.AbstractItem;
import io.intino.konos.model.graph.setcontainer.SetContainerView.Group;
import io.intino.konos.model.graph.setcontainer.SetContainerView.Item;
import io.intino.konos.model.graph.setcontainer.SetContainerView.Items;
import io.intino.tara.magritte.Node;

import java.io.File;
import java.util.List;

import static io.intino.konos.model.graph.View.Hidden.HiddenEnabled;

public class ViewRenderer extends UIPrototypeRenderer {
	private final View view;
	private final Component owner;
	private final String box;
	private final String packageName;

	public ViewRenderer(View view, Component owner, String box, String packageName) {
		super(view.name$(), box, packageName);
		this.view = view;
		this.owner = owner;
		this.box = box;
		this.packageName = packageName;
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder builder = super.frameBuilder()
				.add("view")
				.add("owner", owner.name$())
				.add("ownerClass", owner.getClass().getSimpleName())
				.add("label", view.label())
				.add("layout", view.layout().name())
				.add("width", view.width());
		if (view.hidden() == HiddenEnabled) {
			builder.add("hidden", hidden(view, box));
			builder.add("hasMethods");
		}

		addViewProperties(builder);
		return builder;
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(new ViewTemplate());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(new AbstractViewTemplate());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	private void addViewProperties(FrameBuilder builder) {
		builder.add("viewDisplayLoaders", new FrameBuilder("viewDisplayLoaders").add("package", packageName));
		addCollectionContainerProperties(view, builder);
		addDisplayContainerProperties(view, builder);
		addMoldContainerProperties(view, builder);
		addCatalogContainerProperties(view, builder);
		addPanelContainerProperties(view, builder);
		addSetContainerProperties(view, builder);
	}

	private void addCollectionContainerProperties(View view, FrameBuilder builder) {
		if (!view.isCollectionContainer()) return;

		CollectionContainerView collectionView = this.view.asCollectionContainer();
		builder.add("mold", collectionView.mold().name$());

		if (collectionView.noItemsMessage() != null) builder.add("noItemsMessage", collectionView.noItemsMessage());

		addListContainerProperties(collectionView, builder);
		addGridContainerProperties(collectionView, builder);
		addMapContainerProperties(collectionView, builder);
		addMagazineContainerProperties(collectionView, builder);
	}

	private void addListContainerProperties(CollectionContainerView view, FrameBuilder builder) {
		if (!view.i$(ListContainerView.class)) return;
		builder.add("list");
	}

	private void addGridContainerProperties(CollectionContainerView view, FrameBuilder builder) {
		if (!view.i$(GridContainerView.class)) return;
		builder.add("grid");
	}

	private void addMapContainerProperties(CollectionContainerView view, FrameBuilder builder) {
		if (!view.i$(MapContainerView.class)) return;
		builder.add("map");

		MapContainerView mapView = view.a$(MapContainerView.class);

		if (mapView.center() != null)
			builder.add("latitude", mapView.center().latitude()).add("longitude", mapView.center().longitude());

		builder.add("zoom", new FrameBuilder("zoom")
				.add("default", mapView.zoom().defaultZoom())
				.add("min", mapView.zoom().min())
				.add("max", mapView.zoom().max()));
	}

	private void addMagazineContainerProperties(CollectionContainerView view, FrameBuilder frame) {
		if (!view.i$(MagazineContainerView.class)) return;
		frame.add("magazine");
	}

	private void addDisplayContainerProperties(View view, FrameBuilder frame) {
		if (!view.isDisplayContainer()) return;
		frame.add("display");

		DisplayContainerView displayView = view.asDisplayContainer();
		frame.add("hideNavigator", displayView.hideNavigator());
		frame.add("display", displayView.display());

		if (owner.i$(Catalog.class))
			frame.add("catalogScope", new FrameBuilder("catalogScope").add("name", view.name$()).add("display", displayView.display()).add("box", box).add("owner", owner.name$()));
	}

	private void addMoldContainerProperties(View view, FrameBuilder frame) {
		if (!view.isMoldContainer()) return;
		frame.add("mold");
		final Mold mold = view.asMoldContainer().mold();
		frame.add("mold", mold.name$());
	}

	private void addCatalogContainerProperties(View view, FrameBuilder frame) {
		if (!view.isCatalogContainer()) return;
		frame.add("catalog");
		CatalogContainerView catalogContainerView = view.asCatalogContainer();
		frame.add("catalog", catalogContainerView.catalog().name$());
		frame.add("catalogDisplayLoader", new FrameBuilder("catalogDisplayLoader").add("package", packageName));
		if (catalogContainerView.filtered()) frame.add("filter", filterFrame(view, box));
	}

	private void addPanelContainerProperties(View view, FrameBuilder frame) {
		if (!view.isPanelContainer()) return;
		frame.add("panel");
		PanelContainerView panelContainerView = view.asPanelContainer();
		frame.add("panel", panelContainerView.panel().name$());
	}

	private void addSetContainerProperties(View view, FrameBuilder frame) {
		if (!view.isSetContainer()) return;
		frame.add("set");
		SetContainerView setView = view.asSetContainer();
		frame.add("viewContainerSetItems", new FrameBuilder("viewContainerSetItems").add("item", frameOf(setView.abstractItemList())));
	}

	private Frame[] frameOf(List<AbstractItem> items) {
		return items.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(AbstractItem item) {
		if (item.i$(Group.class)) return frameOf(item.a$(Group.class));
		else if (item.i$(Items.class)) return frameOf(item.a$(Items.class));
		else return frameOf(item.a$(Item.class));
	}

	private Frame frameOf(Group group) {
		final FrameBuilder builder = baseFrameBuilder(group).add("group").add("label", group.label()).add("mode", group.mode());
		if (!group.itemList().isEmpty())
			builder.add("item", group.itemList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!group.itemsList().isEmpty())
			builder.add("item", group.itemsList().stream().map(this::frameOf).toArray(Frame[]::new));
		return builder.toFrame();
	}

	private Frame frameOf(Item item) {
		final FrameBuilder builder = baseFrameBuilder(item).add("item");
		final View view = item.view();
		builder.add("label", item.label());
		builder.add("view", new ViewRenderer(view, owner, box, packageName).frameBuilder().toFrame());
		return builder.toFrame();
	}

	private Frame frameOf(Items items) {
		FrameBuilder builder = baseFrameBuilder(items).add("items").add("layout", owner.name$()).add("path", pathOf(items.core$())).add("modelClass", items.itemClass());
		final View view = items.view();
		builder.add("view", new ViewRenderer(view, owner, box, packageName).frameBuilder().toFrame());
		return builder.toFrame();
	}

	private FrameBuilder baseFrameBuilder(AbstractItem item) {
		FrameBuilder builder = new FrameBuilder("set", item.getClass().getSimpleName().toLowerCase()).add("box", box).add("name", item.name$());
		if (item.hidden() == AbstractItem.Hidden.HiddenEnabled)
			builder.add("hidden", new FrameBuilder().add("box", box).add("layout", owner.name$()).add("path", pathOf(item.core$())).toFrame());
		return builder;
	}

	private FrameBuilder filterFrame(View view, String box) {
		final String itemClass = view.asCatalogContainer().catalog().itemClass();
		return new FrameBuilder("filter").add("owner", owner.name$()).add("view", view.name$()).add("box", box).add("itemClass", itemClass);
	}

	private FrameBuilder hidden(View view, String box) {
		return new FrameBuilder("hidden").add("owner", owner.name$()).add("view", view.name$()).add("box", box);
	}

	private String pathOf(Node node) {
		StringBuilder qn = new StringBuilder();
		Node parent = node;
		while (!parent.equals(view.core$())) {
			qn.insert(0, StringFormatters.firstUpperCase().format(parent.name()).toString() + ((qn.length() == 0) ? "" : "."));
			parent = parent.owner();
		}
		qn.insert(0, StringFormatters.firstUpperCase().format(view.core$().name()).toString() + ((qn.length() == 0) ? "" : "."));
		return qn.toString();
	}

}
