package io.intino.alexandria.ui.displays.roots;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.AlexandriaRoot;
import io.intino.alexandria.ui.displays.components.AlexandriaImage;
import io.intino.alexandria.ui.displays.components.AlexandriaText;
import io.intino.alexandria.ui.displays.notifiers.ComponentsNotifier;

public class Components extends AlexandriaRoot<ComponentsNotifier> {
	private UiFrameworkBox box;

	public Components(UiFrameworkBox box) {
		super();
		this.box = box;
	}

	@Override
	protected void init() {
		super.init();
		addAndPersonify(new AlexandriaText());
		addAndPersonify(new AlexandriaImage());
	}

}