package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.I18n;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.model.OperationWidget;
import io.intino.alexandria.ui.documentation.model.collection.*;
import io.intino.alexandria.ui.documentation.model.data.*;
import io.intino.alexandria.ui.documentation.model.operation.DownloadSelectionWidget;
import io.intino.alexandria.ui.documentation.model.operation.DownloadWidget;
import io.intino.alexandria.ui.documentation.model.operation.ExportWidget;
import io.intino.alexandria.ui.documentation.model.other.BlockWidget;
import io.intino.alexandria.ui.documentation.model.other.ChartWidget;

public class WidgetMold extends AbstractWidgetMold<UiFrameworkBox> {

    private boolean infoAdded = false;

    public WidgetMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        events.set(new EventsDisplay(box()));
    }

    @Override
    public void refresh() {
        super.refresh();
        showLoading();
        if (item() == null) return;
        Widget widget = item();
        title.update(I18n.translate(widget.getClass().getSimpleName().replace("Widget", ""), language()));
        description.update(I18n.translate(widget.description(), language()));
        highlightFacets.addAll(widget.facets());
        updateExamplesVisibility();
        updateInfo();
        hideLoading();
    }

    private void updateExamplesVisibility() {
        Widget widget = item();
        textExamples.visible(widget instanceof TextWidget);
        numberExamples.visible(widget instanceof NumberWidget);
        fileExamples.visible(widget instanceof FileWidget);
        imageExamples.visible(widget instanceof ImageWidget);
        dateExamples.visible(widget instanceof DateWidget);
        chartExamples.visible(widget instanceof ChartWidget);
        blockExamples.visible(widget instanceof BlockWidget);
        listExamples.visible(widget instanceof ListWidget);
        tableExamples.visible(widget instanceof TableWidget);
        mapExamples.visible(widget instanceof MapWidget);
        exportExamples.visible(widget instanceof ExportWidget);
        downloadExamples.visible(widget instanceof DownloadWidget);
        downloadSelectionExamples.visible(widget instanceof DownloadSelectionWidget);
        operationExamples.visible(widget instanceof OperationWidget && !(widget instanceof ExportWidget) && !(widget instanceof DownloadWidget) && !(widget instanceof DownloadSelectionWidget));
        groupingExamples.visible(widget instanceof GroupingWidget);
        sortingExamples.visible(widget instanceof SortingWidget);
        searchBoxExamples.visible(widget instanceof SearchBoxWidget);
    }

    private void updateInfo() {
        if (infoAdded) return;
        Widget widget = item();
        facetsNames.update(widget.facets().size() > 0 ? String.join(", ", widget.facets()) : I18n.translate("no facets", language()));
        refreshProperties();
        refreshMethods();
        refreshEventsDisplay();
        infoAdded = true;
    }

    private void refreshProperties() {
        item().propertyList().forEach(p -> properties.add(p));
    }

    private void refreshMethods() {
        item().methodList().forEach(m -> methods.add(m));
    }

    private void refreshEventsDisplay() {
        events.<EventsDisplay>get().events(item().eventList());
        events.refresh();
    }

}