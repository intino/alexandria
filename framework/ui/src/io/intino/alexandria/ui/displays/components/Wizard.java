package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.WizardInfo;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.StepEvent;
import io.intino.alexandria.ui.displays.events.StepListener;
import io.intino.alexandria.ui.displays.notifiers.WizardNotifier;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Wizard<DN extends WizardNotifier, B extends Box> extends AbstractWizard<B> {
    private List<StepListener> stepArrivalListeners = new ArrayList<>();
    private List<Listener> finishListeners = new ArrayList<>();
    protected int active = 0;
    private boolean finished = false;

    public Wizard(B box) {
        super(box);
    }

    public Wizard<DN, B> onStepArrival(StepListener listener) {
        this.stepArrivalListeners.add(listener);
        return this;
    }

    public Wizard<DN, B> onFinish(Listener listener) {
        this.finishListeners.add(listener);
        return this;
    }

    public boolean allowBack() {
        return active >= 1;
    }

    public boolean canBack() {
        return canBack(active);
    }

    public boolean allowNext() {
        return active < stepsCount() - 1;
    }

    public boolean canNext() {
        return canNext(active);
    }

    public boolean allowFinish() {
        return active == stepsCount()-1;
    }

    public boolean canFinish() {
        return canFinish(active);
    }

    public boolean finished() {
        return finished;
    }

    @Override
    public <D extends Display> D register(D child) {
        D registered = super.register(child);
        if (child instanceof Step) ((Step)child).visible(true);
        refreshInfo();
        refreshSteps();
        return registered;
    }

    public void add(Step step) {
        add(step, "steps");
    }

    public void reset() {
        finished = false;
        select(0);
    }

    public void select(Step step) {
        select(posOf(step));
    }

    public void select(int pos) {
        this.active = pos;
        this.finished = false;
        refreshInfo();
        refreshSteps();
        notifyStepArrival();
    }

    public void back() {
        if (!canBack()) return;
        doBack();
        select(active);
    }

    public void next() {
        if (!canNext()) return;
        doNext();
        select(active);
    }

    public void finish() {
        notifier.showConfirmDialog();
    }

    public void finishConfirmed() {
        doFinish();
    }

    private void doFinish() {
        finished = true;
        refreshSteps();
        finishListeners.forEach(l -> l.accept(new Event(this)));
    }

    protected List<Step> steps() {
        return children(Step.class);
    }

    protected int active() {
        return active;
    }

    protected boolean canNext(int index) {
        return index < stepsCount()-1 && stepOf(index).allowNext();
    }

    protected boolean canFinish(int index) {
        return index == stepsCount()-1 && !finishListeners.isEmpty();
    }

    protected boolean canBack(int index) {
        return index > 0 && stepOf(index).allowBack();
    }

    private void doNext() {
        List<Boolean> visibility = stepsVisibility();
        do this.active++;
        while (this.active < visibility.size() && !visibility.get(this.active));
    }

    private void doBack() {
        finished = false;
        List<Boolean> visibility = stepsVisibility();
		do this.active--;
		while (this.active > 0 && !visibility.get(this.active));
    }

    private void refreshInfo() {
        notifier.refresh(new WizardInfo().active(active).allowBack(allowBack()).allowNext(allowNext()).allowFinish(allowFinish()).visibleList(stepsVisibility()));
    }

    private List<Boolean> stepsVisibility() {
        return steps().stream().map(Component::isVisible).collect(toList());
    }

    private int stepsCount() {
        return (int) steps().stream().filter(Component::isVisible).count();
    }

    private Step stepOf(int index) {
        return steps().get(index);
    }

    private int posOf(Step<?, ?> step) {
        List<Step> steps = steps();
        for (int i = 0; i< steps.size(); i++) if (steps.get(i) == step) return i;
        return -1;
    }

    private void notifyStepArrival() {
        stepArrivalListeners.forEach(l -> l.accept(new StepEvent(this, stepOf(active))));
    }

    private void refreshSteps() {
        int index = 0;
        for (Step<?, ?> step : steps()) {
            step._isActive(index == active);
            step._isCompleted(index < active || finished);
            if (index == active) step.show();
            step.refresh();
            index++;
        }
    }

}