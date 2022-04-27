package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

public class BoardExamplesMold extends AbstractBoardExamplesMold<UiFrameworkBox> {

    public BoardExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        board2.selected("Google");
        board2.refresh();
    }
}