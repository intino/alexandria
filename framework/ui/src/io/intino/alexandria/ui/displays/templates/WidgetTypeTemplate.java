package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.documentation.Model;

public class WidgetTypeTemplate extends AbstractWidgetTypeTemplate<AlexandriaUiBox> {
    private Model.WidgetType type;

    public WidgetTypeTemplate(AlexandriaUiBox box) {
        super(box);
    }

    public WidgetMold widget() {
        return widget;
    }

    @Override
    public void init() {
        super.init();
        widget.item(Model.widget(type));
    }

    public void type(String type) {
        this.type = Model.WidgetType.from(type);
    }
}