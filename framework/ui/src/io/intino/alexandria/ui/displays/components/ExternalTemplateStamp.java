package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.ProxyDisplay;
import io.intino.alexandria.ui.displays.notifiers.ExternalTemplateStampNotifier;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class ExternalTemplateStamp<DN extends ExternalTemplateStampNotifier, B extends Box> extends AbstractExternalTemplateStamp<B> {
	private ProxyDisplay proxy;
	private java.util.Map<String, String> parameters = new HashMap<>();

	public ExternalTemplateStamp(B box) {
		super(box);
	}

	public ExternalTemplateStamp parameter(String name, String value) {
		parameters.put(name, value);
		refreshProxy();
		return this;
	}

	@Override
	public void refresh() {
		super.refresh();
		addProxy();
	}

	@SuppressWarnings("unchecked")
	public <T extends ProxyDisplay> T proxy() {
		return (T) this.proxy;
	}

	public ExternalTemplateStamp proxy(ProxyDisplay display) {
		_proxy(display);
		return this;
	}

	protected ExternalTemplateStamp _proxy(ProxyDisplay display) {
		this.proxy = display;
		return this;
	}

	private void addProxy() {
		clear();
		proxy.session(session());
		add(proxy);
	}

	private void refreshProxy() {
		proxy.parameters(parameters);
		proxy.refresh();
	}
}