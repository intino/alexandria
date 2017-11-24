package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.DisplayView;
import io.intino.konos.model.graph.Operation;
import io.intino.konos.model.graph.Panel;
import io.intino.tara.magritte.Node;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class PanelRenderer extends PrototypeRenderer {
	private final Project project;

	public PanelRenderer(Project project, Panel panel, File src, File gen, String packageName, String boxName) {
		super(panel, boxName, packageName, src, gen);
		this.project = project;
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Panel panel = this.display.a$(Panel.class);
		final Frame frame = super.createFrame();
		frame.addSlot("label", panel.label());
		if (panel.toolbar() != null) frame.addSlot("toolbar", frameOf(panel.toolbar()));
		for (DisplayView view : panel.views().displayViewList()) frame.addSlot("view", frameOf(view, panel));
		return frame;
	}

	private Frame frameOf(DisplayView view, Panel panel) {
		final Frame frame = new Frame("view")
				.addSlot("owner", panel.name$())
				.addSlot("name", view.name$());
		if (view.label() != null) frame.addSlot("label", view.label());
		return frame;
	}

	private Frame frameOf(Panel.Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		frame.addSlot("box", box).addSlot("canSearch", toolbar.canSearch());
		if (toolbar.download() != null) frame.addSlot("operation", frameOf(toolbar.download(), toolbar.core$().owner()));
		if (toolbar.export() != null) frame.addSlot("operation", frameOf(toolbar.export(), toolbar.core$().owner()));
		if (toolbar.openDialog() != null) frame.addSlot("operation", frameOf(toolbar.openDialog(), toolbar.core$().owner()));
		if (toolbar.task() != null) frame.addSlot("operation", frameOf(toolbar.task(), toolbar.core$().owner()));
		return frame;
	}

	private Frame frameOf(Operation operation, Node panel) {
		Frame frame = new Frame("operation", operation.getClass().getSimpleName())
				.addSlot("name", operation.name$())
				.addSlot("title", operation.title())
				.addSlot("panel", panel.name());
		if (operation.alexandriaIcon() != null) frame.addSlot("icon", operation.alexandriaIcon().getPath());
		return frame;
	}

	@Override
	protected Template template() {
		return customize(PanelTemplate.create());
	}
}
