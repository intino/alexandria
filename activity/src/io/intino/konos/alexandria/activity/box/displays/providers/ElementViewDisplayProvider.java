package io.intino.konos.alexandria.activity.box.displays.providers;

import io.intino.konos.alexandria.activity.box.Resource;
import io.intino.konos.alexandria.activity.box.services.push.User;
import io.intino.konos.alexandria.activity.box.displays.CatalogInstantBlock;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.activity.box.displays.ElementDisplayManager;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaStampDisplay;
import io.intino.konos.alexandria.activity.box.model.Element;
import io.intino.konos.alexandria.activity.box.model.Item;
import io.intino.konos.alexandria.activity.box.model.TimeRange;
import io.intino.konos.alexandria.activity.box.model.mold.Block;
import io.intino.konos.alexandria.activity.box.model.Mold;
import io.intino.konos.alexandria.activity.box.model.mold.Stamp;
import io.intino.konos.alexandria.activity.box.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.box.schemas.SaveItemParameters;

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

    AlexandriaStampDisplay display(String stamp);

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

