package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.DisplayHelper;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;
import io.intino.alexandria.ui.documentation.model.actionable.DownloadSelectionWidget;
import io.intino.alexandria.ui.documentation.model.actionable.DownloadWidget;
import io.intino.alexandria.ui.documentation.model.actionable.ExportWidget;
import io.intino.alexandria.ui.documentation.model.collection.*;
import io.intino.alexandria.ui.documentation.model.data.*;
import io.intino.alexandria.ui.documentation.model.other.*;

import java.util.function.Consumer;

public class WidgetMold extends AbstractWidgetMold<AlexandriaUiBox> {
    private boolean infoAdded = false;
    private Mode mode = Mode.Normal;
    private Consumer<Boolean> backListener;

    public WidgetMold(AlexandriaUiBox box) {
        super(box);
    }

    public enum Mode { Normal, Embedded }
    public void mode(Mode mode) {
        this.mode = mode;
    }

    public void onBack(Consumer<Boolean> listener) {
        this.backListener = listener;
    }

    @Override
    public void init() {
        super.init();
        events.display(new EventsDisplay(box()));
        backTrigger.onExecute(e -> notifyBack());
    }

    @Override
    public void refresh() {
        super.refresh();
        showLoading();
        if (item() == null) return;
        Widget widget = item();
        backTrigger.visible(mode == Mode.Embedded);
        title.value(DisplayHelper.label(widget, this::translate));
        description.value(translate(widget.description()));
        highlightFacets.clear();
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
        appDirectoryExamples.visible(widget instanceof AppDirectoryWidget);
        digitalSignatureExamples.visible(widget instanceof DigitalSignatureWidget);
        blockExamples.visible(widget instanceof BlockWidget);
        listExamples.visible(widget instanceof ListWidget);
        tableExamples.visible(widget instanceof TableWidget);
        dynamicTableExamples.visible(widget instanceof DynamicTableWidget);
        gridExamples.visible(widget instanceof GridWidget);
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
        dateNavigatorExamples.visible(widget instanceof DateNavigatorWidget);
        timelineExamples.visible(widget instanceof TimelineWidget);
        eventlineExamples.visible(widget instanceof EventlineWidget);
        reelExamples.visible(widget instanceof ReelWidget);
        documentEditorExamples.visible(widget instanceof DocumentEditorWidget);
        kpiExamples.visible(widget instanceof KpiWidget);
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

    private void notifyBack() {
        if (backListener == null) return;
        backListener.accept(true);
    }

}