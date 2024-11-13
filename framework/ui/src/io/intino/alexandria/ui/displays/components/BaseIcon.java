package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseIconNotifier;

public class BaseIcon<DN extends BaseIconNotifier, B extends Box> extends AbstractBaseIcon<DN, B> {
    private String title;
    protected String icon;

    public BaseIcon(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        refreshIcon();
    }

    public String title() {
        return title;
    }

    public BaseIcon<DN, B> title(String title) {
        _title(title);
        notifier.refreshTitle(title);
        return this;
    }

    protected BaseIcon<DN, B> _title(String title) {
        this.title = title;
        return this;
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

    protected void refreshIcon() {
        notifier.refreshIcon(icon);
    }
}