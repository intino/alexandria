package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaCatalogDisplayViewNotifier;
import io.intino.konos.alexandria.ui.model.catalog.Scope;
import io.intino.konos.alexandria.ui.model.catalog.views.DisplayView;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.Item;
import io.intino.konos.alexandria.ui.schemas.PictureData;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;

public class AlexandriaCatalogDisplayView extends AlexandriaCatalogView<AlexandriaCatalogDisplayViewNotifier> {
    private AlexandriaDisplay display;

    public AlexandriaCatalogDisplayView(Box box) {
        super(box);
    }

    @Override
    public void reset() {
    }

    @Override
    public List<io.intino.konos.alexandria.ui.model.Item> selectedItems() {
        return emptyList();
    }

    @Override
    public void refresh() {
        super.refresh();
        this.display.refresh();
    }

    @Override
    public void refresh(Item... items) {
    }

    @Override
    public void refresh(Item item) {
    }

    @Override
    public void refreshValidation(String validationMessage, Stamp stamp, Item item) {
    }

    @Override
    public void refreshSelection(List<String> selection) {
    }

    @Override
    protected void refreshPicture(PictureData data) {
    }

    public void refresh(Scope scope) {
        DisplayView displayView = (DisplayView) definition().raw();
        displayView.update(display, scope);
    }

    @Override
    protected void init() {
        super.init();
        this.display = ((DisplayView) definition().raw()).display(provider().element(), loadingListener(), instantListener(), session());
        sendDisplayType(display);
        add(display);
        display.personifyOnce();
    }

    private void sendDisplayType(AlexandriaDisplay display) {
        notifier.displayType(display.name());
    }

    private Consumer<Boolean> loadingListener() {
        return value -> AlexandriaCatalogDisplayView.this.notifyLoading((Boolean) value);
    }

    private Consumer<CatalogInstantBlock> instantListener() {
        return block -> AlexandriaCatalogDisplayView.this.selectInstant((CatalogInstantBlock) block);
    }

    private void selectInstant(CatalogInstantBlock block) {
        provider().selectInstant(block);
    }

}