package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.activity.displays.ElementDisplayManager;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Mold;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.schemas.SaveItemParameters;
import io.intino.konos.alexandria.activity.services.push.User;

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

    <E extends AlexandriaElementDisplay> E openElement(String label);

    List<Block> blocks(Mold mold);
    List<Stamp> stamps(Mold mold);
    List<Stamp> expandedStamps(Mold mold);
    Stamp stamp(Mold mold, String name);

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

