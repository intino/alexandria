package io.intino.konos.server.activity.displays.elements;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.catalogs.CatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.TemporalCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.TemporalRangeCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.TemporalTimeCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.catalogs.model.TemporalCatalog;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.layouts.model.Layout;
import io.intino.konos.server.activity.displays.panels.PanelDisplay;
import io.intino.konos.server.activity.displays.panels.model.Panel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class ElementStoreDisplay<DN extends DisplayNotifier> extends ElementDisplay<Layout, DN> implements ElementDisplayManager {
    private Map<Class<? extends Element>, Function<Element, ElementDisplay>> builders = new HashMap<>();
    private Map<String, ElementDisplay> displayMap = new HashMap<>();

    public ElementStoreDisplay(Box box) {
        super(box);
        registerBuilders();
    }

    public <E extends ElementDisplay> E openElement(String label) {
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

    public <E extends ElementDisplay> E createElement(Element element, Item target) {
        E display = displayWithLabel(target.name());
        if (display != null) return display;
        return buildDisplay(element, target, target.name());
    }

    @Override
    public <E extends ElementDisplay> E displayWithLabel(String label) {
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

    private PanelDisplay buildPanelDisplay(Element component) {
        PanelDisplay display = new PanelDisplay(box);
        display.element((Panel) component);
        return display;
    }

    private CatalogDisplay buildCatalogDisplay(Element component) {
        CatalogDisplay display = new CatalogDisplay(box);
        display.element(component);
        return display;
    }

    private TemporalCatalogDisplay buildTemporalCatalogDisplay(Element component) {
        TemporalCatalog catalog = (TemporalCatalog)component;
        TemporalCatalogDisplay display = (catalog.type() == TemporalCatalog.Type.Time) ? new TemporalTimeCatalogDisplay(box) : new TemporalRangeCatalogDisplay(box);
        display.element(component);
        return display;
    }

    private <E extends ElementDisplay> E addAndBuildDisplay(Element element, Item target, String label) {
        E display = buildDisplay(element, target, label);
        display.personifyOnce(label);
        return display;
    }

    private <E extends ElementDisplay> E buildDisplay(Element element, Item target, String label) {
        E display = buildDisplayFor(element, target, label);
        display.elementDisplayManager(this);
        add(display);

        display.onLoading((value) -> {
            if ((Boolean)value) refreshLoading(false);
            else refreshLoaded();
        });

        return display;
    }

    private <E extends ElementDisplay> E buildDisplayFor(Element element, Item target, String label) {
        Class clazz = classFor(element);

        ElementDisplay display = builders.get(clazz).apply(element);
        display.label(label);
        display.element(element);
        display.target(target);
        displayMap.put(label, display);

        return (E) display;
    }

}