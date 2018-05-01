package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationFrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewFrameBuilder;
import io.intino.konos.model.graph.AbstractToolbar.OpenCatalog;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Catalog.Events.OnClickItem;
import io.intino.konos.model.graph.Catalog.Events.OnClickItem.CatalogEvent;
import io.intino.konos.model.graph.Catalog.Events.OnClickItem.OpenPanel;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class CatalogRenderer extends Renderer {

	private final Project project;
	private final String itemClass;

	public CatalogRenderer(Project project, Catalog catalog, File src, File gen, String packageName, String box) {
		super(catalog, box, packageName, src, gen);
		this.project = project;
		this.itemClass = catalog.itemClass();
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeGen(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Catalog catalog = this.display.a$(Catalog.class);
		final Frame frame = super.createFrame().addTypes("catalog").addSlot("type", itemClass);
		if (catalog.i$(TemporalCatalog.class)) {
			final TemporalCatalog temporalCatalog = catalog.a$(TemporalCatalog.class);
			frame.addSlot("mode", temporalCatalog.type().name());
			frame.addSlot("scale", temporalCatalog.scales().stream().map(Enum::name).toArray());
			frame.addSlot("range", new Frame().addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", itemClass));
			if (temporalCatalog.temporalFilter() != null)
				frame.addSlot("temporalFilter", frameOf(temporalCatalog, temporalCatalog.temporalFilter()));
		}
		if (catalog.label() != null) frame.addSlot("label", catalog.label());
		if (catalog.events() != null) frame.addSlot("event", frameOf(catalog.events().onClickItem()));
		if (catalog.toolbar() != null) frame.addSlot("toolbar", frameOf(catalog.toolbar()));
		views(catalog, frame);
		arrangements(catalog, frame);
		return frame;
	}

	private Frame frameOf(TemporalCatalog catalog, TemporalCatalog.TemporalFilter filter) {
		Frame frame = new Frame();

		frame.addSlot("temporalFilterLayout", filter.layout().toString());

		Frame enabledFrame = new Frame();
		if (filter.enabled() == TemporalCatalog.TemporalFilter.Enabled.Conditional)
			enabledFrame.addSlot("catalog", catalog.name$());

		frame.addSlot("temporalFilterEnabled", enabledFrame.addSlot(filter.enabled().toString(), filter.enabled().toString()));

		Frame visibleFrame = new Frame();
		if (filter.visible() == TemporalCatalog.TemporalFilter.Visible.Conditional)
			visibleFrame.addSlot("catalog", catalog.name$());

		frame.addSlot("temporalFilterVisible", visibleFrame.addSlot(filter.visible().toString(), filter.visible().toString()));

		return frame;
	}

	private Frame frameOf(OnClickItem onClickItem) {
		final CatalogEvent catalogEvent = onClickItem.catalogEvent();
		if (catalogEvent.i$(OpenPanel.class))
			return frameOf(catalogEvent.a$(OpenPanel.class), display.a$(Catalog.class), box, itemClass);
		else if (catalogEvent.i$(OpenCatalog.class))
			return frameOf(catalogEvent.a$(OpenCatalog.class), display.a$(Catalog.class), box, itemClass);
		return frameOf(catalogEvent.a$(OnClickItem.OpenDialog.class), this.display).addSlot("box", box).addSlot("package", packageName);
	}

	public static Frame frameOf(OnClickItem.OpenDialog openDialog, Display catalog) {
		final Frame frame = new Frame("event", openDialog.getClass().getSimpleName());
		if (openDialog.height() >= 0) frame.addSlot("height", openDialog.height());
		if (openDialog.width() >= 0) frame.addSlot("width", openDialog.width());
		frame.addSlot("dialog", openDialog.dialog().name$());
		frame.addSlot("catalog", catalog.name$());
		return frame;
	}

	public static Frame frameOf(OpenPanel openPanel, Catalog catalog, String box, String modelClass) {
		final Frame frame = new Frame("event", openPanel.getClass().getSimpleName());
		frame.addSlot("panel", openPanel.panel().name$());
		if (openPanel.hasBreadcrumbs())
			frame.addSlot("breadcrumbs", new Frame("breadCrumbs").addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", modelClass));
		return frame;
	}

	public static Frame frameOf(OpenCatalog openCatalog, Catalog catalog, String box, String modelClass) {
		final Frame frame = new Frame("event", openCatalog.getClass().getSimpleName());
		frame.addSlot("catalog", openCatalog.catalog().name$());
		if (openCatalog.openItem())
			frame.addSlot("openCatalogLoader", new Frame("openCatalogLoader").addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", modelClass));
		if (openCatalog.filtered())
			frame.addSlot("openCatalogFilter", new Frame("openCatalogFilter").addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", modelClass));
		return frame;
	}

	private void views(Catalog catalog, Frame frame) {
		if (catalog.views().viewList().stream().anyMatch(v -> v.i$(MagazineView.class))) {
			final Frame hasMagazineFrame = baseFrame().addSlot("type", catalog.itemClass());
			if (catalog.i$(TemporalCatalog.class)) {
				final TemporalCatalog temporalCatalog = catalog.a$(TemporalCatalog.class);
				hasMagazineFrame.addSlot("mode", temporalCatalog.type().name());
				hasMagazineFrame.addSlot("scale", temporalCatalog.scales().stream().map(Enum::name).toArray());
			}
			frame.addSlot("hasMagazineView", hasMagazineFrame);
		}

		catalog.views().viewList().forEach(view -> {
			ViewFrameBuilder builder = new ViewFrameBuilder(view, display, box, packageName);
			frame.addSlot("view", buildingSrc() ? builder.buildSrc() : builder.buildGen());
		});
	}

	private void arrangements(Catalog catalog, Frame frame) {
		Catalog.Content content = catalog.content();
		boolean existsGroupings = content != null && !content.groupingList().isEmpty();
		if (existsGroupings) frame.addSlot("hasGroupings", baseFrame().addSlot("histogramsMode", content.histograms().toString()));
		if (content == null) return;
		content.groupingList().forEach(grouping -> frame.addSlot("arrangement", frameOf(grouping, catalog, this.box, this.itemClass)));
		content.sortingList().forEach(sorting -> frame.addSlot("arrangement", frameOf(sorting, catalog, this.box, this.itemClass)));
	}

	private Frame baseFrame() {
		return new Frame().addSlot("box", box).addSlot("name", display.name$());
	}

	public static Frame frameOf(Catalog.Content.Sorting sorting, Catalog catalog, String box, String modelClass) {
		return new Frame("arrangement", sorting.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("name", sorting.name$())
				.addSlot("label", sorting.label())
				.addSlot("visible", sorting.visible())
				.addSlot("catalog", catalog.name$())
				.addSlot("type", modelClass);
	}

	public static Frame frameOf(Catalog.Content.Grouping grouping, Catalog catalog, String box, String modelClass) {
		return new Frame("arrangement", grouping.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("name", (String) grouping.name$())
				.addSlot("label", ((String) grouping.label()))
				.addSlot("catalog", catalog.name$())
				.addSlot("type", modelClass)
				.addSlot("histogram", grouping.histogram());
	}

	private Frame frameOf(Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		frame.addSlot("box", box).addSlot("type", this.itemClass).addSlot("canSearch", toolbar.canSearch());
		toolbar.operations().forEach(operation -> {
			OperationFrameBuilder builder = new OperationFrameBuilder(operation, display, box, packageName);
			frame.addSlot("operation", buildingSrc() ? builder.buildSrc() : builder.buildGen());
		});
		return frame;
	}

	protected Template srcTemplate() {
		return Formatters.customize(CatalogSrcTemplate.create());
	}

	protected Template genTemplate() {
		return Formatters.customize(CatalogGenTemplate.create());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new CatalogUpdater(sourceFile, display.a$(Catalog.class), project, packageName, box);
	}
}
