package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.documentation.Model;

public class WidgetTypeTemplate extends AbstractWidgetTypeTemplate<UiFrameworkBox> {
    private Model.WidgetType type;

    public WidgetTypeTemplate(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        body.widgetTypeBlock.widget = Model.widget(type);
        body.widgetTypeBlock.refresh();
    }

    public void type(String type) {
        this.type = Model.WidgetType.valueOf(type.substring(0, 1).toUpperCase() + type.substring(1));
    }
}