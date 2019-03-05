package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.templates.DisplayTemplate;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRendererFactory;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public abstract class BaseDisplayRenderer<D extends Display> extends PassiveViewRenderer<D> {
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	protected BaseDisplayRenderer(Settings settings, D display, TemplateProvider templateProvider, Target target) {
		super(settings, display, templateProvider, target);
	}

	public void execute() {
		if (element == null) return;
		String path = path(element);
		final String newDisplay = snakeCaseToCamelCase(element.name$());
		classes().put("Display#" + element.name$(), path + "." + newDisplay);
		Frame frame = buildFrame();
		createPassiveViewFiles(frame);
		write(frame, src(), gen(), path(element));
		if (element.isAccessible()) writeDisplaysFor(element.asAccessible(), frame);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("display").addTypes(typeOf(element));
		addRenderTagFrames(frame);
		addDecoratedFrames(frame);
		frame.addSlot("componentType", element.components().stream().map(this::typeOf).distinct().map(type -> new Frame().addSlot("componentType", type)).toArray(Frame[]::new));
		if (element.parentDisplay() != null) addParent(element, frame);
		if (!element.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName()));
		if (element.isAccessible())
			frame.addSlot("parameter", element.asAccessible().parameters().stream().map(p -> new Frame("parameter", "accessible").addSlot("value", p)).toArray(Frame[]::new));
		return frame;
	}

	protected void addRenderTagFrames(Frame frame) {
		Frame renderTag = new Frame("renderTag");
		if (element.i$(Block.class)) {
			ComponentRenderer renderer = factory.renderer(settings, element.a$(Block.class), templateProvider, target);
			renderTag.addTypes("block");
			renderTag.addSlot("properties", renderer.properties());
		}
		frame.addSlot("renderTag", renderTag);
	}

	protected void addDecoratedFrames(Frame frame) {
		addDecoratedFrames(frame, element.isDecorated());
	}

	protected void addDecoratedFrames(Frame frame, boolean decorated) {
		if (decorated) frame.addSlot("abstract", "Abstract");
		else frame.addSlot("notDecorated", element.name$());
		Frame abstractBoxFrame = new Frame().addTypes("box");
		if (decorated) abstractBoxFrame.addTypes("decorated");
		abstractBoxFrame.addSlot("box", boxName());
		frame.addSlot("abstractBox", abstractBoxFrame);
	}

	protected Frame componentFrame(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		renderer.buildChildren(true);
		renderer.decorated(element.isDecorated());
		return renderer.buildFrame();
	}

	protected void addComponent(Component component, Frame frame) {
		frame.addSlot("component", componentFrame(component));
	}

	private void writeDisplaysFor(AccessibleDisplay display, Frame frame) {
		frame.addTypes("accessible");
		final String name = snakeCaseToCamelCase(display.name$());
		writeFrame(new File(src(), path(display.a$(Display.class))), name + "Proxy", setup(DisplayTemplate.create()).format(frame.addTypes("accessible")));
		writeNotifier(display.a$(PassiveView.class), frame);
		writeRequester(display.a$(PassiveView.class), frame);
	}

	private void addParent(Display display, Frame frame) {
		String parent = parent();
		final Frame parentFrame = new Frame().addSlot("value", display.parentDisplay()).addSlot("dsl", parent).addSlot("package", parent.substring(0, parent.lastIndexOf(".")));
		frame.addSlot("parent", parentFrame);
	}

}