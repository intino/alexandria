package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaDesktopNotifier;

public class AlexandriaDesktop<DN extends AlexandriaDesktopNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	public AlexandriaDesktop(B box) {
		super(box);
	}

}