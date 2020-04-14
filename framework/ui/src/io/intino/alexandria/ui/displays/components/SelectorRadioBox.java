package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.AbstractSelectorRadioBox;
import io.intino.alexandria.ui.displays.notifiers.SelectorMenuNotifier;
import io.intino.alexandria.ui.displays.notifiers.SelectorRadioBoxNotifier;

import java.util.List;

import static java.util.Collections.singletonList;

public class SelectorRadioBox<DN extends SelectorRadioBoxNotifier, B extends Box> extends AbstractSelectorRadioBox<DN, B> {
	private String selected;

    public SelectorRadioBox(B box) {
        super(box);
    }

	public void select(String value) {
		notifier.refreshSelected(value);
		if (selected != null && selected.equals(value)) return;
		selected = value;
		notifySelection();
	}

	@Override
	public List<String> selection() {
		return singletonList(selected);
	}

	@Override
	public void reset() {
		selected = null;
		notifier.refreshSelected(null);
	}
}