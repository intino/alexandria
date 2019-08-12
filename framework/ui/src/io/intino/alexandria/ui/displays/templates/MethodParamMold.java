package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Parameter;
import io.intino.alexandria.ui.AlexandriaUiBox;

public class MethodParamMold extends AbstractMethodParamMold<AlexandriaUiBox> {
    public boolean addComma;

    public MethodParamMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        Parameter parameter = item();
        name.value(parameter.name());
        type.value(parameter.type());
        if (addComma) comma.value(", ");
    }

}