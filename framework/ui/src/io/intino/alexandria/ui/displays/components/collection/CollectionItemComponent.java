package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.ComponentNotifier;

public abstract class CollectionItemComponent<DN extends ComponentNotifier, Type, B extends Box> extends Component<DN, B> implements CollectionItemDisplay<Type> {
	private Type item;

	protected CollectionItemComponent(B box) {
		super(box);
	}

	@Override
	public Type item() {
		return this.item;
	}

	@Override
	public String section() {
		return properties().getOrDefault("section", null);
	}

	@Override
	public void update(Type item) {
		item(item);
	}

	public CollectionItemComponent<DN, Type, B> item(Type item) {
		this.item = item;
		return this;
	}

	public CollectionItemComponent<DN, Type, B> section(String section) {
		properties().put("section", section);
		return this;
	}

}
