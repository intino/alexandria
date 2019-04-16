package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.molds.MethodParamMold;
import io.intino.alexandria.ui.displays.templates.AbstractMethodMold;

public class MethodMold extends AbstractMethodMold<UiFrameworkBox> {

    public MethodMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        name.update(method.name());
        addParameters();
        if (method.facets() != null) facets.update(String.join(", ", method.facets()));
        description.update(method.description());
        returnType.update(method.returnType());
    }

    private void addParameters() {
        for (int i = 0; i < method.params().size(); i++)
            addParameterMold(method.params().get(i), i != method.params().size()-1);
    }

    private io.intino.alexandria.ui.displays.molds.MethodParamMold addParameterMold(Parameter parameter, boolean addComma) {
        io.intino.alexandria.ui.displays.molds.MethodParamMold mold = new MethodParamMold(box());
        mold.parameter = parameter;
        mold.addComma = addComma;
        params.addParamMold(mold);
        mold.refresh();
        return mold;
    }

}