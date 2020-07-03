package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.ActionSwitch;
import io.intino.alexandria.ui.displays.components.ActionToggle;

public interface Selection {
	void add(Component container);
	void bindTo(Selector selector, String option);
	void bindTo(ActionToggle action, String option);
}
