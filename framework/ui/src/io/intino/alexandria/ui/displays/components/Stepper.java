package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.StepperInfo;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.StepperNotifier;

import java.util.List;

public class Stepper<DN extends StepperNotifier, B extends Box> extends AbstractStepper<B> {
    private Listener finishListener = null;
    protected int active = 0;

    public Stepper(B box) {
        super(box);
    }

    public Stepper onFinish(Listener listener){
        this.finishListener = listener;
        return this;
    }

    @Override
    public void init() {
        super.init();
        updateActive();
    }

    @Override
    public <D extends Display> D register(D child) {
        D registered = super.register(child);
        updateSteps();
        return registered;
    }

    public void add(Step step) {
        add(step, "steps");
    }

    protected List<Step> steps() {
        return children(Step.class);
    }

    private void updateSteps() {
        int index = 0;
        for (Step step : steps()) {
            step._isActive(index == active);
            step._isCompleted(index < active);
            step.refresh();
            if (index == active) step.show();
            index++;
        }
    }

    public void reset() {
        this.active = 0;
        updateActive();
    }

    public void next() {
        if (allowNext()) {
            this.active++;
            updateActive();
            updateSteps();
            checkFinish();
        }
    }

    public void back() {
        if (allowBack()){
            this.active--;
            updateActive();
            updateSteps();
        }
    }

    private boolean allowNext(){
        return inRange() && steps().get(this.active).allowNext();
    }

    private boolean allowBack(){
        return this.active > 0 && (!inRange() || steps().get(this.active).allowBack());
    }

    private boolean inRange(){
        return this.active > -1 && this.active < steps().size();
    }

    private boolean isFinished() {
        return this.active >= steps().size();
    }

    private void checkFinish() {
        if (isFinished() && finishListener != null) finishListener.accept(new Event(this));
    }

    private void updateActive() {
        notifier.refresh(new StepperInfo().active(active).allowNext(allowNext()).allowBack(allowBack()));
    }
}