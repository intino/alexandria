package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

public class MicroSiteExamplesMold extends AbstractMicroSiteExamplesMold<UiFrameworkBox> {

    public MicroSiteExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    public void page(String page) {
        site.page(page);
        site.refresh();
    }
}