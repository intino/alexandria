package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BlockParallaxNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class BlockParallax<DN extends BlockParallaxNotifier, B extends Box> extends AbstractBlockParallax<B> {
    private URL background;

    public BlockParallax(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    @Override
    public void refresh() {
        String background = background();
        if (background == null) return;
        notifier.refresh(background);
    }

    public String background() {
        if (background == null) return null;
        return Asset.toResource(baseAssetUrl(), background).toUrl().toString();
    }

    protected BlockParallax _background(URL background) {
        this.background = background;
        return this;
    }

}