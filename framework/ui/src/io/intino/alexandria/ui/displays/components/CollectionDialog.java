package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.CollectionDialogNotifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CollectionDialog<DN extends CollectionDialogNotifier, B extends Box> extends AbstractCollectionDialog<DN, B> {
    private boolean allowSearch = true;
    private SelectionListener selectionListener = null;
	private SearchBox searchBox;
	private Collection collection;
	private List selection = Collections.emptyList();

	public CollectionDialog(B box) {
        super(box);
    }

	@Override
	public void init() {
		super.init();
		createSearchBox();
	}

	@Override
	public void open() {
		super.open();
		selection = new ArrayList<>();
		notifier.refreshSelectionCount(selection.size());
		searchBox().ifPresent(sb -> sb.search(""));
		if (collection != null) collection.init();
	}

    public CollectionDialog onSelect(SelectionListener listener) {
    	this.selectionListener = listener;
    	return this;
	}

	public void bindTo(Selectable collection) {
		this.collection = (Collection) collection;
    	searchBox().ifPresent(searchBox -> searchBox.bindTo((Collection) collection));
    	collection.onSelect((event) -> updateSelection(event.selection()));
	}

	protected CollectionDialog _allowSearch(boolean value) {
		this.allowSearch = value;
		return this;
	}

	private <T> void updateSelection(List<T> selection) {
		this.selection = selection;
		notifier.refreshSelectionCount(selection.size());
		collection.reload();
	}

	private void notifySelection(List selection) {
		if (selectionListener == null) return;
		selectionListener.accept(new SelectionEvent(this, selection));
	}

	private void createSearchBox() {
		if (!allowSearch) return;
		this.searchBox = new SearchBox<>(box());
		add(searchBox);
	}

	private Optional<SearchBox> searchBox() {
		return Optional.ofNullable(searchBox);
	}

	public void accept() {
		close();
		notifySelection(selection);
	}
}