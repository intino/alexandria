package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.I18n;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.model.*;

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
        if (widget == null) return;
        title.update(I18n.translate(widget.getClass().getSimpleName().replace("Widget", ""), language()));
        updateExamplesVisibility();
        updateInfo();
    }

    private void updateExamplesVisibility() {
        textExamples.visible(widget instanceof TextWidget);
        numberExamples.visible(widget instanceof NumberWidget);
        fileExamples.visible(widget instanceof FileWidget);
        imageExamples.visible(widget instanceof ImageWidget);
        dateExamples.visible(widget instanceof DateWidget);
        chartExamples.visible(widget instanceof ChartWidget);
        blockExamples.visible(widget instanceof BlockWidget);
    }

    private void updateInfo() {
        if (infoAdded) return;
        facetsNames.update(widget.facets().size() > 0 ? String.join(", ", widget.facets()) : I18n.translate("no facets", language()));
        refreshPropertiesDisplay();
        refreshMethodsDisplay();
        refreshEventsDisplay();
        infoAdded = true;
    }

    private void refreshPropertiesDisplay() {
        widget.propertyList().forEach(this::propertyMoldOf);
    }

    private void propertyMoldOf(Property property) {
        PropertyMold mold = new PropertyMold(box());
        mold.property = property;
        properties.addPropertyMold(mold);
        mold.refresh();
    }

    private void refreshMethodsDisplay() {
        widget.methodList().forEach(this::methodMoldOf);
    }

    private void methodMoldOf(Method method) {
        MethodMold mold = new MethodMold(box());
        mold.method = method;
        methods.addMethodMold(mold);
        mold.refresh();
    }

    private void refreshEventsDisplay() {
        events.<EventsDisplay>get().events(widget.eventList());
        events.refresh();
    }

}