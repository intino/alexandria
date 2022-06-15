package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.SelectorListBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SelectorListBox<DN extends SelectorListBoxNotifier, B extends Box> extends AbstractSelectorListBox<DN, B> {
	private java.util.List<String> selection = new ArrayList<>();
	private java.util.Set<String> hiddenOptions = new HashSet<>();

    public SelectorListBox(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		selection(selection);
	}

	public void show(Component<?,?> option) {
    	hiddenOptions.remove(option.id());
		option.visible(true);
    	notifier.refreshHiddenOptions(new ArrayList<>(hiddenOptions));
	}

	public void hide(Component<?,?> option) {
		hiddenOptions.add(option.id());
		option.visible(false);
		notifier.refreshHiddenOptions(new ArrayList<>(hiddenOptions));
	}

	public void visible(Component<?,?> option, boolean value) {
    	if (value) show(option);
    	else hide(option);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selection() {
		return selection;
	}

	@Override
	public void reset() {
		select();
	}

	public void selection(String... options) {
		selection(Arrays.asList(options));
	}

	public void selection(List<String> selection) {
		this.selection = selection;
		notifier.refreshSelection(this.selection);
	}

	public void select(String... options) {
		updateSelection(Arrays.asList(options));
	}

	public void updateSelection(List<String> options) {
		selection(options);
		notifySelection();
	}
}