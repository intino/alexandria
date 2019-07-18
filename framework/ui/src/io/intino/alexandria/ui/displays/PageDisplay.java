package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.PageDisplayNotifier;

public class PageDisplay<DN extends PageDisplayNotifier, B extends Box> extends Display<DN, B> {

	public PageDisplay(B box) {
		super(box);
	}

}