package io.intino.alexandria.ui.displays.providers;

import io.intino.alexandria.ui.Resource;
import io.intino.alexandria.ui.displays.AlexandriaElementDisplay;
import io.intino.alexandria.ui.displays.CatalogInstantBlock;
import io.intino.alexandria.ui.displays.ElementDisplayManager;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.Mold;
import io.intino.alexandria.ui.model.TimeRange;
import io.intino.alexandria.ui.model.mold.Block;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.konos.alexandria.ui.schemas.ElementOperationParameters;

import java.net.URL;
import java.util.List;

public interface ElementViewDisplayProvider {
    Element element();
    Item item(String id);
    UISession session();
    URL baseAssetUrl();
    boolean embedded();
    boolean selectionEnabledByDefault();

    ElementDisplayManager elementDisplayManager();
    TimeRange range();

    void selectInstant(CatalogInstantBlock block);

    <D extends AlexandriaElementDisplay> D openElement(String label);
    <D extends AlexandriaElementDisplay> D openElement(String label, String ownerId);

    List<Block> blocks(Mold mold);
    List<Stamp> stamps(Mold mold);
    List<Stamp> expandedStamps(Mold mold);
    Stamp stamp(Mold mold, String name);

    void executeOperation(ElementOperationParameters params, List<Item> selection);
    Resource downloadOperation(ElementOperationParameters params, List<Item> selection);
    void changeItem(Item item, Stamp stamp, String value);
    void validateItem(Item item, Stamp stamp, String value);

    interface Sorting {
        String name();
        Mode mode();
        int comparator(Item item1, Item item2);
        enum Mode { Ascendant, Descendant }
    }
}

