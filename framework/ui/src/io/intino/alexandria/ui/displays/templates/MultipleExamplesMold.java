package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

import java.util.List;

public class MultipleExamplesMold extends AbstractMultipleExamplesMold<UiFrameworkBox> {

    public MultipleExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        multiple1.addAll(List.of("Value 1", "Value 2", "Value 3"));
        multiple2.add("Value 1", "lorem ipsum dolor sit amet");
        multiple2.add("Value 2");
        multiple2.add("Value 3");
    }
}