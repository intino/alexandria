package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.ProxyDisplay;
import io.intino.alexandria.ui.displays.notifiers.ProxyStampNotifier;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class ProxyStamp<DN extends ProxyStampNotifier, B extends Box> extends AbstractProxyStamp<B> {
    private ProxyDisplay proxy;
    private java.util.Map<String, String> parameters = new HashMap<>();

    public ProxyStamp(B box) {
        super(box);
    }

    public ProxyStamp parameter(String name, String value) {
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

    protected ProxyStamp _proxy(ProxyDisplay display) {
        this.proxy = display;
        return this;
    }

    private void addProxy() {
        if (children().size() > 0) return;
        proxy.session(session());
        add(proxy);
    }

    private void refreshProxy() {
        proxy.parameters(parameters);
        proxy.refresh();
    }
}