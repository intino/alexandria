package io.intino.konos.alexandria.ui.displays.providers;

import io.intino.konos.alexandria.ui.Resource;
import io.intino.konos.alexandria.ui.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.ui.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.ui.displays.ElementDisplayManager;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Mold;
import io.intino.konos.alexandria.ui.model.TimeRange;
import io.intino.konos.alexandria.ui.model.mold.Block;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.net.URL;
import java.util.List;

public interface ElementViewDisplayProvider {
    Element element();
    Item item(String id);
    UISession session();
    URL baseAssetUrl();
    boolean embedded();

    ElementDisplayManager elementDisplayManager();
    TimeRange range();

    void selectInstant(CatalogInstantBlock block);

    <E extends AlexandriaElementDisplay> E openElement(String label);

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

