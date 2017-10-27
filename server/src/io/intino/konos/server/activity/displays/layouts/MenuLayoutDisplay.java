package io.intino.konos.server.activity.displays.layouts;

import io.intino.konos.Box;

public class MenuLayoutDisplay<DN extends LayoutDisplayNotifier> extends LayoutDisplay<DN> {

    public MenuLayoutDisplay(Box box) {
        super(box);
    }

    @Override
    public void reset() {
    }

    @Override
    protected void showDialog() {
    }

    @Override
    protected void currentItem(String id) {
    }

    @Override
    protected io.intino.konos.server.activity.displays.elements.model.Item currentItem() {
        return null;
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