package io.intino.konos.builder.codegeneration.services.ui.display.view;

import io.intino.konos.builder.codegeneration.services.ui.Renderer;
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
import org.siani.itrules.Template;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.konos.model.graph.View.Hidden.HiddenEnabled;

public class ViewRenderer extends Renderer {
	private final View view;
	private final Component owner;
	private final String box;
	private final String packageName;
	private boolean buildingSrc = false;

	public ViewRenderer(View view, Component owner, String box, String packageName) {
		super(view.name$(), box, packageName);
		this.view = view;
		this.owner = owner;
		this.box = box;
		this.packageName = packageName;
	}

	public String buildSrc() {
		buildingSrc = true;
		return ViewSrcTemplate.create().format(createFrame());
	}

	public String buildGen() {
		buildingSrc = false;
		return ViewGenTemplate.create().format(createFrame());
	}

	@Override
	protected Template srcTemplate() {
		return null;
	}

	@Override
	protected Template genTemplate() {
		return null;
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	@Override
	protected Frame createFrame() {
		Frame frame = super.createFrame();
		frame.addTypes("view")
				.addSlot("owner", owner.name$())
				.addSlot("ownerClass", owner.getClass().getSimpleName())
				.addSlot("label", view.label())
				.addSlot("layout", view.layout().name())
				.addSlot("width", view.width());

		if (view.hidden() == HiddenEnabled) {
			frame.addSlot("hidden", hidden(view, box));
			frame.addTypes("hasMethods");
		}

		addViewProperties(frame);

		return frame;
	}

	private void addViewProperties(Frame frame) {
		frame.addSlot("viewDisplayTypeLoader", new Frame("viewDisplayTypeLoader").addSlot("package", packageName));

		addCollectionContainerProperties(view, frame);
		addDisplayContainerProperties(view, frame);
		addMoldContainerProperties(view, frame);
		addCatalogContainerProperties(view, frame);
		addPanelContainerProperties(view, frame);
		addSetContainerProperties(view, frame);
	}

	private void addCollectionContainerProperties(View view, Frame frame) {
		if (!view.isCollectionContainer()) return;

		CollectionContainerView collectionView = this.view.asCollectionContainer();
		frame.addSlot("mold", collectionView.mold().name$());

		if (collectionView.noItemsMessage() != null) frame.addSlot("noItemsMessage", collectionView.noItemsMessage());

		addListContainerProperties(collectionView, frame);
		addGridContainerProperties(collectionView, frame);
		addMapContainerProperties(collectionView, frame);
		addMagazineContainerProperties(collectionView, frame);
	}

	private void addListContainerProperties(CollectionContainerView view, Frame frame) {
		if (!view.i$(ListContainerView.class)) return;
		frame.addTypes("list");
	}

	private void addGridContainerProperties(CollectionContainerView view, Frame frame) {
		if (!view.i$(GridContainerView.class)) return;
		frame.addTypes("grid");
	}

	private void addMapContainerProperties(CollectionContainerView view, Frame frame) {
		if (!view.i$(MapContainerView.class)) return;
		frame.addTypes("map");

		MapContainerView mapView = view.a$(MapContainerView.class);

		if (mapView.center() != null)
			frame.addSlot("latitude", mapView.center().latitude()).addSlot("longitude", mapView.center().longitude());

		frame.addSlot("zoom", new Frame("zoom")
				.addSlot("default", mapView.zoom().defaultZoom())
				.addSlot("min", mapView.zoom().min())
				.addSlot("max", mapView.zoom().max()));
	}

	private void addMagazineContainerProperties(CollectionContainerView view, Frame frame) {
		if (!view.i$(MagazineContainerView.class)) return;
		frame.addTypes("magazine");
	}

	private void addDisplayContainerProperties(View view, Frame frame) {
		if (!view.isDisplayContainer()) return;
		frame.addTypes("display");

		DisplayContainerView displayView = view.asDisplayContainer();
		frame.addSlot("hideNavigator", displayView.hideNavigator());
		frame.addSlot("display", displayView.display());

		if (owner.i$(Catalog.class))
			frame.addSlot("catalogScope", new Frame("catalogScope").addSlot("name", view.name$()).addSlot("display", displayView.display()).addSlot("box", box).addSlot("owner", owner.name$()));
	}

	private void addMoldContainerProperties(View view, Frame frame) {
		if (!view.isMoldContainer()) return;
		frame.addTypes("mold");
		final Mold mold = view.asMoldContainer().mold();
		frame.addSlot("mold", mold.name$());
	}

	private void addCatalogContainerProperties(View view, Frame frame) {
		if (!view.isCatalogContainer()) return;
		frame.addTypes("catalog");
		CatalogContainerView catalogContainerView = view.asCatalogContainer();
		frame.addSlot("catalog", catalogContainerView.catalog().name$());
		frame.addSlot("catalogDisplayLoader", new Frame("catalogDisplayLoader").addSlot("package", packageName));
		if (catalogContainerView.filtered()) frame.addSlot("filter", filterFrame(view, box));
	}

	private void addPanelContainerProperties(View view, Frame frame) {
		if (!view.isPanelContainer()) return;
		frame.addTypes("panel");
		PanelContainerView panelContainerView = view.asPanelContainer();
		frame.addSlot("panel", panelContainerView.panel().name$());
	}

	private void addSetContainerProperties(View view, Frame frame) {
		if (!view.isSetContainer()) return;
		frame.addTypes("set");
		SetContainerView setView = view.asSetContainer();
		frame.addSlot("setViewItems", new Frame("setViewItems").addSlot("item", frameOf(setView.abstractItemList())));
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
		final Frame frame = baseFrame(group).addTypes("group").addSlot("label", group.label()).addSlot("mode", group.mode());
		if (!group.itemList().isEmpty())
			frame.addSlot("item", group.itemList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!group.itemsList().isEmpty())
			frame.addSlot("item", group.itemsList().stream().map(this::frameOf).toArray(Frame[]::new));
		return frame;
	}

	private Frame frameOf(Item item) {
		final Frame frame = baseFrame(item).addTypes("item");
		final View view = item.view();
		ViewRenderer builder = new ViewRenderer(view, owner, box, packageName);
		frame.addSlot("view", buildingSrc ? builder.buildSrc() : builder.buildGen());
		return frame;
	}

	private Frame frameOf(Items items) {
		Frame frame = baseFrame(items).addTypes("items").addSlot("layout", owner.name$()).addSlot("path", pathOf(items.core$())).addSlot("modelClass", items.itemClass());
		final View view = items.view();
		ViewRenderer builder = new ViewRenderer(view, owner, box, packageName);
		frame.addSlot("view", buildingSrc ? builder.buildSrc() : builder.buildGen());
		return frame;
	}

	private Frame baseFrame(AbstractItem item) {
		Frame frame = new Frame("set", item.getClass().getSimpleName().toLowerCase()).addSlot("box", box).addSlot("name", item.name$());
		if (item.hidden() == AbstractItem.Hidden.HiddenEnabled)
			frame.addSlot("hidden", new Frame().addSlot("box", box).addSlot("layout", owner.name$()).addSlot("path", pathOf(item.core$())));
		return frame;
	}

	private Frame filterFrame(View view, String box) {
		final String itemClass = view.asCatalogContainer().catalog().itemClass();
		return new Frame("filter").addSlot("owner", owner.name$()).addSlot("view", view.name$()).addSlot("box", box).addSlot("itemClass", itemClass);
	}

	private Frame hidden(View view, String box) {
		return new Frame("hidden").addSlot("owner", owner.name$()).addSlot("view", view.name$()).addSlot("box", box);
	}

	private String pathOf(Node node) {
		String qn = "";
		Node parent = node;
		while (!parent.equals(owner.core$())) {
			qn = StringFormatter.firstUpperCase().format(parent.name()).toString() + (qn.isEmpty() ? "" : ".") + qn;
			parent = parent.owner();
		}
		return qn;
	}

}
