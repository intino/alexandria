package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.AbstractBaseNumber;
import io.intino.alexandria.ui.displays.notifiers.BaseNumberNotifier;

public class BaseNumber<DN extends BaseNumberNotifier, B extends Box> extends AbstractBaseNumber<DN, B> {
    private boolean expanded;

    public BaseNumber(B box) {
        super(box);
    }

    public BaseNumber<DN, B> expanded(boolean value) {
        this.expanded = value;
        notifier.refreshExpanded(value);
        return this;
    }

    public BaseNumber<DN, B> countDecimals(int count) {
        notifier.refreshDecimals(count);
        return this;
    }

    public boolean expanded() {
        return expanded;
    }

    public BaseNumber<DN, B> prefix(String prefix) {
        notifier.refreshSuffix(prefix);
        return this;
    }

    public BaseNumber<DN, B> suffix(String suffix) {
        notifier.refreshSuffix(suffix);
        return this;
    }
}