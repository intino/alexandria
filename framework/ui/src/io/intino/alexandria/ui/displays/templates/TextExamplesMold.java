package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;

public class TextExamplesMold extends AbstractTextExamplesMold<AlexandriaUiBox> {

    public TextExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        text1.update("lorem ipsum");
        text2.update("lorem ipsum");
        text3.update("lorem ipsum");
        text3.onChange((event) -> text3.notifyUser(String.format("Valor: %s", (String)event.value()), UserMessage.Type.Info));
        textCode2.onChange((event) -> textCode2.notifyUser(String.format("Valor: %s", (String)event.value()), UserMessage.Type.Info));
    }

}