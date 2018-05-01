package io.intino.konos.builder.codegeneration.services.ui.display.toolbar;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.graph.AbstractToolbar;
import io.intino.konos.model.graph.AbstractToolbar.Operation;
import io.intino.konos.model.graph.Catalog;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Toolbar;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.model.Frame;

public class OperationFrameBuilder extends Frame {
	private final Operation operation;
	private final Component owner;
	private final String box;
	private final String packageName;

	public OperationFrameBuilder(Operation operation, Component owner, String box, String packageName) {
		super();
		this.operation = operation;
		this.owner = owner;
		this.box = box;
		this.packageName = packageName;
	}

	public String buildSrc() {
		return Formatters.customize(OperationSrcTemplate.create()).format(createFrame());
	}

	public String buildGen() {
		return Formatters.customize(OperationGenTemplate.create()).format(createFrame());
	}

	private Frame createFrame() {
		Frame frame = new Frame("operation", operation.getClass().getSimpleName())
				.addSlot("name", operation.name$())
				.addSlot("title", operation.title())
				.addSlot("owner", owner.name$())
				.addSlot("ownerClass", owner.getClass().getSimpleName())
				.addSlot("mode", operation.mode().toString())
				.addSlot("box", box)
				.addSlot("package", packageName);

		if (owner.i$(Catalog.class)) frame.addSlot("itemClass", owner.a$(Catalog.class).itemClass());
		if (operation.polymerIcon() != null) frame.addSlot("icon", operation.polymerIcon());

		addTaskProperties(frame);
		addTaskSelectionProperties(frame);
		addOpenDialogProperties(frame);
		addOpenCatalogProperties(frame);
		addOpenCatalogSelectionProperties(frame);

		return frame;
	}

	private void addTaskProperties(Frame frame) {
		if (!operation.i$(AbstractToolbar.Task.class)) return;
		AbstractToolbar.Task task = operation.a$(AbstractToolbar.Task.class);
		if (task.confirmText() == null) return;
		frame.addSlot("confirmText", task.confirmText());
	}

	private void addTaskSelectionProperties(Frame frame) {
		if (!operation.i$(AbstractToolbar.TaskSelection.class)) return;
		AbstractToolbar.TaskSelection taskSelection = operation.a$(AbstractToolbar.TaskSelection.class);
		if (taskSelection.confirmText() == null) return;
		frame.addSlot("confirmText", taskSelection.confirmText());
	}

	private void addOpenDialogProperties(Frame frame) {
		if (!operation.i$(AbstractToolbar.OpenDialog.class)) return;
		frame.addSlot("dialog", operation.a$(AbstractToolbar.OpenDialog.class).dialog().name$());
	}

	private void addOpenCatalogProperties(Frame frame) {
		if (!operation.i$(Toolbar.OpenCatalog.class)) return;
		Toolbar.OpenCatalog openCatalog = operation.a$(Toolbar.OpenCatalog.class);
		frame.addSlot("width", openCatalog.width());
		frame.addSlot("height", openCatalog.height());
		frame.addSlot("position", openCatalog.position().toString());
		frame.addSlot("openCatalog", openCatalog.catalog().name$());
		frame.addSlot("view", openCatalog.views().stream().map(Layer::name$).toArray(String[]::new));
		if (openCatalog.filtered()) frame.addSlot("openCatalogOperationFilter", new Frame().addSlot("owner", owner.name$()).addSlot("ownerClass", owner.getClass().getSimpleName()).addSlot("box", box));
	}

	private void addOpenCatalogSelectionProperties(Frame frame) {
		if (!operation.i$(AbstractToolbar.OpenCatalogSelection.class)) return;
		String itemClass = owner.a$(Catalog.class).itemClass();
		AbstractToolbar.OpenCatalogSelection openCatalogSelection = operation.a$(AbstractToolbar.OpenCatalogSelection.class);
		frame.addSlot("width", openCatalogSelection.width());
		frame.addSlot("height", openCatalogSelection.height());
		frame.addSlot("position", openCatalogSelection.position().toString());
		frame.addSlot("openCatalog", openCatalogSelection.catalog().name$());
		frame.addSlot("view", openCatalogSelection.views().stream().map(Layer::name$).toArray(String[]::new));
		frame.addSlot("selection", openCatalogSelection.selection().toString());
		if (openCatalogSelection.filtered()) frame.addSlot("openCatalogSelectionOperationFilter", new Frame().addSlot("owner", owner.name$()).addSlot("ownerClass", owner.getClass().getSimpleName()).addSlot("box", box).addSlot("itemClass", itemClass));
		frame.addSlot("openCatalogSelectionOperation", new Frame().addSlot("owner", owner.name$()).addSlot("ownerClass", owner.getClass().getSimpleName()).addSlot("box", box).addSlot("itemClass", itemClass));
	}

}
