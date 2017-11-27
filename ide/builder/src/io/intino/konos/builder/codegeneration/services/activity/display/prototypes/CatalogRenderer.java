package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Catalog;
import io.intino.konos.model.graph.Catalog.Arrangement.Grouping;
import io.intino.konos.model.graph.Catalog.Arrangement.Sorting;
import io.intino.konos.model.graph.CatalogView;
import io.intino.konos.model.graph.DisplayView;
import io.intino.konos.model.graph.Operation;
import io.intino.tara.magritte.Node;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class CatalogRenderer extends PrototypeRenderer {

	private final Project project;

	public CatalogRenderer(Project project, Catalog catalog, File src, File gen, String packageName, String box) {
		super(catalog, box, packageName, src, gen);
		this.project = project;
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Catalog catalog = this.display.a$(Catalog.class);
		final Frame frame = super.createFrame().addSlot("label", catalog.label()).addSlot("type", catalog.modelClass());
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
		for (CatalogView view : catalog.views().catalogViewList()) frame.addSlot("view", frameOf(view, catalog));
		if (catalog.views().displayView() != null) frame.addSlot("view", frameOf(catalog.views().displayView(), catalog));
	}

	private void arrangements(Catalog catalog, Frame frame) {
		for (Grouping grouping : catalog.arrangement().groupingList())
			frame.addSlot("arrangement", frameOf(grouping, catalog));
		for (Sorting sorting : catalog.arrangement().sortingList())
			frame.addSlot("arrangement", frameOf(sorting, catalog));
	}

	private Object frameOf(Sorting sorting, Catalog catalog) {
		return new Frame("arrangement", sorting.getClass().getSimpleName())
				.addSlot("name", sorting.name$())
				.addSlot("catalog", catalog.name$())
				.addSlot("type", catalog.modelClass());
	}

	private Frame frameOf(Grouping grouping, Catalog catalog) {
		return new Frame("arrangement", grouping.getClass().getSimpleName())
				.addSlot("box", box)
				.addSlot("name", grouping.name$())
				.addSlot("catalog", catalog.name$())
				.addSlot("type", catalog.modelClass())
				.addSlot("histogram", grouping.histogram());
	}

	private Frame frameOf(CatalogView view, Catalog catalog) {
		return new Frame("view", view.getClass().getSimpleName())
				.addSlot("catalog", catalog.name$())
				.addSlot("name", view.name$());
	}

	private Frame frameOf(DisplayView view, Catalog catalog) {
		return new Frame("view", view.getClass().getSimpleName())
				.addSlot("box", box)
				.addSlot("catalog", catalog.name$())
				.addSlot("name", view.name$())
				.addSlot("display", view.display());
	}

	private Frame frameOf(Catalog.Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		final Node owner = toolbar.core$().owner();
		frame.addSlot("box", box).addSlot("canSearch", toolbar.canSearch());
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
		Frame frame = new Frame("operation", operation.getClass().getSimpleName())
				.addSlot("name", operation.name$())
				.addSlot("title", operation.title())
				.addSlot("catalog", catalog.name());
		if (operation.alexandriaIcon() != null) frame.addSlot("icon", operation.alexandriaIcon());
		return frame;
	}

	protected Template template() {
		return customize(CatalogTemplate.create());
	}
}
