package io.intino.konos.server.activity.displays.elements;

import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.elements.providers.ElementViewDisplayProvider;
import io.intino.konos.server.activity.displays.molds.model.Stamp;
import io.intino.konos.server.activity.displays.molds.model.stamps.Tree;
import io.intino.konos.server.activity.displays.panels.model.Panel;

import java.util.function.Consumer;

public interface ElementViewDisplay<P extends ElementViewDisplayProvider> {
	void provider(P provider);
	void onLoading(Consumer<Boolean> listener);
	ElementView view();
	void view(ElementView view);
	void refresh();
	void refresh(io.intino.konos.server.activity.displays.schemas.Item... items);

	void onOpenItem(Consumer<OpenItemEvent> listener);
	void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener);
	void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener);

	interface OpenItemEvent {
		String label();
		String itemId();
		Item item();
		Panel panel();
		TimeRange range();
		Tree breadcrumbs();
	}

	interface OpenItemDialogEvent {
		String item();
		String path();
		int width();
		int height();
	}

	interface ExecuteItemTaskEvent {
		String item();
		Stamp stamp();
	}
}
