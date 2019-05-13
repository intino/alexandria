package io.intino.konos.builder.codegeneration.services.ui.display.toolbar;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIPrototypeRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.AbstractToolbar;
import io.intino.konos.model.graph.AbstractToolbar.Operation;
import io.intino.konos.model.graph.Catalog;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Toolbar;
import io.intino.tara.magritte.Layer;

import java.io.File;

public class OperationRenderer extends UIPrototypeRenderer {
	private final Operation operation;
	private final Component owner;

	public OperationRenderer(Operation operation, Component owner, String box, String packageName) {
		super(operation.name$(), box, packageName);
		this.operation = operation;
		this.owner = owner;
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder builder = super.frameBuilder().add("operation").add(operation.getClass().getSimpleName())
				.add("title", operation.title())
				.add("owner", owner.name$())
				.add("ownerClass", owner.getClass().getSimpleName())
				.add("mode", operation.mode().toString());

		if (owner.i$(Catalog.class)) builder.add("itemClass", owner.a$(Catalog.class).itemClass());
		if (operation.polymerIcon() != null) builder.add("icon", operation.polymerIcon());

		addTaskProperties(builder);
		addTaskSelectionProperties(builder);
		addOpenDialogProperties(builder);
		addOpenCatalogProperties(builder);
		addOpenCatalogSelectionProperties(builder);

		return builder;
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(new OperationTemplate());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(new AbstractOperationTemplate());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	private void addTaskProperties(FrameBuilder builder) {
		if (!operation.i$(AbstractToolbar.Task.class)) return;
		AbstractToolbar.Task task = operation.a$(AbstractToolbar.Task.class);
		if (task.confirmText() == null) return;
		builder.add("confirmText", task.confirmText());
	}

	private void addTaskSelectionProperties(FrameBuilder builder) {
		if (!operation.i$(AbstractToolbar.TaskSelection.class)) return;
		AbstractToolbar.TaskSelection taskSelection = operation.a$(AbstractToolbar.TaskSelection.class);
		if (taskSelection.confirmText() == null) return;
		builder.add("confirmText", taskSelection.confirmText());
	}

	private void addOpenDialogProperties(FrameBuilder builder) {
		if (!operation.i$(AbstractToolbar.OpenDialog.class)) return;
		builder.add("dialog", operation.a$(AbstractToolbar.OpenDialog.class).dialog().name$());
	}

	private void addOpenCatalogProperties(FrameBuilder builder) {
		if (!operation.i$(Toolbar.OpenCatalog.class)) return;
		Toolbar.OpenCatalog openCatalog = operation.a$(Toolbar.OpenCatalog.class);
		builder.add("width", openCatalog.width());
		builder.add("height", openCatalog.height());
		builder.add("position", openCatalog.position().toString());
		builder.add("openCatalog", openCatalog.catalog().name$());
		builder.add("view", openCatalog.views().stream().map(Layer::name$).toArray(String[]::new));
		if (openCatalog.filtered())
			builder.add("openCatalogOperationFilter", new FrameBuilder().add("owner", owner.name$()).add("ownerClass", owner.getClass().getSimpleName()).add("box", box));
	}

	private void addOpenCatalogSelectionProperties(FrameBuilder builder) {
		if (!operation.i$(AbstractToolbar.OpenCatalogSelection.class)) return;
		String itemClass = owner.a$(Catalog.class).itemClass();
		AbstractToolbar.OpenCatalogSelection openCatalogSelection = operation.a$(AbstractToolbar.OpenCatalogSelection.class);
		builder.add("width", openCatalogSelection.width());
		builder.add("height", openCatalogSelection.height());
		builder.add("position", openCatalogSelection.position().toString());
		builder.add("openCatalog", openCatalogSelection.catalog().name$());
		builder.add("view", openCatalogSelection.views().stream().map(Layer::name$).toArray(String[]::new));
		builder.add("selection", openCatalogSelection.selection().toString());
		if (openCatalogSelection.filtered())
			builder.add("openCatalogSelectionOperationFilter", new FrameBuilder().add("owner", owner.name$()).add("ownerClass", owner.getClass().getSimpleName()).add("box", box).add("itemClass", itemClass));
		builder.add("openCatalogSelectionOperation", new FrameBuilder().add("owner", owner.name$()).add("ownerClass", owner.getClass().getSimpleName()).add("box", box).add("itemClass", itemClass));
	}

}
