package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;

public class BlockExamplesMold extends AbstractBlockExamplesMold<AlexandriaUiBox> {

    public BlockExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
//        drawerBlock1.onToggle()
        block2Code.onChange(event -> block2.spacing(event.<String>value().replace("DP", "")));
        block3Code.onChange(event -> block3.layout(event.value()));
        openBlock2.onExecute(event -> splitterBlock.show(splitBlock2));
        resizableBlock.onLayoutChanged(e -> notifyUser("New sizes: " + e.layout(), UserMessage.Type.Info));
    }

}