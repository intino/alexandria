package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.GroupingToolbarFilter;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.collection.ApplyFiltersEvent;
import io.intino.alexandria.ui.displays.events.collection.ApplyFiltersListener;
import io.intino.alexandria.ui.displays.notifiers.GroupingToolbarNotifier;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GroupingToolbar<DN extends GroupingToolbarNotifier, B extends Box> extends AbstractGroupingToolbar<B> {
    private java.util.Map<String, List<String>> filtersMap = new HashMap<>();
    private List<Grouping<?, ?>> groupings = new ArrayList<>();
    private Listener beforeApplyFiltersListener;
    private ApplyFiltersListener applyFiltersListener;
    private Listener beforeResetFiltersListener;
    private Listener resetFiltersListener;
    private boolean reseting = false;

    public GroupingToolbar(B box) {
        super(box);
    }

    public GroupingToolbar<DN, B> filter(String grouping, List<String> groups) {
        if (groups.size() <= 0) filtersMap.remove(grouping);
        else filtersMap.put(grouping, groups);
        if (!reseting && filtersMap.size() <= 0) clearCollections();
        refreshFilters();
        return this;
    }

    public GroupingToolbar<DN, B> onBeforeApply(Listener listener) {
        this.beforeApplyFiltersListener = listener;
        return this;
    }

    public GroupingToolbar<DN, B> onApply(ApplyFiltersListener listener) {
        this.applyFiltersListener = listener;
        return this;
    }

    public GroupingToolbar<DN, B> onBeforeReset(Listener listener) {
        this.beforeResetFiltersListener = listener;
        return this;
    }

    public GroupingToolbar<DN, B> onReset(Listener listener) {
        this.resetFiltersListener = listener;
        return this;
    }

    public GroupingToolbar<DN, B> apply() {
        if (beforeApplyFiltersListener != null) beforeApplyFiltersListener.accept(new Event(this));
        collections().forEach(c -> c.filter(filtersMap));
        if (applyFiltersListener != null) applyFiltersListener.accept(new ApplyFiltersEvent(this, filtersMap));
        return this;
    }

    public GroupingToolbar<DN, B> reset() {
        reseting = true;
        if (beforeResetFiltersListener != null) beforeResetFiltersListener.accept(new Event(this));
        filtersMap.clear();
        groupings.forEach(g -> g.select(Collections.emptyList()));
        clearCollections();
        if (resetFiltersListener != null) resetFiltersListener.accept(new Event(this));
        reseting = false;
        return this;
    }

    public GroupingToolbar<DN, B> removeFilter(String filter) {
        groupingOf(filter).ifPresent(g -> g.select(Collections.emptyList()));
        return this;
    }

    public GroupingToolbar<DN, B> bindTo(Grouping<?, ?>... groupings) {
        this.groupings.clear();
        this.groupings.addAll(Arrays.asList(groupings));
        this.groupings.forEach(g -> g.bindTo(this));
        return this;
    }

    private void clearCollections() {
        collections().forEach(Collection::clearFilters);
    }

    private void refreshFilters() {
        notifier.refreshFilters(filtersToSchema());
    }

    private List<GroupingToolbarFilter> filtersToSchema() {
        return filtersMap.entrySet().stream().map(entry -> {
            Optional<Grouping<?, ?>> grouping = groupingOf(entry.getKey());
            return filterToSchema(entry.getKey(), grouping.isPresent() ? grouping.get().groupsOf(entry.getValue()) : Collections.emptyList());
        }).collect(toList());
    }

    private Optional<Grouping<?, ?>> groupingOf(String key) {
        return groupings.stream().filter(g -> g.key().equals(key)).findFirst();
    }

    private GroupingToolbarFilter filterToSchema(String name, List<Group> options) {
        return new GroupingToolbarFilter().name(name).options(options.stream().map(Group::label).collect(toList()));
    }

    private List<Collection<?, ?>> collections() {
        return groupings.stream().map(BaseGrouping::_collectionBindings).flatMap(java.util.Collection::stream).map(c -> (Collection<?, ?>)c).distinct().collect(toList());
    }
}