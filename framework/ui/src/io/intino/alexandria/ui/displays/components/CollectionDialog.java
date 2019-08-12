package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.CollectionDialogNotifier;

import java.util.List;
import java.util.Optional;

public class CollectionDialog<DN extends CollectionDialogNotifier, B extends Box> extends AbstractCollectionDialog<DN, B> {
    private boolean allowSearch = true;
    private SelectionListener selectionListener = null;
	private SearchBox searchBox;
	private Collection collection;

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
		close();
		notifySelection(selection);
	}

	private <T> void notifySelection(List<T> selection) {
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
}