package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.addressable.Addressable;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.ActionListener;
import io.intino.alexandria.ui.displays.notifiers.ActionNotifier;

public class Action<DN extends ActionNotifier, B extends Box> extends AbstractAction<DN, B> implements Addressable {
    private ActionListener actionListener;
    private String path;
    private String address;

    public Action(B box) {
        super(box);
    }

    public void onExecute(ActionListener listener) {
        this.actionListener = listener;
    }

    public void execute() {
        if (this.actionListener == null) return;
        this.actionListener.accept(new Event(this));
    }

    @Override
    public void init() {
        super.init();
        if (validAddress()) notifier.addressed(address);
    }

    public String path() {
        return this.path;
    }

    protected Action<DN, B> _path(String path) {
        this.path = path;
        this._address(path);
        return this;
    }

    protected Action<DN, B> _address(String address) {
        this.address = address;
        return this;
    }

    protected void address(String value) {
        this._address(value);
        notifier.addressed(address);
    }

    private boolean validAddress() {
        return address != null && !address.contains(":");
    }

}