package io.intino.alexandria.ui.displays.roots;

import io.intino.alexandria.UiFrameworkBox;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        block.value.update("soy un valor de campo");
        block.valueInput.update("campo value editable");
        block.valueInput.onChange((e) -> System.out.println((String) e.value()));
    }
}