package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaContainerViewDisplayNotifier;
import io.intino.konos.alexandria.ui.model.catalog.Scope;
import io.intino.konos.alexandria.ui.model.views.ContainerView;
import io.intino.konos.alexandria.ui.model.views.container.DisplayContainer;

import java.util.function.Consumer;


public class AlexandriaContainerViewDisplay extends AlexandriaContainerView<AlexandriaContainerViewDisplayNotifier> {
    private AlexandriaDisplay display;

    public AlexandriaContainerViewDisplay(Box box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        ContainerView view = view();
        DisplayContainer container = view.container();
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
        ContainerView view = view();
        DisplayContainer container = view.container();
        container.update(display, scope);
    }

    private void sendDisplayType(AlexandriaDisplay display) {
        notifier.displayType(display.name());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> AlexandriaContainerViewDisplay.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> AlexandriaContainerViewDisplay.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        provider().selectInstant(block);
    }
}