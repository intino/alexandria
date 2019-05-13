package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SearchBoxNotifier;

public class SearchBox<DN extends SearchBoxNotifier, B extends Box> extends AbstractSearchBox<B> {

    public SearchBox(B box) {
        super(box);
    }

}