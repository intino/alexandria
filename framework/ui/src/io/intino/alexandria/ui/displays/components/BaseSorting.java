package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.BaseSortingNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class BaseSorting<DN extends BaseSortingNotifier, B extends Box> extends AbstractBaseSorting<DN, B> {
	private SelectListener selectListener;
	private java.util.List<Collection> collections = new ArrayList<>();
	private boolean selected = false;

	public BaseSorting(B box) {
		super(box);
	}

	public BaseSorting<DN, B> onSelect(SelectListener listener) {
		this.selectListener = listener;
		return this;
	}

	public BaseSorting<DN, B> bindTo(Collection... collections) {
		this.collections = Arrays.stream(collections).filter(Objects::nonNull).collect(toList());
		return this;
	}

	public void toggle() {
		selected = !selected;
		notifySelected();
	}

	public boolean selected() {
		return selected;
	}

	public BaseSorting<DN, B> selection(boolean value) {
		this.selected = value;
		notifier.refreshSelection(value);
		return this;
	}

	private void notifySelected() {
		notifyCollections();
		notifyListener();
	}

	private void notifyCollections() {
		collections.forEach(c -> {
			if (selected) c.addSorting(key());
			else c.removeSorting(key());
		});
	}

	private void notifyListener() {
		if (selectListener == null) return;
		selectListener.accept(new SelectEvent(this, key()));
	}

	private String key() {
		return label() != null && !label().isEmpty() ? label() : name();
	}

}