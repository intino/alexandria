package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.MaterialIconNotifier;

public class MaterialIcon<DN extends MaterialIconNotifier, B extends Box> extends AbstractMaterialIcon<DN, B> {

    public MaterialIcon(B box) {
        super(box);
    }

    public MaterialIcon<DN, B> icon(String materialIcon) {
        return (MaterialIcon<DN, B>) super.icon(materialIcon);
    }

    public MaterialIcon<DN, B> update(String icon) {
        return (MaterialIcon<DN, B>) super.update(icon);
    }

}