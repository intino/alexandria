package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.EditorNotifier;

public class Editor<DN extends EditorNotifier, B extends Box> extends Display<DN, B> {

	public Editor(B box) {
		super(box);
	}

}