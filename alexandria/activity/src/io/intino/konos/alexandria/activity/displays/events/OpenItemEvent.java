package io.intino.konos.alexandria.activity.displays.events;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;

public interface OpenItemEvent {
	String label();
	String itemId();
	Item item();
	Panel panel();
	TimeRange range();
	Tree breadcrumbs();
}
