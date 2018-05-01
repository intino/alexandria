package io.intino.konos.alexandria.ui.displays.events;

import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.Position;

public interface OpenItemCatalogEvent {
	Item item();
	Stamp stamp();
	Catalog catalog();
	Position position();
	String itemToShow();
	boolean filtered();
	boolean filter(Item target);
}
