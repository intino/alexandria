package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;

public class ItemMold extends AbstractItemMold<UiFrameworkBox> {

    public ItemMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        avatar.update(item.label());
        label.update(item.label());
    }
}