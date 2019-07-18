package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class OperationExamplesMold extends AbstractOperationExamplesMold<AlexandriaUiBox> {

    public OperationExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        operation1.onExecute((event) -> operation1.notifyUser("User clicked operation"));
        operation2.onExecute((event) -> operation2.notifyUser("User clicked operation"));
        operation3.onExecute((event) -> operation3.notifyUser("User clicked operation"));
        operation4.onExecute((event) -> operation4.notifyUser("User clicked operation"));
        operation5.onExecute((event) -> operation5.notifyUser("User clicked operation"));
        operation6.onExecute((event) -> operation6.notifyUser("User clicked operation"));
        operation7.onExecute((event) -> operation7.notifyUser("User clicked operation"));
    }
}