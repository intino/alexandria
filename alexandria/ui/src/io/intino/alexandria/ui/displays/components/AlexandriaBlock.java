package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.AlexandriaComponent;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaBlockNotifier;

import java.util.ArrayList;
import java.util.List;

public class AlexandriaBlock<B extends Box> extends io.intino.alexandria.ui.displays.AlexandriaBlock<AlexandriaBlockNotifier, B> {
	private List<AlexandriaComponent> components = new ArrayList<>();

	public AlexandriaBlock(B box) {
		super(box);
	}

	public void add(AlexandriaComponent component) {
		components.add(component);
	}
}