package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.I18n;
import io.intino.alexandria.ui.displays.molds.WidgetMold;
import io.intino.alexandria.ui.documentation.Model;

public class DocsTemplate extends AbstractDocsTemplate<UiFrameworkBox> {

    public DocsTemplate(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        menu.select("Image");
        updateMolds();
    }

    private void updateMolds() {
        updateMold(panels.textPanel.textBlock, Model.WidgetType.Text);
        updateMold(panels.imagePanel.imageBlock, Model.WidgetType.Image);
        updateMold(panels.datePanel.dateBlock, Model.WidgetType.Date);
    }

    private void updateMold(WidgetMold mold, Model.WidgetType type) {
        mold.title.update(I18n.translate(type.name() + " widget", language()));
        mold.widget = Model.widget(type);
        mold.refresh();
    }


}