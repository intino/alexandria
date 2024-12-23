package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class BlockExamplesMold extends AbstractBlockExamplesMold<AlexandriaUiBox> {

    public BlockExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();

        myIcon.title("Hola soy RL");

        block2Code.onChange(event -> block2.spacing(event.<String>value().replace("DP", "")));
        block3Code.onChange(event -> block3.layout(event.value()));
        openBlock2.onExecute(event -> splitterBlock.show(splitBlock2));
//        drawerBlock1.onToggle()
    }

}