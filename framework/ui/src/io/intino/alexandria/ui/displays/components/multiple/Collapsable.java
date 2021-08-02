package io.intino.alexandria.ui.displays.components.multiple;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;

public interface Collapsable<B extends Box, C extends Component, V extends Object> {
    C add(V value, String label, String description);

    default C add(V value, String label) {
        return add(value, label, null);
    }

    default C add(String label, String description) {
        return add(null, label, description);
    }

    default C add(String label) {
        return add(null, label, null);
    }
}
