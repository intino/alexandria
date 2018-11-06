package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AlexandriaElementStore<E extends Element, DN extends AlexandriaDisplayNotifier> extends AlexandriaElementDisplay<E, DN> implements ElementDisplayManager {
    private Map<String, AlexandriaElementDisplay> displayMap = new HashMap<>();
    private String selected = null;

    public AlexandriaElementStore(Box box) {
        super(box);
    }

    public <E extends AlexandriaElementDisplay> E openElement(String key) {
        return openElement(key, id());
    }

    public <E extends AlexandriaElementDisplay> E openElement(String key, String ownerId) {
        String normalizedKey = normalize(key);
        Element element = elementWithKey(normalizedKey);
        Item target = targetWithKey(normalizedKey);
        E display = displayWithKey(normalizedKey);

        if (normalizedKey.equals(selected)) return display;
        selected = normalizedKey;

        if (display != null) {
            display.clearFilter();
            refreshOpened(normalizedKey);
            return display;
        }

        refreshLoading(true);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        display = addAndBuildDisplay(element, target, key, ownerId);
        refreshLoaded();
        refreshOpened(normalizedKey);

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
    protected abstract Item targetWithKey(String key);
    protected abstract String normalize(String key);
    protected abstract AlexandriaElementDisplay newDisplay(Element element, Item item);

    protected Class classFor(Element element) {
        return element.getClass();
    }

    private <E extends AlexandriaElementDisplay> E addAndBuildDisplay(Element element, Item target, String label, String ownerId) {
        E display = buildDisplay(element, target, label);
        display.personify(ownerId + normalize(label));
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
        displayMap.put(normalize(label), display);

        return (E) display;
    }

}