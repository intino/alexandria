package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Property;

public class PropertyMold extends AbstractPropertyMold<UiFrameworkBox> {

    public PropertyMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        name.update(property.name());
        type.update(property.type().name().toLowerCase());
        description.update(property.description());
        //valuesLabel.update(property.type() == Property.Type.Word ? translate("allowed values") : translate("default value"));
        valuesLabel.update(valuesLabel());
        valuesSeparator.update(hasValues() ? ":" : null);
        values.update(String.join(", ", property.values()));
    }

    private String valuesLabel() {
        if (!hasValues()) return null;
        return property.type() == Property.Type.Word ? "allowed values" : "default value";
    }

    private boolean hasValues() {
        return property.values().size() > 0;
    }

}