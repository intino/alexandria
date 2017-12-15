package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Catalog.Arrangement.Grouping;
import io.intino.konos.model.graph.Catalog.Arrangement.Sorting;
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
		final Frame frame = super.createFrame().addSlot("label", catalog.label()).addSlot("type", this.modelClass);
		toolbar(catalog, frame);
		views(catalog, frame);
		arrangements(catalog, frame);
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
				.addSlot("name", view.name$());
		if (view.i$(MapView.class)) {
			MapView mapView = view.a$(MapView.class);
			if (mapView.center() != null)
				frame.addSlot("latitude", mapView.center().latitude()).addSlot("longitude", mapView.center().longitude());
			frame.addSlot("zoom", new Frame("zoom")
					.addSlot("default", mapView.zoom().defaultZoom())
					.addSlot("min", mapView.zoom().min()))
					.addSlot("max", mapView.zoom().max());
		}
		return frame;
	}

	private Frame frameOf(DisplayView view, Catalog catalog) {
		return new Frame("view", view.getClass().getSimpleName())
				.addSlot("box", box)
				.addSlot("catalog", catalog.name$())
				.addSlot("name", view.name$())
				.addSlot("package", this.packageName)
				.addSlot("display", view.display());
	}

	private Frame frameOf(Catalog.Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		final Node owner = toolbar.core$().owner();
		frame.addSlot("box", box).addSlot("type", this.modelClass).addSlot("canSearch", toolbar.canSearch());
		if (toolbar.download() != null) frame.addSlot("operation", frameOf(toolbar.download(), owner));
		if (toolbar.export() != null) frame.addSlot("operation", frameOf(toolbar.export(), owner));
		if (toolbar.openDialog() != null) frame.addSlot("operation", frameOf(toolbar.openDialog(), owner));
		if (toolbar.task() != null) frame.addSlot("operation", frameOf(toolbar.task(), owner));
		if (toolbar.taskSelection() != null) frame.addSlot("operation", taskSelectionFrameOf(toolbar.taskSelection(), owner));
		if (toolbar.exportSelection() != null) frame.addSlot("operation", frameOf(toolbar.exportSelection(), owner));
		if (toolbar.downloadSelection() != null) frame.addSlot("operation", frameOf(toolbar.downloadSelection(), owner));
		if (toolbar.groupingSelection() != null) frame.addSlot("operation", frameOf(toolbar.groupingSelection(), owner));
		return frame;
	}

	private Frame taskSelectionFrameOf(Catalog.Toolbar.TaskSelection task, Node owner) {
		return frameOf(task, owner).addSlot("refresh", task.refresh().name());
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
