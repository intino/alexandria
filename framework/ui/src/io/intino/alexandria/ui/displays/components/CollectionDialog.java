package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.CollectionDialogNotifier;

import java.util.List;

public class CollectionDialog<DN extends CollectionDialogNotifier, B extends Box> extends AbstractCollectionDialog<DN, B> {
    private boolean allowSearch = true;
    private Object selected = null;
    private SelectListener selectListener = null;

    public CollectionDialog(B box) {
        super(box);
    }

    public CollectionDialog allowSearch(boolean value) {
        this.allowSearch = value;
        return this;
    }

    public CollectionDialog onSelect(SelectListener listener) {
    	this.selectListener = listener;
    	return this;
	}

    public <T> T selected() {
    	return (T) selected;
	}

	public void bindTo(Selectable collection) {
    	collection.onSelect((event) -> updateSelection(event.selection()));
	}

	private <T> void updateSelection(List<T> selection) {
		close();
		selected = selection.size() > 0 ? selection.get(0) : null;
		notifySelection();
	}

	private void notifySelection() {
		if (selectListener != null) selectListener.accept(new SelectEvent(this, selected));
	}

}