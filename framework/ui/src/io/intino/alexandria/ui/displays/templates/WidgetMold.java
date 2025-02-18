package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.DisplayHelper;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;
import io.intino.alexandria.ui.documentation.model.actionable.DownloadSelectionWidget;
import io.intino.alexandria.ui.documentation.model.actionable.DownloadWidget;
import io.intino.alexandria.ui.documentation.model.actionable.ExportWidget;
import io.intino.alexandria.ui.documentation.model.actionable.UploadWidget;
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
        downloadSelectionExamples.onShow(e -> downloadSelectionExamples.refresh());
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
        if (textExamples.isVisible()) textExamples.textExamplesMold.refresh();
        numberExamples.visible(widget instanceof NumberWidget);
        if (numberExamples.isVisible()) numberExamples.numberExamplesMold.refresh();
        fileExamples.visible(widget instanceof FileWidget);
        if (fileExamples.isVisible()) fileExamples.fileExamplesMold.refresh();
        imageExamples.visible(widget instanceof ImageWidget);
        if (imageExamples.isVisible()) imageExamples.imageExamplesMold.refresh();
        dateExamples.visible(widget instanceof DateWidget);
        if (dateExamples.isVisible()) dateExamples.dateExamplesMold.refresh();
        multipleExamples.visible(widget instanceof MultipleWidget);
        if (multipleExamples.isVisible()) multipleExamples.multipleExamplesMold.refresh();
        chartExamples.visible(widget instanceof ChartWidget);
        if (chartExamples.isVisible()) chartExamples.chartExamplesMold.refresh();
        dashboardExamples.visible(widget instanceof DashboardWidget);
        if (dashboardExamples.isVisible()) dashboardExamples.dashboardExamplesMold.refresh();
        appDirectoryExamples.visible(widget instanceof AppDirectoryWidget);
        if (appDirectoryExamples.isVisible()) appDirectoryExamples.appDirectoryExamplesMold.refresh();
        digitalSignatureExamples.visible(widget instanceof DigitalSignatureWidget);
        if (digitalSignatureExamples.isVisible()) digitalSignatureExamples.digitalSignatureExamplesMold.refresh();
        blockExamples.visible(widget instanceof BlockWidget);
        if (blockExamples.isVisible()) blockExamples.blockExamplesMold.refresh();
        listExamples.visible(widget instanceof ListWidget);
        if (listExamples.isVisible()) listExamples.listExamplesMold.refresh();
        tableExamples.visible(widget instanceof TableWidget);
        if (tableExamples.isVisible()) tableExamples.tableExamplesMold.refresh();
        dynamicTableExamples.visible(widget instanceof DynamicTableWidget);
        if (dynamicTableExamples.isVisible()) dynamicTableExamples.dynamicTableExamplesMold.refresh();
        gridExamples.visible(widget instanceof GridWidget);
        if (gridExamples.isVisible()) gridExamples.gridExamplesMold.refresh();
        mapExamples.visible(widget instanceof MapWidget);
        if (mapExamples.isVisible()) mapExamples.mapExamplesMold.refresh();
        exportExamples.visible(widget instanceof ExportWidget);
        if (exportExamples.isVisible()) exportExamples.exportExamplesMold.refresh();
        downloadExamples.visible(widget instanceof DownloadWidget);
        if (downloadExamples.isVisible()) downloadExamples.downloadExamplesMold.refresh();
        downloadSelectionExamples.visible(widget instanceof DownloadSelectionWidget);
        if (downloadSelectionExamples.isVisible()) downloadSelectionExamples.downloadSelectionExamplesMold.refresh();
        uploadExamples.visible(widget instanceof UploadWidget);
        if (uploadExamples.isVisible()) uploadExamples.uploadExamplesMold.refresh();
        actionableExamples.visible(widget instanceof ActionableWidget && !(widget instanceof ExportWidget) && !(widget instanceof DownloadWidget) && !(widget instanceof UploadWidget) && !(widget instanceof DownloadSelectionWidget));
        if (actionableExamples.isVisible()) actionableExamples.actionableExamplesMold.refresh();
        groupingExamples.visible(widget instanceof GroupingWidget);
        if (groupingExamples.isVisible()) groupingExamples.groupingExamplesMold.refresh();
        groupingToolbarExamples.visible(widget instanceof GroupingToolbarWidget);
        if (groupingToolbarExamples.isVisible()) groupingToolbarExamples.groupingToolbarExamplesMold.refresh();
        sortingExamples.visible(widget instanceof SortingWidget);
        if (sortingExamples.isVisible()) sortingExamples.sortingExamplesMold.refresh();
        searchBoxExamples.visible(widget instanceof SearchBoxWidget);
        if (searchBoxExamples.isVisible()) searchBoxExamples.searchBoxExamplesMold.refresh();
        sliderExamples.visible(widget instanceof SliderWidget);
        if (sliderExamples.isVisible()) sliderExamples.sliderExamplesMold.refresh();
        dialogExamples.visible(widget instanceof DialogWidget);
        if (dialogExamples.isVisible()) dialogExamples.dialogExamplesMold.refresh();
        layerExamples.visible(widget instanceof LayerWidget);
        if (layerExamples.isVisible()) layerExamples.layerExamplesMold.refresh();
        dividerExamples.visible(widget instanceof DividerWidget);
        if (dividerExamples.isVisible()) dividerExamples.dividerExamplesMold.refresh();
        userExamples.visible(widget instanceof UserWidget);
        if (userExamples.isVisible()) userExamples.userExamplesMold.refresh();
        locationExamples.visible(widget instanceof LocationWidget);
        if (locationExamples.isVisible()) locationExamples.locationExamplesMold.refresh();
        selectorExamples.visible(widget instanceof SelectorWidget);
        if (selectorExamples.isVisible()) selectorExamples.selectorExamplesMold.refresh();
        wizardExamples.visible(widget instanceof WizardWidget);
        if (wizardExamples.isVisible()) wizardExamples.wizardExamplesMold.refresh();
        frameExamples.visible(widget instanceof FrameWidget);
        if (frameExamples.isVisible()) frameExamples.frameExamplesMold.refresh();
        microSiteExamples.visible(widget instanceof MicroSiteWidget);
        if (microSiteExamples.isVisible()) microSiteExamples.microSiteExamplesMold.refresh();
        htmlViewerExamples.visible(widget instanceof HtmlViewerWidget);
        if (htmlViewerExamples.isVisible()) htmlViewerExamples.htmlViewerExamplesMold.refresh();
        dateNavigatorExamples.visible(widget instanceof DateNavigatorWidget);
        if (dateNavigatorExamples.isVisible()) dateNavigatorExamples.dateNavigatorExamplesMold.refresh();
        timelineExamples.visible(widget instanceof TimelineWidget);
        if (timelineExamples.isVisible()) timelineExamples.timelineExamplesMold.refresh();
        eventlineExamples.visible(widget instanceof EventlineWidget);
        if (eventlineExamples.isVisible()) eventlineExamples.eventlineExamplesMold.refresh();
        reelExamples.visible(widget instanceof ReelWidget);
        if (reelExamples.isVisible()) reelExamples.reelExamplesMold.refresh();
        documentEditorExamples.visible(widget instanceof DocumentEditorWidget);
        if (documentEditorExamples.isVisible()) documentEditorExamples.documentEditorExamplesMold.refresh();
        kpiExamples.visible(widget instanceof KpiWidget);
        if (kpiExamples.isVisible()) kpiExamples.kpiExamplesMold.refresh();
        chatExamples.visible(widget instanceof ChatWidget);
        if (chatExamples.isVisible()) chatExamples.chatExamplesMold.refresh();
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