package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.FrameBuilder;

import static java.io.File.separator;

public abstract class UIRenderer {
	protected static final String UI = "";
	protected static final String RESOURCES = UI + "resources";
	protected static final String DIALOGS = UI + "dialogs";
	protected static final String DISPLAYS = UI + "displays";
	protected static final String NOTIFIERS = DISPLAYS + separator + "notifiers";
	protected static final String REQUESTERS = DISPLAYS + separator + "requesters";
	protected final String box;
	protected final String packageName;

	protected UIRenderer(String box, String packageName) {
		this.box = box;
		this.packageName = packageName;
	}

	protected FrameBuilder frameBuilder() {
		return new FrameBuilder().add("box", box).add("package", packageName);
	}
}
