package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.molds.WidgetMold;
import io.intino.alexandria.ui.documentation.Model;

public class DocsTemplate extends AbstractDocsTemplate<UiFrameworkBox> {

    public DocsTemplate(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        update();
        menu.select("Date");
    }

    private void update() {
        updateDataWidgets();
        updateOtherWidgets();
    }

    private void updateDataWidgets() {
        updateMold(panels.textPanel.textBlock, Model.WidgetType.Text);
        panels.textPanel.onShow((event) -> updateMold(panels.textPanel.textBlock, Model.WidgetType.Text));
        panels.numberPanel.onShow((event) -> updateMold(panels.numberPanel.numberBlock, Model.WidgetType.Number));
        panels.imagePanel.onShow((event) -> updateMold(panels.imagePanel.imageBlock, Model.WidgetType.Image));
        panels.filePanel.onShow((event) -> updateMold(panels.filePanel.fileBlock, Model.WidgetType.File));
        panels.datePanel.onShow((event) -> updateMold(panels.datePanel.dateBlock, Model.WidgetType.Date));
    }

    private void updateOtherWidgets() {
        panels.chartPanel.onShow((event) -> updateMold(panels.chartPanel.chartBlock, Model.WidgetType.Chart));
    }

    private void updateMold(WidgetMold mold, Model.WidgetType type) {
        mold.widget = Model.widget(type);
        mold.refresh();
    }


}