package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Layout;

import java.util.HashMap;
import java.util.Map;

public abstract class AlexandriaElementStore<DN extends AlexandriaDisplayNotifier> extends AlexandriaElementDisplay<Layout, DN> implements ElementDisplayManager {
    private Map<String, AlexandriaElementDisplay> displayMap = new HashMap<>();
    private String selected = null;

    public AlexandriaElementStore(Box box) {
        super(box);
    }

    public <E extends AlexandriaElementDisplay> E openElement(String label) {
        Element element = elementWithLabel(label);
        Item target = targetWithLabel(label);
        E display = displayWithLabel(label);

        if (label.equals(selected)) return display;
        selected = label;

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
    public void removeElement(Item item) {
        if (!displayMap.containsKey(item.name())) return;
        displayMap.remove(item.name());
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
    protected abstract AlexandriaElementDisplay newDisplay(Element element, Item item);

    protected Class classFor(Element element) {
        return element.getClass();
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

        AlexandriaElementDisplay display = newDisplay(element, target);
        display.label(label);
        display.element(element);
        display.target(target);
        displayMap.put(label, display);

        return (E) display;
    }

}