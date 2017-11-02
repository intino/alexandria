package io.intino.alexandria.framework.box.displays.providers;

import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.schemas.ClusterGroup;

import java.util.List;

public interface CatalogViewDisplayProvider extends ElementViewDisplayProvider {
    Object concept();
    Item target();

    int countItems(String condition);
    List<Item> items(int start, int limit, String condition);
    List<Item> items(int start, int limit, String condition, Sorting sorting);
    Item rootItem(List<Item> itemList);
    Item defaultItem(String name);
    List<io.intino.alexandria.framework.box.model.catalog.arrangement.Sorting> sortings();

    io.intino.alexandria.framework.box.model.catalog.arrangement.Sorting sorting(String name);

    void createClusterGroup(ClusterGroup value);
}

