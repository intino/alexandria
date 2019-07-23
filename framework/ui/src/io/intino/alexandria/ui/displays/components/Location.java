package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.LocationNotifier;

public class Location<DN extends LocationNotifier, B extends Box> extends AbstractLocation<DN, B> {

    public Location(B box) {
        super(box);
    }

}