package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaEditorNotifier;

public class AlexandriaEditor<DN extends AlexandriaEditorNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	public AlexandriaEditor(B box) {
		super(box);
	}

}