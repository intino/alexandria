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
        name.update(parameter.name());
        type.update(parameter.type());
        if (addComma) comma.update(", ");
    }

}