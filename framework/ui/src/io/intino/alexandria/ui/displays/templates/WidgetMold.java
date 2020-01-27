package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.model.OperationWidget;
import io.intino.alexandria.ui.documentation.model.collection.*;
import io.intino.alexandria.ui.documentation.model.data.*;
import io.intino.alexandria.ui.documentation.model.operation.DownloadSelectionWidget;
import io.intino.alexandria.ui.documentation.model.operation.DownloadWidget;
import io.intino.alexandria.ui.documentation.model.operation.ExportWidget;
import io.intino.alexandria.ui.documentation.model.other.*;

public class WidgetMold extends AbstractWidgetMold<AlexandriaUiBox> {

    private boolean infoAdded = false;

    public WidgetMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        events.display(new EventsDisplay(box()));
    }

    @Override
    public void refresh() {
        super.refresh();
        showLoading();
        if (item() == null) return;
        Widget widget = item();
        title.value(translate(widget.getClass().getSimpleName().replace("Widget", "")));
        description.value(translate(widget.description()));
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
        dashboardExamples.visible(widget instanceof DashboardWidget);
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
        sliderExamples.visible(widget instanceof SliderWidget);
        dialogExamples.visible(widget instanceof DialogWidget);
        dividerExamples.visible(widget instanceof DividerWidget);
        userExamples.visible(widget instanceof UserWidget);
        locationExamples.visible(widget instanceof LocationWidget);
        selectorExamples.visible(widget instanceof SelectorWidget);
        stepperExamples.visible(widget instanceof StepperWidget);
    }

    private void updateInfo() {
        if (infoAdded) return;
        Widget widget = item();
        facetsNames.value(widget.facets().size() > 0 ? String.join(", ", widget.facets()) : translate("no facets"));
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