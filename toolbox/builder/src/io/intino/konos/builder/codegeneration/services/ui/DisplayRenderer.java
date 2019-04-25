package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.model.graph.Display;
import org.siani.itrules.model.Frame;

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
	public Frame buildFrame() {
		return super.buildFrame().addTypes(display.getClass().getSimpleName().toLowerCase());
	}
}