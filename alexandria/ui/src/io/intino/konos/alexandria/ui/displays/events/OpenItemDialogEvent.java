package io.intino.konos.alexandria.ui.displays.events;

import io.intino.konos.alexandria.ui.displays.AlexandriaDialog;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;

public interface OpenItemDialogEvent {
	Item item();
	Stamp stamp();
	AlexandriaDialog dialog();
}
