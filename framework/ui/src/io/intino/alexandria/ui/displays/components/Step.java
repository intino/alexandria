package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.stepper.StepChecker;
import io.intino.alexandria.ui.displays.notifiers.StepNotifier;

public class Step<DN extends StepNotifier, B extends Box> extends AbstractStep<B> {
    private StepChecker nextChecker = null;
    private StepChecker backChecker = null;
    private String icon;
    private boolean isActive = false;
    private boolean isCompleted = false;
    private boolean isDisabled = false;

    public Step(B box) {
        super(box);
    }

    public void icon(String icon) {
        _icon(icon);
        refresh();
    }

    protected Step _icon(String icon) {
        this.icon = icon;
        return this;
    }

    protected Step _isActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    protected Step _isDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
        return this;
    }

    protected Step _isCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public Step canNext(StepChecker checker){
        this.nextChecker = checker;
        return this;
    }

    public Step canBack(StepChecker checker){
        this.backChecker = checker;
        return this;
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    public void contentRendered() {
        children().forEach(Display::refresh);
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.refresh(
            new StepInfo()
                .icon(this.icon)
                .isActive(this.isActive)
                .isCompleted(this.isCompleted)
                .isDisabled(this.isDisabled)
        );
    }

    protected boolean allowNext(){
        return this.nextChecker == null || this.nextChecker.check(this);
    }

    protected boolean allowBack() {
        return this.backChecker == null || this.backChecker.check(this);
    }
}