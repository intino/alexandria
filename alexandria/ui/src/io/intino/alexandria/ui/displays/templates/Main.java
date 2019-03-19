package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.I18n;
import io.intino.alexandria.ui.displays.molds.Model;
import io.intino.alexandria.ui.displays.molds.WidgetMold;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        title.update("Alexandria widgets");
        menu.select(I18n.translate("Date", language()));
        updateWidgets();
    }

    private void updateWidgets() {
        updateWidget(panels.textPanel.textBlock, Model.WidgetType.Text);
        updateWidget(panels.imagePanel.imageBlock, Model.WidgetType.Image);
        updateWidget(panels.datePanel.dateBlock, Model.WidgetType.Date);
    }

    private void updateWidget(WidgetMold widget, Model.WidgetType type) {
        widget.title.update(type.name().toLowerCase() + " widget");
        widget.properties(Model.properties(type));
        widget.methods(Model.methods(type));
        widget.events(Model.events(type));
        widget.refresh();
    }

}