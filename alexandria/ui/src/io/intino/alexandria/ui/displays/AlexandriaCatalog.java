package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaCatalogNotifier;

public abstract class AlexandriaCatalog<DN extends AlexandriaCatalogNotifier, B extends Box> extends AlexandriaDisplay<DN, B> {

	protected AlexandriaCatalog(B box) {
		super(box);
	}

}