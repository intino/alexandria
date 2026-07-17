package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SearchEvent;
import io.intino.alexandria.ui.displays.events.SearchListener;
import io.intino.alexandria.ui.displays.notifiers.SearchBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class SearchBox<DN extends SearchBoxNotifier, B extends Box> extends AbstractSearchBox<B> {
    private SearchListener searchListener;
    private java.util.List<Collection> collections = new ArrayList<>();
    private String condition = null;
	private String newCondition = null;
	private boolean searching = false;

	public SearchBox(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        if (condition != null) notifier.refreshCondition(condition);
    }

    public SearchBox<DN, B> onSearch(SearchListener listener) {
        this.searchListener = listener;
        return this;
    }

    public SearchBox<DN, B> bindTo(Collection... collections) {
        this.collections = Arrays.stream(collections).filter(Objects::nonNull).collect(toList());
        return this;
    }

	public SearchBox<DN, B> condition(String condition) {
		this.condition = condition;
		notifier.refreshCondition(condition);
		return this;
	}

	public String condition() {
		return condition;
	}

    public synchronized void search(String condition) {
		this.condition = condition;
		if (searching) {
			newCondition = condition;
			return;
		}
		searching = true;
		notifySelected();
    }

    private void notifySelected() {
		CompletableFuture.runAsync(() -> {
			try {
				notifyCollections();
				notifyListener();
			} finally {
				synchronized (this) {
					searching = false;
					if (newCondition != null) {
						String next = newCondition;
						newCondition = null;
						search(next);
					}
				}
			}
		});
    }

    private void notifyCollections() {
        collections.forEach(c -> c.filter(condition));
        if (collections.size() > 0) notifier.refreshCount(collections.get(0).itemCount());
    }

    private void notifyListener() {
        if (searchListener == null) return;
        searchListener.accept(new SearchEvent(this, condition.trim()));
    }
}
