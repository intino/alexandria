package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.*;
import io.intino.tara.magritte.Layer;
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
		for (Panel.Views.View view : panel.views().viewList()) frame.addSlot("view", frameOf(view, panel));
		return frame;
	}

	private Frame frameOf(Panel.Views.View view, Panel panel) {
		final Frame frame = new Frame("view")
				.addSlot("owner", panel.name$())
				.addSlot("name", view.name$())
				.addSlot("label", view.label())
				.addSlot("box", box);
		final ElementRenderer renderer = view.elementRenderer();
		if (renderer.i$(RenderDisplay.class)) {
			frame.addTypes("display");
			frame.addSlot("display", renderer.a$(RenderDisplay.class).display());
		}
		if (renderer.i$(RenderMold.class)) {
			frame.addTypes("mold");
			final Mold mold = renderer.a$(RenderMold.class).mold();
			frame.addSlot("mold", mold.name$());
		}
		if (renderer.i$(RenderCatalogs.class)) {
			frame.addTypes("catalogs");
			RenderCatalogs renderCatalogs = renderer.a$(RenderCatalogs.class);
			frame.addSlot("catalog", renderCatalogs.catalogs().stream().map(Layer::name$).toArray(String[]::new));
			if (renderCatalogs.filtered())
				frame.addSlot("filter", new Frame().addSlot("panel", panel.name$()).addSlot("name", view.name$()));
		}
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
		if (operation.polymerIcon() != null) frame.addSlot("icon", operation.polymerIcon());
		return frame;
	}

	@Override
	protected Template template() {
		return customize(PanelTemplate.create());
	}
}
