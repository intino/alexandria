package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;

public class DialogExamplesMold extends AbstractDialogExamplesMold<UiFrameworkBox> {

    public DialogExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        dialog2.selector1.onSelect((event -> {
            String option = (String) event.selection().get(0);
            dialog2.notifyUser("Se ha seleccionado la opción " + option, UserMessage.Type.Info);
        }));
    }
}