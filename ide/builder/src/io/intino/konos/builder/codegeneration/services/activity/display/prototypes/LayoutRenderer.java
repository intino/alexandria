package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Layout;
import io.intino.konos.model.graph.Option;
import io.intino.konos.model.graph.RenderCatalog;
import io.intino.konos.model.graph.RenderPanel;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

public class LayoutRenderer extends PrototypeRenderer {
	private final Project project;

	public LayoutRenderer(Project project, Layout layout, File src, File gen, String packageName, String boxName) {
		super(layout, boxName, packageName, src, gen);
		this.project = project;
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Layout layout = this.display.a$(Layout.class);
		final Frame frame = super.createFrame();
		frame.addSlot("mode", layout.mode().name());
		frame.addSlot("group", framesOf(layout.groupList()));
		return frame;
	}

	private Frame[] framesOf(List<Layout.Group> groups) {
		return groups.stream().map(this::framesOf).toArray(Frame[]::new);
	}

	private Frame framesOf(Layout.Group group) {
		final Frame frame = new Frame("group")
				.addSlot("label", group.label())
				.addSlot("mode", group.mode());
		if (!group.optionList().isEmpty()) frame.addSlot("option", optionFramesOf(group.optionList()));
		return frame;
	}

	private Frame[] optionFramesOf(List<Option> options) {
		return options.stream().map(this::framesOf).toArray(Frame[]::new);
	}

	private Frame framesOf(Option option) {
		final Frame frame = new Frame("option", option.elementRenderer() instanceof RenderCatalog ? "catalog" : "panel").addSlot("label", option.label());
		if (option.elementRenderer() instanceof RenderCatalog)
			frame.addSlot("catalog", option.elementRenderer().a$(RenderCatalog.class).catalog().name$());
		else frame.addSlot("panel", option.elementRenderer().a$(RenderPanel.class).panel().name$());
		return frame;
	}

	protected Template template() {
		return customize(LayoutTemplate.create());
	}
}
