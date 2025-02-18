package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseIconNotifier;

public class BaseIcon<DN extends BaseIconNotifier, B extends Box> extends AbstractBaseIcon<DN, B> {
    private String title;
    protected String icon;
    protected String darkIcon;

    public BaseIcon(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        refreshIcon();
        refreshDarkIcon();
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

    protected BaseIcon<DN, B> _darkIcon(String icon) {
        this.darkIcon = icon;
        return this;
    }

    protected BaseIcon<DN, B> darkIcon(String icon) {
        _darkIcon(icon);
        refreshDarkIcon();
        return this;
    }

    protected void refreshIcon() {
        notifier.refreshIcon(icon);
    }

    protected void refreshDarkIcon() {
        notifier.refreshDarkIcon(darkIcon);
    }
}