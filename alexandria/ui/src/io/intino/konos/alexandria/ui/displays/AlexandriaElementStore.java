package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AlexandriaElementStore<E extends Element, DN extends AlexandriaDisplayNotifier> extends AlexandriaElementDisplay<E, DN> implements ElementDisplayManager {
    private Map<String, AlexandriaElementDisplay> displayMap = new HashMap<>();
    private String selected = null;

    public AlexandriaElementStore(Box box) {
        super(box);
    }

    public <E extends AlexandriaElementDisplay> E openElement(String label) {
        Element element = elementWithKey(label);
        Item target = targetWithKey(label);
        E display = displayWithKey(label);

        if (label.equals(selected)) return display;
        selected = label;

        if (display != null) {
            display.clearFilter();
            refreshOpened(label);
            return display;
        }

        refreshLoading(true);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        display = addAndBuildDisplay(element, target, label);
        refreshLoaded();
        refreshOpened(label);

        return display;
    }

    public <E extends AlexandriaElementDisplay> E createElement(Element element, Item target) {
        E display = displayWithKey(target.name());
        if (display != null) return display;
        return buildDisplay(element, target, target.name());
    }

    @Override
    public void removeElement(Item item) {
        if (!displayMap.containsKey(item.name())) return;
        displayMap.remove(item.name());
    }

    @Override
    public <E extends AlexandriaElementDisplay> E displayWithKey(String key) {
        return (E) displayMap.getOrDefault(key, null);
    }

    protected abstract void refreshOpened(String label);
    protected abstract void refreshLoading(boolean withMessage);
    protected abstract void refreshLoaded();
    protected abstract Element elementWithKey(String key);
    protected abstract Item targetWithKey(String label);
    protected abstract AlexandriaElementDisplay newDisplay(Element element, Item item);

    protected Class classFor(Element element) {
        return element.getClass();
    }

    private <E extends AlexandriaElementDisplay> E addAndBuildDisplay(Element element, Item target, String label) {
        E display = buildDisplay(element, target, label);
        display.personifyOnce(id() + label);
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
        AlexandriaElementDisplay display = newDisplay(element, target);

        display.route(routeSubPath());
        display.label(label);
        display.element(element);
        display.target(target);
        displayMap.put(label, display);

        return (E) display;
    }

}