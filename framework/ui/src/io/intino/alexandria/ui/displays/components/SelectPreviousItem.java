package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.events.actionable.SelectItemListener;
import io.intino.alexandria.ui.displays.events.collection.RefreshListener;
import io.intino.alexandria.ui.displays.events.collection.SelectItemEvent;
import io.intino.alexandria.ui.displays.notifiers.SelectPreviousItemNotifier;

public class SelectPreviousItem<DN extends SelectPreviousItemNotifier, B extends Box> extends AbstractSelectPreviousItem<DN, B> {
	private Collection collection;
	private SelectItemListener selectItemListener = null;

	public SelectPreviousItem(B box) {
		super(box);
	}

	public SelectPreviousItem onSelect(SelectItemListener listener) {
		this.selectItemListener = listener;
		return this;
	}

	public SelectPreviousItem bindTo(Collection collection) {
		if (collection != null) releaseFrom(collection);
		this.collection = collection;
		this.collection.onRefresh(e -> readonly(!collection.canSelectPreviousItem()));
		this.collection.addSelectionListener(event -> readonly(!collection.canSelectPreviousItem()));
		return this;
	}

	public void execute() {
		if (this.collection == null) return;
		Object item = this.collection.selectPreviousItem();
		if (selectItemListener != null) selectItemListener.accept(new SelectItemEvent(this, collection, item));
	}

	private void releaseFrom(Collection collection) {
		collection.unRefresh(refreshListener(collection));
		collection.removeSelectionListener(selectionListener(collection));
	}

	private SelectionListener selectionListener(Collection collection) {
		return event -> readonly(!collection.canSelectNextItem());
	}

	private RefreshListener refreshListener(Collection collection) {
		return e -> readonly(!collection.canSelectNextItem());
	}

}