package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.SortingNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class Sorting<DN extends SortingNotifier, B extends Box> extends AbstractSorting<DN, B> {

    public Sorting(B box) {
        super(box);
    }

}