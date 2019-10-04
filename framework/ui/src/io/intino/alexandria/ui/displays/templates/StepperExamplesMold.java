package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.stepper.StepChecker;
import io.intino.alexandria.ui.displays.templates.AbstractStepperExamplesMold;

public class StepperExamplesMold extends AbstractStepperExamplesMold<UiFrameworkBox> {

    public StepperExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();

        stepper1.onFinish(event -> stepper1.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper1.step1.onNext(nextChecker()).onBack(backChecker());
        stepper1.step2.onNext(nextChecker()).onBack(backChecker());
        stepper1.step3.onNext(nextChecker()).onBack(backChecker());

        stepper2.onFinish(event -> stepper2.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper2.step4.onNext(nextChecker()).onBack(backChecker());
        stepper2.step5.onNext(nextChecker()).onBack(backChecker());

        stepper3.onFinish(event -> stepper3.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper3.step6.onNext(nextChecker()).onBack(backChecker());
        stepper3.step7.onNext(nextChecker()).onBack(backChecker());

        stepper4.onFinish(event -> stepper4.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper4.step8.onNext(nextChecker()).onBack(backChecker());
        stepper4.step9.onNext(nextChecker()).onBack(backChecker());

        stepper5.onFinish(event -> stepper5.notifyUser("Stepper finalizado", UserMessage.Type.Info));
        stepper5.step10.onNext(nextChecker()).onBack(backChecker());
        stepper5.step11.onNext(nextChecker()).onBack(backChecker());
    }

    private StepChecker nextChecker() {
        return step -> { notifyUser(step.label() + " terminado", UserMessage.Type.Info); return true; };
    }

    private StepChecker backChecker() {
        return step -> { notifyUser(step.label() + " retrasado", UserMessage.Type.Info); return true; };
    }
}