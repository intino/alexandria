package io.intino.konos.builder.codegeneration.services.ui.display.view;

import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.catalogcontainer.CatalogContainerContainerView;
import io.intino.konos.model.graph.displaycontainer.DisplayContainerContainerView;
import io.intino.konos.model.graph.panelcontainer.PanelContainerContainerView;
import io.intino.konos.model.graph.setcontainer.SetContainerContainerView;
import io.intino.konos.model.graph.setcontainer.SetContainerContainerView.AbstractItem;
import io.intino.konos.model.graph.setcontainer.SetContainerContainerView.Group;
import io.intino.konos.model.graph.setcontainer.SetContainerContainerView.Item;
import io.intino.konos.model.graph.setcontainer.SetContainerContainerView.Items;
import io.intino.tara.magritte.Node;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.Frame;

import java.util.List;

import static io.intino.konos.model.graph.View.Hidden.HiddenEnabled;

public class ViewFrameBuilder extends Frame {
	private final View view;
	private final Component owner;
	private final String box;
	private final String packageName;
	private boolean buildingSrc = false;

	public ViewFrameBuilder(View view, Component owner, String box, String packageName) {
		super();
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

	private Frame createFrame() {
		final Frame frame = new Frame("view")
				.addSlot("owner", owner.name$())
				.addSlot("ownerClass", owner.getClass().getSimpleName())
				.addSlot("name", view.name$())
				.addSlot("label", view.label())
				.addSlot("layout", view.layout().name())
				.addSlot("box", box)
				.addSlot("packageName", packageName)
				.addSlot("width", view.width());

		if (view.hidden() == HiddenEnabled) frame.addSlot("hidden", hidden(view, box));

		addContainerViewProperties(frame);
		addCatalogViewProperties(frame);

		return frame;
	}

	private void addContainerViewProperties(Frame frame) {
		if (!view.i$(ContainerView.class)) return;

		frame.addTypes("container");
		frame.addSlot("containerViewDisplayTypeLoader", new Frame("containerViewDisplayTypeLoader"));

		ContainerView view = this.view.a$(ContainerView.class);
		addDisplayContainerProperties(view, frame);
		addMoldContainerProperties(view, frame);
		addCatalogContainerProperties(view, frame);
		addPanelContainerProperties(view, frame);
		addSetContainerProperties(view, frame);
	}

	private void addCatalogViewProperties(Frame frame) {
		if (!view.i$(CatalogView.class)) return;

		CatalogView view = this.view.a$(CatalogView.class);
		frame.addTypes("catalog");
		frame.addSlot("mold", view.mold().name$());

		if (view.noItemsMessage() != null) frame.addSlot("noItemsMessage", view.noItemsMessage());

		addListViewProperties(view, frame);
		addGridViewProperties(view, frame);
		addMapViewProperties(view, frame);
		addMagazineViewProperties(view, frame);
	}

	private void addListViewProperties(CatalogView view, Frame frame) {
		if (!view.i$(ListView.class)) return;
		frame.addTypes("list");
	}

	private void addGridViewProperties(CatalogView view, Frame frame) {
		if (!view.i$(GridView.class)) return;
		frame.addTypes("grid");
	}

	private void addMapViewProperties(CatalogView view, Frame frame) {
		if (!view.i$(MapView.class)) return;
		frame.addTypes("map");

		MapProperties properties = view.a$(MapView.class).mapProperties();

		if (properties.center() != null)
			frame.addSlot("latitude", properties.center().latitude()).addSlot("longitude", properties.center().longitude());

		frame.addSlot("zoom", new Frame("zoom")
				.addSlot("default", properties.zoom().defaultZoom())
				.addSlot("min", properties.zoom().min())
				.addSlot("max", properties.zoom().max()));
	}

	private void addMagazineViewProperties(CatalogView view, Frame frame) {
		if (!view.i$(MagazineView.class)) return;
		frame.addTypes("magazine");
	}

	private void addDisplayContainerProperties(ContainerView view, Frame frame) {
		if (!view.isDisplayContainer()) return;
		frame.addTypes("display");

		DisplayContainerContainerView displayView = view.asDisplayContainer();
		frame.addSlot("display", displayView.display());
		frame.addSlot("hideNavigator", displayView.hideNavigator());
		frame.addSlot("display", displayView.display().name$());

		if (owner.i$(Catalog.class))
			frame.addSlot("catalogScope", new Frame("catalogScope").addSlot("display", displayView.display().name$()).addSlot("box", box).addSlot("owner", owner.name$()));
	}

	private void addMoldContainerProperties(ContainerView view, Frame frame) {
		if (!view.isMoldContainer()) return;
		frame.addTypes("mold");
		final Mold mold = view.asMoldContainer().mold();
		frame.addSlot("mold", mold.name$());
	}

	private void addCatalogContainerProperties(ContainerView view, Frame frame) {
		if (!view.isCatalogContainer()) return;
		frame.addTypes("catalog");
		CatalogContainerContainerView catalogContainerView = view.asCatalogContainer();
		frame.addSlot("catalog", catalogContainerView.catalog().name$());
		frame.addSlot("displayLoader", "");
		if (catalogContainerView.filtered()) frame.addSlot("filter", filterFrame(view, box));
	}

	private void addPanelContainerProperties(ContainerView view, Frame frame) {
		if (!view.isPanelContainer()) return;
		frame.addTypes("panel");
		PanelContainerContainerView panelContainerView = view.asPanelContainer();
		frame.addSlot("panel", panelContainerView.panel().name$());
	}

	private void addSetContainerProperties(ContainerView view, Frame frame) {
		if (!view.isSetContainer()) return;
		frame.addTypes("set");
		SetContainerContainerView setView = view.asSetContainer();
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
		final View view = item.containerView();
		ViewFrameBuilder builder = new ViewFrameBuilder(view, owner, box, packageName);
		frame.addSlot("view", buildingSrc ? builder.buildSrc() : builder.buildGen());
		return frame;
	}

	private Frame frameOf(Items items) {
		Frame frame = baseFrame(items).addTypes("items").addSlot("layout", owner.name$()).addSlot("path", pathOf(items.core$())).addSlot("modelClass", items.itemClass());
		final View view = items.containerView();
		ViewFrameBuilder builder = new ViewFrameBuilder(view, owner, box, packageName);
		frame.addSlot("view", buildingSrc ? builder.buildSrc() : builder.buildGen());
		return frame;
	}

	private Frame baseFrame(AbstractItem item) {
		Frame frame = new Frame("set", item.getClass().getSimpleName().toLowerCase()).addSlot("box", box).addSlot("name", item.name$());
		if (item.hidden() == AbstractItem.Hidden.HiddenEnabled)
			frame.addSlot("hidden", new Frame().addSlot("box", box).addSlot("layout", owner.name$()).addSlot("path", pathOf(item.core$())));
		return frame;
	}

	private Frame filterFrame(ContainerView view, String box) {
		final String itemClass = view.asCatalogContainer().catalog().itemClass();
		return new Frame("filter").addSlot("owner", owner.name$()).addSlot("view", view.name$()).addSlot("box", box).addSlot("itemClass", itemClass);
	}

	private Frame hidden(View view, String box) {
		return new Frame().addSlot("panel", owner.name$()).addSlot("view", view.name$()).addSlot("box", box);
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
