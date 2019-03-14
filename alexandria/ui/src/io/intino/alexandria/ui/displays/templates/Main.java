package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.blocks.WidgetBlock;
import io.intino.alexandria.ui.displays.blocks.WidgetHelper;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        title.update("Alexandria widgets");
        menu.select("Date");
        updatePanels();
    }

    private void updatePanels() {
        updatePanel(panels.textPanel.textBlock, WidgetHelper.WidgetType.Text);
        updatePanel(panels.imagePanel.imageBlock, WidgetHelper.WidgetType.Image);
        updatePanel(panels.datePanel.dateBlock, WidgetHelper.WidgetType.Date);
    }

    private void updatePanel(WidgetBlock block, WidgetHelper.WidgetType type) {
        block.title.update(type.name().toLowerCase() + " widget");
        block.properties(WidgetHelper.properties(type));
        block.methods(WidgetHelper.methods(type));
        block.events(WidgetHelper.events(type));
        block.refresh();
    }

}