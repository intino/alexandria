package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.addressable.Addressable;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.TaskListener;
import io.intino.alexandria.ui.displays.notifiers.TaskNotifier;

public class Task<DN extends TaskNotifier, B extends Box> extends AbstractTask<DN, B> implements Addressable {
    private TaskListener taskListener;
    private String path;
    private String address;

    public Task(B box) {
        super(box);
    }

    public void onExecute(TaskListener listener) {
        this.taskListener = listener;
    }

    public void execute() {
        if (this.taskListener == null) return;
        this.taskListener.accept(new Event(this));
    }

    @Override
    public void init() {
        super.init();
        if (validAddress()) notifier.addressed(address);
    }

    public String path() {
        return this.path;
    }

    protected Task<DN, B> _path(String path) {
        this.path = path;
        this._address(path);
        return this;
    }

    protected Task<DN, B> _address(String address) {
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