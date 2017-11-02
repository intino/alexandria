package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.framework.box.displays.providers.ElementViewDisplayProvider;
import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.mold.Stamp;
import io.intino.alexandria.framework.box.model.mold.stamps.Tree;
import io.intino.alexandria.framework.box.model.Panel;

import java.util.function.Consumer;

public interface ElementViewDisplay<P extends ElementViewDisplayProvider> {
	void provider(P provider);
	void onLoading(Consumer<Boolean> listener);
	ElementView view();
	void view(ElementView view);
	void refresh();
	void refresh(io.intino.alexandria.framework.box.schemas.Item... items);

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
