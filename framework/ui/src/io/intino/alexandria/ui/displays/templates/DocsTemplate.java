package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.documentation.Model;

public class DocsTemplate extends AbstractDocsTemplate<AlexandriaUiBox> {

    public DocsTemplate(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        addDataWidgets();
        addCatalogWidgets();
        addOperationWidgets();
        addOtherWidgets();
    }

    private void addDataWidgets() {
        body.dataBlock.dataWidgets.add(Model.widget(Model.WidgetType.Text));
        body.dataBlock.dataWidgets.add(Model.widget(Model.WidgetType.Number));
        body.dataBlock.dataWidgets.add(Model.widget(Model.WidgetType.Image));
        body.dataBlock.dataWidgets.add(Model.widget(Model.WidgetType.File));
        body.dataBlock.dataWidgets.add(Model.widget(Model.WidgetType.Date));
    }

    private void addCatalogWidgets() {
        body.catalogBlock.catalogWidgets.add(Model.widget(Model.WidgetType.List));
        body.catalogBlock.catalogWidgets.add(Model.widget(Model.WidgetType.Table));
        body.catalogBlock.catalogWidgets.add(Model.widget(Model.WidgetType.Map));
        body.catalogBlock.catalogWidgets.add(Model.widget(Model.WidgetType.Grouping));
        body.catalogBlock.catalogWidgets.add(Model.widget(Model.WidgetType.Sorting));
        body.catalogBlock.catalogWidgets.add(Model.widget(Model.WidgetType.SearchBox));
    }

    private void addOperationWidgets() {
        body.operationBlock.operationWidgets.add(Model.widget(Model.WidgetType.OpenPage));
        body.operationBlock.operationWidgets.add(Model.widget(Model.WidgetType.OpenBlock));
        body.operationBlock.operationWidgets.add(Model.widget(Model.WidgetType.Task));
        body.operationBlock.operationWidgets.add(Model.widget(Model.WidgetType.Export));
        body.operationBlock.operationWidgets.add(Model.widget(Model.WidgetType.Download));
        body.operationBlock.operationWidgets.add(Model.widget(Model.WidgetType.DownloadSelection));
    }

    private void addOtherWidgets() {
        body.otherBlock.otherWidgets.add(Model.widget(Model.WidgetType.Block));
        body.otherBlock.otherWidgets.add(Model.widget(Model.WidgetType.Chart));
        body.otherBlock.otherWidgets.add(Model.widget(Model.WidgetType.Dashboard));
        body.otherBlock.otherWidgets.add(Model.widget(Model.WidgetType.Slider));
        body.otherBlock.otherWidgets.add(Model.widget(Model.WidgetType.Dialog));
    }

    //    @Override
//    public void init() {
//        super.init();
//        update();
//        menu.select("Block");
//    }
//
//    private void update() {
//        updateDataWidgets();
//        updateOtherWidgets();
//    }
//
//    private void updateDataWidgets() {
//        updateMold(panels.textPanel.textBlock, Model.WidgetType.Text);
//        panels.textPanel.onShow((event) -> updateMold(panels.textPanel.textBlock, Model.WidgetType.Text));
//        panels.numberPanel.onShow((event) -> updateMold(panels.numberPanel.numberBlock, Model.WidgetType.Number));
//        panels.imagePanel.onShow((event) -> updateMold(panels.imagePanel.imageBlock, Model.WidgetType.Image));
//        panels.filePanel.onShow((event) -> updateMold(panels.filePanel.fileBlock, Model.WidgetType.File));
//        panels.datePanel.onShow((event) -> updateMold(panels.datePanel.dateBlock, Model.WidgetType.Date));
//        panels.blockPanel.onShow((event) -> updateMold(panels.blockPanel.blockBlock, Model.WidgetType.Block));
//        panels.collectionPanel.onShow((event) -> updateMold(panels.collectionPanel.collectionBlock, Model.WidgetType.Collection));
//    }
//
//    private void updateOtherWidgets() {
//        panels.chartPanel.onShow((event) -> updateMold(panels.chartPanel.chartBlock, Model.WidgetType.Chart));
//    }
//
//    private void updateMold(WidgetMold mold, Model.WidgetType type) {
//        mold.widget = Model.widget(type);
//        mold.refresh();
//    }

}