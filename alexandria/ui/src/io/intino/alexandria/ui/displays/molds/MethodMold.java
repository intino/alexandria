package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Parameter;

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

    private MethodParamMold addParameterMold(Parameter parameter, boolean addComma) {
        MethodParamMold mold = new MethodParamMold(box());
        mold.parameter = parameter;
        mold.addComma = addComma;
        params.addParamMold(mold);
        mold.refresh();
        return mold;
    }
}