package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AlexandriaPanelView<DN extends AlexandriaDisplayNotifier> extends AlexandriaElementView<DN, ElementViewDisplayProvider> {
	private Panel context;
	private Item target;
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();

	public AlexandriaPanelView(Box box) {
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
	public void refresh(io.intino.konos.alexandria.activity.schemas.Item... items) {
		refresh();
	}

	@Override
	public void refresh(io.intino.konos.alexandria.activity.schemas.Item item) {
		refresh();
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.activity.schemas.Item item) {
	}

	void notifyOpenItem(OpenItemEvent event) {
		openItemListeners.forEach(l -> l.accept(event));
	}
}
