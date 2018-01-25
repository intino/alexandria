package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters.LayoutUpdater;
import io.intino.konos.model.graph.*;
import io.intino.tara.magritte.Layer;
import io.intino.tara.magritte.Node;
import org.siani.itrules.Template;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.RenderPanels.WithObject.withObject;


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
		frame.addSlot("elementOption", frameOf(layout.elementOptionList()));
		return frame;
	}

	private Frame[] frameOf(List<ElementOption> elementOptions) {
		return elementOptions.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	public Frame frameOf(ElementOption element) {
		if (element.i$(Group.class)) return frameOf(element.a$(Group.class));
		else if (element.i$(Options.class)) return frameOf(element.a$(Options.class));
		else return frameOf(element.a$(Option.class));
	}

	private Frame frameOf(Options options) {
		Frame frame = new Frame("elementOption", "options").addSlot("box", box)
				.addSlot("layout", this.display.a$(Layout.class).name$())
				.addSlot("name", options.name$())
				.addSlot("path", pathOf(options.core$()))
				.addSlot("modelClass", options.modelClass());
		render(options.elementRenderer(), frame);
		return frame;
	}

	private Frame frameOf(Group group) {
		final Frame frame = new Frame("elementOption", "group").addSlot("box", box)
				.addSlot("label", group.label())
				.addSlot("name", group.name$())
				.addSlot("mode", group.mode());
		if (!group.optionList().isEmpty())
			frame.addSlot("elementOption", group.optionList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!group.optionsList().isEmpty())
			frame.addSlot("elementOption", group.optionsList().stream().map(this::frameOf).toArray(Frame[]::new));
		return frame;
	}

	private Frame frameOf(Option option) {
		final Frame frame = new Frame("option", "elementOption").addSlot("name", option.name$()).addSlot("box", box);
		final ElementRenderer renderer = option.elementRenderer();
		frame.addTypes(renderer instanceof RenderCatalogs ? "catalogs" : "panels").addSlot("label", option.label());
		render(renderer, frame);
		return frame;
	}

	private void render(ElementRenderer renderer, Frame frame) {
		if (renderer instanceof RenderCatalogs) frame.addSlot("render", renderCatalogs(renderer.a$(RenderCatalogs.class)));
		else if (renderer instanceof RenderPanels) frame.addSlot("render", renderPanels(renderer.a$(RenderPanels.class)));
		else if (renderer instanceof RenderObjects) frame.addSlot("render", renderObjects(renderer.a$(RenderObjects.class)));
	}

	private Frame renderCatalogs(RenderCatalogs render) {
		final Frame renderFrame = new Frame("render", "catalogs")
				.addSlot("box", box)
				.addSlot("catalog", render.catalogs().stream().map(Layer::name$).toArray(String[]::new));
		if (render.filtered()) {
			renderFrame.addSlot("filtered", "");
			renderFrame.addSlot("layout", this.display.a$(Layout.class).name$()).addSlot("path", pathOf(render.core$().owner()));
		}
		return renderFrame;
	}

	private Frame renderPanels(RenderPanels render) {
		final Frame renderFrame = new Frame("render", "panels").addSlot("box", box)
				.addSlot("panel", render.panels().stream().map(Layer::name$).toArray(String[]::new));
		if (render.withObject().equals(withObject))
			renderFrame.addSlot("layout", this.display.a$(Layout.class).name$()).addSlot("path", pathOf(render.core$().owner()));
		return renderFrame;
	}

	private Frame renderObjects(RenderObjects render) {
		Frame frame = new Frame("render", "objects").addSlot("box", box).addSlot("panel", render.panel().name$());
		frame.addSlot("layout", this.display.a$(Layout.class).name$()).addSlot("path", pathOf(render.core$().owner()));
		return frame;
	}

	private String pathOf(Node node) {
		String qn = "";
		Node parent = node;
		while (!parent.equals(display.core$())) {
			qn = StringFormatter.firstUpperCase().format(parent.name()).toString() + (qn.isEmpty() ? "" : ".") + qn;
			parent = parent.owner();
		}
		return qn;
	}

	protected Template template() {
		return customize(AbstractLayoutTemplate.create());
	}

	protected Template srcTemplate() {
		return customize(LayoutTemplate.create());
	}

	void writeSrc(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		File sourceFile = javaFile(new File(src, DISPLAYS), newDisplay);
		if (!sourceFile.exists()) writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(frame));
		else new LayoutUpdater(sourceFile, display.a$(Layout.class), project, packageName, box).update();
	}
}
