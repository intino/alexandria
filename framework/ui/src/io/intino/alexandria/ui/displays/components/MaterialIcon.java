package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.MaterialIconNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class MaterialIcon<DN extends MaterialIconNotifier, B extends Box> extends AbstractMaterialIcon<DN, B> {

    public MaterialIcon(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        if (session().browser().isMobile()) refreshIcon();
    }

    protected MaterialIcon<DN, B> _icon(String materialIcon) {
        return (MaterialIcon<DN, B>) super._icon(materialIcon);
    }

    public MaterialIcon<DN, B> icon(String icon) {
        return (MaterialIcon<DN, B>) super.icon(icon);
    }

    @Override
    public void init() {
        super.init();
        if (session().browser().isMobile()) refreshIcon();
    }

    @Override
    protected void refreshIcon() {
        notifier.refreshIcon(iconReference());
    }

    private static final String PngMaterialIcon = "/icons/mobile/%s.png";
    private String iconReference() {
        if (session().browser().isMobile()) {
            URL iconResource = Actionable.class.getResource(String.format(PngMaterialIcon, icon));
            return iconResource != null ? Asset.toResource(baseAssetUrl(), iconResource).setLabel(String.format(PngMaterialIcon, icon)).toUrl().toString() : icon;
        }
        return icon;
    }

}