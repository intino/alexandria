package io.intino.konos.alexandria.activity.displays.events;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public interface OpenItemDialogEvent {
	Item item();
	Stamp stamp();
	AlexandriaDialog dialog();
}
