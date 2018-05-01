package io.intino.konos.alexandria.ui.displays.events;

import io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;

public interface ExecuteItemTaskEvent {
	Item item();
	Stamp stamp();
	AlexandriaDisplay self();
}
