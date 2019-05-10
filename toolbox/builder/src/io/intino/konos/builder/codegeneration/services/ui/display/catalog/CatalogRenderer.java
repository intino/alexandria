package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Catalog.Events.OnClickItem;
import io.intino.konos.model.graph.Catalog.Events.OnClickItem.CatalogEvent;
import io.intino.konos.model.graph.Catalog.Events.OnClickItem.OpenPanel;

import java.io.File;

public class CatalogRenderer extends DisplayRenderer {

	private final Project project;
	private final String itemClass;

	public CatalogRenderer(Project project, Catalog catalog, String packageName, String box) {
		super(catalog, box, packageName);
		this.project = project;
		this.itemClass = catalog.itemClass();
	}

	@Override
	public FrameBuilder frameBuilder() {
		final Catalog catalog = display().a$(Catalog.class);
		final FrameBuilder builder = super.frameBuilder().add("catalog").add("type", itemClass);
		if (catalog.i$(TemporalCatalog.class)) {
			final TemporalCatalog temporalCatalog = catalog.a$(TemporalCatalog.class);
			builder.add("mode", temporalCatalog.type().name());
			builder.add("scale", temporalCatalog.scales().stream().map(Enum::name).toArray());
			builder.add("range", new FrameBuilder().add("catalog", catalog.name$()).add("box", box).add("type", itemClass).toFrame());
			if (temporalCatalog.temporalFilter() != null)
				builder.add("temporalFilter", frameOf(temporalCatalog, temporalCatalog.temporalFilter()));
		}
		if (catalog.label() != null) builder.add("label", catalog.label());
		if (catalog.events() != null) builder.add("event", frameOf(catalog.events().onClickItem()));
		if (catalog.toolbar() != null) builder.add("toolbar", frameOf(catalog.toolbar()));
		if (catalog.hasCustomItemsArrivalMessage()) builder.add("hasCustomItemsArrivalMessage", baseFrameBuilder());
		views(catalog, builder);
		arrangements(catalog, builder);
		return builder;
	}

	private Frame frameOf(TemporalCatalog catalog, TemporalCatalog.TemporalFilter filter) {
		FrameBuilder builder = new FrameBuilder().add("temporalFilterLayout", filter.layout().toString());
		FrameBuilder enabledFrame = new FrameBuilder();
		if (filter.enabled() == TemporalCatalog.TemporalFilter.Enabled.Conditional)
			enabledFrame.add("catalog", catalog.name$());
		builder.add("temporalFilterEnabled", enabledFrame.add(filter.enabled().toString(), filter.enabled().toString()));
		FrameBuilder visibleFrame = new FrameBuilder();
		if (filter.visible() == TemporalCatalog.TemporalFilter.Visible.Conditional)
			visibleFrame.add("catalog", catalog.name$());
		builder.add("temporalFilterVisible", visibleFrame.add(filter.visible().toString(), filter.visible().toString()));
		return builder.toFrame();
	}

	private Frame frameOf(OnClickItem onClickItem) {
		final CatalogEvent catalogEvent = onClickItem.catalogEvent();
		Display display = display();
		if (catalogEvent.i$(OpenPanel.class))
			return frameOf(catalogEvent.a$(OpenPanel.class), display.a$(Catalog.class), box, itemClass);
		else if (catalogEvent.i$(OnClickItem.OpenCatalog.class))
			return frameOf(catalogEvent.a$(OnClickItem.OpenCatalog.class), display.a$(Catalog.class), box, itemClass);
		return frameOf(catalogEvent.a$(OnClickItem.OpenDialog.class), display);
	}

	public Frame frameOf(OnClickItem.OpenDialog openDialog, Display catalog) {
		final FrameBuilder builder = new FrameBuilder("event", openDialog.getClass().getSimpleName()).add("box", box).add("package", packageName);
		if (openDialog.height() >= 0) builder.add("height", openDialog.height());
		if (openDialog.width() >= 0) builder.add("width", openDialog.width());
		builder.add("dialog", openDialog.dialog().name$());
		builder.add("catalog", catalog.name$());
		return builder.toFrame();
	}

	public Frame frameOf(OnClickItem.OpenPanel openPanel, Catalog catalog, String box, String modelClass) {
		final FrameBuilder builder = new FrameBuilder("event", openPanel.getClass().getSimpleName());
		builder.add("panel", openPanel.panel().name$());
		if (openPanel.hasBreadcrumbs())
			builder.add("breadcrumbs", new FrameBuilder("breadCrumbs").add("catalog", catalog.name$()).add("box", box).add("type", modelClass));
		return builder.toFrame();
	}

	public Frame frameOf(OnClickItem.OpenCatalog openCatalog, Catalog catalog, String box, String modelClass) {
		final FrameBuilder builder = new FrameBuilder("event", openCatalog.getClass().getSimpleName());
		builder.add("catalog", openCatalog.catalog().name$());
		if (openCatalog.openItem())
			builder.add("openCatalogLoader", new FrameBuilder("openCatalogLoader").add("catalog", catalog.name$()).add("box", box).add("type", modelClass));
		if (openCatalog.filtered())
			builder.add("openCatalogFilter", new FrameBuilder("openCatalogFilter").add("catalog", catalog.name$()).add("box", box).add("type", modelClass));
		return builder.toFrame();
	}

	private void views(Catalog catalog, FrameBuilder frameBuilder) {
		if (catalog.views().viewList().stream().anyMatch(View::isMagazineContainer)) {
			final FrameBuilder hasMagazineBuilder = baseFrameBuilder().add("type", catalog.itemClass());
			if (catalog.i$(TemporalCatalog.class)) {
				final TemporalCatalog temporalCatalog = catalog.a$(TemporalCatalog.class);
				hasMagazineBuilder.add("mode", temporalCatalog.type().name());
				hasMagazineBuilder.add("scale", temporalCatalog.scales().stream().map(Enum::name).toArray());
			}
			frameBuilder.add("hasMagazineView", hasMagazineBuilder.toFrame());
		}

		catalog.views().viewList().forEach(view -> {
			ViewRenderer builder = new ViewRenderer(view, display(), box, packageName);
			frameBuilder.add("view", new FrameBuilder("view").add("value", builder.frameBuilder()).toFrame());
		});
	}

	private Frame frameOf(Toolbar toolbar) {
		final FrameBuilder builder = new FrameBuilder("toolbar")
				.add("box", box)
				.add("type", this.itemClass)
				.add("canSearch", toolbar.canSearch());
		toolbar.operations().forEach(operation -> {
			OperationRenderer renderer = new OperationRenderer(operation, display(), box, packageName);
			builder.add("operation", renderer.frameBuilder());
		});
		return builder.toFrame();
	}

	private void arrangements(Catalog catalog, FrameBuilder builder) {
		Catalog.Content content = catalog.content();
		boolean existsGroupings = content != null && !content.groupingList().isEmpty();
		if (existsGroupings)
			builder.add("hasGroupings", baseFrameBuilder().add("histogramsMode", content.histograms().toString()).add("position", content.groupingsPosition().toString()));
		if (content == null) return;
		content.groupingList().forEach(grouping -> builder.add("arrangement", frameOf(grouping, catalog, this.box, this.itemClass)));
		content.sortingList().forEach(sorting -> builder.add("arrangement", frameOf(sorting, catalog, this.box, this.itemClass)));
	}

	private FrameBuilder baseFrameBuilder() {
		return new FrameBuilder().add("box", box).add("name", display().name$());
	}

	public Frame frameOf(Catalog.Content.Grouping grouping, Catalog catalog, String box, String modelClass) {
		return new FrameBuilder("arrangement", grouping.getClass().getSimpleName().toLowerCase())
				.add("box", box)
				.add("name", grouping.name$())
				.add("label", grouping.label())
				.add("catalog", catalog.name$())
				.add("type", modelClass)
				.add("histogram", grouping.histogram()).toFrame();
	}

	public Frame frameOf(Catalog.Content.Sorting sorting, Catalog catalog, String box, String modelClass) {
		return new FrameBuilder("arrangement", sorting.getClass().getSimpleName().toLowerCase())
				.add("box", box)
				.add("name", sorting.name$())
				.add("label", sorting.label())
				.add("visible", sorting.visible())
				.add("catalog", catalog.name$())
				.add("type", modelClass).toFrame();
	}

	protected Template srcTemplate() {
		return Formatters.customize(new CatalogTemplate());
	}

	protected Template genTemplate() {
		return Formatters.customize(new AbstractCatalogTemplate());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new CatalogUpdater(sourceFile, display().a$(Catalog.class), project, packageName, box);
	}
}