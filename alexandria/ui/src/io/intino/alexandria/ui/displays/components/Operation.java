package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.OperationInfo;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.OperationNotifier;

public class Operation<DN extends OperationNotifier, B extends Box> extends Component<DN, B> {
    private String title;
    private boolean disabled = true;

    public Operation(B box) {
        super(box);
    }

    public String title() {
        return title;
    }

    public Operation<DN, B> title(String title) {
        this.title = title;
        return this;
    }

    public boolean disabled() {
        return disabled;
    }

    public Operation<DN, B> disabled(boolean value) {
        this.disabled = value;
        return this;
    }

    public void refresh() {
        notifier.refresh(new OperationInfo().title(title()).disabled(disabled()));
    }

    public void execute() {
    }

}