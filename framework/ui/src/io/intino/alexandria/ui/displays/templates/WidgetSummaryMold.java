package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;

public class WidgetSummaryMold extends AbstractWidgetSummaryMold<AlexandriaUiBox> {

    public WidgetSummaryMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        if (this.item() == null) return;
        Widget item = item();
        updateTitle();
        description.value(translate(item.description()));
        facets.addAll(item.facets());
    }

    private void updateTitle() {
        String simpleName = item().getClass().getSimpleName().replace("Widget", "");
        title.title(translate(simpleName));
        title.path("/docs/data/" + simpleName.toLowerCase());
    }

}