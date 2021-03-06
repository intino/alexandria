package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Parameter;
import io.intino.alexandria.ui.AlexandriaUiBox;

import java.util.List;

public class MethodMold extends AbstractMethodMold<AlexandriaUiBox> {

    public MethodMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        Method method = item();
        name.value(method.name());
        addParameters();
        if (method.facets() != null) facets.value(String.join(", ", method.facets()));
        description.value(method.description());
        returnType.value(method.returnType());
    }

    private void addParameters() {
        Method method = item();
        List<Parameter> params = method.params();
        for (int i = 0; i < params.size(); i++) {
            MethodParamMold mold = this.params.add(params.get(i));
            mold.addComma = i != params.size()-1;
            mold.refresh();
        }
    }

}