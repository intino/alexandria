package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaMoldNotifier;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Mold;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;

public abstract class AlexandriaMold<DN extends AlexandriaMoldNotifier> extends AlexandriaElementDisplay<Mold, DN> {

    public AlexandriaMold(Box box) {
        super(box);
    }

    @Override
    public void reset() {
    }

    @Override
    public void notifyUser(String message) {
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

}