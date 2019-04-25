package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.model.Item;

public interface ExecuteItemTaskEvent {
	Item item();
	Stamp stamp();
	AlexandriaDisplay self();
}
