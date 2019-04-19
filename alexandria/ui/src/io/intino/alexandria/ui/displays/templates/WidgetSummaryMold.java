package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.I18n;

public class WidgetSummaryMold extends AbstractWidgetSummaryMold<UiFrameworkBox> {

    public WidgetSummaryMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        if (this.item() == null) return;
        Widget item = item();
        updateTitle();
        description.update(I18n.translate(item.description(), language()));
        facets.addAll(item.facets());
    }

    private void updateTitle() {
        String simpleName = item().getClass().getSimpleName().replace("Widget", "");
        title.title(I18n.translate(simpleName, language()));
        title.path("/docs/data/" + simpleName.toLowerCase());
        title.refresh();
    }

}