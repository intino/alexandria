package io.intino.alexandria.framework.box.displays.providers;

import io.intino.alexandria.foundation.activity.Resource;
import io.intino.alexandria.foundation.activity.services.push.User;
import io.intino.alexandria.framework.box.displays.CatalogInstantBlock;
import io.intino.alexandria.framework.box.displays.AlexandriaElementDisplay;
import io.intino.alexandria.framework.box.displays.ElementDisplayManager;
import io.intino.alexandria.framework.box.displays.AlexandriaStampDisplay;
import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.mold.Block;
import io.intino.alexandria.framework.box.model.Mold;
import io.intino.alexandria.framework.box.model.mold.Stamp;
import io.intino.alexandria.framework.box.schemas.ElementOperationParameters;
import io.intino.alexandria.framework.box.schemas.SaveItemParameters;

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

