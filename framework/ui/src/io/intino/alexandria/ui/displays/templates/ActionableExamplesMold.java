package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.components.actionable.SignInfoProvider;

public class ActionableExamplesMold extends AbstractActionableExamplesMold<AlexandriaUiBox> {
    private String reason;

    public ActionableExamplesMold(AlexandriaUiBox box) {
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
        operation5.onBeforeAffirmed(e -> true);
        operation5.onCancelAffirmed(e -> operation5.notifyUser("User canceled operation"));
        operation6.onExecute((event) -> operation6.notifyUser("User clicked operation"));
        operation7.onExecute((event) -> operation7.notifyUser("User clicked operation"));
        operation8.onToggle((event) -> operation7.notifyUser("Edition state to " + event.state().name()));
        operation9.signChecker((sign, reason) -> sign.equals("1234"));
        operation9.onExecute(e -> operation9.notifyUser("User clicked operation"));
        operation10.signChecker((sign, reason) -> { this.reason = reason; return sign.equals("1234"); });
        operation10.onExecute(e -> operation10.notifyUser("User clicked operation. Reason: " + reason));
        operation11.onToggle((event) -> operation11.notifyUser("Edition state to " + event.state().name()));
        operation12.onToggle((event) -> operation12.notifyUser("Edition state to " + event.state().name()));
        operation13.onToggle((event) -> operation13.notifyUser("Edition state to " + event.state().name()));
        operation14.onExecute((event) -> operation14.notifyUser("User clicked operation " + event.option()));
        operation15.onExecute((event) -> operation15.notifyUser("User clicked operation " + event.option()));
        operation16.onExecute((event) -> operation16.notifyUser("User clicked operation"));
        operation18.signInfoProvider(signInfoProvider());
        operation18.onExecute((event) -> operation18.notifyUser("User clicked operation"));
    }

    private SignInfoProvider signInfoProvider() {
        return new SignInfoProvider() {
            @Override
            public String company() {
                return "Company";
            }

            @Override
            public String email() {
                return "info@company.com";
            }

            @Override
            public String secret() {
                return "N2NHKZQ6N2GPVX7EAQYYFUAHT6MO2S7P";
            }
        };
    }

}