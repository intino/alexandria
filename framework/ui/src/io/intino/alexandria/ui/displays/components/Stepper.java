package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.StepperInfo;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.StepperNotifier;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        select(0);
    }

    public void select(Step step) {
        select(posOf(step));
    }

    public void select(int pos) {
        this.active = pos;
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
        return visiblePosOfActive() < visibleStepsCount()-1 && steps().get(this.active).allowNext();
    }

    private boolean allowBack() {
        return visiblePosOfActive() > 0 && steps().get(this.active).allowBack();
    }

    private int visiblePosOfActive() {
        if (steps().size() <= 0) return 0;
        int pos = -1;
        List<Boolean> visibleSteps = visibleSteps();
        for (int i=0; i<=this.active; i++) if (visibleSteps.get(i)) pos++;
        return pos;
    }

    private boolean isFinished() {
        return this.active >= visibleStepsCount();
    }

    private void checkFinish() {
        if (isFinished() && finishListener != null) finishListener.accept(new Event(this));
    }

    private void updateActive() {
        notifier.refresh(new StepperInfo().active(active).allowNext(allowNext()).allowBack(allowBack()).visibleList(visibleSteps()));
    }

    private List<Boolean> visibleSteps() {
        return steps().stream().map(Component::isVisible).collect(toList());
    }

    private long visibleStepsCount() {
        return steps().stream().filter(Component::isVisible).count();
    }

    private int posOf(Step step) {
        List<Step> steps = steps();
        for (int i = 0; i< steps.size(); i++) if (steps.get(i) == step) return i;
        return -1;
    }

}