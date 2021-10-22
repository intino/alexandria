package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.LayerNotifier;

import java.util.ArrayList;
import java.util.UUID;

public class Layer<DN extends LayerNotifier, B extends Box> extends AbstractLayer<B> {
	private Component<?, ?> template;
	private String title;
	private java.util.List<Listener> openListeners = new ArrayList<>();
	private java.util.List<Listener> closeListeners = new ArrayList<>();
	private OpenLayer<?, ?> homeAction;

	public Layer(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		refresh();
	}

	public Layer<DN, B> bindTo(OpenLayer<?, ?> actionable) {
		this.homeAction = actionable;
		return this;
	}

	@Override
	public void refresh() {
		super.refresh();
		notifier.refreshHome(homeAction != null);
	}

	public Layer<DN, B> onOpen(Listener listener) {
		openListeners.add(listener);
		return this;
	}

	public Layer<DN, B> onClose(Listener listener) {
		closeListeners.add(listener);
		return this;
	}

	public void open() {
		notifier.open();
		soul().pushLayer(this);
		notifyOpen();
	}

	public void close() {
		notifier.close();
		soul().popLayer();
		notifyClose();
	}

	public void home() {
		if (homeAction == null) return;
		homeAction.openAddress();
	}

	public Layer<DN, B> title(String title) {
		_title(title);
		notifier.refreshTitle(title);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <C extends Component<?, ?>> C get() {
		return (C) template;
	}

	public <C extends Component<?, ?>> C template() {
		return get();
	}

	public void template(Component<?, ?> template) {
		clear();
		this.template = template;
		this.template.id(UUID.randomUUID().toString());
		add(this.template);
	}

	protected Layer<DN, B> _title(String title) {
		this.title = title;
		return this;
	}

	private void notifyOpen() {
		openListeners.forEach(l -> l.accept(new Event(this)));
	}

	private void notifyClose() {
		closeListeners.forEach(l -> l.accept(new Event(this)));
	}
}