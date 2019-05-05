package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.components.toolbar.Linked;
import io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Toolbar<B extends Box> extends AbstractToolbar<B> implements Linked {

    public Toolbar(B box) {
        super(box);
    }

    @Override
    public void bindTo(Selectable collection) {
        collection.onSelect(event -> selectionOperations().forEach(child -> child.selectedItems(event.items())));
    }

    private List<SelectionOperation> selectionOperations() {
        return children().stream().filter(c -> c instanceof SelectionOperation).map(c -> ((SelectionOperation)c)).collect(toList());
    }
}