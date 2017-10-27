package io.intino.konos.server.activity.displays.elements.providers;

import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.catalogs.CatalogInstantBlock;
import io.intino.konos.server.activity.displays.elements.*;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.molds.model.Block;
import io.intino.konos.server.activity.displays.molds.model.Mold;
import io.intino.konos.server.activity.displays.molds.model.Stamp;
import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.schemas.ElementOperationParameters;
import io.intino.konos.server.activity.displays.schemas.SaveItemParameters;
import io.intino.konos.server.activity.services.push.User;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ElementViewDisplayProvider {
    Element element();
    Item item(String id);
    Optional<User> user();
    URL baseAssetUrl();
    boolean embedded();

    ElementDisplayManager elementDisplayManager();
    TimeRange range();

    void selectInstant(CatalogInstantBlock block);

    <E extends ElementDisplay> E openElement(String label);

    List<Block> blocks(Mold mold);
    List<Stamp> stamps(Mold mold);
    List<Stamp> expandedStamps(Mold mold);
    Stamp stamp(Mold mold, String name);

    StampDisplay display(String stamp);

    void executeOperation(ElementOperationParameters params, List<Item> selection);
    Resource downloadOperation(ElementOperationParameters params, List<Item> selection);
    void saveItem(SaveItemParameters params, Item item);

    interface Sorting {
        String name();
        Mode mode();
        int comparator(Item item1, Item item2);
        enum Mode { Ascendant, Descendant }
    }
}

