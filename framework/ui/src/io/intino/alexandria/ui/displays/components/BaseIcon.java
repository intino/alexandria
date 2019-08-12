package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseIconNotifier;

public class BaseIcon<DN extends BaseIconNotifier, B extends Box> extends AbstractBaseIcon<DN, B> {
    private String icon;

    public BaseIcon(B box) {
        super(box);
    }

    protected BaseIcon<DN, B> _icon(String icon) {
        this.icon = icon;
        return this;
    }

    protected BaseIcon<DN, B> icon(String icon) {
        _icon(icon);
        refreshIcon();
        return this;
    }

    private void refreshIcon() {
        notifier.refreshIcon(icon);
    }
}