package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;

public class NumberExamplesMold extends AbstractNumberExamplesMold<AlexandriaUiBox> {

    public NumberExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        number1.countDecimals(10);
        number3.onChange((event) -> number3.notifyUser(String.format("Valor: %.0f", (double)event.value()), UserMessage.Type.Info));
        number3.focus();
        number7.error("Field value is wrong");
    }

}