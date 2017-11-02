package io.intino.alexandria.framework.box.displays.builders;

import io.intino.alexandria.framework.box.model.catalog.arrangement.GroupingManager;
import io.intino.alexandria.framework.box.schemas.Catalog;
import io.intino.alexandria.framework.box.schemas.Group;
import io.intino.alexandria.framework.box.schemas.Grouping;
import io.intino.alexandria.framework.box.schemas.Sorting;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class CatalogBuilder {

    public static Catalog build(io.intino.alexandria.framework.box.model.Catalog catalog, GroupingManager manager, String label, boolean embedded) {
        return new Catalog().name(catalog.name()).label(label)
                .embedded(embedded)
                .hideGroupings(catalog.groupings().size() <= 0)
                .groupingList(buildGroupingList(catalog, manager))
                .sortingList(buildSortingList(catalog));
    }

    private static List<Grouping> buildGroupingList(io.intino.alexandria.framework.box.model.Catalog catalog, GroupingManager manager) {
        return catalog.groupings().stream().map(g -> buildGrouping(g, manager)).filter(Objects::nonNull).collect(toList());
    }

    private static Grouping buildGrouping(io.intino.alexandria.framework.box.model.catalog.arrangement.Grouping grouping, GroupingManager manager) {
        List<Group> groupList = buildGroupList(grouping, manager);
        int countItems = groupList.stream().mapToInt(Group::count).sum();

        if (countItems <= 0) return null;

        return new Grouping().name(grouping.name()).label(grouping.label()).histogram(grouping.histogram().toString())
                .type(typeOf(grouping))
                .groupList(groupList)
                .countItems(countItems);
    }

    private static List<Sorting> buildSortingList(io.intino.alexandria.framework.box.model.Catalog catalog) {
        return CatalogSortingBuilder.buildList(catalog.sortings());
    }

    private static String typeOf(io.intino.alexandria.framework.box.model.catalog.arrangement.Grouping grouping) {
        return !grouping.cluster() ? "Grouping" : "ClusterGrouping";
    }

    private static List<Group> buildGroupList(io.intino.alexandria.framework.box.model.catalog.arrangement.Grouping grouping, GroupingManager manager) {
        List<io.intino.alexandria.framework.box.model.catalog.arrangement.Group> groups = manager.groups(grouping).toList();
        List selected = manager.filteredGroups(grouping);
        return groups.stream().map(g -> buildGroup(g, selected.contains(g.label()))).collect(toList());
    }

    private static Group buildGroup(io.intino.alexandria.framework.box.model.catalog.arrangement.Group group, boolean selected) {
        return new Group().name(group.name()).label(group.label()).selected(selected).count(group.countItems());
    }

}
