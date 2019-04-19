package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Parameter;

import java.util.List;

public class MethodMold extends AbstractMethodMold<UiFrameworkBox> {

    public MethodMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        Method method = item();
        name.update(method.name());
        addParameters();
        if (method.facets() != null) facets.update(String.join(", ", method.facets()));
        description.update(method.description());
        returnType.update(method.returnType());
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