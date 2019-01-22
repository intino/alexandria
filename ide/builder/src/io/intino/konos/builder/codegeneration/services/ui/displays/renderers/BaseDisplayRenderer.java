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
	final D display;

	protected BaseDisplayRenderer(Settings settings, D display) {
		super(settings, display);
		this.display = display;
	}

	public void execute() {
		if (display == null) return;
		String path = path(display);
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		classes().put("Display#" + display.name$(), path + "." + newDisplay);
		Frame frame = baseFrame();
		createPassiveViewFiles(frame);
		write(frame, src(), gen(), path(display));
		if (display.isAccessible()) writeDisplaysFor(display.asAccessible(), frame);
	}

	private void writeDisplaysFor(AccessibleDisplay display, Frame frame) {
		frame.addTypes("accessible");
		final String name = snakeCaseToCamelCase(display.name$());
		writeFrame(new File(src(), path(display.a$(Display.class))), name + "Proxy", setup(srcTemplate()).format(frame.addTypes("accessible")));
		writeNotifier(display.a$(PassiveView.class), frame);
		writeRequester(display.a$(PassiveView.class), frame);
	}

	protected Display display() {
		return display;
	}

	@Override
	protected Frame baseFrame() {
		Frame frame = super.baseFrame().addTypes("display").addTypes(display.getClass().getSimpleName().toLowerCase());
		frame.addSlot("component", display.components().stream().map(Layer::name$).toArray(String[]::new));
		if (display.parentDisplay() != null) addParent(display, frame);
		if (!display.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName()));
		if (display.isAccessible())
			frame.addSlot("parameter", display.asAccessible().parameters().stream().map(p -> new Frame("parameter", "accessible").addSlot("value", p)).toArray(Frame[]::new));
		return frame;
	}

	private void addParent(Display display, Frame frame) {
		String parent = parent();
		final Frame parentFrame = new Frame().addSlot("value", display.parentDisplay()).addSlot("dsl", parent).addSlot("package", parent.substring(0, parent.lastIndexOf(".")));
		frame.addSlot("parent", parentFrame);
	}

}