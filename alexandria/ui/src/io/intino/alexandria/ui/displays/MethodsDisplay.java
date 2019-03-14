package io.intino.alexandria.ui.displays;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Method;

import java.util.ArrayList;
import java.util.List;

public class MethodsDisplay extends AbstractMethodsDisplay<UiFrameworkBox> {
    private List<Method> methodList = new ArrayList<>();

    public MethodsDisplay(UiFrameworkBox box) {
        super(box);
    }

    public MethodsDisplay methods(List<Method> methodList) {
        this.methodList = methodList;
        return this;
    }

    @Override
    public void init() {
        super.init();
        notifier.refresh(methodList);
    }
}