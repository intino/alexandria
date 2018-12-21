package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.model.catalog.arrangement.Sorting;
import io.intino.alexandria.ui.displays.providers.CatalogViewDisplayProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CatalogSortingBuilder {

    public static io.intino.alexandria.ui.schemas.Sorting build(Sorting sorting) {
        return new io.intino.alexandria.ui.schemas.Sorting().name(sorting.name()).label(sorting.label()).visible(sorting.visible());
    }

    public static io.intino.alexandria.ui.schemas.Sorting build(CatalogViewDisplayProvider.Sorting sorting) {
        return new io.intino.alexandria.ui.schemas.Sorting().name(sorting.name()).mode(sorting.mode().toString()).visible(true);
    }

    public static io.intino.alexandria.ui.schemas.Sorting build(String name, String mode) {
        return new io.intino.alexandria.ui.schemas.Sorting().name(name).mode(mode);
    }

    public static List<io.intino.alexandria.ui.schemas.Sorting> buildList(List<Sorting> sortingList) {
        return sortingList.stream().map(CatalogSortingBuilder::build).collect(toList());
    }

}
