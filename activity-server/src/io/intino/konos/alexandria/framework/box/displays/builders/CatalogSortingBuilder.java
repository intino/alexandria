package io.intino.konos.alexandria.framework.box.displays.builders;

import io.intino.konos.alexandria.framework.box.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.framework.box.schemas.Sorting;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CatalogSortingBuilder {

    public static Sorting build(io.intino.konos.alexandria.framework.box.model.catalog.arrangement.Sorting sorting) {
        return new Sorting().name(sorting.name()).label(sorting.label());
    }

    public static Sorting build(CatalogViewDisplayProvider.Sorting sorting) {
        return new Sorting().name(sorting.name()).mode(sorting.mode().toString());
    }

    public static Sorting build(String name, String mode) {
        return new Sorting().name(name).mode(mode);
    }

    public static List<Sorting> buildList(List<io.intino.konos.alexandria.framework.box.model.catalog.arrangement.Sorting> sortingList) {
        return sortingList.stream().map(CatalogSortingBuilder::build).collect(toList());
    }

}
