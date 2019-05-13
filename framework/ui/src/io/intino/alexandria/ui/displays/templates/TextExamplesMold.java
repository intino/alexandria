package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

public class TextExamplesMold extends AbstractTextExamplesMold<UiFrameworkBox> {

    public TextExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        text1.update("lorem ipsum");
        text2.update("lorem ipsum");
        text3.update("lorem ipsum");
    }

}