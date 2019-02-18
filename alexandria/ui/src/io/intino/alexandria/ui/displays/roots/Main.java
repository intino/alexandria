package io.intino.alexandria.ui.displays.roots;

import io.intino.alexandria.UiFrameworkBox;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        value.update("soy un valor de campo");
        valueInput.update("campo value editable");
    }
}