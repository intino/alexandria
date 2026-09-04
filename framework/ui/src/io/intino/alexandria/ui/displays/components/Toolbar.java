package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.components.toolbar.Linked;
import io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation;
import io.intino.alexandria.ui.displays.notifiers.ToolbarNotifier;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Toolbar<DN extends ToolbarNotifier, B extends Box> extends AbstractToolbar<B> implements Linked {

    public Toolbar(B box) {
        super(box);
    }

    @Override
    public void bindTo(Selectable collection) {
        collection.onSelect(event -> selectionOperations().forEach(child -> child.selectedItems(event.selection())));
    }

    private List<SelectionOperation> selectionOperations() {
        return selectionOperations(this);
    }

    private List<SelectionOperation> selectionOperations(Display<?, ?> display) {
        if (display instanceof SelectionOperation) return List.of((SelectionOperation) display);
        return display.children().stream().map(this::selectionOperations).flatMap(Collection::stream).collect(toList());
    }
}