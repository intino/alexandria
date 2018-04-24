package io.intino.konos.alexandria.activity.displays.events;

import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.Position;

public interface OpenItemCatalogEvent {
	Item item();
	Stamp stamp();
	Catalog catalog();
	Position position();
	String itemToShow();
	boolean filtered();
	boolean filter(Item target);
}
