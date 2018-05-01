package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationFrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewFrameBuilder;
import io.intino.konos.model.graph.Panel;
import io.intino.konos.model.graph.Toolbar;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class PanelRenderer extends Renderer {
	private final Project project;

	public PanelRenderer(Project project, Panel panel, File src, File gen, String packageName, String boxName) {
		super(panel, boxName, packageName, src, gen);
		this.project = project;
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeGen(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Panel panel = this.display.a$(Panel.class);
		final Frame frame = super.createFrame();
		if (panel.label() != null) frame.addSlot("label", panel.label());
		if (panel.toolbar() != null) frame.addSlot("toolbar", frameOf(panel.toolbar()));
		frame.addSlot("views", frameOf(panel.views()));
		return frame;
	}

	private Frame frameOf(Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		frame.addSlot("box", box).addSlot("canSearch", toolbar.canSearch());
		toolbar.operations().forEach(operation -> {
			OperationFrameBuilder builder = new OperationFrameBuilder(operation, display, box, packageName);
			frame.addSlot("operation", buildingSrc() ? builder.buildSrc() : builder.buildGen());
		});
		return frame;
	}

	private Frame frameOf(Panel.Views views) {
		final Frame frame = new Frame("views");
		views.containerViewList().forEach(view -> {
			ViewFrameBuilder builder = new ViewFrameBuilder(view, display, box, packageName);
			frame.addSlot("view", buildingSrc() ? builder.buildSrc() : builder.buildGen());
		});
		return frame;
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(PanelSrcTemplate.create());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(PanelGenTemplate.create());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new PanelUpdater(sourceFile, display.a$(Panel.class), project, packageName, box);
	}
}
