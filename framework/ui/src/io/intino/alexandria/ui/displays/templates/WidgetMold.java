package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;
import io.intino.alexandria.ui.documentation.model.collection.*;
import io.intino.alexandria.ui.documentation.model.data.*;
import io.intino.alexandria.ui.documentation.model.actionable.DownloadSelectionWidget;
import io.intino.alexandria.ui.documentation.model.actionable.DownloadWidget;
import io.intino.alexandria.ui.documentation.model.actionable.ExportWidget;
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
        multipleExamples.visible(widget instanceof MultipleWidget);
        chartExamples.visible(widget instanceof ChartWidget);
        dashboardExamples.visible(widget instanceof DashboardWidget);
        boardExamples.visible(widget instanceof BoardWidget);
        blockExamples.visible(widget instanceof BlockWidget);
        listExamples.visible(widget instanceof ListWidget);
        tableExamples.visible(widget instanceof TableWidget);
        dynamicTableExamples.visible(widget instanceof DynamicTableWidget);
        mapExamples.visible(widget instanceof MapWidget);
        exportExamples.visible(widget instanceof ExportWidget);
        downloadExamples.visible(widget instanceof DownloadWidget);
        downloadSelectionExamples.visible(widget instanceof DownloadSelectionWidget);
        actionableExamples.visible(widget instanceof ActionableWidget && !(widget instanceof ExportWidget) && !(widget instanceof DownloadWidget) && !(widget instanceof DownloadSelectionWidget));
        groupingExamples.visible(widget instanceof GroupingWidget);
        groupingToolbarExamples.visible(widget instanceof GroupingToolbarWidget);
        sortingExamples.visible(widget instanceof SortingWidget);
        searchBoxExamples.visible(widget instanceof SearchBoxWidget);
        sliderExamples.visible(widget instanceof SliderWidget);
        dialogExamples.visible(widget instanceof DialogWidget);
        layerExamples.visible(widget instanceof LayerWidget);
        dividerExamples.visible(widget instanceof DividerWidget);
        userExamples.visible(widget instanceof UserWidget);
        locationExamples.visible(widget instanceof LocationWidget);
        selectorExamples.visible(widget instanceof SelectorWidget);
        stepperExamples.visible(widget instanceof StepperWidget);
        frameExamples.visible(widget instanceof FrameWidget);
        microSiteExamples.visible(widget instanceof MicroSiteWidget);
        htmlViewerExamples.visible(widget instanceof HtmlViewerWidget);
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
        events.<EventsDisplay>display().events(item().eventList());
        //events.refresh();
    }

}