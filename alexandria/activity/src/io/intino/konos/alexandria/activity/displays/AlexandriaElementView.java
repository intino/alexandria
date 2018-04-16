package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;
import io.intino.konos.alexandria.activity.schemas.Position;

import java.util.function.Consumer;

public interface AlexandriaElementView<P extends ElementViewDisplayProvider> {
	void provider(P provider);
	void onLoading(Consumer<Boolean> listener);
	ElementView view();
	void view(ElementView view);
	void refresh();
	void refresh(io.intino.konos.alexandria.activity.schemas.Item... items);
	void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.activity.schemas.Item item);

	void onOpenItem(Consumer<OpenItemEvent> listener);
	void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener);
	void onOpenItemCatalog(Consumer<OpenItemCatalogEvent> listener);
	void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener);

	interface OpenItemEvent {
		String label();
		String itemId();
		Item item();
		Panel panel();
		TimeRange range();
		Tree breadcrumbs();
	}

	interface OpenElementEvent {
		String label();
	}

	interface OpenItemDialogEvent {
		Item item();
		Stamp stamp();
		AlexandriaDialog dialog();
	}

	interface OpenItemCatalogEvent {
		Item item();
		Stamp stamp();
		Catalog catalog();
		Position position();
		String itemToShow();
		boolean filtered();
		boolean filter(Item target);
	}

	interface ExecuteItemTaskEvent {
		Item item();
		Stamp stamp();
		AlexandriaDisplay self();
	}

}
