package io.intino.alexandria.ui.displays.blocks;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Property;

public class PropertyBlock extends AbstractPropertyBlock<UiFrameworkBox> {
    private Property property;

    public PropertyBlock(UiFrameworkBox box) {
        super(box);
    }

    public void property(Property property) {
        this.property = property;
    }

    @Override
    public void refresh() {
        super.refresh();
        name.update(property.name());
        type.update(property.type().name());
    }
}