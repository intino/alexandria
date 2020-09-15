package io.intino.alexandria.ui.displays.events.editable;

import io.intino.alexandria.ui.displays.events.ChangeItemEvent;
import io.intino.alexandria.ui.displays.events.RemoveItemEvent;

public interface RemoveItemListener {
	void accept(RemoveItemEvent event);
}
