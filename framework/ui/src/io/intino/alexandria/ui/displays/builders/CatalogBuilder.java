package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.catalog.arrangement.Group;
import io.intino.alexandria.ui.model.catalog.arrangement.Grouping;
import io.intino.alexandria.ui.model.catalog.arrangement.GroupingManager;
import io.intino.alexandria.ui.model.catalog.arrangement.Sorting;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class CatalogBuilder {

    public static io.intino.alexandria.ui.schemas.Catalog build(Catalog catalog, GroupingManager manager, String label, boolean embedded) {
        return new io.intino.alexandria.ui.schemas.Catalog().name(catalog.name()).label(label)
                .embedded(embedded)
                .hideGroupings(catalog.groupings().size() <= 0 || allGroupingsEmpty(catalog, manager))
                .groupingList(buildGroupingList(catalog, manager))
                .sortingList(buildSortingList(catalog))
                .arrangementHistogramsMode(catalog.arrangementHistogramsMode().toString())
                .arrangementPosition(catalog.arrangementPosition().toString())
                .mode(catalog.mode().toString());
    }

    private static boolean allGroupingsEmpty(Catalog catalog, GroupingManager manager) {
        return catalog.groupings().stream().filter(g -> !manager.groups(g).isEmpty()).count() <= 0;
    }

    private static List<io.intino.alexandria.ui.schemas.Grouping> buildGroupingList(Catalog catalog, GroupingManager manager) {
        return catalog.groupings().stream().map(g -> buildGrouping(g, manager)).filter(Objects::nonNull).collect(toList());
    }

    private static io.intino.alexandria.ui.schemas.Grouping buildGrouping(Grouping grouping, GroupingManager manager) {
        List<io.intino.alexandria.ui.schemas.Group> groupList = buildGroupList(grouping, manager);
        int countItems = groupList.stream().mapToInt(io.intino.alexandria.ui.schemas.Group::count).sum();

        if (countItems <= 0) return null;

        return new io.intino.alexandria.ui.schemas.Grouping().name(grouping.name()).label(grouping.label()).histogram(grouping.histogram().toString())
                .type(typeOf(grouping))
                .groupList(groupList)
                .countItems(countItems);
    }

    private static List<io.intino.alexandria.ui.schemas.Sorting> buildSortingList(Catalog catalog) {
        return CatalogSortingBuilder.buildList(catalog.sortings().stream().filter(Sorting::visible).collect(toList()));
    }

    private static String typeOf(Grouping grouping) {
        return "Grouping";
    }

    private static List<io.intino.alexandria.ui.schemas.Group> buildGroupList(Grouping grouping, GroupingManager manager) {
        List<Group> groups = manager.groups(grouping).toList();
        List selected = manager.filteredGroups(grouping);
        return groups.stream().map(g -> buildGroup(g, selected.contains(Group.name(g.label())))).collect(toList());
    }

    private static io.intino.alexandria.ui.schemas.Group buildGroup(Group group, boolean selected) {
        return new io.intino.alexandria.ui.schemas.Group().name(group.name()).label(group.label()).selected(selected).count(group.countObjects());
    }

}
