package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.PropertyList;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.BaseSelectorNotifier;
import io.intino.alexandria.ui.displays.notifiers.TextNotifier;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSelector<DN extends BaseSelectorNotifier, B extends Box> extends AbstractBaseSelector<DN, B> implements io.intino.alexandria.ui.displays.components.selector.Selector {
    private boolean multipleSelection = false;

    public BaseSelector(B box) {
        super(box);
    }

    private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();

    @Override
    public BaseSelector onSelect(SelectionListener selectionListener) {
        this.selectionListeners.add(selectionListener);
        return this;
    }

    @Override
    public java.util.List<SelectorOption> options() {
        return findOptions();
    }

    public BaseSelector<DN, B> add(String option) {
		Display display = new Text(box()).name(option);
        display.properties().put("value", option);
        display.properties().put("color", "black");
        addComponent((Component) display);
        return this;
    }

	@Override
	public void add(SelectorOption option) {
		addComponent((Component) option);
	}

	@Override
	public <D extends Display> D add(D child) {
		return addComponent((Component) child);
	}

	public void optionsRendered() {
        children().forEach(Display::update);
    }

    protected BaseSelector<DN, B> _multipleSelection(boolean value) {
        this.multipleSelection = value;
        return this;
    }

    protected void notifySelection() {
        selectionListeners.forEach(l -> l.accept(new SelectionEvent(this, selection())));
    }

    String nameOf(int option) {
        if (option == -1) return null;
        SelectorOption child = findOption(option);
        return child != null ? child.name() : null;
    }

    SelectorOption findOption(int option) {
        List<SelectorOption> options = options();
        return options.size() > 0 ? options.get(option) : null;
    }

    int position(String option) {
        List<SelectorOption> options = options();
        for (int i = 0; i< options.size(); i++) {
            SelectorOption selectorOption = options.get(i);
            if (selectorOption.name().equalsIgnoreCase(option) || selectorOption.id().equals(option))
                return i;
        }
        return -1;
    }

    private java.util.List<SelectorOption> findOptions() {
        java.util.List<SelectorOption> result = new ArrayList<>();
        java.util.List<Component> children = children(Component.class);
        children.forEach(c -> result.addAll(findOptions(c)));
        return result;
    }

    private java.util.List<SelectorOption> findOptions(Component child) {
        java.util.List<SelectorOption> result = new ArrayList<>();
        if (child instanceof SelectorOption) result.add((SelectorOption)child);
        List<Component> children = child.children(Component.class);
        children.forEach(c -> result.addAll(findOptions(c)));
        return result;
    }

    private <D extends Display> D addComponent(Component option) {
        PropertyList properties = option.properties();
        properties.addClassName("option");
        properties.put("name", option.name());
        return (D) super.add(option);
    }

	private static class Text extends io.intino.alexandria.ui.displays.components.Text<TextNotifier, Box> implements SelectorOption {
		public Text(Box box) {
			super(box);
		}
	}
}