package io.intino.alexandria.ui.displays.roots;

import io.intino.alexandria.UiFrameworkBox;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        title.update("Alexandria widgets");
        value.update("soy un valor de campo");
        valueEditable.update("campo value editable");
        valueEditable.onChange((e) -> System.out.println((String) e.value()));
    }
}