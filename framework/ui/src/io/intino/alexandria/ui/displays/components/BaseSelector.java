package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.PropertyList;
import io.intino.alexandria.ui.displays.components.addressable.Addressable;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.BaseSelectorNotifier;
import io.intino.alexandria.ui.displays.notifiers.TextNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseSelector<DN extends BaseSelectorNotifier, B extends Box> extends AbstractBaseSelector<DN, B> implements io.intino.alexandria.ui.displays.components.selector.Selector, Addressable {
    private boolean readonly;
    private boolean multipleSelection = false;
    private String path;
    private String address;
    private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();
    private List<Component> components = new ArrayList<>();

    public BaseSelector(B box) {
        super(box);
    }

    @Override
    public BaseSelector<DN, B> onSelect(SelectionListener selectionListener) {
        this.selectionListeners.add(selectionListener);
        return this;
    }

    public boolean readonly() {
        return readonly;
    }

    public BaseSelector<DN, B> readonly(boolean readonly) {
        _readonly(readonly);
        notifier.refreshReadonly(readonly);
        return this;
    }

    @Override
    public void init() {
        super.init();
        if (validAddress()) notifier.addressed(address);
    }

    @Override
    public void didMount() {
        super.didMount();
        reloadComponents();
        if (validAddress()) notifier.addressed(address);
    }

    @Override
    public java.util.List<SelectorOption> options() {
        return findOptions();
    }

    public BaseSelector<DN, B> add(String option) {
		Display display = new Text(box()).name(option);
		display.id(UUID.randomUUID().toString());
        display.properties().put("value", option);
        display.properties().put("color", "black");
        addOption((Component) display);
        return this;
    }

	@Override
	public void add(SelectorOption option) {
		addOption((Component) option);
	}

    @Override
    public <D extends Display> D add(D child) {
        return addOption((Component) child);
    }

    @Override
    public void clear() {
        super.clear();
        components.clear();
    }

    @Override
    public void addDivider() {
        Divider divider = new Divider(box());
        PropertyList properties = divider.properties();
        properties.addClassName("divider");
        super.add(divider);
    }

	public void optionsRendered() {
        children().forEach(Display::update);
    }

    public String path() {
        return this.path;
    }

    public boolean multipleSelection() {
        return multipleSelection;
    }

    protected List<Component> components() {
        return this.components;
    }

    protected BaseSelector<DN, B> components(List<Component> components) {
        this.components = components;
        return this;
    }

    protected void reloadComponents() {
        List<Component> components = new ArrayList<>(this.components.size() > 0 ? this.components : this.children(Component.class));
        clear();
        this.components = new ArrayList<>();
        components.forEach(this::add);
    }

    protected BaseSelector<DN, B> _readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    protected BaseSelector<DN, B> _multipleSelection(boolean value) {
        this.multipleSelection = value;
        return this;
    }

    protected void notifySelection(List selection) {
        selectionListeners.forEach(l -> l.accept(new SelectionEvent(this, selection)));
    }

    protected void notifySelection() {
        selectionListeners.forEach(l -> l.accept(new SelectionEvent(this, selection())));
    }

    protected BaseSelector<DN, B> _path(String path) {
        this.path = path;
        this._address(path);
        return this;
    }

    protected BaseSelector<DN, B> _address(String address) {
        this.address = address;
        return this;
    }

    protected void address(String value) {
        this._address(value);
        notifier.addressed(address);
    }

    String nameOf(String label) {
        if (label == null) return null;
        int position = position(label);
        if (position == -1) return null;
        return nameOf(position);
    }

    String nameOf(int option) {
        if (option == -1) return null;
        SelectorOption child = findOption(option);
        return child != null ? child.name() : null;
    }

    SelectorOption findOption(int option) {
        if (option < 0) return null;
        List<SelectorOption> options = options();
        int size = options.size();
        return size > 0 && option < size ? options.get(option) : null;
    }

    int position(String option) {
        List<SelectorOption> options = options();
        for (int i = 0; i< options.size(); i++) {
            SelectorOption selectorOption = options.get(i);
            if (selectorOption.name().equalsIgnoreCase(option) || selectorOption.id().equals(option) ||
                ((selectorOption instanceof BaseText) && ((BaseText<?,?>)selectorOption).value().equals(option)))
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

    private <D extends Display> D addOption(Component option) {
        components.add(option);
        PropertyList properties = option.properties();
        properties.addClassName("option");
        properties.put("id", option.id());
        properties.put("name", option.name());
        properties.put("label"  , option.label());
        return (D) super.add(option);
    }

	private static class Text extends io.intino.alexandria.ui.displays.components.Text<TextNotifier, Box> implements SelectorOption {
		public Text(Box box) {
			super(box);
		}
	}

    private boolean validAddress() {
        return address != null && address.chars().filter(c -> c == ':').count() <= 1;
    }
}