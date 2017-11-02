package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.foundation.activity.displays.DisplayNotifier;
import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.Catalog;
import io.intino.alexandria.framework.box.model.TemporalCatalog;
import io.intino.alexandria.framework.box.model.Layout;
import io.intino.alexandria.framework.box.model.Panel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AlexandriaElementStoreDisplay<DN extends DisplayNotifier> extends AlexandriaElementDisplay<Layout, DN> implements ElementDisplayManager {
    private Map<Class<? extends Element>, Function<Element, AlexandriaElementDisplay>> builders = new HashMap<>();
    private Map<String, AlexandriaElementDisplay> displayMap = new HashMap<>();

    public AlexandriaElementStoreDisplay(Box box) {
        super(box);
        registerBuilders();
    }

    public <E extends AlexandriaElementDisplay> E openElement(String label) {
        Element element = elementWithLabel(label);
        Item target = targetWithLabel(label);
        E display = displayWithLabel(label);

        if (display != null) {
            display.clearFilter();
            refreshSelected(label);
            return display;
        }

        refreshLoading(true);
        display = addAndBuildDisplay(element, target, label);
        refreshLoaded();
        refreshSelected(label);

        return display;
    }

    public <E extends AlexandriaElementDisplay> E createElement(Element element, Item target) {
        E display = displayWithLabel(target.name());
        if (display != null) return display;
        return buildDisplay(element, target, target.name());
    }

    @Override
    public <E extends AlexandriaElementDisplay> E displayWithLabel(String label) {
        return (E) displayMap.getOrDefault(label, null);
    }

    protected abstract void refreshSelected(String label);
    protected abstract void refreshLoading(boolean withMessage);
    protected abstract void refreshLoaded();
    protected abstract Element elementWithLabel(String label);
    protected abstract Item targetWithLabel(String label);

    protected Class classFor(Element element) {
        return element.getClass();
    }

    private void registerBuilders() {
        builders.put(Panel.class, this::buildPanelDisplay);
        builders.put(Catalog.class, this::buildCatalogDisplay);
        builders.put(TemporalCatalog.class, this::buildTemporalCatalogDisplay);
    }

    private AlexandriaPanelDisplay buildPanelDisplay(Element component) {
        AlexandriaPanelDisplay display = new AlexandriaPanelDisplay(box);
        display.element((Panel) component);
        return display;
    }

    private AlexandriaCatalogDisplay buildCatalogDisplay(Element component) {
        AlexandriaCatalogDisplay display = new AlexandriaCatalogDisplay(box);
        display.element(component);
        return display;
    }

    private AlexandriaTemporalCatalogDisplay buildTemporalCatalogDisplay(Element component) {
        TemporalCatalog catalog = (TemporalCatalog)component;
        AlexandriaTemporalCatalogDisplay display = (catalog.type() == TemporalCatalog.Type.Time) ? new AlexandriaTemporalTimeCatalogDisplay(box) : new AlexandriaTemporalRangeCatalogDisplay(box);
        display.element(component);
        return display;
    }

    private <E extends AlexandriaElementDisplay> E addAndBuildDisplay(Element element, Item target, String label) {
        E display = buildDisplay(element, target, label);
        display.personifyOnce(label);
        return display;
    }

    private <E extends AlexandriaElementDisplay> E buildDisplay(Element element, Item target, String label) {
        E display = buildDisplayFor(element, target, label);
        display.elementDisplayManager(this);
        add(display);

        display.onLoading((value) -> {
            if ((Boolean)value) refreshLoading(false);
            else refreshLoaded();
        });

        return display;
    }

    private <E extends AlexandriaElementDisplay> E buildDisplayFor(Element element, Item target, String label) {
        Class clazz = classFor(element);

        AlexandriaElementDisplay display = builders.get(clazz).apply(element);
        display.label(label);
        display.element(element);
        display.target(target);
        displayMap.put(label, display);

        return (E) display;
    }

}