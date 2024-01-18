package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.documentation.DisplayHelper;

import java.util.function.Consumer;

public class WidgetSummaryMold extends AbstractWidgetSummaryMold<AlexandriaUiBox> {
    private Consumer<Widget> selectListener;

    public WidgetSummaryMold(AlexandriaUiBox box) {
        super(box);
    }

    public WidgetSummaryMold onSelect(Consumer<Widget> listener) {
        this.selectListener = listener;
        return this;
    }

    @Override
    public void init() {
        super.init();
        title.onExecute(e -> selectListener.accept(item()));
    }

    @Override
    public void refresh() {
        if (this.item() == null) return;
        Widget item = item();
        updateTitle();
        description.value(translate(item.description()));
        facets.clear();
        facets.addAll(item.facets());
    }

    private void updateTitle() {
        title.title(DisplayHelper.label(item(), this::translate));
    }

}