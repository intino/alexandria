package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.model.graph.Display;

public abstract class DisplayRenderer extends UIPrototypeRenderer {
	private final Display display;

	protected DisplayRenderer(Display display, String box, String packageName) {
		super(display.name$(), box, packageName);
		this.display = display;
	}

	protected Display display() {
		return display;
	}

	@Override
	public FrameBuilder frameBuilder() {
		return super.frameBuilder().add(display.getClass().getSimpleName().toLowerCase());
	}
}