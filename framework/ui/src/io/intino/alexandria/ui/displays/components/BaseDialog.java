package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.DialogSize;
import io.intino.alexandria.ui.displays.events.BeforeListener;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.BaseDialogNotifier;

import java.util.ArrayList;

public class BaseDialog<DN extends BaseDialogNotifier, B extends Box> extends AbstractBaseDialog<DN, B> {
    private String title;
    private DialogSize size;
    private java.util.List<BeforeListener> beforeOpenListeners = new ArrayList<>();
    private java.util.List<Listener> openListeners = new ArrayList<>();
    private java.util.List<Listener> closeListeners = new ArrayList<>();

    public BaseDialog(B box) {
        super(box);
    }

    public BaseDialog onBeforeOpen(BeforeListener listener) {
        beforeOpenListeners.add(listener);
        return this;
    }

    public BaseDialog onOpen(Listener listener) {
        openListeners.add(listener);
        return this;
    }

    public BaseDialog onClose(Listener listener) {
        closeListeners.add(listener);
        return this;
    }

    public void open() {
        if (!notifyBeforeOpen()) return;
        notifier.open();
        notifyOpen();
    }

    public void close() {
        notifier.close();
        notifyClose();
    }

    public BaseDialog<DN, B> title(String title) {
        _title(title);
        notifier.refreshTitle(title);
        return this;
    }

    public BaseDialog<DN, B> relativeWidth(int width) {
        return relativeSize(width, 100);
    }

    public BaseDialog<DN, B> relativeHeight(int height) {
        return relativeSize(100, height);
    }

    public BaseDialog<DN, B> relativeSize(int width, int height) {
        if (width > 100) width = 100;
        if (height > 100) height = 100;
        return size(width + "%", height + "%");
    }

    public BaseDialog<DN, B> absoluteWidth(int width) {
        return size(width + "px", "100%");
    }

    public BaseDialog<DN, B> absoluteHeight(int height) {
        return size("100%", height + "px");
    }

    public BaseDialog<DN, B> absoluteSize(int width, int height) {
        return size(width + "px", height + "px");
    }

    public BaseDialog<DN, B> size(String width, String height) {
        _size(new DialogSize().width(width).height(height));
        notifier.refreshSize(size);
        return this;
    }

    protected BaseDialog<DN, B> _title(String title) {
        this.title = title;
        return this;
    }

    protected BaseDialog<DN, B> _size(DialogSize size) {
        this.size = size;
        return this;
    }

    private boolean notifyBeforeOpen() {
        return beforeOpenListeners.stream().allMatch(l -> l.accept(new Event(this)));
    }

    private void notifyOpen() {
        openListeners.forEach(l -> l.accept(new Event(this)));
    }

    private void notifyClose() {
        closeListeners.forEach(l -> l.accept(new Event(this)));
    }
}