package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Catalog;
import io.intino.konos.model.graph.CatalogView;
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
		final Frame frame = super.createFrame();
		frame.addSlot("label", catalog.label());
		if (catalog.toolbar() != null) frame.addSlot("toolbar", frameOf(catalog.toolbar()));
		for (CatalogView view : catalog.views().catalogViewList()) frame.addSlot("view", frameOf(view, catalog));
		return frame;
	}

	private Frame frameOf(CatalogView view, Catalog panel) {
		final Frame frame = new Frame("view")
				.addSlot("owner", panel.name$())
				.addSlot("name", view.name$());
		if (view.label() != null) frame.addSlot("label", view.label());
		return frame;
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
		if (operation.alexandriaIcon() != null) frame.addSlot("icon", operation.alexandriaIcon().getPath());
		return frame;
	}


	@Override
	protected Template template() {
		return customize(CatalogTemplate.create());
	}
}
