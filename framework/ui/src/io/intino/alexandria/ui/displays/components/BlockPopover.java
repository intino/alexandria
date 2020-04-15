package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.AbstractBlockPopover;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.events.actionable.ToggleEvent;
import io.intino.alexandria.ui.displays.events.actionable.ToggleListener;
import io.intino.alexandria.ui.displays.notifiers.BlockPopoverNotifier;

import java.util.ArrayList;

public class BlockPopover<DN extends BlockPopoverNotifier, B extends Box> extends AbstractBlockPopover<B> {
    private java.util.List<OpenListener> openListeners = new ArrayList<>();

    public BlockPopover(B box) {
        super(box);
    }

    public BlockPopover<DN, B> onOpen(OpenListener listener) {
        this.openListeners.add(listener);
        return this;
    }

    public BlockPopover<DN, B> open(String triggerId) {
        update(triggerId);
        return this;
    }

    public BlockPopover<DN, B> close() {
        update(null);
        return this;
    }

    private void update(String triggerId) {
        notifier.refresh(triggerId);
        openListeners.forEach(l -> l.accept(new Event(this)));
    }
}