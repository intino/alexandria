package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.Panel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AlexandriaViewContainer<DN extends AlexandriaDisplayNotifier> extends AlexandriaElementView<DN, ElementViewDisplayProvider> {
	private Panel context;
	private Item target;
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();

	public AlexandriaViewContainer(Box box) {
		super(box);
	}

	public Panel context() {
		return this.context;
	}

	public void context(Panel context) {
		this.context = context;
	}

	public Item target() {
		return this.target;
	}

	public void target(Item target) {
		this.target = target;
	}

	public void onOpenItem(Consumer<OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	@Override
	public void refresh(io.intino.konos.alexandria.ui.schemas.Item... items) {
		refresh();
	}

	@Override
	public void refresh(io.intino.konos.alexandria.ui.schemas.Item item) {
		refresh();
	}

	@Override
	public void refresh(io.intino.konos.alexandria.ui.schemas.Item item, boolean highlight) {
		refresh();
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.ui.schemas.Item item) {
	}

	void notifyOpenItem(OpenItemEvent event) {
		openItemListeners.forEach(l -> l.accept(event));
	}
}
