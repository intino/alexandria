package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.display.catalog.FullCatalogTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewRenderer;
import io.intino.konos.model.graph.Panel;
import io.intino.konos.model.graph.Toolbar;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class PanelRenderer extends DisplayRenderer {
	private final Project project;

	public PanelRenderer(Project project, Panel panel, String packageName, String boxName) {
		super(panel, boxName, packageName);
		this.project = project;
	}

	@Override
	protected Frame createFrame() {
		final Panel panel = display().a$(Panel.class);
		final Frame frame = super.createFrame();
		frame.addTypes(panel.isDesktop() ? "desktop" : "panel");
		if (panel.label() != null) frame.addSlot("label", panel.label());
		if (panel.toolbar() != null) frame.addSlot("toolbar", frameOf(panel.toolbar()));
		views(panel.views(), frame);
		return frame;
	}

	private Frame frameOf(Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		frame.addSlot("box", box).addSlot("canSearch", toolbar.canSearch());
		toolbar.operations().forEach(operation -> {
			OperationRenderer builder = new OperationRenderer(operation, display(), box, packageName);
			frame.addSlot("operation", builder.buildFrame());
		});
		return frame;
	}

	private void views(Panel.Views views, Frame frame) {
		views.viewList().forEach(view -> {
			ViewRenderer builder = new ViewRenderer(view, display(), box, packageName);
			frame.addSlot("view", builder.buildFrame());
		});
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(FullPanelTemplate.create());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(AbstractPanelTemplate.create());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new PanelUpdater(sourceFile, display().a$(Panel.class), project, packageName, box);
	}
}
