package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaMoldNotifier;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Mold;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;

public abstract class AlexandriaMold<DN extends AlexandriaMoldNotifier> extends AlexandriaElementDisplay<Mold, DN> {

    public AlexandriaMold(Box box) {
        super(box);
    }

    @Override
    public void reset() {
    }

    @Override
    protected void showDialogBox() {
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
    protected void createPanel(CreatePanelParameters params) {
    }

    @Override
    protected void showPanel() {
    }

    @Override
    protected void hidePanel() {
    }

    @Override
    protected void showOperationMessage(String message) {
    }

}