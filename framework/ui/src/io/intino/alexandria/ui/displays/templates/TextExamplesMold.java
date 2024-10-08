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
        text1.value("lorem ipsum");
        text2.value("lorem ipsum");
        text3.value("lorem ipsum");
        text3.focus();
        text3.onFocus((event) -> text3.notifyUser("Field focused", UserMessage.Type.Info));
        text3.onBlur((event) -> text3.notifyUser("Field losses focus", UserMessage.Type.Info));
        text3.onChange((event) -> text3.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
        text4.onChange((event) -> text3.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
        text5.error("Field value is wrong");
        textCode2.onChange((event) -> textCode2.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
    }

}