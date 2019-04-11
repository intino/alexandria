package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.I18n;

public class WidgetSummaryMold extends AbstractWidgetSummaryMold<UiFrameworkBox> {

    public WidgetSummaryMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        if (this.widget == null) return;
        updateTitle();
        description.update(I18n.translate(widget.description(), language()));
        facets.addAll(widget.facets());
    }

    private void updateTitle() {
        String simpleName = widget.getClass().getSimpleName().replace("Widget", "");
        title.title(I18n.translate(simpleName, language()));
        title.path("/docs/data/" + simpleName.toLowerCase());
        title.refresh();
    }
}