package io.intino.alexandria.ui.displays;

import io.intino.konos.framework.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaViewContainerDisplayNotifier;
import io.intino.alexandria.ui.model.catalog.Scope;
import io.intino.alexandria.ui.model.view.container.DisplayContainer;

import java.util.function.Consumer;


public class AlexandriaViewContainerDisplay extends AlexandriaViewContainer<AlexandriaViewContainerDisplayNotifier> {
    private AlexandriaDisplay display;

    public AlexandriaViewContainerDisplay(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        DisplayContainer container = view().container();
        display = container.display(target(), loadingListener(), instantListener(), session());
        if (display == null) return;
        sendDisplayType(display);
        add(display);
        display.personifyOnce(id());
    }

    @Override
    public void refresh() {
        super.refresh();
        display.refresh();
    }

    public void refresh(Scope scope) {
        DisplayContainer container = view().container();
        container.update(display, scope);
    }

    private void sendDisplayType(AlexandriaDisplay display) {
        notifier.displayType(display.name());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> AlexandriaViewContainerDisplay.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> AlexandriaViewContainerDisplay.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        provider().selectInstant(block);
    }

}