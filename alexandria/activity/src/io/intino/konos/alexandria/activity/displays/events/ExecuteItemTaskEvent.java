package io.intino.konos.alexandria.activity.displays.events;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public interface ExecuteItemTaskEvent {
	Item item();
	Stamp stamp();
	AlexandriaDisplay self();
}
