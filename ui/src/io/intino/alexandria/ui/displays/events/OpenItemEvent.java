package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.Panel;
import io.intino.alexandria.ui.model.TimeRange;
import io.intino.alexandria.ui.model.mold.stamps.Tree;

public interface OpenItemEvent {
	String label();
	String itemId();
	Item item();
	Panel panel();
	TimeRange range();
	Tree breadcrumbs();
}
