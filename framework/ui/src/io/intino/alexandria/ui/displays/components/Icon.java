package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.notifiers.IconNotifier;

import java.net.URL;

public class Icon<DN extends IconNotifier, B extends Box> extends AbstractIcon<DN, B> {

    public Icon(B box) {
        super(box);
    }

    public Icon<DN, B> icon(URL icon) {
        icon(serialize(icon));
        return this;
    }

    public Icon<DN, B> update(URL icon) {
        update(serialize(icon));
        return this;
    }

    private String serialize(URL icon) {
        return Asset.toResource(baseAssetUrl(), icon).toUrl().toString();
    }
}