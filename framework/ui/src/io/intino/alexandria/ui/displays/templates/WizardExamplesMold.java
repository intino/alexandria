package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.wizard.StepChecker;

public class WizardExamplesMold extends AbstractWizardExamplesMold<UiFrameworkBox> {

    public WizardExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();

        wizard1.onFinish(event -> wizard1.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        wizard1.step1.canNext(nextChecker()).canBack(backChecker());
        wizard1.step2.canNext(nextChecker()).canBack(backChecker());
        wizard1.step3.canNext(nextChecker()).canBack(backChecker());
        wizard1.reset();

        wizard2.onFinish(event -> wizard2.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        wizard2.step4.canNext(nextChecker()).canBack(backChecker());
        wizard2.step5.canNext(nextChecker()).canBack(backChecker());
        wizard2.reset();

        wizard3.onFinish(event -> wizard3.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        wizard3.step6.canNext(nextChecker()).canBack(backChecker());
        wizard3.step7.canNext(nextChecker()).canBack(backChecker());
        wizard3.reset();

        wizard4.onFinish(event -> wizard4.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        wizard4.step8.canNext(nextChecker()).canBack(backChecker());
        wizard4.step9.canNext(nextChecker()).canBack(backChecker());
        wizard4.reset();

        wizard5.onFinish(event -> wizard5.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        wizard5.step10.canNext(nextChecker()).canBack(backChecker());
        wizard5.step11.canNext(nextChecker()).canBack(backChecker());
        wizard5.reset();

        wizard6.onFinish(event -> wizard6.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        wizard6.step12.canNext(nextChecker()).canBack(backChecker());
        wizard6.step13.canNext(nextChecker()).canBack(backChecker());
        wizard6.reset();
    }

    private StepChecker nextChecker() {
        return step -> true;
    }

    private StepChecker backChecker() {
        return step -> true;
    }
}