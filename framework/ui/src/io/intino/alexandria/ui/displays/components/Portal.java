package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.ProxyDisplay;
import io.intino.alexandria.ui.displays.notifiers.PortalNotifier;

import java.util.HashMap;

public abstract class Portal<DN extends PortalNotifier, B extends Box> extends AbstractPortal<B> {
    private String to;
    private String in;
    private java.util.Map<String, String> parameters = new HashMap<>();
    private ProxyDisplay proxy;

    public Portal(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        if (this.to == null || this.in == null) return;
        this.proxy = buildProxy();
    }

    @Override
    public void refresh() {
        super.refresh();
        refreshProxy();
    }

    public Portal parameter(String name, String value) {
    	parameters.put(name, value);
    	refreshProxy();
    	return this;
	}

    protected abstract ProxyDisplay buildProxy();

    protected Portal _to(String accesibleDisplay, String in) {
        this.to = accesibleDisplay;
        this.in = in;
        return this;
    }

    protected Portal _add(String name, String value) {
        this.parameters.put(name, value);
        return this;
    }

    protected Portal _parameters(java.util.Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    private void refreshProxy() {
        proxy.parameters(parameters);
        proxy.refresh();
    }

}