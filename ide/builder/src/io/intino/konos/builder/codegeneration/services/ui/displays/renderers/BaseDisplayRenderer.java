package io.intino.konos.builder.codegeneration.services.ui.displays.renderers;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.passiveview.PassiveViewRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public abstract class BaseDisplayRenderer<D extends Display> extends PassiveViewRenderer<D> {

	protected BaseDisplayRenderer(Settings settings, D display) {
		super(settings, display);
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
		if (element.isDecorated()) frame.addSlot("abstract", "Abstract");
		frame.addSlot("component", element.components().stream().map(Layer::name$).toArray(String[]::new));
		if (element.parentDisplay() != null) addParent(element, frame);
		if (!element.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName()));
		if (element.isAccessible())
			frame.addSlot("parameter", element.asAccessible().parameters().stream().map(p -> new Frame("parameter", "accessible").addSlot("value", p)).toArray(Frame[]::new));
		return frame;
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