package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.ProxyDisplay;
import io.intino.alexandria.ui.displays.notifiers.ProxyStampNotifier;

@SuppressWarnings("rawtypes")
public class ProxyStamp<DN extends ProxyStampNotifier, B extends Box> extends AbstractProxyStamp<B> {
    private ProxyDisplay proxy;

    public ProxyStamp(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        add(proxy);
    }

    public ProxyStamp _proxy(ProxyDisplay display) {
        this.proxy = display;
        return this;
    }
}