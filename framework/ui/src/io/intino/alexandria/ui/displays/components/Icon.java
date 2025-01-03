package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.notifiers.IconNotifier;

import java.net.URL;

public class Icon<DN extends IconNotifier, B extends Box> extends AbstractIcon<DN, B> {
    private URL icon;
    private URL darkIcon;

    public Icon(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        refreshIcon();
        refreshDarkIcon();
    }

    public Icon<DN, B> icon(URL icon) {
    	if (icon == null) return this;
    	_icon(icon);
        refreshIcon();
        return this;
    }

    public Icon<DN, B> darkIcon(URL icon) {
    	if (icon == null) return this;
    	_darkIcon(icon);
        refreshDarkIcon();
        return this;
    }

    protected Icon<DN, B> _icon(URL icon) {
        this.icon = icon;
        return this;
    }

    protected Icon<DN, B> _darkIcon(URL icon) {
        this.darkIcon = icon;
        return this;
    }

    private String serialize(URL icon) {
        return Asset.toResource(baseAssetUrl(), icon).toUrl().toString();
    }

    @Override
    protected void refreshIcon() {
        if (icon == null) return;
        notifier.refreshIcon(serialize(icon));
    }

    @Override
    protected void refreshDarkIcon() {
        if (darkIcon == null) return;
        notifier.refreshDarkIcon(serialize(darkIcon));
    }
}