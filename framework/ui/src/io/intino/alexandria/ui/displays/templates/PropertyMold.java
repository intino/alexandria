package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.AlexandriaUiBox;

public class PropertyMold extends AbstractPropertyMold<AlexandriaUiBox> {

    public PropertyMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        Property property = item();
        name.value(property.name());
        type.value(property.type().name().replace("Array", "[]"));
        if (property.facets() != null) facets.value(String.join(", ", property.facets()));
        description.value(property.description());
        //valuesLabel.update(property.type() == Property.Type.Word ? translate("allowed values") : translate("default value"));
        valuesLabel.value(valuesLabel());
        valuesSeparator.value(hasValues() ? ":" : null);
        values.value(String.join(", ", property.values()));
    }

    private String valuesLabel() {
        if (!hasValues()) return null;
        return item().type() == Property.Type.Word ? "allowed values" : "default value";
    }

    private boolean hasValues() {
        return item().values().size() > 0;
    }

}