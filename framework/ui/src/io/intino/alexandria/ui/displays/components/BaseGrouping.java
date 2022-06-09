package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.addressable.Addressable;
import io.intino.alexandria.ui.displays.components.collection.CollectionAddressResolver;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.BaseGroupingNotifier;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class BaseGrouping<DN extends BaseGroupingNotifier, B extends Box> extends AbstractBaseGrouping<DN, B> implements Addressable {
	private List<Collection> collections = new ArrayList<>();
	private List<String> selection;
	private SelectionListener selectionListener;
	private SelectionListener attachedListener;
	private java.util.Map<String, java.util.List<Group>> groups = new HashMap<>();
	private GroupingToolbar toolbar;
	private String path;
	private String address;

	public BaseGrouping(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		if (selection != null) notifier.refreshSelection(selection);
		notifier.refreshVisibility(isVisible());
	}

	public String path() {
		return this.path;
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

	protected BaseGrouping<DN, B> _path(String path) {
		this.path = path;
		this._address(path);
		return this;
	}

	protected BaseGrouping<DN, B> _address(String address) {
		this.address = address;
		return this;
	}

	protected void address(String value) {
		this._address(value);
	}

	public void setupAddress(List<String> groups) {
		if (address == null || collections.size() <= 0) {
			select(groups);
			return;
		}
		String queryString = CollectionAddressResolver.queryString(collections.get(0), key(), groups);
		if (queryString == null) {
			select(groups);
			return;
		}
		notifier.addressed(address + (address.contains("?") ? ":" : "?") + queryString);
		notifier.registerSelection(groups);
		select(groups);
	}

	public void select(List<String> groups) {
		this.selection = new ArrayList<>(groups);
		notifySelection();
		notifyBindings();
		notifier.addressed(null);
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

	public BaseGrouping<DN, B> attachTo(BaseGrouping<?, ?> grouping) {
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
		List<Group> groups = flattenGroups();
		return names.stream().map(name -> findGroup(name, groups)).collect(Collectors.toList());
	}

	protected BaseGrouping _groups(List<Group> groups) {
		this.groups = groups.stream().collect(groupingBy(g -> g.category() != null ? g.category() : "default"));
		return this;
	}

	private void loadGroups() {
		if (collections.size() <= 0) return;
		_groups(collections.get(0).source().groups(key()));
		refreshGroups();
	}

	private void refreshGroups() {
		notifier.refreshGroups(groups.entrySet().stream().map(this::groupOf).collect(toList()));
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
		List<Group> groups = flattenGroups();
		return selection != null ? selection.stream().map(v -> findGroup(v, groups).name()).collect(Collectors.toList()) : null;
	}

	private io.intino.alexandria.schemas.GroupEntry groupOf(Map.Entry<String, List<Group>> group) {
		return new io.intino.alexandria.schemas.GroupEntry().label(group.getKey()).groups(group.getValue().stream().map(this::groupOf).collect(toList()));
	}

	private io.intino.alexandria.schemas.Group groupOf(Group group) {
		return new io.intino.alexandria.schemas.Group().label(group.label()).count(group.count()).color(group.color());
	}

	private Group findGroup(String key, List<Group> in) {
		return in.stream().filter(g -> g.name().equals(key) || g.label().equals(key)).findFirst().orElse(null);
	}

	private void notifyAttachedChanges(List<String> selection) {
		if (attachedListener == null) return;
		attachedListener.accept(new SelectionEvent(this, selection));
	}

	private List<Group> flattenGroups() {
		return this.groups.values().stream().flatMap(java.util.Collection::stream).collect(toList());
	}

}