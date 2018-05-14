package io.intino.konos.alexandria.ui.displays.events;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Panel;
import io.intino.konos.alexandria.ui.model.TimeRange;
import io.intino.konos.alexandria.ui.model.mold.stamps.Tree;

public interface OpenItemEvent {
	String label();
	String itemId();
	Item item();
	Panel panel();
	TimeRange range();
	Tree breadcrumbs();
}
