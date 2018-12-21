package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.displays.AlexandriaDialog;
import io.intino.alexandria.ui.model.Item;

public interface OpenItemDialogEvent {
	Item item();
	Stamp stamp();
	AlexandriaDialog dialog();
}
