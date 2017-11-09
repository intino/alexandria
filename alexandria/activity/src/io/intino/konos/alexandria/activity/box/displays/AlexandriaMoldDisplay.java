package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.notifiers.AlexandriaMoldDisplayNotifier;
import io.intino.konos.alexandria.activity.box.model.Item;
import io.intino.konos.alexandria.activity.box.model.Mold;

public abstract class AlexandriaMoldDisplay<DN extends AlexandriaMoldDisplayNotifier> extends AlexandriaElementDisplay<Mold, DN> {

    public AlexandriaMoldDisplay(Box box) {
        super(box);
    }

    @Override
    public void reset() {
    }

    @Override
    protected void showDialog() {
    }

    @Override
    protected Item currentItem() {
        return null;
    }

    @Override
    protected void currentItem(String id) {
    }

    @Override
    protected void notifyFiltered(boolean value) {
    }

    @Override
    protected void refreshBreadcrumbs(String breadcrumbs) {
    }

    @Override
    protected void createPanel(String item) {
    }

    @Override
    protected void showPanel() {
    }

    @Override
    protected void hidePanel() {
    }
}