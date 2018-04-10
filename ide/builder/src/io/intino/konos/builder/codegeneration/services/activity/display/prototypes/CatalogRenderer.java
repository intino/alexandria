package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters.CatalogUpdater;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.AbstractToolbar.*;
import io.intino.konos.model.graph.Catalog.Arrangement.Grouping;
import io.intino.konos.model.graph.Catalog.Arrangement.Sorting;
import io.intino.konos.model.graph.Catalog.Events.OnClickRecord;
import io.intino.konos.model.graph.Catalog.Events.OnClickRecord.CatalogEvent;
import io.intino.konos.model.graph.Catalog.Events.OnClickRecord.OpenCatalog;
import io.intino.konos.model.graph.Catalog.Events.OnClickRecord.OpenDialog;
import io.intino.konos.model.graph.Catalog.Events.OnClickRecord.OpenPanel;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class CatalogRenderer extends PrototypeRenderer {

	private final Project project;
	private final String modelClass;

	public CatalogRenderer(Project project, Catalog catalog, File src, File gen, String packageName, String box) {
		super(catalog, box, packageName, src, gen);
		this.project = project;
		this.modelClass = catalog.modelClass();
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Catalog catalog = this.display.a$(Catalog.class);
		final Frame frame = super.createFrame().addTypes("catalog").addSlot("type", modelClass);
		if (catalog.i$(TemporalCatalog.class)) {
			final TemporalCatalog temporalCatalog = catalog.a$(TemporalCatalog.class);
			frame.addSlot("mode", temporalCatalog.type().name());
			frame.addSlot("scale", temporalCatalog.scales().stream().map(Enum::name).toArray());
			frame.addSlot("range", new Frame().addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", modelClass));
			if (temporalCatalog.temporalFilter() != null)
				frame.addSlot("temporalFilter", frameOf(temporalCatalog, temporalCatalog.temporalFilter()));
		}
		if (catalog.label() != null) frame.addSlot("label", catalog.label());
		events(catalog, frame);
		toolbar(catalog, frame);
		views(catalog, frame);
		arrangements(catalog, frame);
		return frame;
	}

	private void events(Catalog catalog, Frame frame) {
		if (catalog.events() != null)
			frame.addSlot("event", frameOf(catalog.events().onClickRecord()));
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

	private Frame frameOf(OnClickRecord onClickRecord) {
		final CatalogEvent catalogEvent = onClickRecord.catalogEvent();
		if (catalogEvent.i$(OpenPanel.class))
			return frameOf(catalogEvent.a$(OpenPanel.class), display.a$(Catalog.class), box, modelClass);
		else if (catalogEvent.i$(OpenCatalog.class))
			return frameOf(catalogEvent.a$(OpenCatalog.class), display.a$(Catalog.class), box, modelClass);
		return frameOf(catalogEvent.a$(OpenDialog.class), this.display).addSlot("box", box).addSlot("package", packageName);
	}

	public static Frame frameOf(OpenDialog openDialog, Display catalog) {
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

	private void toolbar(Catalog catalog, Frame frame) {
		if (catalog.toolbar() != null) {
			frame.addSlot("toolbar", frameOf(catalog.toolbar()));
// TODO MC filtros
//			if (catalog.toolbar().groupingSelection() != null)
//				frame.addSlot("groupingselection", baseFrame());
		}
	}

	private void views(Catalog catalog, Frame frame) {
		if (catalog.views().catalogViewList().stream().anyMatch(v -> v.i$(MagazineView.class))) {
			final Frame hasMagazineFrame = baseFrame().addSlot("type", catalog.modelClass());
			if (catalog.i$(TemporalCatalog.class)) {
				final TemporalCatalog temporalCatalog = catalog.a$(TemporalCatalog.class);
				hasMagazineFrame.addSlot("mode", temporalCatalog.type().name());
				hasMagazineFrame.addSlot("scale", temporalCatalog.scales().stream().map(Enum::name).toArray());
			}
			frame.addSlot("hasMagazineView", hasMagazineFrame);
		}
		for (CatalogView view : catalog.views().catalogViewList()) frame.addSlot("view", frameOf(view, catalog));
		if (catalog.views().displayView() != null) frame.addSlot("view", frameOf(catalog.views().displayView(), catalog, box, packageName));
	}

	private void arrangements(Catalog catalog, Frame frame) {
		Catalog.Arrangement arrangement = catalog.arrangement();
		boolean existsGroupings = arrangement != null && !arrangement.groupingList().isEmpty();
		if (existsGroupings) frame.addSlot("hasGroupings", baseFrame().addSlot("histogramsMode", arrangement.histograms().toString()));
		if (arrangement == null) return;
		arrangement.groupingList().forEach(grouping -> frame.addSlot("arrangement", frameOf(grouping, catalog, this.box, this.modelClass)));
		arrangement.sortingList().forEach(sorting -> frame.addSlot("arrangement", frameOf(sorting, catalog, this.box, this.modelClass)));
	}

	private Frame baseFrame() {
		return new Frame().addSlot("box", box).addSlot("name", display.name$());
	}

	public static Frame frameOf(Sorting sorting, Catalog catalog, String box, String modelClass) {
		return new Frame("arrangement", sorting.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("name", sorting.name$())
				.addSlot("label", sorting.label())
				.addSlot("visible", sorting.visible())
				.addSlot("catalog", catalog.name$())
				.addSlot("type", modelClass);
	}

	public static Frame frameOf(Grouping grouping, Catalog catalog, String box, String modelClass) {
		return new Frame("arrangement", grouping.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("name", (String) grouping.name$())
				.addSlot("label", ((String) grouping.label()))
				.addSlot("catalog", catalog.name$())
				.addSlot("type", modelClass)
				.addSlot("histogram", grouping.histogram());
	}

	private Frame frameOf(CatalogView view, Catalog catalog) {
		final Frame frame = new Frame("view", view.getClass().getSimpleName())
				.addSlot("label", view.label())
				.addSlot("catalog", catalog.name$())
				.addSlot("package", this.packageName)
				.addSlot("name", view.name$())
				.addSlot("mold", view.mold().name$())
				.addSlot("width", view.width());
		if (view.noRecordMessage() != null) frame.addSlot("noRecordMessage", view.noRecordMessage());
		if (view.i$(MapView.class)) {
			MapView mapView = view.a$(MapView.class);
			if (mapView.center() != null)
				frame.addSlot("latitude", mapView.center().latitude()).addSlot("longitude", mapView.center().longitude());
			frame.addSlot("zoom", new Frame("zoom")
					.addSlot("default", mapView.zoom().defaultZoom())
					.addSlot("min", mapView.zoom().min())
					.addSlot("max", mapView.zoom().max()));
		}
		return frame;
	}

	public static Frame frameOf(DisplayView view, Catalog catalog, String box, String packageName) {
		return new Frame("view", view.getClass().getSimpleName())
				.addSlot("box", box)
				.addSlot("catalog", catalog.name$())
				.addSlot("name", view.name$())
				.addSlot("label", view.label())
				.addSlot("package", packageName)
				.addSlot("hideNavigator", view.hideNavigator())
				.addSlot("display", view.display());
	}

	private Frame frameOf(Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		final Catalog owner = toolbar.core$().ownerAs(Catalog.class);
		frame.addSlot("box", box).addSlot("type", this.modelClass).addSlot("canSearch", toolbar.canSearch());
		for (Operation operation : toolbar.operations()) {
			if (operation.i$(Download.class)) frame.addSlot("operation", frameOf(toolbar.download(), owner, box, modelClass, packageName));
			if (operation.i$(Export.class)) frame.addSlot("operation", frameOf(toolbar.export(), owner, box, modelClass, packageName));
			if (operation.i$(AbstractToolbar.OpenDialog.class))
				frame.addSlot("operation", frameOf(toolbar.openDialog(), owner, box, modelClass, packageName));
			if (operation.i$(AbstractToolbar.Task.class))
				frame.addSlot("operation", frameOf(toolbar.task(), owner, box, modelClass, packageName));
			if (operation.i$(TaskSelection.class))
				frame.addSlot("operation", frameOf(toolbar.taskSelection(), owner, box, modelClass, packageName));
			if (operation.i$(ExportSelection.class))
				frame.addSlot("operation", frameOf(toolbar.exportSelection(), owner, box, modelClass, packageName));
			if (operation.i$(DownloadSelection.class))
				frame.addSlot("operation", frameOf(toolbar.downloadSelection(), owner, box, modelClass, packageName));
			if (operation.i$(OpenCatalog.class))
				frame.addSlot("operation", frameOf(toolbar.openCatalog(), owner, box, modelClass, packageName));
			if (operation.i$(OpenCatalogSelection.class))
				frame.addSlot("operation", frameOf(toolbar.openCatalogSelection(), owner, box, modelClass, packageName));
		}
		return frame;
	}

	public static Frame frameOf(Operation operation, Catalog catalog, String box, String modelClass, String packageName) {
		Frame frame = new Frame("operation", operation.getClass().getSimpleName().toLowerCase())
				.addSlot("name", operation.name$())
				.addSlot("box", box)
				.addSlot("type", modelClass)
				.addSlot("title", operation.title())
				.addSlot("catalog", catalog.name$())
				.addSlot("mode", operation.mode().toString())
				.addSlot("package", packageName);
		if (operation.polymerIcon() != null) frame.addSlot("icon", operation.polymerIcon());
		if (operation.i$(OpenDialog.class)) frame.addSlot("dialog", operation.a$(OpenDialog.class).dialog().name$());
		if (operation.i$(Toolbar.OpenCatalog.class)) {
			Toolbar.OpenCatalog openCatalog = operation.a$(Toolbar.OpenCatalog.class);
			frame.addSlot("width", openCatalog.width());
			frame.addSlot("height", openCatalog.height());
			frame.addSlot("position", openCatalog.position().toString());
			frame.addSlot("openCatalog", openCatalog.catalog().name$());
			frame.addSlot("view", openCatalog.views().stream().map(Layer::name$).toArray(String[]::new));
			if (openCatalog.filtered()) frame.addSlot("openCatalogOperationFilter", new Frame().addSlot("catalog", catalog.name$()).addSlot("box", box));
		}
		if (operation.i$(OpenCatalogSelection.class)) {
			OpenCatalogSelection openCatalogSelection = operation.a$(OpenCatalogSelection.class);
			frame.addSlot("width", openCatalogSelection.width());
			frame.addSlot("height", openCatalogSelection.height());
			frame.addSlot("position", openCatalogSelection.position().toString());
			frame.addSlot("openCatalog", openCatalogSelection.catalog().name$());
			frame.addSlot("view", openCatalogSelection.views().stream().map(Layer::name$).toArray(String[]::new));
			frame.addSlot("selection", openCatalogSelection.selection().toString());
			if (openCatalogSelection.filtered()) frame.addSlot("openCatalogSelectionOperationFilter", new Frame().addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", modelClass));
			frame.addSlot("openCatalogSelectionOperation", new Frame().addSlot("catalog", catalog.name$()).addSlot("box", box).addSlot("type", modelClass));
		}
		if ((operation.i$(Toolbar.Task.class) && operation.a$(Toolbar.Task.class).showMessageToUser()) ||
			(operation.i$(Toolbar.TaskSelection.class) && operation.a$(Toolbar.TaskSelection.class).showMessageToUser()))
			frame.addSlot("operationMessageLoader", new Frame().addSlot("catalog", catalog.name$()).addSlot("operationType", operation.getClass().getSimpleName()));
		return frame;
	}

	protected Template template() {
		return customize(AbstractCatalogTemplate.create());
	}

	protected Template srcTemplate() {
		return customize(CatalogTemplate.create());
	}

	void writeSrc(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		File sourceFile = javaFile(new File(src, DISPLAYS), newDisplay);
		if (!sourceFile.exists()) writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(frame));
		else new CatalogUpdater(sourceFile, display.a$(Catalog.class), project, packageName, box).update();
	}
}
