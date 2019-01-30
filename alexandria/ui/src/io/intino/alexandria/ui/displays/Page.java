package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.PageNotifier;

public class Page<DN extends PageNotifier, B extends Box> extends Display<DN, B> {

	public Page(B box) {
		super(box);
	}

}