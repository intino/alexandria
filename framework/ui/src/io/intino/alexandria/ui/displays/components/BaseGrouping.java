package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.BaseGroupingNotifier;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BaseGrouping<DN extends BaseGroupingNotifier, B extends Box> extends AbstractBaseGrouping<DN, B> {
	private List<Collection> collections = new ArrayList<>();
	private List<String> selection;
	private SelectionListener selectionListener;
	private SelectionListener attachedListener;
	private List<Group> groups = new ArrayList<>();
	private GroupingToolbar toolbar;

	public BaseGrouping(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		notifier.refreshVisibility(isVisible());
	}

	public List<String> selection() {
		return namesOf(selection);
	}

	public BaseGrouping onSelect(SelectionListener listener) {
    	this.selectionListener = listener;
    	return this;
	}

	public BaseGrouping onAttachedChanges(SelectionListener listener) {
		this.attachedListener = listener;
		return this;
	}

	public BaseGrouping<DN, B> groups(List<Group> groups) {
		_groups(groups);
		refresh();
		return this;
	}

	public void select(List<String> groups) {
		this.selection = new ArrayList<>(groups);
		notifySelection();
		notifyBindings();
	}

	public BaseGrouping<DN, B> bindTo(GroupingToolbar toolbar) {
		this.toolbar = toolbar;
		return this;
	}

	public BaseGrouping<DN, B> bindTo(Collection... collections) {
		this.collections = Arrays.stream(collections).filter(Objects::nonNull).collect(toList());
		if (this.collections.size() > 0) {
			Collection collection = this.collections.get(0);
			if (collection.ready()) loadGroups();
			else collection.onReady((event) -> loadGroups());
		}
		return this;
	}

	public BaseGrouping<DN, B> attachTo(Grouping grouping) {
		grouping.onSelect(e -> notifyAttachedChanges(e.selection()));
		return this;
	}

	public String key() {
		return label() != null && !label().isEmpty() ? label() : name();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (groups.size() > 0) refreshGroups();
	}

	protected java.util.List<Collection> _collectionBindings() {
		return collections;
	}

	protected List<Group> groupsOf(List<String> names) {
		return names.stream().map(this::groupOf).collect(Collectors.toList());
	}

	protected BaseGrouping _groups(List<Group> groups) {
		this.groups = groups;
		return this;
	}

	private void loadGroups() {
		if (collections.size() <= 0) return;
		_groups(collections.get(0).source().groups(key()));
		refreshGroups();
	}

	private void refreshGroups() {
		notifier.refreshGroups(groups.stream().map(this::groupOf).collect(toList()));
	}

	private io.intino.alexandria.schemas.Group groupOf(Group group) {
		return new io.intino.alexandria.schemas.Group().label(group.label()).count(group.count()).color(group.color());
	}

	private void notifySelection() {
		notifier.refreshSelection(selection);
		if (selectionListener != null) selectionListener.accept(new SelectionEvent(this, namesOf(selection)));
	}

	private void notifyBindings() {
		if (toolbar != null) toolbar.filter(key(), namesOf(selection));
		else collections.forEach(c -> c.filter(key(), namesOf(selection)));
	}

	private List<String> namesOf(List<String> selection) {
		return selection != null ? selection.stream().map(v -> groupOf(v).name()).collect(Collectors.toList()) : null;
	}

	private Group groupOf(String key) {
		return groups.stream().filter(g -> g.name().equals(key) || g.label().equals(key)).findFirst().orElse(null);
	}

	private void notifyAttachedChanges(List<String> selection) {
		this.attachedListener.accept(new SelectionEvent(this, selection));
	}

}