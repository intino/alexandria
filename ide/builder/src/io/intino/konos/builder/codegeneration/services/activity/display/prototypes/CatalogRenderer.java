package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.AbstractToolbar.*;
import io.intino.konos.model.graph.Catalog.Arrangement.Grouping;
import io.intino.konos.model.graph.Catalog.Arrangement.Sorting;
import io.intino.konos.model.graph.Catalog.Events.OnClickRecord.CatalogEvent;
import io.intino.tara.magritte.Node;
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
		final Frame frame = super.createFrame().addSlot("label", catalog.label()).addSlot("type", modelClass);
		events(catalog, frame);
		toolbar(catalog, frame);
		views(catalog, frame);
		arrangements(catalog, frame);
		return frame;
	}

	private void events(Catalog catalog, Frame frame) {
		if (catalog.events() != null) frame.addSlot("event", frameOf(catalog.events().onClickRecord()));
	}

	private Frame frameOf(Catalog.Events.OnClickRecord onClickRecord) {
		final CatalogEvent catalogEvent = onClickRecord.catalogEvent();
		if (catalogEvent.i$(Catalog.Events.OnClickRecord.OpenDialog.class))
			return frameFor(catalogEvent.a$(Catalog.Events.OnClickRecord.OpenDialog.class)).addSlot("box", box);
		return frameFor(catalogEvent.a$(Catalog.Events.OnClickRecord.OpenPanel.class));
	}

	private Frame frameFor(Catalog.Events.OnClickRecord.OpenDialog openDialog) {
		final Frame frame = new Frame("event", openDialog.getClass().getSimpleName());
		if (openDialog.height() >= 0) frame.addSlot("height", openDialog.height());
		if (openDialog.width() >= 0) frame.addSlot("width", openDialog.width());
		frame.addSlot("catalog", display.name$());
		return frame;
	}

	private Frame frameFor(Catalog.Events.OnClickRecord.OpenPanel openPanel) {
		final Frame frame = new Frame("event", openPanel.getClass().getSimpleName());
		frame.addSlot("panel", openPanel.panel().name$());
		if (openPanel.hasBreadcrumbs())
			frame.addSlot("breadcrumbs", new Frame("breadCrumbs").addSlot("catalog", display.name$()).addSlot("box", box).addSlot("type", modelClass));
		return frame;
	}

	private void toolbar(Catalog catalog, Frame frame) {
		if (catalog.toolbar() != null) {
			frame.addSlot("toolbar", frameOf(catalog.toolbar()));
			if (catalog.toolbar().groupingSelection() != null) frame.addSlot("groupingselection", "");
		}
	}

	private void views(Catalog catalog, Frame frame) {
		for (CatalogView view : catalog.views().catalogViewList())
			frame.addSlot("view", frameOf(view, catalog));
		if (catalog.views().displayView() != null) frame.addSlot("view", frameOf(catalog.views().displayView(), catalog));
	}

	private void arrangements(Catalog catalog, Frame frame) {
		if (catalog.arrangement() == null) return;
		for (Grouping grouping : catalog.arrangement().groupingList())
			frame.addSlot("arrangement", frameOf(grouping, catalog));
		for (Sorting sorting : catalog.arrangement().sortingList())
			frame.addSlot("arrangement", frameOf(sorting, catalog));
	}

	private Frame frameOf(Sorting sorting, Catalog catalog) {
		return new Frame("arrangement", sorting.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("name", sorting.name$())
				.addSlot("label", sorting.label())
				.addSlot("catalog", catalog.name$())
				.addSlot("type", this.modelClass);
	}

	private Frame frameOf(Grouping grouping, Catalog catalog) {
		return new Frame("arrangement", grouping.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("name", (String) grouping.name$())
				.addSlot("label", ((String) grouping.label()))
				.addSlot("catalog", catalog.name$())
				.addSlot("type", this.modelClass)
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

	private Frame frameOf(DisplayView view, Catalog catalog) {
		return new Frame("view", view.getClass().getSimpleName())
				.addSlot("box", box)
				.addSlot("catalog", catalog.name$())
				.addSlot("name", view.name$())
				.addSlot("package", this.packageName)
				.addSlot("hideNavigator", view.hideNavigator())
				.addSlot("display", view.display());
	}

	private Frame frameOf(Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		final Node owner = toolbar.core$().owner();
		frame.addSlot("box", box).addSlot("type", this.modelClass).addSlot("canSearch", toolbar.canSearch());
		for (Operation operation : toolbar.operations()) {
			if (operation.i$(Download.class)) frame.addSlot("operation", frameOf(toolbar.download(), owner));
			if (operation.i$(Export.class)) frame.addSlot("operation", frameOf(toolbar.export(), owner));
			if (operation.i$(AbstractToolbar.OpenDialog.class)) frame.addSlot("operation", frameOf(toolbar.openDialog(), owner));
			if (operation.i$(AbstractToolbar.Task.class)) frame.addSlot("operation", frameOf(toolbar.task(), owner));
			if (operation.i$(TaskSelection.class)) frame.addSlot("operation", frameOf(toolbar.taskSelection(), owner));
			if (operation.i$(ExportSelection.class)) frame.addSlot("operation", frameOf(toolbar.exportSelection(), owner));
			if (operation.i$(DownloadSelection.class)) frame.addSlot("operation", frameOf(toolbar.downloadSelection(), owner));
			if (operation.i$(GroupingSelection.class)) frame.addSlot("operation", frameOf(toolbar.groupingSelection(), owner));
		}
		return frame;
	}

	private Frame frameOf(Operation operation, Node catalog) {
		Frame frame = new Frame("operation", operation.getClass().getSimpleName().toLowerCase())
				.addSlot("name", operation.name$())
				.addSlot("box", box)
				.addSlot("type", modelClass)
				.addSlot("title", operation.title())
				.addSlot("catalog", catalog.name());
		if (operation.alexandriaIcon() != null) frame.addSlot("icon", operation.alexandriaIcon());
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
		if (!javaFile(new File(src, DISPLAYS), newDisplay).exists())
			writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(frame));
	}
}
