package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.TemplateStampEditableNotifier;

public class TemplateStampEditable<DN extends TemplateStampEditableNotifier, B extends Box> extends AbstractTemplateStampEditable<B> implements Editable<DN, B> {
    private boolean readonly;
    private ChangeListener changeListener = null;

    public TemplateStampEditable(B box) {
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
    public TemplateStampEditable<DN, B> readonly(boolean readonly) {
        this.readonly = readonly;
        children().stream().filter(c -> c instanceof Editable).forEach(c -> ((Editable<?,?>)c).readonly(readonly));
        return this;
    }

    @Override
    public TemplateStampEditable<DN, B> onChange(ChangeListener listener) {
        this.changeListener = listener;
        return this;
    }

    private void notifyChange(ChangeEvent event) {
        if (changeListener == null) return;
        changeListener.accept(new ChangeEvent(this, event.value()));
    }
}