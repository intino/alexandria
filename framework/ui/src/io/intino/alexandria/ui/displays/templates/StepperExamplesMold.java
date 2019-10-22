package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.stepper.StepChecker;

public class StepperExamplesMold extends AbstractStepperExamplesMold<UiFrameworkBox> {

    public StepperExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();

        stepper1.onFinish(event -> stepper1.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper1.step1.canNext(nextChecker()).canBack(backChecker());
        stepper1.step2.canNext(nextChecker()).canBack(backChecker());
        stepper1.step3.canNext(nextChecker()).canBack(backChecker());

        stepper2.onFinish(event -> stepper2.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper2.step4.canNext(nextChecker()).canBack(backChecker());
        stepper2.step5.canNext(nextChecker()).canBack(backChecker());

        stepper3.onFinish(event -> stepper3.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper3.step6.canNext(nextChecker()).canBack(backChecker());
        stepper3.step7.canNext(nextChecker()).canBack(backChecker());

        stepper4.onFinish(event -> stepper4.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper4.step8.canNext(nextChecker()).canBack(backChecker());
        stepper4.step9.canNext(nextChecker()).canBack(backChecker());

        stepper5.onFinish(event -> stepper5.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper5.step10.canNext(nextChecker()).canBack(backChecker());
        stepper5.step11.canNext(nextChecker()).canBack(backChecker());
    }

    private StepChecker nextChecker() {
        return step -> { notifyUser(step.label() + " terminado", UserMessage.Type.Info); return true; };
    }

    private StepChecker backChecker() {
        return step -> { notifyUser(step.label() + " retrasado", UserMessage.Type.Info); return true; };
    }
}