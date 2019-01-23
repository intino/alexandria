package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.ui.displays.AlexandriaComponent;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaBlockNotifier;

import java.util.ArrayList;
import java.util.List;

public class AlexandriaBlock<DN extends AlexandriaBlockNotifier> extends AlexandriaComponent<DN> {
	private List<AlexandriaComponent> components = new ArrayList<>();

	public void add(AlexandriaComponent component) {
		components.add(component);
	}
}