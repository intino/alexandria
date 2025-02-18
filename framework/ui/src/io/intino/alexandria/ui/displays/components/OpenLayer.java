package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.PropertyList;
import io.intino.alexandria.ui.displays.components.addressable.Addressable;
import io.intino.alexandria.ui.displays.events.actionable.OpenLayerEvent;
import io.intino.alexandria.ui.displays.events.actionable.OpenLayerListener;
import io.intino.alexandria.ui.displays.notifiers.OpenLayerNotifier;

import java.util.UUID;

public class OpenLayer<DN extends OpenLayerNotifier, B extends Box> extends AbstractOpenLayer<DN, B> implements Addressable {
	private Layer<?, ?> layer;
	private OpenLayerListener openListener = null;
	private String path;
	private String address;
	private Transition transition = Transition.Slide;
	private boolean showHeader = true;

	public OpenLayer(B box) {
		super(box);
	}

	public enum Transition { Slide, Zoom, Fade, Grow }

	public OpenLayer<DN, B> onOpen(OpenLayerListener listener) {
		this.openListener = listener;
		return this;
	}

	public void openAddress() {
		if (!validAddress()) return;
		notifier.addressed(address);
		notifier.openAddress();
		notifier.addressed(null);
	}

	public String path() {
		return this.path;
	}

	protected OpenLayer<DN, B> _path(String path) {
		this.path = path;
		this._address(path);
		return this;
	}

	protected OpenLayer<DN, B> _transition(Transition transition) {
		this.transition = transition;
		return this;
	}

	protected OpenLayer<DN, B> _address(String address) {
		this.address = address;
		return this;
	}

	protected OpenLayer<DN, B> _showHeader(boolean value) {
		this.showHeader = value;
		return this;
	}

	protected void address(String value) {
		this._address(value);
	}

	public void openLayer() {
		this.layer = registerLayer(showHeader);
		this.layer.open();
		if (openListener != null) openListener.accept(new OpenLayerEvent(this, layer));
		openAddress();
	}

	public void execute() {
	}

	private boolean validAddress() {
		return address != null && !address.contains(":");
	}

	private Layer<?, ?> registerLayer(boolean showHeader) {
		clear();
		layer = add(createLayer(showHeader));
		return layer;
	}

	private Layer<?, ?> createLayer(boolean showHeader) {
		Layer<?, ?> result = new Layer<>(box()).id(UUID.randomUUID().toString());
		PropertyList properties = result.properties();
		properties.put("showHeader", String.valueOf(showHeader));
		properties.put("transition", transition.name());
		if (color() != null) properties.put("color", color());
		result.bindTo(this);
		return result;
	}

}