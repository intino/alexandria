package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.*;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
		List<Frame> frames = new ArrayList<>();
		for (ElementOption element : elementOptions) {
			if (element.i$(Group.class)) frames.add(frameOf(element.a$(Group.class)));
			else if (element.i$(Options.class)) frames.add(frameOf(element.a$(Options.class)));
			else frames.add(frameOf(element.a$(Option.class)));
		}
		return frames.toArray(new Frame[0]);
	}


	private Frame frameOf(Options options) {
		Frame frame = new Frame("elementOption", "options")
				.addSlot("layout", this.display.a$(Layout.class).name$())
				.addSlot("name", options.name$());
		render(options.elementRenderer(), frame);
		return frame;
	}

	private Frame frameOf(Group group) {
		final Frame frame = new Frame("elementOption", "group")
				.addSlot("label", group.label())
				.addSlot("mode", group.mode());
		if (!group.optionList().isEmpty()) frame.addSlot("option", group.optionList().stream().map(this::frameOf).toArray(Frame[]::new));
		return frame;
	}

	private Frame frameOf(Option option) {
		final Frame frame = new Frame("option", "elementOption");
		final ElementRenderer renderer = option.elementRenderer();
		frame.addTypes(renderer instanceof RenderCatalogs ? "catalog" : "panel").addSlot("label", option.label());
		render(renderer, frame);
		return frame;
	}

	private void render(ElementRenderer renderer, Frame frame) {
		if (renderer instanceof RenderCatalogs) frame.addSlot("render", renderCatalogs(renderer.a$(RenderCatalogs.class));
		else if (renderer instanceof RenderPanels) frame.addSlot("render", renderPanels(renderer.a$(RenderPanels.class));
		else if (renderer instanceof RenderObjects) frame.addSlot("render", renderObjects(renderer.a$(RenderObjects.class));
	}

	private Frame renderCatalogs(RenderCatalogs render) {
		final Frame renderFrame = new Frame("render", "catalogs").addSlot("catalog", render.catalogs().stream().map(Layer::name$).toArray(String[]::new));
		if (render.filtered()) renderFrame.addSlot("layout", this.display.a$(Layout.class).name$()).addSlot("path", "a-resolver");
		return renderFrame;
	}

	private Frame renderPanels(RenderPanels render) {
		final Frame renderFrame = new Frame("render", "panels").addSlot("panel", render.panels().stream().map(Layer::name$).toArray(String[]::new));
		if (render.withObject().equals(withObject))
			renderFrame.addSlot("layout", this.display.a$(Layout.class).name$()).addSlot("path", "a-resolver");
		return renderFrame;
	}

	private Frame renderObjects(RenderObjects render) {
		return new Frame("render", "objects").addSlot("render", new Frame().addSlot("name", render.name$()));
	}

	protected Template template() {
		return customize(LayoutTemplate.create());
	}
}
