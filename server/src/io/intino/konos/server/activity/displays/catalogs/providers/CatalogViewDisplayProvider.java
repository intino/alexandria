package io.intino.konos.server.activity.displays.catalogs.providers;

import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.providers.ElementViewDisplayProvider;
import io.intino.konos.server.activity.displays.schemas.ClusterGroup;

import java.util.List;

public interface CatalogViewDisplayProvider extends ElementViewDisplayProvider {
    Object concept();
    Item target();

    int countItems(String condition);
    List<Item> items(int start, int limit, String condition);
    List<Item> items(int start, int limit, String condition, Sorting sorting);
    Item rootItem(List<Item> itemList);
    Item defaultItem(String name);
    List<io.intino.konos.server.activity.displays.catalogs.model.arrangement.Sorting> sortings();

    io.intino.konos.server.activity.displays.catalogs.model.arrangement.Sorting sorting(String name);

    void createClusterGroup(ClusterGroup value);
}

