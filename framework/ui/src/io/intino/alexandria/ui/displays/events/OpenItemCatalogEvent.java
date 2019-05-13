package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.schemas.Position;

public interface OpenItemCatalogEvent {
	Item item();
	Stamp stamp();
	Catalog catalog();
	Position position();
	String itemToShow();
	boolean filtered();
	boolean filter(Item target);
}
