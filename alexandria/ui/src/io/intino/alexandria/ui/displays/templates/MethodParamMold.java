package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Parameter;

public class MethodParamMold extends AbstractMethodParamMold<UiFrameworkBox> {
    public boolean addComma;

    public MethodParamMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        Parameter parameter = item();
        name.update(parameter.name());
        type.update(parameter.type());
        if (addComma) comma.update(", ");
    }

}