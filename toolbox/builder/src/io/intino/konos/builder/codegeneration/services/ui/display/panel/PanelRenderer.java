package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewRenderer;
import io.intino.konos.model.graph.Panel;
import io.intino.konos.model.graph.Toolbar;

import java.io.File;

public class PanelRenderer extends DisplayRenderer {
	private final Project project;

	public PanelRenderer(Project project, Panel panel, String packageName, String boxName) {
		super(panel, boxName, packageName);
		this.project = project;
	}

	@Override
	public FrameBuilder frameBuilder() {
		final Panel panel = display().a$(Panel.class);
		final FrameBuilder builder = super.frameBuilder();
		builder.add(panel.isDesktop() ? "desktop" : "panel");
		if (panel.label() != null) builder.add("label", panel.label());
		if (panel.toolbar() != null) builder.add("toolbar", frameOf(panel.toolbar()));
		views(panel.views(), builder);
		return builder;
	}

	private FrameBuilder frameOf(Toolbar toolbar) {
		return new FrameBuilder("toolbar")
				.add("box", box).add("canSearch", toolbar.canSearch())
				.add("operation", toolbar.operations().stream().map(o -> new OperationRenderer(o, display(), box, packageName).frameBuilder().toFrame()).toArray(Frame[]::new));
	}

	private void views(Panel.Views views, FrameBuilder frame) {
		views.viewList().forEach(view -> {
			frame.add("view", new FrameBuilder().add("value", new ViewRenderer(view, display(), box, packageName).frameBuilder()));
		});
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(new PanelTemplate());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(new AbstractPanelTemplate());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new PanelUpdater(sourceFile, display().a$(Panel.class), project, packageName, box);
	}
}
