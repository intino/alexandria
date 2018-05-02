package io.intino.konos.alexandria.ui.displays.providers;

import io.intino.konos.alexandria.ui.model.Item;

import java.util.List;

public interface CatalogViewDisplayProvider extends ElementViewDisplayProvider {
    Item target();

    int countItems(String condition);
    List<Item> items(int start, int limit, String condition);
    List<Item> items(int start, int limit, String condition, Sorting sorting);
    Item rootItem(List<Item> itemList);
    Item defaultItem(String name);
    List<io.intino.konos.alexandria.ui.model.catalog.arrangement.Sorting> sortings();

    io.intino.konos.alexandria.ui.model.catalog.arrangement.Sorting sorting(String name);
}

