package io.intino.alexandria.ui.displays.providers;

import io.intino.alexandria.ui.model.Item;

import java.util.List;

public interface CatalogViewDisplayProvider extends ElementViewDisplayProvider {
    Item target();

    int countItems(String condition);
    List<Item> items(int start, int limit, String condition);
    List<Item> items(int start, int limit, String condition, Sorting sorting);
    Item rootItem(List<Item> itemList);
    Item defaultItem(String name);
    List<io.intino.alexandria.ui.model.catalog.arrangement.Sorting> sortings();
    void loadMoreItems(String condition, Sorting sorting, int minCount);

    io.intino.alexandria.ui.model.catalog.arrangement.Sorting sorting(String name);
}

