package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.TemplateNotifier;

public class Template<DN extends TemplateNotifier, Item, B extends Box> extends Component<DN, B> {
    private Item item;
    private boolean closeManagerEnabled = false;

    public Template(B box) {
        super(box);
    }

    public void value(Item item) {
        item(item);
    }

    public void item(Item item) {
        _item(item);
        refresh();
    }

    public Item item() {
        return item;
    }

    public void canClose(boolean value) {
        enableCloseManager();
        notifier.canClose(value);
    }

    protected Template<DN, Item, B> _item(Item item) {
        this.item = item;
        return this;
    }

    private void enableCloseManager() {
        if (closeManagerEnabled) return;
        notifier.enableCloseManager();
        closeManagerEnabled = true;
    }

}