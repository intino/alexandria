package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;

public class BlockExamplesMold extends AbstractBlockExamplesMold<UiFrameworkBox> {

    public BlockExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        block2Code.onChange(event -> block2.spacing(event.<String>value().replace("DP", "")));
        block3Code.onChange(event -> block3.layout(event.value()));
    }
}