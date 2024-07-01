package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

public class AppDirectoryExamplesMold extends AbstractAppDirectoryExamplesMold<UiFrameworkBox> {

    public AppDirectoryExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        appDirectory2.selected("Google");
        appDirectory2.refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        appDirectory2.refresh();
    }
}