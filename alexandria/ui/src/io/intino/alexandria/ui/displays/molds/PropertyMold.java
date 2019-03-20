package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.molds.AbstractPropertyMold;

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
        valuesLabel.update(property.type() == Property.Type.Word ? "allowed values" : "default value");
        values.update(String.join(", ", property.values()));
    }

}