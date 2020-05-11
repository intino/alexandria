package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;

import java.util.List;

public class SelectorExamplesMold extends AbstractSelectorExamplesMold<UiFrameworkBox> {

    public SelectorExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        selector1.onSelect(e -> notify(e.selection()));
        selector2.onSelect(e -> notify(e.selection()));
        selector3.onSelect(e -> notify(e.selection()));
        selector4.onSelect(e -> notify(e.selection()));
        selector5.onSelect(e -> notify(e.selection()));
        selector5.select("option 3");
    }

    private void notify(List<String> selection) {
        if (selection.size() <= 0) {
            selector1.notifyUser("no option selected", UserMessage.Type.Info);
            return;
        }
        selector1.notifyUser(String.format("%s option selected", selection.get(0)), UserMessage.Type.Info);
    }

}