package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.AbstractFrame;
import io.intino.alexandria.ui.displays.notifiers.FrameNotifier;

public class Frame<DN extends FrameNotifier, B extends Box> extends AbstractFrame<B> {
    private String url;

    public Frame(B box) {
        super(box);
    }

    public String url() {
        return url;
    }

    public void url(String url) {
        _url(url).refresh();
    }

    protected Frame<DN, B> _url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.refresh(url);
    }
}