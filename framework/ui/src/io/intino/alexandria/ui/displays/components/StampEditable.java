package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.StampEditableNotifier;

public class StampEditable<DN extends StampEditableNotifier, B extends Box> extends AbstractStampEditable<B> implements Editable<DN, B> {
    private boolean readonly;
    private ChangeListener changeListener = null;

    public StampEditable(B box) {
        super(box);
    }

    @Override
    public boolean readonly() {
        return readonly;
    }

    @Override
    public void init() {
        super.init();
        children().stream().filter(c -> c instanceof Editable).forEach(c -> ((Editable<?,?>)c).onChange(this::notifyChange));
    }

    @Override
    public void reload() {
        children().stream().filter(c -> c instanceof Editable).forEach(c -> ((Editable<?,?>)c).reload());
    }

    @Override
    public StampEditable<DN, B> readonly(boolean readonly) {
        this.readonly = readonly;
        children().stream().filter(c -> c instanceof Editable).forEach(c -> ((Editable<?,?>)c).readonly(readonly));
        return this;
    }

    @Override
    public StampEditable<DN, B> onChange(ChangeListener listener) {
        this.changeListener = listener;
        return this;
    }

    private void notifyChange(ChangeEvent event) {
        if (changeListener == null) return;
        changeListener.accept(new ChangeEvent(this, event.value()));
    }
}