package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.model.graph.Display;
import org.siani.itrules.model.Frame;

public abstract class DisplayRenderer extends Renderer {
	private final Display display;

	protected DisplayRenderer(Display display, String box, String packageName) {
		super(display.name$(), box, packageName);
		this.display = display;
	}

	protected Display display() {
		return display;
	}

	@Override
	protected Frame createFrame() {
		Frame frame = super.createFrame();
		frame.addTypes(display.getClass().getSimpleName().toLowerCase());
		return frame;
	}
}
